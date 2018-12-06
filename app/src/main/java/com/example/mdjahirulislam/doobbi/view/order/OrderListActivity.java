package com.example.mdjahirulislam.doobbi.view.order;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.OrderListAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.ItemClickListener;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetOrderListResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.authentication.LoginActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.OrderSummaryActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.SelectItemActivity;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_TEMPORARY_ORDER_LIST;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_USER_DETAILS;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions._INTENT_FROM;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;

public class OrderListActivity extends AppCompatActivity {

    private static final String TAG = OrderListActivity.class.getSimpleName();
    private String userPhone;
    private RecyclerView orderListRV;
    private TextView pleaseLoginTV;

    private ConnectionAPI connectionApi;
    private LinearLayoutManager mLayoutManager;
    private OrderListAdapter mAdapter;
    private ArrayList<GetOrderListResponseModel.Order> orderList;
    private SessionManager sessionManager;
    private GetOrderListResponseModel getOrderListResponseModel;


    private void initialization() {
        orderListRV = findViewById( R.id.orderListRV );
        pleaseLoginTV = findViewById( R.id.orderListLoginTV );
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        orderList = new ArrayList<>();
        sessionManager = new SessionManager( this );

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_list );
        //        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        initialization();

        if (!sessionManager.isLoggedIn()) {
            orderListRV.setVisibility( View.GONE );
            pleaseLoginTV.setVisibility( View.VISIBLE );
            pleaseLoginTV.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity( new Intent( OrderListActivity.this, LoginActivity.class )
                            .putExtra( _INTENT_FROM, OrderListActivity.class.getSimpleName() ) );
                    finish();
                }
            } );


        } else {
            orderListRV.setVisibility( View.VISIBLE );
            pleaseLoginTV.setVisibility( View.GONE );
            userPhone = DBFunctions.getUserInformation().getPhone();

            mLayoutManager = new LinearLayoutManager( this );
            mAdapter = new OrderListAdapter( this, orderList );
            orderListRV.setAdapter( mAdapter );

            orderListRV.setLayoutManager( mLayoutManager );


            Thread temporaryOrderListThread = new Thread( new GetTemporaryOrderListThread() );
            temporaryOrderListThread.start();

            Functions.ProgressDialog( this );
            Functions.showDialog();


            orderListRV.addOnItemTouchListener( new ItemClickListener( this, orderListRV, new ItemClickListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {
                    startActivity( new Intent( OrderListActivity.this, OrderDetailsActivity.class ).putExtra( "position", position ) );
                    Log.d( TAG, "onClick: " + position );
                }

                @Override
                public void onLongClick(View view, int position) {

                }
            } ) );

        }

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

    public class GetTemporaryOrderListThread implements Runnable {

        @Override
        public void run() {
            orderList.clear();
            RequestBody password = RequestBody.create( MultipartBody.FORM, API_ACCESS_PASSWORD );
            RequestBody user = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
            RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_GET_TEMPORARY_ORDER_LIST );
            RequestBody phone = RequestBody.create( MultipartBody.FORM, String.valueOf( userPhone ) );

            Log.d( TAG, "run: data: " + phone + "\ndataModel: " + userPhone );

            final Call<GetOrderListResponseModel> insertUserResponseModelCallBack = connectionApi.getTemporaryOrderList( password, user,
                    function, phone );


            insertUserResponseModelCallBack.enqueue( new Callback<GetOrderListResponseModel>() {
                @Override
                public void onResponse(Call<GetOrderListResponseModel> call, Response<GetOrderListResponseModel> response) {
                    if (response.code() == 200) {
                        getOrderListResponseModel = response.body();
                        String status = getOrderListResponseModel.getStatus();
                        Log.d( TAG, "Status : " + getOrderListResponseModel.toString() );
                        // success code is 100
                        if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) {

                            if(getOrderListResponseModel.getOrders().size()>0){

                                for (GetOrderListResponseModel.Order myOrder: getOrderListResponseModel.getOrders()){
                                    orderList.add( myOrder );
                                    Log.d( TAG, "onResponse: " +myOrder.toString());
                                }

                                mAdapter = new OrderListAdapter( OrderListActivity.this, orderList );
                                orderListRV.setAdapter( mAdapter );

                            }else {
                                Toast.makeText( OrderListActivity.this, "Some thing is wrong!!!", Toast.LENGTH_SHORT ).show();
                            }



                        }
                        // NO ORDER FOUND code is 101
                        else if (status.equalsIgnoreCase( NO_USER_FOUND_CODE )) {
                            String error_msg = getOrderListResponseModel.getDetail();
                            Log.d( TAG, "onResponse: " + error_msg );
//                            Toast.makeText( OrderListActivity.this, error_msg, Toast.LENGTH_SHORT ).show();
                            orderListRV.setVisibility( View.GONE );
                            pleaseLoginTV.setVisibility( View.VISIBLE );
                            pleaseLoginTV.setText( error_msg );

                        }
                        else {
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
                public void onFailure(Call<GetOrderListResponseModel> call, Throwable t) {

                    Log.d( TAG, "onFailure: " + t.getLocalizedMessage() );
                    Log.d( TAG, "onFailure: " + t.toString() );
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                    hideDialog();

                }
            } );
        }
    }
}
