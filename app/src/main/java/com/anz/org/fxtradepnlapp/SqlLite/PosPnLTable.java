package com.anz.org.fxtradepnlapp.SqlLite;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.anz.org.fxtradepnlapp.Common.Constants;

/**
 * Created by dell on 8/21/2016.
 */
public class PosPnLTable
{
    public static final String TableName = "PosPnlTable";

    public static final String Id = "_id";
    public static final String Ccy = "Ccy";
    public static final String Pos = "Pos";
    public static final String PosUsd = "PosUsd";
    public static final String Age = "Age";
    public static final String Pnl = "Pnl";
    public static final String BookMid = "BookMidPrice";
    public static final String MarketMid = "MarketMidPrice";
    public static final String Starred = "Starred";
    public static final String Timestamp = "Timestamp";

    public static void CreateTable(SQLiteDatabase db)
    {
        Log.i(Constants.APPNAME, "Creating Table" + TableName);
        String sql = "CREATE TABLE IF NOT EXISTS "
                + TableName + " ( "
                + Id + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Ccy + " TEXT NOT NULL, "
                + Pos + " DECIMAL(12,3) NOT NULL, "
                + PosUsd + " DECIMAL(12,3) NOT NULL, "
                + Pnl + " DECIMAL(8,2), "
                + Starred + " BOOLEAN, "
                + Age + " INTEGER, "
                + BookMid + " DECIMAL(12,5), "
                + MarketMid + " DECIMAL(12,5), "
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
