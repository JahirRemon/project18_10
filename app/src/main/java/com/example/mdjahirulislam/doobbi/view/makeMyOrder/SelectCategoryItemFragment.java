package com.example.mdjahirulislam.doobbi.view.makeMyOrder;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.schedule.ScheduleListActivity;
import com.hanks.htextview.base.HTextView;

import java.util.function.BinaryOperator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCategoryItemFragment extends Fragment {
    private static final String TAG = SelectCategoryItemFragment.class.getSimpleName();

    private static final String ARG_POSITION = "position";
    private ImageView plusIV;
    private ImageView minusIV;
    private TextView quantityTV;
    private TextView priceTV;
    private TextView totalPriceTV;

    private int quantity = 0;
    private int regularPrice = 8;
    private int premiumPrice = 10;
    private int totalPrice = 0;

    private SessionManager sessionManager;

    public SelectCategoryItemFragment() {
        // Required empty public constructor
    }

    public interface onTotalPriceListener {
        public void setPrice(int price, int operator);
    }

    private onTotalPriceListener totalPriceListener;


    public static SelectCategoryItemFragment newInstance(int position) {
        SelectCategoryItemFragment fragment = new SelectCategoryItemFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_POSITION, position );
        fragment.setArguments( args );
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            totalPriceListener = (onTotalPriceListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_select_category_item, container, false );
        plusIV = view.findViewById( R.id.singlePlusIV );
        minusIV = view.findViewById( R.id.singleMinusIV );
        quantityTV = view.findViewById( R.id.singleQuantityTV );
        priceTV = view.findViewById( R.id.singlePriceTV );
        totalPriceTV = view.findViewById( R.id.singleTotalPriceTV );

        sessionManager = new SessionManager( getContext() );


        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );


        quantityTV.setText( String.valueOf( quantity ) );
        priceTV.setText( String.valueOf( regularPrice ) );
        totalPriceTV.setText( String.valueOf( regularPrice*quantity ) );


//        ((SeekBar) view.findViewById(R.id.seekbar)).setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                quantityTV.setProgress(progress / 100f);
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

        plusIV.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = plusIV.getWidth();
                float height = plusIV.getHeight();
//                Log.d( TAG, "onTouch 1: plusIV ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        plusIV.setAlpha( 0.5f );
//                        Log.d( TAG, "onTouch 2: plusIV ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            plusIV.setAlpha( 1.0f );
                            quantity++;
                            totalPrice = regularPrice*quantity;
                            quantityTV.setText( String.valueOf( quantity ) );
                            totalPriceTV.setText( String.valueOf( totalPrice ) );
                            totalPriceListener.setPrice( regularPrice, 1 );

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        plusIV.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        plusIV.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );

        minusIV.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = minusIV.getWidth();
                float height = minusIV.getHeight();
//                Log.d( TAG, "onTouch 1: classTimeLL ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        minusIV.setAlpha( 0.5f );
//                        Log.d( TAG, "onTouch 2: classTimeLL ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            minusIV.setAlpha( 1.0f );
                            if (quantity>0)
                            {
                                quantity--;
                                totalPriceListener.setPrice( regularPrice, 2 );

                            }else {
                                quantity =0;
                            }
                            totalPrice = regularPrice*quantity;
                            Log.d( TAG, "onTouch: "+totalPrice );
                            quantityTV.setText( String.valueOf( quantity ) );
                            totalPriceTV.setText( String.valueOf( totalPrice ) );

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        minusIV.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        minusIV.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );



    }
}
