package com.example.mdjahirulislam.doobbi.controller.requestThread;

import android.content.Context;
import android.util.Log;

import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetTadItemResponseModel;

import java.util.ArrayList;

import io.realm.Realm;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_LOGIN;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabNames;

public class GetTadItemThread extends Thread {

    private final String TAG = GetTadItemThread.class.getSimpleName();

    private Context context;
    private GetTadItemResponseModel tadItemResponseModel;
    private InsertUserDataModel userDetailsModelDB;
    private ConnectionAPI connectionApi;
    private ArrayList<String> tabNameList;
    private ArrayList<String> tabIdList;

    private Realm mRealm = null;


    public GetTadItemThread(Context context) {
        this.context = context;
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );

        tabNameList = new ArrayList<>(  );
        tabIdList = new ArrayList<>(  );
    }

    @Override
    public void run() {
        super.run();
        try {

            RequestBody password = RequestBody.create( MultipartBody.FORM, API_ACCESS_PASSWORD );
            RequestBody user = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
            RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_LOGIN );

//            Log.d( TAG, "run: data: " + phone + "\ndataModel: " + userPhone );

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

                            tabNames = tabNameList;
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
        } finally {

            if (mRealm != null){
                mRealm.close();
            }
        }

    }


}
