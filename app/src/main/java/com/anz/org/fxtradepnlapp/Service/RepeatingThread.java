package com.anz.org.fxtradepnlapp.Service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.anz.org.fxtradepnlapp.Common.Deal;
import com.anz.org.fxtradepnlapp.Common.Quote;
import com.anz.org.fxtradepnlapp.CurrenciesTab;
import com.anz.org.fxtradepnlapp.DealsTab;
import com.anz.org.fxtradepnlapp.MainActivity;
import com.anz.org.fxtradepnlapp.QuotesTab;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.sql.Time;

public class RepeatingThread implements Runnable {
    private final Handler handler=new Handler();
    private int count=0;
    private Time lastProcessedTime=null;
    private Time  currentTime=java.sql.Time.valueOf("00:00:00");
    private MathProcessor proc;
    public RepeatingThread(MathProcessor processor)
    {
        proc = processor;
    }

    @Override
    public  void run()
    {
        Looper.myLooper();
        count++;
        Log.i("Log","Service Started for " + count +" Time");

        // currentTime = java.sql.Time.valueOf( "00:" +String.valueOf(Calendar.getInstance().get(Calendar.MINUTE)) + ":" +
        //           String.valueOf(Calendar.getInstance().get(Calendar.SECOND)));
        try{
            sendEvent();
            serviceHandler();
            sendRefreshUI();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        lastProcessedTime=currentTime;
        currentTime=new Time(currentTime.getTime()+3000);
        handler.postDelayed(this,30000);
    }

    private void serviceHandler()
    {
       MyService.mMyServiceHandler = new Handler()
        {
            //here we will receive messages from activity(using sendMessage() from activity)
            public void handleMessage(Message msg)
            {
                Log.i("BackgroundThread","handleMessage(Message msg)" );
                switch(msg.what)
                {
                    case 0:
                        if(null != CurrenciesTab.mUiHandler)
                        {
                            Message msgToActivity = new Message();
                            msgToActivity.what = 0;
                            if(true ==MyService.mIsServiceRunning)
                                msgToActivity.obj  = "Request Received. Service is Running"; // you can put extra message here
                            else
                                msgToActivity.obj  = "Request Received. Service is not Running"; // you can put extra message here

                            CurrenciesTab.mUiHandler.sendMessage(msgToActivity);
                        }

                        break;

                    default:
                        break;
                }
            }
        };
    }

    private void sendRefreshUI()
    {
        Message msgToActivity = new Message();
        msgToActivity.what = 0;

        CurrenciesTab.mUiHandler.sendMessage(msgToActivity);
        if(DealsTab.mUiDealHandler != null) {
            Message msgToActivity1 = new Message();
            msgToActivity1.what = 0;
            DealsTab.mUiDealHandler.sendMessage(msgToActivity1);
        }
        if(QuotesTab.mUiQuoteHandler != null) {
            Message msgToActivity2 = new Message();
            msgToActivity2.what = 0;
            QuotesTab.mUiQuoteHandler.sendMessage(msgToActivity2);
        }
    }
    private void sendEvent()
    {
        int cnt=0;
        List<String> rawData=BufferedData.GetData();
        for (String row:rawData)
        {
            String[] line=row.split(",");
            if(line.length != 6)
            {
                Log.e("Error", "Wrong Data");
                return;
            }

            String strTime=line[1].substring(0,8);
            Time  mktTime= java.sql.Time.valueOf(strTime);
            if(currentTime.equals(mktTime) ||(currentTime.after(mktTime) && (lastProcessedTime == null || lastProcessedTime.before(mktTime))))
            {
                if(line[0].equalsIgnoreCase("deal")) {
                    Deal d =new Deal();
                    d.DealCcy=line[2].substring(0,3);
                    d.BaseCcy=line[2].substring(3);
                    d.Quantity=Integer.valueOf(line[3].toString());
                    d.Price=Double.valueOf(line[4].toString());
                    d.Buy = line[5].toString().equalsIgnoreCase("B") ?true:false;
                    d.Date = Calendar.getInstance().getTime();
                    proc.DealEvent(d);
                }
                else {
                    Quote q=new Quote();
                    q.QuoteCcy=line[2].substring(0,3);
                    q.BaseCcy=line[2].substring(3);
                    q.MarketType=line[3].toString();
                    q.BidPrice=Double.valueOf(line[4].toString());
                    q.AskPrice=Double.valueOf(line[5].toString());
                    q.QuoteDate = Calendar.getInstance().getTime();
                    q.MidPrice = Calculator.CalculateMid(q.BidPrice,q.AskPrice);
                    if(q.MarketType.equalsIgnoreCase("AGG")) {
                        proc.QuoteEvent(q);
                    }
                }
                cnt++;
            }

        }
    }
}
