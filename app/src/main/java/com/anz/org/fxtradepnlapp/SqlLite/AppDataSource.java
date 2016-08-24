package com.anz.org.fxtradepnlapp.SqlLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anz.org.fxtradepnlapp.Common.Deal;
import com.anz.org.fxtradepnlapp.Common.PosPnl;
import com.anz.org.fxtradepnlapp.Common.Quote;
import com.anz.org.fxtradepnlapp.Common.Constants;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//TODO: NULL Handler in cursor

/**
 * Created by dell on 8/20/2016.
 */
public class AppDataSource
{
    SqlLiteHelper dbHelper;
    SQLiteDatabase db;

    public AppDataSource(Context ctx)
    {
        dbHelper = new SqlLiteHelper(ctx);
    }

    public void Open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
    }

    public void BeginTransaction() throws SQLException
    {
        db.beginTransaction();
    }

    public void SetTransaction() throws SQLException
    {
        db.setTransactionSuccessful();
    }

    public void EndTransaction() throws SQLException
    {
        db.endTransaction();
    }


    public void Close()
    {
        db.close();
    }

    public void AddDeal(Deal deal)
    {
        try {
            ContentValues values = new ContentValues();
            values.put(DealTable.BaseCcy, deal.BaseCcy);
            values.put(DealTable.DealCcy, deal.DealCcy);
            values.put(DealTable.Buy, deal.Buy);
            values.put(DealTable.Quantity, deal.Quantity);
            values.put(DealTable.Price, deal.Price);
            if (deal.Date != null) {
                values.put(DealTable.Timestamp, deal.Date.getTime());
            } else {
                values.put(DealTable.Timestamp, Calendar.getInstance().get(Calendar.SECOND)); //TODO: Need to Test this
            }
            db.insert(DealTable.TableName, null, values);
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "AddDeal: " + ex.getMessage());
        }
    }

    public List<Deal> GetDeals(int numberOfDeals, String ccy)
    {
        List<Deal> deals = new ArrayList<Deal>();
        try {
            String query = "SELECT " +
                    DealTable.DealCcy + " , " +
                    DealTable.BaseCcy + " , " +
                    DealTable.Quantity + " , " +
                    DealTable.Buy + " , " +
                    DealTable.Price + " , " +
                    DealTable.Timestamp +
                    " FROM " + DealTable.TableName;
            if (ccy != null && ccy.length() > 0) {
                query = query + " WHERE " + DealTable.DealCcy + " = " + ccy;
            }
            query = query + " ORDER BY " + DealTable.Timestamp;
            if (numberOfDeals > 0) {
                query = query + " DESC LIMIT " + numberOfDeals;
            }
            Cursor cur = db.rawQuery(query, null);
            if (cur != null & cur.moveToFirst())
            {
                do {
                    Deal tmp = ConstructDeal(cur);
                    if (tmp != null) {
                        deals.add(tmp);
                    }
                }
                while (cur.moveToNext());
            }
            cur.close();
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "GetDeals: " + ex.getMessage());
        }
        return deals;
    }

    public void AddUpdatePosPnl(PosPnl pp, Boolean ignoreStarred) {
        try {
            BeginTransaction();
            if (pp.Id != -1) {
                String[] q_args = {};
                q_args[0] = String.valueOf(pp.Id);
                ContentValues values = new ContentValues();
                values.put(PosPnLTable.Age, pp.Age);
                values.put(PosPnLTable.Pnl, pp.Pnl);
                values.put(PosPnLTable.Pos, pp.Pos);
                values.put(PosPnLTable.PosUsd, pp.PosUsd);
                values.put(PosPnLTable.BookMid, pp.BookMid);
                values.put(PosPnLTable.MarketMid, pp.MarketMid);
                if(!ignoreStarred) {
                    values.put(PosPnLTable.Starred, pp.Starred);
                }
                values.put(PosPnLTable.Timestamp, Calendar.getInstance().get(Calendar.SECOND));
                db.update(PosPnLTable.TableName, values, PosPnLTable.Id + " = ?", new String[]{String.valueOf(pp.Id)});
            } else {
                ContentValues values = new ContentValues();
                values.put(PosPnLTable.Ccy, pp.Ccy);
                values.put(PosPnLTable.Age, pp.Age);
                values.put(PosPnLTable.Pnl, pp.Pnl);
                values.put(PosPnLTable.Pos, pp.Pos);
                values.put(PosPnLTable.PosUsd, pp.PosUsd);
                values.put(PosPnLTable.BookMid, pp.BookMid);
                values.put(PosPnLTable.MarketMid, pp.MarketMid);
                values.put(PosPnLTable.Starred, pp.Starred);
                values.put(PosPnLTable.Timestamp, Calendar.getInstance().get(Calendar.SECOND));
                db.insert(PosPnLTable.TableName, null, values);
            }
            AddHistoryPosPnl(pp);
            SetTransaction();
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "AddUpdatePosPnl: " + ex.getMessage());
        }
        finally {
            EndTransaction();
        }
    }

    public void AddHistoryPosPnl(PosPnl pp)
    {
        try {
            ContentValues values = new ContentValues();
            values.put(PosPnlHistoryTable.Ccy, pp.Ccy);
            values.put(PosPnlHistoryTable.PosUsd, pp.PosUsd);
            values.put(PosPnlHistoryTable.Pnl, pp.Pnl);
            values.put(PosPnlHistoryTable.Pos, pp.Pos);
            values.put(PosPnlHistoryTable.Timestamp, Calendar.getInstance().get(Calendar.SECOND));
            db.insert(PosPnlHistoryTable.TableName, null, values);
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "AddHistoryPosPnl: " + ex.getMessage());
        }
    }

    public List<PosPnl> GetPosPnl(String whereClause)
    {
        List<PosPnl> pnls = new ArrayList<PosPnl>();
        try {
            String query = "SELECT " +
                    PosPnLTable.Ccy + ", " +
                    PosPnLTable.Age + ", " +
                    PosPnLTable.Pnl + ", " +
                    PosPnLTable.Pos + ", " +
                    PosPnLTable.BookMid + ", " +
                    PosPnLTable.MarketMid + ", " +
                    PosPnLTable.Starred + ", " +
                    PosPnLTable.Timestamp + ", " +
                    PosPnLTable.PosUsd + ", " +
                    PosPnLTable.Id +
                    " FROM " + PosPnLTable.TableName;
            if (whereClause != null) {
                query = query + " WHERE " + PosPnLTable.Ccy + " IN (" + whereClause + ")";
            }

            Cursor cur = db.rawQuery(query, null);
            if (cur != null && cur.moveToFirst()) {
                do {
                    PosPnl tmp = ConstructPnl(cur);
                    if (tmp != null) {
                        pnls.add(tmp);
                    }
                }
                while (cur.moveToNext());
            }
            cur.close();
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "GetPosPnl: " + ex.getMessage());
        }
        return pnls;
    }

    public void AddQuote(Quote ccy)
    {
        try {
            ContentValues values = new ContentValues();
            values.put(QuoteTable.BaseCcy, ccy.BaseCcy);
            values.put(QuoteTable.QuoteCcy, ccy.QuoteCcy);
            values.put(QuoteTable.BidPrice, ccy.BidPrice);
            values.put(QuoteTable.AskPrice, ccy.AskPrice);
            values.put(QuoteTable.Timestamp, ccy.QuoteDate.getTime());
            db.insert(QuoteTable.TableName, null, values);
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "AddQuote: " + ex.getMessage());
        }
    }

    public List<Quote> GetQuote(String ccy, int limit)
    {
        List<Quote> quotes = new ArrayList<Quote>();
        try {
            String query = "SELECT " +
                    QuoteTable.QuoteCcy + " , " +
                    QuoteTable.BidPrice + " , " +
                    QuoteTable.AskPrice + " , " +
                    QuoteTable.Timestamp +
                    " FROM " + QuoteTable.TableName;
            if (ccy != null && ccy.length() > 0) {
                query = query + " WHERE " + QuoteTable.QuoteCcy + " = " + ccy;
            }
            query = query + " ORDER BY " + QuoteTable.Timestamp;
            if (limit > 0) {
                query = query + " DESC LIMIT " + limit;
            }
            Cursor cur = db.rawQuery(query, null);
            if (cur != null  && cur.moveToFirst()) {
                do {
                    Quote tmp = ConstructQuote(cur);
                    if (tmp != null) {
                        quotes.add(tmp);
                    }
                }
                while (cur.moveToNext());
            }
            cur.close();
        }
        catch (Exception ex)
        {
            Log.e(Constants.APPNAME, "GetQuote: " + ex.getMessage());
        }
        return quotes;
    }

    private Quote ConstructQuote(Cursor cur)
    {
        Quote quote;
        try{
            quote = new Quote();
            quote.BaseCcy = "USD";
            quote.QuoteCcy =  cur.getString(0);
            quote.BidPrice = cur.getDouble(1);
            quote.AskPrice = cur.getDouble(2);
            quote.QuoteDate = new Date(cur.getLong(3));
        }
        catch (Exception ex)
        {
            quote = null;
            Log.e(Constants.APPNAME, "ConstructQuote: " + ex.getMessage());
        }
        return quote;
    }

    private PosPnl ConstructPnl(Cursor cur)
    {
        PosPnl pnl;
        try{
            pnl = new PosPnl();
            pnl.Ccy = cur.getString(0);
            pnl.Age = cur.getInt(1);
            pnl.Pnl = cur.isNull(2) ? 0 : cur.getDouble(2);
            pnl.Pos = cur.isNull(3) ? 0 : cur.getDouble(3);
            pnl.BookMid = cur.isNull(4) ? 0 : cur.getDouble(4);
            pnl.MarketMid = cur.isNull(5) ? 0: cur.getDouble(5);
            pnl.Starred = cur.getInt(6)== 0? false:true;
            pnl.Timestamp = new Date(cur.getLong(7));
            pnl.PosUsd = cur.isNull(8) ? 0 : cur.getDouble(2);
            pnl.Id = cur.getInt(9);
        }
        catch (Exception ex)
        {
            pnl = null;
            Log.e(Constants.APPNAME, "ConstructPnl: " + ex.getMessage());
        }
        return pnl;
    }


    private Deal ConstructDeal(Cursor cur)
    {
        Deal deal;
        try {
            deal = new Deal();
            deal.DealCcy = cur.getString(0);
            deal.BaseCcy = cur.getString(1);
            deal.Quantity = cur.getInt(2);
            deal.Buy = cur.getInt(3) == 0 ? false : true;
            deal.Price = cur.getDouble(4);
            deal.Date = new Date(cur.getLong(5));
        }
        catch (Exception ex)
        {
            deal = null;
            Log.e(Constants.APPNAME, "ConstructDeal: " + ex.getMessage());
            // Do nothing , just ignore the deal
        }
        return deal;
    }


}