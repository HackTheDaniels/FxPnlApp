package com.anz.org.fxtradepnlapp.SqlLite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.anz.org.fxtradepnlapp.Common.Constants;

/**
 * Created by dell on 8/20/2016.
 */
public class DealTable
{
    public static final String TableName = "Deal";

    public static final String BaseCcy = "BaseCcy";
    public static final String DealCcy = "DealCcy";
    public static final String Timestamp = "Timestamp";
    public static final String Quantity = "Quantity";
    public static final String Buy = "Buy";
    public static final String Price = "Price";

    public static final String[] AllDbColumns = {BaseCcy, DealCcy, Timestamp, Quantity, Buy, Price};

    public static void CreateTable(SQLiteDatabase db)
    {
        Log.i(Constants.APPNAME, "Creating Table" + TableName);
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TableName + " ( "
                + BaseCcy + " TEXT NOT NULL, "
                + DealCcy + " TEXT NOT NULL, "
                + Price + " DECIMAL(12,5), "
                + Quantity + " INTEGER, "
                + Buy + " BOOLEAN, "
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
