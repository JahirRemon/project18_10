package com.example.mdjahirulislam.doobbi.view.priceList;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.TabPageAdapter;
import com.example.mdjahirulislam.doobbi.controller.adapter.ViewPagerAdapter;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabIconsAss;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabIconsWhite;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabNames;

public class PriceListActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private TabPageAdapter tabPageAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_price_list );

        //        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewPager = (ViewPager) findViewById( R.id.itemPriceListVP );
        setupViewPager( viewPager );

        tabLayout = (TabLayout) findViewById( R.id.price_list_tab_layout );
        tabLayout.setupWithViewPager( viewPager );

        tabPageAdapter = new TabPageAdapter( this, getSupportFragmentManager(),tabNames,2 );
        viewPager.setAdapter( tabPageAdapter );

        tabLayout.setupWithViewPager( viewPager );

//        tab text color change
//        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));


        setupTabIcons();
//        tabIconsWhite = new int[Functions.tabIconsWhite.length];
//        tabIconsAss = new int[Functions.tabIconsAss.length];
//        tabIconsWhite = Functions.tabIconsWhite;
//        tabIconsAss = Functions.tabIconsAss;

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


    private void setupTabIcons() {

        tabLayout.getTabAt( 0 ).setIcon( tabIconsWhite[0] );
        tabLayout.getTabAt( 1 ).setIcon( tabIconsAss[1] );
        tabLayout.getTabAt( 2 ).setIcon( tabIconsAss[2] );
        tabLayout.getTabAt( 3 ).setIcon( tabIconsAss[3] );
        tabLayout.getTabAt( 4 ).setIcon( tabIconsAss[4] );

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter( getSupportFragmentManager() );
        viewPager.setAdapter( adapter );
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
