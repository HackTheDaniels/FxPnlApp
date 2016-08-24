package com.anz.org.fxtradepnlapp.Service;

/**
 * Created by dell on 8/23/2016.
 */
public class WeightedPrice
{
    public String Ccy;
    public int SellQty;
    public int BuyQty;
    public double SellPrice;
    public double BuyPrice;

    public void Clear()
    {
        SellPrice = 0;
        SellQty = 0;
        BuyPrice = 0;
        BuyQty = 0;
    }
}
