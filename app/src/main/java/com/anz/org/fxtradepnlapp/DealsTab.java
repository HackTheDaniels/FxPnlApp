package com.anz.org.fxtradepnlapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        View dealsTabView = inflater.inflate(R.layout.tab2, container, false);

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
        return dealsTabView;
    }

    /*
     * Preparing the list data
     */
    private void prepareListData() {
        List<Deal> lstDeals = new ArrayList<Deal>();
        //lstDeals =((MainActivity)this.getActivity()).dataSource.GetDeals(0,"USD");
    }
}
