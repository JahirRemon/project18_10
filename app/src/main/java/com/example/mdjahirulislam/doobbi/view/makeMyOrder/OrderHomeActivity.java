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
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderHistoryDBModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetTadItemResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.order.OrderListActivity;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_CATEGORY;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_LOGIN;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;
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

    private ConnectionAPI connectionApi;
    private ArrayList<String> tabNameList;
    private ArrayList<String> tabIdList;
    private GetTadItemResponseModel tadItemResponseModel;
    private Realm mRealm;
    private int totalPrice =0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_home );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

        Thread getTabNameThread = new Thread( new GetTabNameThread() );
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        mRealm = Realm.getDefaultInstance();
        tabNameList = new ArrayList<>();
        tabIdList = new ArrayList<>();
        totalPriceTV = findViewById( R.id.totalPriceTV );


        try {
            getTabNameThread.start();
            Functions.ProgressDialog( OrderHomeActivity.this );
            Functions.showDialog();
//            getTabNameThread.interrupt();


        } catch (Exception e) {
            Log.d( TAG, "catch: " + e.getLocalizedMessage() );
        }

        totalPriceTV.setText( "Tk. "+DBFunctions.getAllOrderHistoryTotalPrice() );

        viewPager = (ViewPager) findViewById( R.id.makeOrderViewPager );
        setupViewPager( viewPager );

        tabLayout = (TabLayout) findViewById( R.id.home_category_tab_layout );
        tabLayout.setupWithViewPager( viewPager );
        Log.d( TAG, "onCreate: " + tabNameList );




//        tab text color change
//        tabLayout.setTabTextColors(Color.parseColor("#000000"), Color.parseColor("#ffffff"));


//        setupTabIcons();


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

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter( getSupportFragmentManager() );
        viewPager.setAdapter( adapter );
    }


    public void goToOrderListActivity(View view) {

        startActivity( new Intent( OrderHomeActivity.this, OrderSummaryActivity.class ) );
//        finish();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent( this, HomeActivity.class );
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity( intent );
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent( this, HomeActivity.class );
            intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
            startActivity( intent );

            finish();
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected( item );
    }


    class GetTabNameThread implements Runnable {
        public void run() {
//            Log.d( TAG, "run: thread is running..." );
//            Log.d( TAG, "run: " + Thread.currentThread().isAlive() );

            RequestBody password = RequestBody.create( MultipartBody.FORM, API_ACCESS_PASSWORD );
            RequestBody user = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
            RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_GET_CATEGORY );

//                Log.d( TAG, "run: data: " + phone + "\ndataModel: " + userPhone );

            final Call<GetTadItemResponseModel> insertUserResponseModelCallBack = connectionApi.getTabItem( password, user,
                    function );


            tabNameList.clear();
            tabIdList.clear();
            insertUserResponseModelCallBack.enqueue( new Callback<GetTadItemResponseModel>() {
                @Override
                public void onResponse(Call<GetTadItemResponseModel> call, Response<GetTadItemResponseModel> response) {
                    if (response.code() == 200) {
                        tadItemResponseModel = response.body();
                        String status = tadItemResponseModel.getStatus();
                        Log.d( TAG, "Status : " + tadItemResponseModel.toString() );
                        // deny code is 100
                        if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) { // Status success code = 100

                            for (int i = 0; i < tadItemResponseModel.getCategory().size(); i++) {
                                tabNameList.add( tadItemResponseModel.getCategory().get( i ).getName() );
                                tabIdList.add( tadItemResponseModel.getCategory().get( i ).getId() );
                            }
                            hideDialog();

                            tabPageAdapter = new TabPageAdapter( OrderHomeActivity.this, getSupportFragmentManager(), tabNameList, tabIdList, 0 );
                            viewPager.setAdapter( tabPageAdapter );
                            tabLayout.setupWithViewPager( viewPager );

                        }
                        // deny code is 101
                        else if (status.equalsIgnoreCase( NO_USER_FOUND_CODE )) {
                            String error_msg = tadItemResponseModel.getDetail();
                            Log.d( TAG, "onResponse: " + error_msg );
                        } else {
                            Log.d( TAG, "onResponse: Some Is Wrong" );
                        }


                    } else {
//                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                        Log.d( TAG, "onResponse: Server Error response.code ===> " + response.code() );
                    }
                    Log.d( TAG, "onResponse: " + response.body() + " \n\n" + response.raw() + " \n\n" + response.toString() + " \n\n" + response.errorBody() );
                    hideDialog();


                }

                @Override
                public void onFailure(Call<GetTadItemResponseModel> call, Throwable t) {

                    Log.d( TAG, "onFailure: " + t.getLocalizedMessage() );
                    Log.d( TAG, "onFailure: " + t.toString() );
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                    hideDialog();

                }
            } );

        }
    }

}
