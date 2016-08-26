package com.anz.org.fxtradepnlapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.anz.org.fxtradepnlapp.Common.Quote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 8/25/2016.
 */
public class QuotesListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private HashMap<String, List<String>> _listDataChild;

    public QuotesListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        String[] headerContents = headerTitle.split(",");
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.quoteslist_group, null);
        }
        TextView txtListSymbol = (TextView) convertView
                .findViewById(R.id.txtListSymbol);

        txtListSymbol.setText(headerContents[1] + "/" + headerContents[0]);

        TextView txtListBid = (TextView) convertView
                .findViewById(R.id.txtListBid);

        txtListBid.setText(headerContents[2]);

        TextView txtListAsk = (TextView) convertView
                .findViewById(R.id.txtListAsk);

        txtListAsk.setText(headerContents[3]);

        ImageView imgBaseCCYFlag = (ImageView) convertView.findViewById(R.id.imgBaseCCYFlag);
        ImageView imgQuoteCCYFlag = (ImageView) convertView.findViewById(R.id.imgQuoteCCYFlag);

        String pngBaseCCYName = headerContents[0].toLowerCase();
        imgBaseCCYFlag.setImageResource(this._context.getResources().getIdentifier("drawable/" + pngBaseCCYName, null, this._context.getPackageName()));

        String pngQuoteCCYName = headerContents[1].toLowerCase();
        imgQuoteCCYFlag.setImageResource(this._context.getResources().getIdentifier("drawable/" + pngQuoteCCYName, null, this._context.getPackageName()));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
