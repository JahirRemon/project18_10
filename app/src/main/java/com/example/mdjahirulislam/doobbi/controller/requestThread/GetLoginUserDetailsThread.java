package com.example.mdjahirulislam.doobbi.controller.requestThread;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetUserDetailsResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_LOGIN_FUNCTION;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;

public class GetLoginUserDetailsThread  extends Thread {

    private final String TAG = InsertNewUserThread.class.getSimpleName();

    private ConnectionAPI connectionApi;
    private Context context;
    private String userPhone;
    private String userPassword;
    private GetUserDetailsResponseModel userDetailsResponseModel;


    public GetLoginUserDetailsThread(Context context,String  userPhone, String userPassword) {
        this.context = context;
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        this.userPhone  = userPhone;
        this.userPassword = userPassword;
    }

    @Override
    public void run() {
        super.run();
        RequestBody password = RequestBody.create( MultipartBody.FORM,API_ACCESS_PASSWORD );
        RequestBody user = RequestBody.create( MultipartBody.FORM,API_ACCESS_ID );
        RequestBody function = RequestBody.create( MultipartBody.FORM,API_ACCESS_LOGIN_FUNCTION );
        RequestBody phone = RequestBody.create( MultipartBody.FORM, String.valueOf( userPhone ) );
        RequestBody userPass = RequestBody.create( MultipartBody.FORM, String.valueOf( userPassword ) );

        Log.d( TAG, "run: data: "+phone+"\ndataModel: " +userPhone);

        final Call<GetUserDetailsResponseModel> insertUserResponseModelCallBack = connectionApi.getLogin(  password,user,
                function,phone,userPass);


        insertUserResponseModelCallBack.enqueue( new Callback<GetUserDetailsResponseModel>() {
            @Override
            public void onResponse(Call<GetUserDetailsResponseModel> call, Response<GetUserDetailsResponseModel> response) {
                if (response.code() == 200) {
                    userDetailsResponseModel = response.body();
                    String status = userDetailsResponseModel.getStatus().toString();
                    Log.d( TAG, "Status : " + status );
                    // deny code is 100
                    if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) {
                        Intent myIntent = new Intent( context, HomeActivity.class );
                        context.startActivity( myIntent );



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