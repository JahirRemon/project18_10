package com.example.mdjahirulislam.doobbi.controller.requestThread;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.model.responseModel.InsertResponseModel;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_ADD_USER_IMAGE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_DENY_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;

public class InsertNewUserPhotoThread extends Thread {


    private final String TAG = InsertNewUserPhotoThread.class.getSimpleName();

    private ConnectionAPI connectionApi;
    private Context context;
    private String imagePath;
    private String mPhone;
    private File file;
    private InsertResponseModel registrationResponseModel;


    public InsertNewUserPhotoThread(Context context,String imagePath,String phone) {
        this.context = context;
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        this.imagePath  = imagePath;
        this.mPhone = phone;
    }

    @Override
    public void run() {
        super.run();

        file = new File(imagePath);
        Log.d( TAG, "run: phone: "+mPhone );

        RequestBody password = RequestBody.create( MultipartBody.FORM,API_ACCESS_PASSWORD );
        RequestBody user = RequestBody.create( MultipartBody.FORM,API_ACCESS_ID );
        RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_ADD_USER_IMAGE );
        RequestBody phone = RequestBody.create( MultipartBody.FORM, mPhone  );
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part image = MultipartBody.Part.createFormData("image",file.getName(),requestBody);

//        final Call<InsertResponseModel> insertUserResponseModelCallBack = connectionApi.image(  API_ACCESS_ID,
//                API_ACCESS_FUNCTION_ADD_USER_IMAGE );

        final Call<InsertResponseModel> insertUserResponseModelCallBack = connectionApi.uploadNewUserPhoto( password , user,
                function ,phone, image);

        Log.d( TAG, "run: " +insertUserResponseModelCallBack.toString());
        insertUserResponseModelCallBack.enqueue( new Callback<InsertResponseModel>() {
            @Override
            public void onResponse(Call<InsertResponseModel> call, Response<InsertResponseModel> response) {
                if (response.code() == 200) {
                    registrationResponseModel = response.body();
                    String status = registrationResponseModel.getStatus().toString();
                    Log.d( TAG, "Status : " + status );
                    if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) {
//                        Intent myIntent = new Intent( context, HomeActivity.class );
//                        context.startActivity( myIntent );
                        Toast.makeText( context, "Photo Upload Successful :) ", Toast.LENGTH_SHORT ).show();
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
