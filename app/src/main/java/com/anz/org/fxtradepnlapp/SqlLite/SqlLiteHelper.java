package com.anz.org.fxtradepnlapp.SqlLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell on 8/20/2016.
 */
public class SqlLiteHelper extends SQLiteOpenHelper
{
    private static final String DB_NAME = "ANZFxApp";
    private static final int DB_VERSION = 1;

    public  SqlLiteHelper(Context ctx)
    {
        super(ctx, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DealTable.CreateTable(db);
        QuoteTable.CreateTable(db);
        PosPnLTable.CreateTable(db);
        PosPnlHistoryTable.CreateTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        DealTable.OnUpgrade(db,oldVersion,newVersion);
        QuoteTable.OnUpgrade(db,oldVersion,newVersion);
    }
}
