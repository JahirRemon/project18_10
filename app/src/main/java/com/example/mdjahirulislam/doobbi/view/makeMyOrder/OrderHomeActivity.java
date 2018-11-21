package com.example.mdjahirulislam.doobbi.view.makeMyOrder;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.TabPageAdapter;
import com.example.mdjahirulislam.doobbi.controller.adapter.ViewPagerAdapter;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.order.OrderListActivity;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabIconsAss;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabIconsWhite;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabNames;

public class OrderHomeActivity extends AppCompatActivity {

    private static final String TAG = OrderHomeActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView totalPriceTV;

    private TabPageAdapter tabPageAdapter;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_home );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Log.d( TAG, "onCreate: " );
        totalPriceTV = findViewById( R.id.totalPriceTV );

        sessionManager = new SessionManager( this );
        Log.d( TAG, "onCreate: " +sessionManager.getUserId());
        if (sessionManager.getTotalPrice()!=null) {
            totalPriceTV.setText( "Tk. " + sessionManager.getTotalPrice() );
        }else {
            totalPriceTV.setText( "Tk. 00" );
        }

        viewPager = (ViewPager) findViewById( R.id.makeOrderViewPager );
        setupViewPager( viewPager );

        tabLayout = (TabLayout) findViewById( R.id.home_category_tab_layout );
        tabLayout.setupWithViewPager( viewPager );

        tabPageAdapter = new TabPageAdapter( this, getSupportFragmentManager(),tabNames,0 );
        viewPager.setAdapter( tabPageAdapter );

        tabLayout.setupWithViewPager( viewPager );

//        tab text color change
//        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));


        setupTabIcons();


        tabLayout.setOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                switch (position) {
                    case 0:
                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
//                        tabLayout.setTabTextColors(R.color.colorBlack,R.color.colorWhite);

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


//        TextView customTab = (TextView) LayoutInflater.from( this ).inflate( R.layout.custom_tab, null );
//        customTab.setText( "Home" );
//        customTab.setTextColor( getResources().getColor( R.color.colorPrimary ) );
//        customTab.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.ic_home_red_700_24dp, 0, 0 );
//        tabLayout.getTabAt( 0 ).setCustomView( customTab );
//
//
//        TextView tabThree = (TextView) LayoutInflater.from( this ).inflate( R.layout.custom_tab, null );
//        tabThree.setText( "Notification" );
//        tabThree.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.ic_notifications_black_24dp, 0, 0 );
//        tabLayout.getTabAt( 1 ).setCustomView( tabThree );
//
//        TextView tabFour = (TextView) LayoutInflater.from( this ).inflate( R.layout.custom_tab, null );
//        tabFour.setText( "Smart Study" );
//        tabFour.setCompoundDrawablesWithIntrinsicBounds( 0, R.drawable.ic_import_contacts_black_24dp, 0, 0 );
//        tabLayout.getTabAt( 2 ).setCustomView( tabFour );


    }

    public void goToOrderListActivity(View view) {

        startActivity( new Intent( OrderHomeActivity.this, OrderSummaryActivity.class ) );
        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected(item);
    }


}
