package com.example.mdjahirulislam.doobbi.controller.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.CategoryListFragment;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.SelectCategoryItemFragment;
import com.example.mdjahirulislam.doobbi.view.priceList.PriceListFragment;

import java.util.ArrayList;

public class TabPageAdapter extends FragmentPagerAdapter {


    private static final String TAG = TabPageAdapter.class.getSimpleName();
    private final ArrayList<String> TITLES;
    private final ArrayList<String> tabID;
    private final int from;


    private Context mContext;

    public TabPageAdapter(Context context, FragmentManager fm, ArrayList<String> titles,ArrayList<String> titlesID, int from) {
        super( fm );
        this.mContext = context;
        this.TITLES = titles;
        this.tabID = titlesID;
        this.from = from;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES.get( position );
    }

    @Override
    public int getCount() {
        return TITLES.size();
    }

    @Override
    public Fragment getItem(int position) {

        Log.d( TAG, "count getItem: " + Integer.parseInt( tabID.get( position ) ) );
        if (from == 0) {
            return CategoryListFragment.newInstance( Integer.parseInt( tabID.get( position ) ) );
        } else if (from == 2){
            return PriceListFragment.newInstance( Integer.parseInt( tabID.get( position ) ) );
        }else {
            return SelectCategoryItemFragment.newInstance( Integer.parseInt( tabID.get( position ) ) );
        }


    }

}
