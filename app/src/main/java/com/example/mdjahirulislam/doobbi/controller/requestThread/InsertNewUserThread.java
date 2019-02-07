package com.example.mdjahirulislam.doobbi.controller.requestThread;


import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.InsertResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.google.gson.Gson;

import java.io.File;

import io.realm.Realm;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_ADD_USER;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_DENY_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;


/**
 * Created by mdjahirulislam on 05/04/18.
 */

public class InsertNewUserThread extends Thread {

    private final String TAG = InsertNewUserThread.class.getSimpleName();

    private ConnectionAPI connectionApi;
    private Context context;
    private String dataModel;
    private File file;
    private InsertResponseModel registrationResponseModel;
    private Realm mRealm;
    private InsertUserDataModel userDetailsModelDB;
    private InsertUserDataModel passData;
    private SessionManager sessionManager;




    public InsertNewUserThread(Context context,InsertUserDataModel  dataModel) {
        this.context = context;
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        passData = dataModel;
        Gson gson = new Gson();
        String data = gson.toJson(dataModel);
        Log.d(TAG, "goToScheduleListActivityFromRegistrationActivity: " + data);
        this.dataModel  = data;
        sessionManager = new SessionManager(context);
    }

    @Override
    public void run() {
        super.run();
        final RequestBody password = RequestBody.create( MultipartBody.FORM,API_ACCESS_PASSWORD );
        RequestBody user = RequestBody.create( MultipartBody.FORM,API_ACCESS_ID );
        RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_ADD_USER );
        RequestBody data = RequestBody.create( MultipartBody.FORM, String.valueOf( dataModel ) );

        Log.d( TAG, "run: data: "+data+"\ndataModel: " +dataModel);

        final Call<InsertResponseModel> insertUserResponseModelCallBack = connectionApi.registration(  password,user,
                function,data);


        insertUserResponseModelCallBack.enqueue( new Callback<InsertResponseModel>() {
            @Override
            public void onResponse(Call<InsertResponseModel> call, Response<InsertResponseModel> response) {
                if (response.code() == 200) {
                    registrationResponseModel = response.body();
                    String status = registrationResponseModel.getStatus().toString();
                    Log.d( TAG, "Status : " + status );
                    if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) {



                        mRealm = Realm.getDefaultInstance();

                        mRealm.beginTransaction();

                        userDetailsModelDB = mRealm.createObject(InsertUserDataModel.class);
                        userDetailsModelDB.setClint_id(passData.getClint_id());
                        userDetailsModelDB.setName(passData.getName());
                        userDetailsModelDB.setPhone(passData.getPhone());
                        userDetailsModelDB.setEmail(passData.getEmail());
                        userDetailsModelDB.setAddress(passData.getAddress());
                        userDetailsModelDB.setClint_image_path(passData.getClint_image_path());
                        userDetailsModelDB.setFlat_no(passData.getFlat_no());
                        userDetailsModelDB.setRoad_no(passData.getRoad_no());
                        userDetailsModelDB.setHouse_no(passData.getHouse_no());
                        userDetailsModelDB.setArea(passData.getArea());
                        userDetailsModelDB.setLatitude(passData.getLatitude());
                        userDetailsModelDB.setLongitude(passData.getLongitude());

                        mRealm.commitTransaction();


                        sessionManager.setLogin(true);
                        sessionManager.setUserID(passData.getClint_id());

                        Intent myIntent = new Intent( context, HomeActivity.class );
                        context.startActivity( myIntent );
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
            public void onFailure(Call<InsertResponseModel> call, Throwable t) {

                Log.d( TAG, "onFailure: "+t.getLocalizedMessage() );
                Log.d( TAG, "onFailure: "+t.toString());
                Log.d( TAG, "onFailure: "+t.getMessage());
                hideDialog();

            }
        } );


    }

}