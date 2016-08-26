package com.anz.org.fxtradepnlapp;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * Created by dell on 8/19/2016.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter{

    private Context _context;
    private List<String> _listDataHeader; // header titles
    private List<String> originalListDataHeader;
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this.originalListDataHeader = listDataHeader;
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

        final String childText = (String) getChild(groupPosition, childPosition);
        String[] childContents = childText.split(",");

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.currencylist_item, null);
        }

        TextView txtPosValue = (TextView) convertView
                .findViewById(R.id.posValue);

        txtPosValue.setText(childContents[0]);
        if(childContents[0].startsWith("-"))
            txtPosValue.setTextColor(Color.RED);
        else
            txtPosValue.setTextColor(Color.BLACK);

        TextView txtBookBidValue = (TextView) convertView
                .findViewById(R.id.bookBidValue);

        txtBookBidValue.setText(childContents[1]);
        if(childContents[1].startsWith("-"))
            txtBookBidValue.setTextColor(Color.RED);
        else
            txtBookBidValue.setTextColor(Color.BLACK);

        TextView txtPosUSDValue = (TextView) convertView
                .findViewById(R.id.posUSDValue);

        txtPosUSDValue.setText("$ "+childContents[2]);
        if(childContents[2].startsWith("-"))
            txtPosUSDValue.setTextColor(Color.RED);
        else
            txtPosUSDValue.setTextColor(Color.BLACK);

        TextView txtMarketBidValue = (TextView) convertView
                .findViewById(R.id.marketBidValue);

        txtMarketBidValue.setText(childContents[3]);
        if(childContents[3].startsWith("-"))
            txtMarketBidValue.setTextColor(Color.RED);
        else
            txtMarketBidValue.setTextColor(Color.BLACK);

        TextView txtCurrentCCy = (TextView) convertView
                .findViewById(R.id.currCCy);
        txtPosUSDValue.setText(childContents[4]);
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
            convertView = infalInflater.inflate(R.layout.currencylist_group, null);
        }

        TextView lblListCCY = (TextView) convertView
                .findViewById(R.id.lblListCCY);

        TextView lblListCCYPnL = (TextView) convertView
                .findViewById(R.id.lblListCCYPnL);

        TextView lblListAge = (TextView) convertView
                .findViewById(R.id.lblListAge);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgViewFlag);

        lblListCCY.setTypeface(null, Typeface.BOLD);
        lblListCCY.setText(headerContents[0]);

        lblListCCYPnL.setTypeface(null, Typeface.BOLD);
        lblListCCYPnL.setText(headerContents[1]);

        lblListAge.setText(headerContents[2] +"min");

        String pngName = headerContents[0].toLowerCase();
        imageView.setImageResource(this._context.getResources().getIdentifier("drawable/" + pngName, null, this._context.getPackageName()));

        return convertView;
    }

    public void filterData(String query){

        query = query.toLowerCase();
       // Log.v("MyListAdapter", String.valueOf(continentList.size()));
        _listDataHeader.clear();

        if(query.isEmpty()){
            _listDataHeader.addAll(originalListDataHeader);
        }
        else {

            for(String header: originalListDataHeader){
                if(header.contains(query))
                    _listDataHeader.add(header);
            }
        }

       // Log.v("MyListAdapter", String.valueOf(continentList.size()));
        notifyDataSetChanged();

    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
