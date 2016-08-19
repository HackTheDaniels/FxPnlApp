package com.anz.org.fxtradepnlapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
/**
 * Created by dell on 8/19/2016.
 */
public class Pager extends FragmentStatePagerAdapter{

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                CurrenciesTab tab1 = new CurrenciesTab();
                return tab1;
            case 1:
                DealsTab tab2 = new DealsTab();
                return tab2;
            case 2:
                QuotesTab tab3 = new QuotesTab();
                return tab3;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}
