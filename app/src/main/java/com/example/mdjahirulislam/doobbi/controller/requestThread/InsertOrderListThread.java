package com.example.mdjahirulislam.doobbi.controller.requestThread;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderDataModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.InsertUserResponseModel;
import com.example.mdjahirulislam.doobbi.view.schedule.ScheduleListActivity;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_DENY_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_ADD_USER;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_INSERT_ORDER_LIST;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;

public class InsertOrderListThread extends Thread {

    private final String TAG = InsertOrderListThread.class.getSimpleName();

    private ConnectionAPI connectionApi;
    private Context context;
    private String  orderList;
    private String noOfOrder;
    private String phoneNo;
    private InsertUserResponseModel registrationResponseModel;


    public InsertOrderListThread(Context context, String orderList, String noOfOrder, String phoneNo) {
        this.context = context;
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        this.orderList = orderList;
        this.noOfOrder = noOfOrder;
        this.phoneNo = phoneNo;
        Log.d( TAG, "InsertOrderListThread: \norder--> "+orderList+"\nno_of_item--> "+noOfOrder+"\nphone--> "+phoneNo );

    }

    @Override
    public void run() {
        super.run();
        RequestBody password = RequestBody.create( MultipartBody.FORM,API_ACCESS_PASSWORD );
        RequestBody user = RequestBody.create( MultipartBody.FORM,API_ACCESS_ID );
        RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_INSERT_ORDER_LIST );
        RequestBody phone = RequestBody.create( MultipartBody.FORM, phoneNo );
        RequestBody noOfItems = RequestBody.create( MultipartBody.FORM, noOfOrder );
        RequestBody data = RequestBody.create( MultipartBody.FORM, String.valueOf( orderList ) );


        Log.d( TAG, "run: data: "+data.toString()+"\norderList: " + orderList.toString() );

        final Call<InsertUserResponseModel> insertUserResponseModelCallBack = connectionApi.submitOrder(  password,user,
                function,phone,noOfItems,data);


        insertUserResponseModelCallBack.enqueue( new Callback<InsertUserResponseModel>() {
            @Override
            public void onResponse(Call<InsertUserResponseModel> call, Response<InsertUserResponseModel> response) {
                if (response.code() == 200) {
                    registrationResponseModel = response.body();
                    String status = registrationResponseModel.getStatus().toString();
                    Log.d( TAG, "Status : " + status );
                    if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) {       //success code = 100
                        Intent myIntent = new Intent( context, ScheduleListActivity.class );
                        context.startActivity( myIntent );

                        DBFunctions.DeleteTempOrderTsble();

                        Toast.makeText( context, "Order Insert Successful", Toast.LENGTH_SHORT ).show();
                    } else if (status.equalsIgnoreCase( API_ACCESS_DENY_CODE)) {
                        String error_msg = registrationResponseModel.getDetail();
                        Log.d( TAG, "onResponse: " + error_msg );
                    }else {
                        Log.d( TAG, "onResponse: Some Is Wrong" );
                    }


                } else {
//                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                    Log.d( TAG, "onResponse: Server Error response.code ===> " + response.code() );
                }
                Log.d( TAG, "onResponse: "+ response.body()+" \n\n"+response.raw()+" \n\n"+response.toString()+" \n\n"+response.errorBody() );
                hideDialog();

            }

            @Override
            public void onFailure(Call<InsertUserResponseModel> call, Throwable t) {

                Log.d( TAG, "onFailure: "+t.getLocalizedMessage() );
                Log.d( TAG, "onFailure: "+t.toString());
                Log.d( TAG, "onFailure: "+t.getMessage());
                hideDialog();

            }
        } );


    }


}
