package com.anz.org.fxtradepnlapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by dell on 8/25/2016.
 */
public class PnlTab extends Fragment  {
    View PnlTabView;
    TextView dateView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       ;
        //Returning the layout file after inflating
        PnlTabView = inflater.inflate(R.layout.tab4, container, false);

        GraphView graph = (GraphView) PnlTabView.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        graph.addSeries(series);
        List<String> currencies = new ArrayList<String>();
        currencies.add("AUD");
        currencies.add("GBP");
        currencies.add("CHN");
        currencies.add("EUR");
//
        // Spinner sp = (Spinner) PnlTabView.findViewById(R.id.spinner1);
         //ArrayAdapter<Object> ard=new ArrayAdapter<Object>(this,R.layout.support_simple_spinner_dropdown_item,currencies);


        return PnlTabView;
    }

    public void GetDate()
    {

    }

}
