package com.anz.org.fxtradepnlapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Created by dell on 8/25/2016.
 */
public class DealsListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    public DealsListAdapter(Context context, List<String> listDataHeader,
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

        final String childText = (String) getChild(groupPosition, childPosition);
        String[] childContents = childText.split(",");

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.deallist_item, null);
        }

        TextView txtTime = (TextView) convertView
                .findViewById(R.id.txtTime);
        txtTime.setText(childContents[2]);

//        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageDeal);
//        String pngName = "usd";
//        imageView.setImageResource(this._context.getResources().getIdentifier("drawable/" + pngName, null, this._context.getPackageName()));

        TextView txtQnt = (TextView) convertView
                .findViewById(R.id.txtQnt);
        txtQnt.setText(childContents[0] );

        if(childContents[0].startsWith("-"))
            txtQnt.setTextColor(Color.RED);
        else
            txtQnt.setTextColor(Color.BLACK);

        TextView txtPriceByQnt = (TextView) convertView
                .findViewById(R.id.txtPriceByQnt);
        txtPriceByQnt.setText("/" + childContents[1] );

        TextView txtBuyOrSell = (TextView) convertView
                .findViewById(R.id.txtBuyOrSell);

        if(childContents[3].equalsIgnoreCase("true"))
             txtBuyOrSell.setText("Buy");
        else
            txtBuyOrSell.setText("Sell");

//        if(childContents[0].startsWith("-"))
//            txtPosValue.setTextColor(Color.RED);
//        else
//            txtPosValue.setTextColor(Color.BLACK);
//
//        TextView txtBookBidValue = (TextView) convertView
//                .findViewById(R.id.bookBidValue);
//
//        txtBookBidValue.setText(childContents[1]);
//        if(childContents[1].startsWith("-"))
//            txtBookBidValue.setTextColor(Color.RED);
//        else
//            txtBookBidValue.setTextColor(Color.BLACK);
//
//        TextView txtPosUSDValue = (TextView) convertView
//                .findViewById(R.id.posUSDValue);
//
//        txtPosUSDValue.setText("$"+childContents[2]);
//        if(childContents[2].startsWith("-"))
//            txtPosUSDValue.setTextColor(Color.RED);
//        else
//            txtPosUSDValue.setTextColor(Color.BLACK);
//
//        TextView txtMarketBidValue = (TextView) convertView
//                .findViewById(R.id.marketBidValue);
//
//        txtMarketBidValue.setText(childContents[3]);
//        if(childContents[3].startsWith("-"))
//            txtMarketBidValue.setTextColor(Color.RED);
//        else
//            txtMarketBidValue.setTextColor(Color.BLACK);

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
            convertView = infalInflater.inflate(R.layout.dealslist_group, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.imgBaseCCYFlag);
        String pngName = headerContents[0].toLowerCase();
        imageView.setImageResource(this._context.getResources().getIdentifier("drawable/" + pngName, null, this._context.getPackageName()));


        TextView lblListCCY = (TextView) convertView
                .findViewById(R.id.txtListSymbol);


        lblListCCY.setTypeface(null, Typeface.BOLD);
        lblListCCY.setText(headerContents[0] + "/" + headerContents[1]);

        ImageView imageView2 = (ImageView) convertView.findViewById(R.id.imgSecondCCYFlag);
        String pngName2 = headerContents[1].toLowerCase();
        imageView2.setImageResource(this._context.getResources().getIdentifier("drawable/" + pngName2, null, this._context.getPackageName()));



        return convertView;
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
