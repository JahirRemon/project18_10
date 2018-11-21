package com.example.mdjahirulislam.doobbi.view.priceList;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mdjahirulislam.doobbi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PriceListFragment extends Fragment {
    private static final String TAG = PriceListFragment.class.getSimpleName();


    private static final String ARG_POSITION = "position";


    public PriceListFragment() {
        // Required empty public constructor
    }

    public static PriceListFragment newInstance(int position) {
        PriceListFragment fragment = new PriceListFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_POSITION, position );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_price_list, container, false );
    }

}
