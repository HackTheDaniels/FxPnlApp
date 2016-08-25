package com.anz.org.fxtradepnlapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

import com.anz.org.fxtradepnlapp.Common.Quote;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dell on 8/25/2016.
 */
public class QuotesListAdapter extends ArrayAdapter<Quote> {

    private Context _context;
    private List<Quote> lQuotes;
    private static LayoutInflater inflater = null;

    public QuotesListAdapter (Context context, int textViewResourceId,List<Quote> _lQuotes) {
        super(context, textViewResourceId, _lQuotes);
        try {
            this._context = context;
            this.lQuotes = _lQuotes;

            inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }
}
