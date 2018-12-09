package com.example.mdjahirulislam.doobbi.controller.requestThread;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetUserDetailsResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.OrderHomeActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.OrderSummaryActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.SelectItemActivity;
import com.example.mdjahirulislam.doobbi.view.order.OrderListActivity;

import io.realm.Realm;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_USER_DETAILS;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;

public class GetUserDetailsThread extends Thread {

    private final String TAG = GetUserDetailsThread.class.getSimpleName();

    private ConnectionAPI connectionApi;
    private Context context;
    private String userPhone;
    private GetUserDetailsResponseModel userDetailsResponseModel;
    private SessionManager sessionManager;
    private String from;
    private Realm mRealm = null;
    private InsertUserDataModel userDetailsModelDB;




    public GetUserDetailsThread(Context context,String  userPhone,String from) {
        this.context = context;
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        this.userPhone  = userPhone;
        sessionManager = new SessionManager(context);
        this.from = from;
        Log.d(TAG, "GetUserDetailsThread: phone: "+ userPhone);
    }

    @Override
    public void run() {
        super.run();
        RequestBody password = RequestBody.create( MultipartBody.FORM,API_ACCESS_PASSWORD );
        RequestBody user = RequestBody.create( MultipartBody.FORM,API_ACCESS_ID );
        RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_USER_DETAILS );
        RequestBody phone = RequestBody.create( MultipartBody.FORM, String.valueOf( userPhone ) );

        Log.d( TAG, "run: data: "+phone+"\ndataModel: " +userPhone);

        final Call<GetUserDetailsResponseModel> insertUserResponseModelCallBack = connectionApi.getUserDetails(  password,user,
                function,phone);


        insertUserResponseModelCallBack.enqueue( new Callback<GetUserDetailsResponseModel>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponseModel> call, Response<GetUserDetailsResponseModel> response) {
                if (response.code() == 200) {
                    userDetailsResponseModel = response.body();
                    String status = userDetailsResponseModel.getStatus().toString();
                    Log.d( TAG, "Status : " + status );
                    // deny code is 100
                    if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) {

                        if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) { // Status success code = 100

                            mRealm = Realm.getDefaultInstance();

                            mRealm.beginTransaction();

                            userDetailsModelDB = mRealm.createObject( InsertUserDataModel.class );
                            userDetailsModelDB.setClint_id( userDetailsResponseModel.getCid() );
                            userDetailsModelDB.setName( userDetailsResponseModel.getCustomerName() );
                            userDetailsModelDB.setPhone( userDetailsResponseModel.getPhone() );
                            userDetailsModelDB.setEmail( userDetailsResponseModel.getEmail() );
                            userDetailsModelDB.setAddress( userDetailsResponseModel.getAddress() );
                            userDetailsModelDB.setClint_image_path( userDetailsResponseModel.getFileLink() );
                            userDetailsModelDB.setFlat_no(userDetailsResponseModel.getFlatNo());
                            userDetailsModelDB.setRoad_no(userDetailsResponseModel.getRoadNo());
                            userDetailsModelDB.setHouse_no(userDetailsResponseModel.getHouseNo());
                            userDetailsModelDB.setArea(userDetailsResponseModel.getArea());
                            userDetailsModelDB.setLatitude(userDetailsResponseModel.getLatitude());
                            userDetailsModelDB.setLongitude(userDetailsResponseModel.getLongitude());

                            mRealm.commitTransaction();


                            sessionManager.setLogin( true );
                            sessionManager.setUserID( userDetailsResponseModel.getCid() );


                            Intent myIntent = new Intent();
                            if (from.equalsIgnoreCase( HomeActivity.class.getSimpleName() )) {
                                myIntent = new Intent( context, HomeActivity.class );

                            }
                            else if (from.equalsIgnoreCase( OrderHomeActivity.class.getSimpleName() )) {
                                myIntent = new Intent( context, OrderHomeActivity.class );

                            }
                            else if (from.equalsIgnoreCase( SelectItemActivity.class.getSimpleName() )) {
                                myIntent = new Intent( context, SelectItemActivity.class );

                            }
                            else if (from.equalsIgnoreCase( OrderSummaryActivity.class.getSimpleName() )) {
                                myIntent = new Intent( context, OrderSummaryActivity.class );

                            }else if (from.equalsIgnoreCase( OrderListActivity.class.getSimpleName() )) {
                                myIntent = new Intent( context, OrderListActivity.class );

                            }
                            context.startActivity( myIntent );
//                            finish();


                        }



                    }
                    // deny code is 101
                    else if (status.equalsIgnoreCase(NO_USER_FOUND_CODE)) {
                        String error_msg = userDetailsResponseModel.getDetail();
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
            public void onFailure(Call<GetUserDetailsResponseModel> call, Throwable t) {

                Log.d( TAG, "onFailure: "+t.getLocalizedMessage() );
                Log.d( TAG, "onFailure: "+t.toString());
                Log.d( TAG, "onFailure: "+t.getMessage());
                hideDialog();

            }
        } );


    }

}