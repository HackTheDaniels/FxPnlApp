package com.anz.org.fxtradepnlapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.anz.org.fxtradepnlapp.Common.PosPnl;
import com.anz.org.fxtradepnlapp.Common.PosPnlHistory;
import com.anz.org.fxtradepnlapp.SqlLite.AppDataSource;
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
    GraphView graph;
    Spinner sp;
    AppDataSource dataSource;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       ;
        //Returning the layout file after inflating
        PnlTabView = inflater.inflate(R.layout.tab4, container, false);

        graph = (GraphView) PnlTabView.findViewById(R.id.graph);

        List<String> currencies = new ArrayList<String>();
        currencies.add("AUD");
        currencies.add("GBP");
        currencies.add("CHN");
        currencies.add("EUR");
//
        sp = (Spinner) PnlTabView.findViewById(R.id.spinner);
        ArrayAdapter<String> ard=new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,currencies);
        sp.setAdapter(ard);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                plotGraph();
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        return PnlTabView;
    }

    private void plotGraph()
    {
        TextView textView = (TextView)sp.getSelectedView();
        if(textView ==null)
            return;
        String ccy = "'" + textView.getText().toString() + "'";
        DataPoint[] dp = GetData(ccy);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dp);
        graph.addSeries(series);
    }

    public DataPoint[] GetData(String ccy)
    {
        if(dataSource == null)
            dataSource = ((MainActivity)this.getActivity()).dataSource;
        List<PosPnlHistory> pnlsHistory = dataSource.GetPosPnlHistory(ccy);
        DataPoint[] dp= new DataPoint[pnlsHistory.size()];
        for(int i=0; i < pnlsHistory.size() ; i++)
        {
            dp[i] = new DataPoint(i,pnlsHistory.get(i).Pnl);
        }
        return  dp;
    }
}

