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

import com.anz.org.fxtradepnlapp.Common.Deal;
import com.anz.org.fxtradepnlapp.Common.PosPnl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 8/19/2016.
 */
public class DealsTab extends Fragment{

    TextView dateView;
    Button previousBtn;
    Button nextBtn;
    SimpleDateFormat df;
    String formattedDate;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<String>> listDataChild = new HashMap<String, List<String>>();
    View dealsTabView;
    DealsListAdapter listAdapter;
    public static Handler mUiDealHandler = null;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handleServiceMsg();

        //Returning the layout file after inflating
        dealsTabView = inflater.inflate(R.layout.tab2, container, false);

        //set the current date
        dateView = (TextView) dealsTabView.findViewById(R.id.dateValue);
        final Calendar c = Calendar.getInstance();

        df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        dateView.setText(formattedDate);

        //set the previous date
        previousBtn=(Button) dealsTabView.findViewById(R.id.btnPrevDate);
        previousBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());

                dateView.setText(formattedDate);
            }
        });

        //set the next date
        nextBtn=(Button) dealsTabView.findViewById(R.id.btnNextDate);
        nextBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, 1);
                formattedDate = df.format(c.getTime());

                dateView.setText(formattedDate);
            }
        });

        expListView = (ExpandableListView) dealsTabView.findViewById(R.id.lDealsExp);
        prepareListData();
        return dealsTabView;
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {

        List<Deal> lstDeals = new ArrayList<Deal>();

        if( ((MainActivity)this.getActivity()) == null) return;
        if(((MainActivity)this.getActivity()).dataSource == null) return;
        lstDeals =((MainActivity)this.getActivity()).dataSource.GetDeals(0,null);


        List<String> chData = null;
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data\
        for(int i = 0;i<lstDeals.size();i++) {


            if(!listDataHeader.contains(lstDeals.get(i).BaseCcy + "," + lstDeals.get(i).DealCcy)) {
                listDataHeader.add(lstDeals.get(i).BaseCcy + "," + lstDeals.get(i).DealCcy);
                if(chData != null) {
                    listDataChild.put(listDataHeader.get(listDataHeader.size()-2), chData);
                }
                chData= new ArrayList<String>();

            }

            SimpleDateFormat dt = new SimpleDateFormat("hh:mm:ss");
            String formattedTime = dt.format(lstDeals.get(i).Date);
            if(chData != null)
                chData.add( String.format("%.2f",  lstDeals.get(i).Price) + "," + lstDeals.get(i).Quantity + "," + formattedTime + "," +  lstDeals.get(i).Buy);


        }
        if(chData != null) {
            listDataChild.put(listDataHeader.get(listDataHeader.size() - 1), chData);
        }
        initializeList();
    }

    private void initializeList(){

        listAdapter = new DealsListAdapter(dealsTabView.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        Display newDisplay = this.getActivity().getWindowManager().getDefaultDisplay();
        int width = newDisplay.getWidth();
        expListView.setIndicatorBounds(width-180, width);

    }

    private void handleServiceMsg()
    {
        mUiDealHandler = new Handler() // Receive messages from service class
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
