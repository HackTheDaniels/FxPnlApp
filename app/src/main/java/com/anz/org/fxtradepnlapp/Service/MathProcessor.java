package com.anz.org.fxtradepnlapp.Service;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;

import com.anz.org.fxtradepnlapp.Common.Deal;
import com.anz.org.fxtradepnlapp.Common.PosPnl;
import com.anz.org.fxtradepnlapp.Common.Quote;
import com.anz.org.fxtradepnlapp.Common.Constants;
import com.anz.org.fxtradepnlapp.R;
import com.anz.org.fxtradepnlapp.SqlLite.AppDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 8/23/2016.
 */
public class MathProcessor
{
    private final String Const_BaseCcy = "USD";
    private HashMap<String,Quote> currentQuotes;
    private HashMap<String,WeightedPrice> avgPrices;
    private AppDataSource dataSource;
    private Context mainCtx;
    public MathProcessor(Context ctx)
    {
        mainCtx=ctx;
        currentQuotes = new HashMap<String,Quote>();
        avgPrices = new HashMap<String,WeightedPrice>();
        dataSource = new AppDataSource(ctx);
        OpenDbConnection();
    }

    public void OpenDbConnection()
    {
        try {
            dataSource.Open();
            ClearTables();
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "OpenDbConnection: " + ex.getMessage());
        }
    }

    public void ClearTables()
    {
        dataSource.DeleteAllTableValues();
    }

    public void CloseDbConnection()
    {
        dataSource.Close();
    }

    public void QuoteEvent(Quote quote)
    {
        Log.d(Constants.APPNAME, "QuoteEvent: Quote event called for ccy " + quote.QuoteCcy);
        if(!quote.BaseCcy.equalsIgnoreCase(Const_BaseCcy))
        {
            if(quote.QuoteCcy.equalsIgnoreCase(Const_BaseCcy))
            {
                Calculator.CorrectQuoteDirection(quote);
            }
            else
            {
                //No USD in quote ignore the quote.
                return;
            }
        }

        SetQuote(quote.QuoteCcy, quote);
        CalculatePnlOnCcy(quote.QuoteCcy);
    }

    public void DealEvent(Deal deal)
    {
        List<Deal> deals = new ArrayList<Deal>();
        if(!deal.BaseCcy.equalsIgnoreCase(Const_BaseCcy))
        {
            if(deal.DealCcy.equalsIgnoreCase(Const_BaseCcy))
            {
                Calculator.CorrectDealDirection(deal);
                deals.add(deal);
            }
            else
            {
                Quote baseQuote = GetQuote(deal.BaseCcy);
                Quote dealQuote = GetQuote(deal.DealCcy);
                if(baseQuote != null && dealQuote != null) {
                    deals = Calculator.SplitDeal(deal, dealQuote.MidPrice, baseQuote.MidPrice);
                }
            }
        }
        else
        {
            deals.add(deal);
            dataSource.AddDeal(deal);
        }
        for(int i = 0; i < deals.size(); i++)
        {
            CalculateWeightedPrice(deals.get(i));
            CalculatePnlOnDeal(deals.get(i));
        }
    }

    private void CalculatePnlOnDeal(Deal deal)
    {
        String whereClause = "'" + deal.DealCcy + "','" + Const_BaseCcy + "'";
        List<PosPnl> pnls = dataSource.GetPosPnl(whereClause);
        PosPnl usdPos = null;
        PosPnl ccyPos = null;
        for(int i = 0; i < pnls.size() ; i++ ) {
            PosPnl p = pnls.get(i);
            if (p.Ccy.equalsIgnoreCase(deal.DealCcy)) {
                ccyPos = p;
            }
            else if (p.Ccy.equalsIgnoreCase(Const_BaseCcy))
            {
                usdPos = p;
            }
        }
        if(usdPos == null) {
            usdPos = new PosPnl();
            usdPos.Ccy = Const_BaseCcy;
            usdPos.Id = -1;
            usdPos.Pos = 0;
            usdPos.Age = 0;
            usdPos.Starred = false;
        }
        if(ccyPos == null) {
            ccyPos = new PosPnl();
            ccyPos.Ccy = deal.DealCcy;
            ccyPos.Id = -1;
            ccyPos.Pos = 0;
            ccyPos.Age = 0;
            ccyPos.Starred= false;
        }

        // Order is important
        double tmpPos = Calculator.CalculatePosUSD(deal.Quantity, deal.Price);
        usdPos.Pos = usdPos.Pos + (deal.Buy == true ? (tmpPos * -1) : tmpPos);
        usdPos.PosUsd = usdPos.Pos;

        CalculatePnl(deal, ccyPos);

        dataSource.AddUpdatePosPnl(ccyPos, true);
        dataSource.AddUpdatePosPnl(usdPos, true);
    }

    private void CalculatePnl(Deal deal, PosPnl currentPnl)
    {
        double originalPos = currentPnl.Pos;
        currentPnl.Pos = currentPnl.Pos + (deal.Buy ? (deal.Quantity) : (-1 * deal.Quantity));
        currentPnl.PosUsd = Calculator.CalculatePosUSD(currentPnl.Pos, deal.Price);
        if((originalPos > 0 && currentPnl.Pos < 0) || (originalPos < 0 && currentPnl.Pos > 0))
        {
            ClearWeightedPrice(currentPnl.Ccy);
            currentPnl.Pnl = 0;
            currentPnl.BookMid = 0;
            currentPnl.MarketMid = 0;
        }
        else
        {
            WeightedPrice price = GetBookAvgPrice(currentPnl.Ccy);
            price = price == null ? new WeightedPrice(): price;
            Quote quote = GetQuote(currentPnl.Ccy);
            double marketMid = quote == null ? 0: quote.MidPrice;
            double oldPnl= currentPnl.Pnl;
            currentPnl.Pnl =  Calculator.CalculatePnL(marketMid, price.BuyPrice, price.SellPrice, currentPnl.PosUsd);
            currentPnl.MarketMid = marketMid;// Will need this in case this is the first deal.
            currentPnl.BookMid = Calculator.CalculateMid(price.SellPrice, price.BuyPrice);
            double newPnl= currentPnl.Pnl;
            threshHoldCalculation(oldPnl,newPnl);
        }
    }

    private void CalculatePnlOnCcy(String ccy)
    {
        WeightedPrice price = GetBookAvgPrice(ccy);
        price = price == null ? new WeightedPrice(): price;
        Quote quote = GetQuote(ccy);
        String whereClause = "'" + ccy + "'" ;
        List<PosPnl> pnls = dataSource.GetPosPnl(whereClause);
        if(pnls.size() != 0)
        {
            PosPnl p = pnls.get(0);
            p.PosUsd = Calculator.CalculatePosUSD(p.Pos, quote.MidPrice);
            double oldPnl=p.Pnl;
            p.Pnl = Calculator.CalculatePnL(quote.MidPrice,price.BuyPrice,price.SellPrice,p.PosUsd);
            double newPnl=p.Pnl;
            dataSource.AddUpdatePosPnl(p, true);
            threshHoldCalculation(oldPnl,newPnl);
        }
    }

    private void CalculateWeightedPrice(Deal deal)
    {
        WeightedPrice price =  GetBookAvgPrice(deal.DealCcy);
        if(price == null)
            price = new WeightedPrice();
        if(deal.Buy)
        {
            price.BuyPrice = Calculator.CalculateWeightedAverage(price.BuyPrice, price.BuyQty, deal);
            price.BuyQty = price.BuyQty + deal.Quantity;
        }
        else
        {
            price.SellPrice = Calculator.CalculateWeightedAverage(price.SellPrice, price.SellQty, deal);
            price.SellQty = price.SellQty + deal.Quantity;
        }
        SetBookAvgPrice(deal.DealCcy, price);
    }

    private void ClearWeightedPrice(String ccy)
    {
        WeightedPrice price =  GetBookAvgPrice(ccy);
        if(price != null)
            ClearAvgPrice(ccy);
    }

    private WeightedPrice GetBookAvgPrice(String ccy)
    {
        return avgPrices.get(ccy);
    }

    private void ClearAvgPrice(String ccy)
    {
        avgPrices.remove(ccy);
    }

    private void SetBookAvgPrice(String ccy, WeightedPrice price)
    {
        avgPrices.put(ccy, price);
    }

    private Quote GetQuote(String ccy)
    {
        return currentQuotes.get(ccy);
    }

    private void SetQuote(String ccy, Quote quote)
    {
        currentQuotes.put(ccy, quote);
        Log.d(Constants.APPNAME, "QuoteEvent: Adding to database " + quote.QuoteCcy);
        dataSource.AddQuote(quote);
        Log.d(Constants.APPNAME, "QuoteEvent: Done Adding to database " + quote.QuoteCcy);
    }

    public void threshHoldCalculation(double prePnl, double currPnl)
    {
        double per=((currPnl-prePnl)/prePnl)*100;
        if(per <-25)
        {
            sendnotification(per);
        }
    }
    public  void sendnotification(double per)
    {
        Notification n  = new Notification.Builder(mainCtx)
                .setContentTitle("Threshold reahced +"+per)
                .setContentText("Subject")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true).build();

        NotificationManager notificationManager =
                (NotificationManager) mainCtx.getSystemService(mainCtx.NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }
}
