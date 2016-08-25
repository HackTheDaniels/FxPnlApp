package com.anz.org.fxtradepnlapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.anz.org.fxtradepnlapp.Common.PosPnl;
import com.anz.org.fxtradepnlapp.Service.MyService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 8/19/2016.
 */
public class CurrenciesTab extends Fragment{

    public static Handler mUiHandler = null;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    TextView dateView;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>>  listDataChild = new HashMap<String, List<String>>();
    Button serviceBtn;
    View currenciesTabView;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handleServiceMsg();
        //Returning the layout file after inflating
        currenciesTabView = inflater.inflate(R.layout.tab1, container, false);

        serviceBtn = (Button)currenciesTabView.findViewById(R.id.serviceBtn);
        serviceBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //prepareListData();
                onClickSendMessage();
            }
        });
        //set the current date
        dateView = (TextView) currenciesTabView.findViewById(R.id.dateValue);
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        dateView.setText(formattedDate);

        // get the listview
        expListView = (ExpandableListView) currenciesTabView.findViewById(R.id.lvExp);
        //initializeList();
        // preparing list data
        //prepareListData();

        return currenciesTabView;
    }

    private void initializeList(){

        listAdapter = new ExpandableListAdapter(currenciesTabView.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        Display newDisplay = this.getActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        expListView.setIndicatorBounds(width-150, width);

    }

    //send message to service
    public void onClickSendMessage ()
    {
        //only we need a handler to send message to any component.
        //here we will get the handler from the service first, then
        //we will send a message to the service.

        if(null != MyService.mMyServiceHandler)
        {
            //first build the message and send.
            //put a integer value here and get it from the service handler
            //For Example: lets use 0 (msg.what = 0;) for getting service running status from the service
            Message msg = new Message();
            msg.what = 0;
            msg.obj  = "Add your Extra Meaage Here"; // you can put extra message here
            MyService.mMyServiceHandler.sendMessage(msg);
        }
    }
    /*
     * Preparing the list data
     */
    public void prepareListData() {

        List<PosPnl> lstPosPnl = new ArrayList<PosPnl>();
        lstPosPnl =((MainActivity)this.getActivity()).dataSource.GetPosPnl(null);
//        PosPnl p1 = new PosPnl();
//        p1.Id = 1;
//        p1.Ccy = "USD";
//        p1.Pnl = 2.3;
//        p1.Pos = 0.63;
//        p1.PosUsd = -0.64;
//        p1.Age = 60;
//        p1.BookBid = 0.76;
//        p1.MarketBid = -0.78;
//        lstPosPnl.add(p1);
//
//        PosPnl p2 = new PosPnl();
//        p2.Id = 2;
//        p2.Ccy = "AUD";
//        p2.Pnl = 3.4;
//        p2.Pos = -0.73;
//        p2.PosUsd = 0.74;
//        p2.Age = 110;
//        p2.BookBid = -0.86;
//        p2.MarketBid = 0.88;
//        lstPosPnl.add(p2);
//
//        PosPnl p3 = new PosPnl();
//        p3.Id = 2;
//        p3.Ccy = "GBP";
//        p3.Pnl = 3.4;
//        p3.Pos = -0.234;
//        p3.PosUsd = -0.232;
//        p3.Age = 4;
//        p3.BookBid = 0.86;
//        p3.MarketBid = 0.88;
//        lstPosPnl.add(p3);



        // Adding child data\
        for(int i = 0;i<lstPosPnl.size();i++) {
            listDataHeader.add(lstPosPnl.get(i).Ccy + "," + String.format("%.2f",  lstPosPnl.get(i).Pnl) + "," + lstPosPnl.get(i).Age);

            List<String> chData = new ArrayList<String>();
            chData.add( String.format("%.2f",  lstPosPnl.get(i).Pos) + "," + String.format("%.4f",lstPosPnl.get(i).BookMid) + "," + String.format("%.2f",lstPosPnl.get(i).PosUsd) + "," + String.format("%.4f",lstPosPnl.get(i).MarketMid));

            listDataChild.put(listDataHeader.get(i), chData);
        }
        initializeList();
    }

    private void handleServiceMsg()
    {
        mUiHandler = new Handler() // Receive messages from service class
        {
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 0:
                        prepareListData();
                        break;

                    default:
                        break;
                }
            }
        };
    }
}
