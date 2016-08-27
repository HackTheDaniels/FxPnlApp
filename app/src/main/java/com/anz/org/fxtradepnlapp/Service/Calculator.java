package com.anz.org.fxtradepnlapp.Service;

import android.util.Log;

import com.anz.org.fxtradepnlapp.Common.Constants;
import com.anz.org.fxtradepnlapp.Common.Deal;
import com.anz.org.fxtradepnlapp.Common.Quote;

import java.util.ArrayList;

/**
 * Created by dell on 8/18/2016.
 */
public class Calculator
{
    private static final String Const_MarketEvent = "AGG";
    private static final String Const_BaseCcy = "USD";

    public static double CalculateMid(double bid, double ask)
    {
        double mid = 0;
        if(bid !=0 && ask!=0)
        {
            mid = (bid+ask)/2;
        }
        else if((bid == 0 && ask != 0) || (bid != 0 && ask == 0))
        {
            mid = bid + ask;
        }
        return mid;
    }

    public static void CorrectQuoteDirection(Quote quote)
    {
        String tmp = quote.BaseCcy;
        quote.BaseCcy = quote.QuoteCcy;
        quote.QuoteCcy = tmp;
        quote.AskPrice = 1/quote.AskPrice;
        quote.BidPrice = 1/quote.BidPrice;
        quote.MidPrice = 1/quote.MidPrice;
    }

    public static void CorrectDealDirection(Deal deal)
    {
        String tmp = deal.BaseCcy;
        deal.BaseCcy = deal.DealCcy;
        deal.DealCcy = tmp;
        deal.Price = 1/deal.Price;
    }

    public static double CalculateWeightedAverage(double avgPrice, int avgQty, Deal newDeal)
    {
        return (avgPrice*avgQty + newDeal.Price*newDeal.Quantity)/(avgQty + newDeal.Quantity);
    }

    public static ArrayList<Deal> SplitDeal(Deal deal,double dealMidPrice, double baseMidPrice)
    {
        ArrayList<Deal> deals = new ArrayList<Deal>();
        if(!deal.BaseCcy.equalsIgnoreCase(Const_BaseCcy))
        {
            Deal buyDeal = new Deal();
            buyDeal.BaseCcy = Const_BaseCcy;
            buyDeal.DealCcy = deal.DealCcy;
            buyDeal.Buy = true;
            buyDeal.Price = dealMidPrice;
            buyDeal.Quantity = deal.Quantity;
            buyDeal.Date = deal.Date;
            deals.add(buyDeal);

            Deal sellDeal = new Deal();
            sellDeal.BaseCcy = Const_BaseCcy;
            sellDeal.DealCcy = deal.BaseCcy;
            sellDeal.Buy = false;
            sellDeal.Price = baseMidPrice;
            sellDeal.Quantity = DoubleToInt(deal.Quantity * deal.Price);
            sellDeal.Date = deal.Date;
            deals.add(sellDeal);
        }
        return  deals;
    }

    public static int DoubleToInt(double d)
    {
        return (int)Math.round(d);
    }

//    public static double Round(double value, int precision)
//    {
//        double d = new BigDecimal(value).setScale(precision,BigDecimal.ROUND_HALF_UP).doubleValue();
//        return d;
//    }

    public static double CalculatePosUSD(double quantity, double Price)
    {
        return Price*quantity;
    }

    public static Quote GetQuotesFromDb(String ccy)
    {
        return  new Quote();
    }

    public static double CalculatePnL(double marketMid, double avgBuyPrice, double avgSellPrice, double posUsd)
    {
        double pnl= 0;
        try {
            double buyPart = 0;
            double sellPart = 0;
            if (avgBuyPrice > 0)
                buyPart = (marketMid - avgBuyPrice);
            if (avgSellPrice > 0)
                sellPart = (avgSellPrice - marketMid);
            pnl = (Math.abs(posUsd) * (buyPart + sellPart)) / 1000;// divide by 1000 as it is to be reported in thousands
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "CalculatePnL: " + ex.getMessage());
        }
        return pnl;
    }
}
