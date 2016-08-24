package com.anz.org.fxtradepnlapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anz.org.fxtradepnlapp.Common.Deal;
import com.anz.org.fxtradepnlapp.Common.Quote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        View quotesTabView = inflater.inflate(R.layout.tab3, container, false);

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

        prepareListData(quotesTabView);
        return quotesTabView;
    }

    /*
     * Preparing the list data
     */
    private void prepareListData(View quotesTabView) {
        List<Quote> lstQuotes = new ArrayList<Quote>();
        //lstQuotes =((MainActivity)this.getActivity()).dataSource.GetQuote("USD",0);
        Quote q1 = new Quote();
        q1.BaseCcy="USD";
        q1.QuoteCcy="AUD";
        q1.BidPrice=5.443453534;
        q1.AskPrice=0.563453534;
        lstQuotes.add(q1);

        Quote q2 = new Quote();
        q2.BaseCcy="GBP";
        q2.QuoteCcy="AUD";
        q2.BidPrice=5.443453534;
        q2.AskPrice=0.563453534;
        lstQuotes.add(q2);

        Quote q3 = new Quote();
        q3.BaseCcy="AUD";
        q3.QuoteCcy="GBP";
        q3.BidPrice=5.443453534;
        q3.AskPrice=0.563453534;
        lstQuotes.add(q3);

        for(int i = 0;i<lstQuotes.size();i++) {
            TextView txtListSymbol = (TextView) quotesTabView
                    .findViewById(R.id.txtListSymbol);

            txtListSymbol.setText(lstQuotes.get(i).BaseCcy + "/" + lstQuotes.get(i).QuoteCcy);

            TextView txtListBid = (TextView) quotesTabView
                    .findViewById(R.id.txtListBid);

            txtListBid.setText(String.format("%.5f",  lstQuotes.get(i).BidPrice));

            TextView txtListAsk = (TextView) quotesTabView
                    .findViewById(R.id.txtListAsk);

            txtListAsk.setText(String.format("%.5f",  lstQuotes.get(i).AskPrice));

            ImageView imgBaseCCYFlag = (ImageView) quotesTabView.findViewById(R.id.imgBaseCCYFlag);
            ImageView imgQuoteCCYFlag = (ImageView) quotesTabView.findViewById(R.id.imgQuoteCCYFlag);

            String pngBaseCCYName = lstQuotes.get(i).BaseCcy.toLowerCase();
            imgBaseCCYFlag.setImageResource(quotesTabView.getContext().getResources().getIdentifier("drawable/" + pngBaseCCYName, null, quotesTabView.getContext().getPackageName()));

            String pngQuoteCCYName = lstQuotes.get(i).QuoteCcy.toLowerCase();
            imgQuoteCCYFlag.setImageResource(quotesTabView.getContext().getResources().getIdentifier("drawable/" + pngQuoteCCYName, null, quotesTabView.getContext().getPackageName()));
        }
    }
}
