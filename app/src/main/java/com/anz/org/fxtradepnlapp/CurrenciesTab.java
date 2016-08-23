package com.anz.org.fxtradepnlapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 8/19/2016.
 */
public class CurrenciesTab extends Fragment{

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    TextView dateView;
    Button previousBtn;
    Button nextBtn;
    SimpleDateFormat df;
    String formattedDate;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        View currenciesTabView = inflater.inflate(R.layout.tab1, container, false);

        //set the current date
        dateView = (TextView) currenciesTabView.findViewById(R.id.dateValue);
        final Calendar c = Calendar.getInstance();

        df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        dateView.setText(formattedDate);

        //set the previous date
        previousBtn=(Button) currenciesTabView.findViewById(R.id.btnPrevDate);
        previousBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());

                dateView.setText(formattedDate);
            }
        });

        //set the next date
        nextBtn=(Button) currenciesTabView.findViewById(R.id.btnNextDate);
        nextBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, 1);
                formattedDate = df.format(c.getTime());

                dateView.setText(formattedDate);
            }
        });

        // get the listview
        expListView = (ExpandableListView) currenciesTabView.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(currenciesTabView.getContext(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);
        expListView.setIndicatorBounds(expListView.getRight()+380, expListView.getWidth());
        return currenciesTabView;
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {

        ArrayList<PosPnl> lstPosPnl = new ArrayList<PosPnl>();
        PosPnl p1 = new PosPnl();
        p1.Id = 1;
        p1.Ccy = "USD";
        p1.Pnl = 2.3;
        p1.Pos = 0.63;
        p1.PosUsd = -0.64;
        p1.Age = 4;
        p1.BookBid = 0.76;
        p1.MarketBid = -0.78;
        lstPosPnl.add(p1);

        PosPnl p2 = new PosPnl();
        p2.Id = 2;
        p2.Ccy = "AUD";
        p2.Pnl = 3.4;
        p2.Pos = -0.73;
        p2.PosUsd = 0.74;
        p2.Age = 4;
        p2.BookBid = -0.86;
        p2.MarketBid = 0.88;
        lstPosPnl.add(p2);

        PosPnl p3 = new PosPnl();
        p3.Id = 2;
        p3.Ccy = "GBP";
        p3.Pnl = 3.4;
        p3.Pos = -0.234;
        p3.PosUsd = -0.232;
        p3.Age = 4;
        p3.BookBid = 0.86;
        p3.MarketBid = 0.88;
        lstPosPnl.add(p3);

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data\
        for(int i = 0;i<lstPosPnl.size();i++) {
            listDataHeader.add(lstPosPnl.get(i).Ccy + "," + lstPosPnl.get(i).Pnl);

            List<String> chData = new ArrayList<String>();
            chData.add(lstPosPnl.get(i).Pos + "," + lstPosPnl.get(i).BookBid + "," + lstPosPnl.get(i).PosUsd + "," + lstPosPnl.get(i).MarketBid);

            listDataChild.put(listDataHeader.get(i), chData);
        }
    }
}
