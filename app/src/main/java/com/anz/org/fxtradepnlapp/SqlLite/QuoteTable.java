package com.anz.org.fxtradepnlapp.SqlLite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.anz.org.fxtradepnlapp.Common.Constants;

/**
 * Created by dell on 8/19/2016.
 */
public class QuoteTable
{
    public static final String TableName = "Quote";

    public static final String BaseCcy = "BaseCcy";
    public static final String QuoteCcy = "QuoteCcy";
    public static final String BidPrice = "BidPrice";
    public static final String AskPrice = "AskPrice";
    public static final String Timestamp = "TimeStamp";

    public static final String[] AllDbColumns = {BaseCcy, QuoteCcy, BidPrice, AskPrice, Timestamp};

    public static void CreateTable(SQLiteDatabase db)
    {
        Log.i(Constants.APPNAME, "Creating Table" + TableName);
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TableName + " ( "
                + BaseCcy + " TEXT NOT NULL, "
                + QuoteCcy + " TEXT NOT NULL, "
                + BidPrice + " DECIMAL(12,5), "
                + AskPrice + " DECIMAL(12,5), "
                + Timestamp + " INTEGER )";
        db.execSQL(sql);
    }

    public static void OnUpgrade(SQLiteDatabase db, int from, int to)
    {
        Log.i(Constants.APPNAME, "Upgrading table:" + TableName + " from " + from + "to" + to);
        db.execSQL("DROP TABLE IF EXISTS " + TableName);
        CreateTable(db);
    }
}
