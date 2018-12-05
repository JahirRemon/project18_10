package com.example.mdjahirulislam.doobbi.view.priceList;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.TabPageAdapter;
import com.example.mdjahirulislam.doobbi.controller.adapter.ViewPagerAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetItemWisePriceResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetTadItemResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.OrderHomeActivity;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_CATEGORY;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabIconsAss;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabIconsWhite;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabNames;

public class PriceListActivity extends AppCompatActivity {

    private static final String TAG = PriceListActivity.class.getSimpleName();

    private TabLayout tabLayout;
    private TabPageAdapter tabPageAdapter;
    private ViewPager viewPager;

    private ConnectionAPI connectionApi;
    private ArrayList<String> itemNameList;
    private ArrayList<String> itemIdList;
    private GetItemWisePriceResponseModel priceResponseModel;

    private Thread getItemWisePriceThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_price_list );

        //        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );

//        getItemWisePriceThread = new Thread( new GetItemWisePriceThread() );
        itemIdList = new ArrayList<>(  );
        itemNameList = new ArrayList<>(  );

        try {
            getItemWisePriceThread.start();
            Functions.ProgressDialog( this );
            Functions.showDialog();
//            Log.d( TAG, "onCreate: try" + getCategoryItemThread.isAlive() );
//            getCategoryItemThread.interrupt();


        } catch (Exception e) {
            Log.d( TAG, "onCreate: " + e.getLocalizedMessage() );
        } finally {
            Log.d( TAG, "onCreate: finally" + getItemWisePriceThread.isAlive() );

//            getTabNameThread.interrupt();
            Log.d( TAG, "onCreate: finally " + Thread.currentThread().isAlive() );
        }

//        viewPager = (ViewPager) findViewById( R.id.itemPriceListVP );
//        setupViewPager( viewPager );
//
//        tabLayout = (TabLayout) findViewById( R.id.price_list_tab_layout );
//        tabLayout.setupWithViewPager( viewPager );

//        tabPageAdapter = new TabPageAdapter( this, getSupportFragmentManager(),tabNames,2 );
//        viewPager.setAdapter( tabPageAdapter );

//        tabLayout.setupWithViewPager( viewPager );

//        tab text color change
//        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));


//        setupTabIcons();
//        tabIconsWhite = new int[Functions.tabIconsWhite.length];
//        tabIconsAss = new int[Functions.tabIconsAss.length];
//        tabIconsWhite = Functions.tabIconsWhite;
//        tabIconsAss = Functions.tabIconsAss;

//        tabLayout.setOnTabSelectedListener( new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int position = tab.getPosition();
//
//                switch (position) {
//                    case 0:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
////                        tabLayout.setTabTextColors(R.color.colorBlack,R.color.colorWhite);
//
//                        break;
//                    case 1:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
//                        break;
//                    case 2:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
//                        break;
//                    case 3:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
//                        break;
//                    case 4:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsWhite[position] );
//                        break;
//                }
//
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//                int position = tab.getPosition();
//
//                switch (position) {
//                    case 0:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
//                        break;
//                    case 1:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
//                        break;
//                    case 2:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
//                        break;
//                    case 3:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
//                        break;
//
//                    case 4:
//                        tabLayout.getTabAt( position ).setIcon( tabIconsAss[position] );
//                        break;
//                }
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        } );


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

//    class GetItemWisePriceThread implements Runnable {
//        public void run() {
//            Log.d( TAG, "run: thread is running..." );
//            Log.d( TAG, "run: "+Thread.currentThread().isAlive() );
//
//            RequestBody password = RequestBody.create( MultipartBody.FORM, API_ACCESS_PASSWORD );
//            RequestBody user = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
//            RequestBody itemID = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
//            RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_GET_CATEGORY );
//
////                Log.d( TAG, "run: data: " + phone + "\ndataModel: " + userPhone );
//
//            final Call<GetItemWisePriceResponseModel> insertUserResponseModelCallBack = connectionApi.getCategoryItemWisePrice( password, user,itemID,
//                    function );
//
//
//            itemNameList.clear();
//            itemIdList.clear();
//            insertUserResponseModelCallBack.enqueue( new Callback<GetItemWisePriceResponseModel>() {
//                @Override
//                public void onResponse(Call<GetItemWisePriceResponseModel> call, Response<GetItemWisePriceResponseModel> response) {
//                    if (response.code() == 200) {
//                        priceResponseModel = response.body();
//                        String status = priceResponseModel.getStatus();
//                        Log.d( TAG, "Status : " + priceResponseModel.toString() );
//                        // deny code is 100
//                        if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) { // Status success code = 100
//
//                            for (int i = 0; i < priceResponseModel.getItem().size(); i++) {
//                                itemNameList.add( priceResponseModel.getItem().get( i ).getServiceName() );
//                                itemIdList.add( priceResponseModel.getItem().get( i ).getPriceId() );
//                            }
//
//                            tabPageAdapter = new TabPageAdapter( PriceListActivity.this, getSupportFragmentManager(), itemNameList,itemIdList, 2 );
//                            viewPager.setAdapter( tabPageAdapter );
//
//                        }
//                        // deny code is 101
//                        else if (status.equalsIgnoreCase( NO_USER_FOUND_CODE )) {
//                            String error_msg = priceResponseModel.getDetail();
//                            Log.d( TAG, "onResponse: " + error_msg );
//                        } else {
//                            Log.d( TAG, "onResponse: Some Is Wrong" );
//                        }
//
//
//                    } else {
////                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
//                        Log.d( TAG, "onResponse: Server Error response.code ===> " + response.code() );
//                    }
//                    Log.d( TAG, "onResponse: " + response.body() + " \n\n" + response.raw() + " \n\n" + response.toString() + " \n\n" + response.errorBody() );
//                    hideDialog();
//
//
//                }
//
//                @Override
//                public void onFailure(Call<GetItemWisePriceResponseModel> call, Throwable t) {
//
//                    Log.d( TAG, "onFailure: " + t.getLocalizedMessage() );
//                    Log.d( TAG, "onFailure: " + t.toString() );
//                    Log.d( TAG, "onFailure: " + t.getMessage() );
//                    hideDialog();
//
//                }
//            } );
//
//        }
//    }

}
