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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.anz.org.fxtradepnlapp.Common.Quote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 8/19/2016.
 */
public class QuotesTab extends Fragment{

    TextView dateView;
    Button previousBtn;
    Button nextBtn;
    SimpleDateFormat df;
    String formattedDate;
    ExpandableListView quotesExpListView;
    View quotesTabView;
    List<String> listDataHeader = new ArrayList<String>();
    QuotesListAdapter quotesListAdapter;
    public static Handler mUiQuoteHandler = null;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handleServiceMsg();

        //Returning the layout file after inflating
        quotesTabView = inflater.inflate(R.layout.tab3, container, false);

        //set the current date
        dateView = (TextView) quotesTabView.findViewById(R.id.dateValueQ);
        final Calendar c = Calendar.getInstance();

        df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        dateView.setText(formattedDate);

        //set the previous date
        previousBtn=(Button) quotesTabView.findViewById(R.id.btnPrevDateQ);
        previousBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());

                dateView.setText(formattedDate);
            }
        });

        //set the next date
        nextBtn=(Button) quotesTabView.findViewById(R.id.btnNextDateQ);
        nextBtn.setOnClickListener(new Button.OnClickListener() {

            @Override
            public void onClick(View v) {

                c.add(Calendar.DATE, 1);
                formattedDate = df.format(c.getTime());

                dateView.setText(formattedDate);
            }
        });

        quotesExpListView = (ExpandableListView) quotesTabView.findViewById(R.id.quotesListView);
        quotesExpListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener(){
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        prepareListData();
        return quotesTabView;
    }

    private void initializeList(){

        quotesListAdapter = new QuotesListAdapter(quotesTabView.getContext(), listDataHeader, new HashMap<String, List<String>>());
        // setting list adapter
        quotesExpListView.setAdapter(quotesListAdapter);
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        List<Quote> lstQuotes = new ArrayList<Quote>();
        if(((MainActivity)this.getActivity()==null)) return;
        if(((MainActivity)this.getActivity()).dataSource == null) return;
        lstQuotes =((MainActivity)this.getActivity()).dataSource.GetQuote(null,0);

        listDataHeader = new ArrayList<String>();

        for(int i = 0;i<lstQuotes.size();i++) {
            listDataHeader.add(lstQuotes.get(i).BaseCcy + "," + lstQuotes.get(i).QuoteCcy + "," + String.format("%.5f",  lstQuotes.get(i).BidPrice) + "," + String.format("%.5f",  lstQuotes.get(i).AskPrice));
        }

        initializeList();
    }


    private void handleServiceMsg()
    {
        mUiQuoteHandler = new Handler() // Receive messages from service class
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
