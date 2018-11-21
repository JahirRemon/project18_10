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

public class TabPageAdapter extends FragmentPagerAdapter {


    private static final String TAG = TabPageAdapter.class.getSimpleName();
    private final String[] TITLES;
    private final int from;


    private Context mContext;

    public TabPageAdapter(Context context, FragmentManager fm, String[] titles, int from) {
        super( fm );
        this.mContext = context;
        this.TITLES = titles;
        this.from = from;

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TITLES[position];
    }

    @Override
    public int getCount() {
        return TITLES.length;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d( TAG, "count getItem: " + position );
        if (from == 0) {
            return CategoryListFragment.newInstance( position );
        } else if (from == 2){
            return PriceListFragment.newInstance( position );
        }else {
            return SelectCategoryItemFragment.newInstance( position );
        }


    }

}
