package com.example.mdjahirulislam.doobbi.view.makeMyOrder;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.SelectedCategoryItemPriceAdapter;
import com.example.mdjahirulislam.doobbi.controller.adapter.TabPageAdapter;
import com.example.mdjahirulislam.doobbi.controller.adapter.ViewPagerAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.CategoryItemsModel;
import java.util.ArrayList;

import io.realm.Realm;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions._PROGRESS_TIME_IN_MILLISECOND;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;

public class SelectItemActivity extends AppCompatActivity implements SelectedCategoryItemPriceAdapter.OnTotalPriceListener {

    private static final String TAG = SelectItemActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView totalPriceTV;
    private int totalPrice = 0;
    private SessionManager sessionManager;


    private ConnectionAPI connectionApi;
    private ArrayList<String> tabNameList;
    private ArrayList<String> tabIdList;
    private int position;
    private ArrayList<CategoryItemsModel> items;
    private Realm mRealm;


    private TabPageAdapter tabPageAdapter;
    private final int[] tabIconsAss = {
            R.drawable.ic_tab_shirt_white,
            R.drawable.ic_tab_pant_white,
            R.drawable.ic_tab_child_white,
            R.drawable.ic_tab_suit_white,
            R.drawable.ic_tab_accessories_white


    };

    private final int[] tabIconsWhite = {
            R.drawable.ic_tab_shirt_ass,
            R.drawable.ic_tab_pant_ass,
            R.drawable.ic_tab_child_ass,
            R.drawable.ic_tab_suit_ass,
            R.drawable.ic_tab_accessories_ass


    };

//    private final String[] tabNames = {"Shirt", "Pant", "Panjabi", "Suit", "Accessories"};


    @Override
    protected void onResume() {
        super.onResume();
//        Functions.hideDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_select_item );
        Functions.ProgressDialog( this );
        Functions.showDialog();
        Thread progressThread = new Thread(new Functions.ProgressThread( ));
        progressThread.start();

//        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        tabNameList = new ArrayList<>();
        tabIdList = new ArrayList<>();
        position = getIntent().getIntExtra( "position", 0 );
        items = (ArrayList<CategoryItemsModel>) getIntent().getSerializableExtra( "itemsModel" );
        mRealm = Realm.getDefaultInstance();

        totalPriceTV = findViewById( R.id.totalSelectedPriceTV );
        sessionManager = new SessionManager( this );

        viewPager = (ViewPager) findViewById( R.id.selectedViewPager );
        setupViewPager( viewPager );

        tabLayout = (TabLayout) findViewById( R.id.select_category_tab_layout );
        tabLayout.setupWithViewPager( viewPager );
        if (items.size() > 4) {
            tabLayout.setTabMode( TabLayout.MODE_SCROLLABLE );
        } else {
            tabLayout.setTabMode( TabLayout.MODE_FIXED );
        }
        for (int i = 0; i < items.size(); i++) {
            tabNameList.add( items.get( i ).getItemName() );
            tabIdList.add( items.get( i ).getItemId() );
        }


        tabPageAdapter = new TabPageAdapter( this, getSupportFragmentManager(), tabNameList, tabIdList, 1 );
        viewPager.setAdapter( tabPageAdapter );

//        tabLayout.setupWithViewPager( viewPager );

        totalPrice = DBFunctions.getAllOrderHistoryTotalPrice();
        totalPriceTV.setText( "Tk. " + String.valueOf( totalPrice ) + ".00" );

        viewPager.setCurrentItem( position );


        tabLayout.setOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position) {
                    case 0:
                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
                        break;
                    case 1:
                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
                        break;
                    case 2:
                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
                        break;
                    case 3:
                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
                        break;
                    case 4:
                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
                        break;
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                int position = tab.getPosition();

                switch (position) {
                    case 0:
                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
                        break;
                    case 1:
                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
                        break;
                    case 2:
                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
                        break;
                    case 3:
                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
                        break;

                    case 4:
                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
                        break;
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        } );

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter( getSupportFragmentManager() );
        viewPager.setAdapter( adapter );
    }

    private void setupTabIcons() {

        tabLayout.getTabAt( 0 ).setIcon( tabIconsWhite[0] );
        tabLayout.getTabAt( 1 ).setIcon( tabIconsAss[1] );
        tabLayout.getTabAt( 2 ).setIcon( tabIconsAss[2] );
        tabLayout.getTabAt( 3 ).setIcon( tabIconsAss[3] );
        tabLayout.getTabAt( 4 ).setIcon( tabIconsAss[4] );

    }

    public void goToOrderSummaryActivity(View view) {

        startActivity( new Intent( SelectItemActivity.this, OrderSummaryActivity.class ) );
    }


//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Log.d( TAG, "onBackPressed: " );
//        startActivity( new Intent( SelectItemActivity.this, OrderHomeActivity.class ).setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP ) );
//        finish();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent( this, OrderHomeActivity.class );
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity( intent );

            finish();
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected( item );
    }

    @Override
    public void setPrice() {
//                    totalPriceTV.setText( "Interface - Tk. "+ String.valueOf( DBFunctions.getAllOrderHistoryTotalPrice() ) );

        Functions.setAnimationNumber( totalPriceTV, "Total Tk. ", totalPrice, DBFunctions.getAllOrderHistoryTotalPrice(), ".00", 1000 );
    }

    public static class ProgressThread extends Thread {
        public void run() {

            try {
                Log.d( TAG, "run: progress thread" );
                Thread.sleep( 2000 );
            } catch (InterruptedException e) {

            } finally {
                hideDialog();
            }
        }

    }

}
