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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
        prepareListData();

        return currenciesTabView;
    }

    private void initializeList(){

        listAdapter = new ExpandableListAdapter(currenciesTabView.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        Display newDisplay = this.getActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        expListView.setIndicatorBounds(width-210, width);

    }

    /*
     * Preparing the list data
     */
    public void prepareListData() {


        if( ((MainActivity)this.getActivity()) == null) return;
        if( ((MainActivity)this.getActivity()).dataSource == null) return;
        List<PosPnl> lstPosPnl = new ArrayList<PosPnl>();
        lstPosPnl =((MainActivity)this.getActivity()).dataSource.GetPosPnl(null);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        Date currentDate = new Date();

        // Adding data to exp list
        for(int i = 0;i<lstPosPnl.size();i++) {
            Date pnlDate = lstPosPnl.get(i).Timestamp;
            long diffInMillis = currentDate.getTime() - pnlDate.getTime();
            long ageInMinutes = TimeUnit.MILLISECONDS.toMinutes(diffInMillis);
            String age="";
            age = ""+ageInMinutes;
            if(ageInMinutes > 120) {
                age =">120";
            }

            listDataHeader.add(lstPosPnl.get(i).Ccy + "," + String.format("%.2f",  lstPosPnl.get(i).Pnl) + "," + age);

            List<String> chData = new ArrayList<String>();
            chData.add( String.format("%.2f",  lstPosPnl.get(i).Pos) + "," + String.format("%.4f",lstPosPnl.get(i).BookMid) + "," + String.format("%.2f",lstPosPnl.get(i).PosUsd) + "," + String.format("%.4f",lstPosPnl.get(i).MarketMid) + "," + lstPosPnl.get(i).Ccy );

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

    public void OnClickMessage(View v)
    {
        if(null !=MyService.mMyServiceHandler) {
        Message msg=new Message();
            msg.what=0;
            msg.obj="AUD";
            MyService.mMyServiceHandler.sendMessage(msg);
        }

    }
}
