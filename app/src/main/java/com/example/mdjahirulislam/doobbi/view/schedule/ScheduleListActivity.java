package com.example.mdjahirulislam.doobbi.view.schedule;

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
import com.example.mdjahirulislam.doobbi.controller.adapter.ScheduleAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetOrderListResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetScheduleListResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.order.OrderListActivity;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_SCHEDULE_LIST;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_TEMPORARY_ORDER_LIST;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;

public class ScheduleListActivity extends AppCompatActivity {

    private static final String TAG = ScheduleListActivity.class.getSimpleName();
    private String userPhone;
    private RecyclerView scheduleListRV;
    private TextView noScheduleTV;

    private ConnectionAPI connectionApi;
    private LinearLayoutManager mLayoutManager;
    private ScheduleAdapter mAdapter;
    private ArrayList<GetScheduleListResponseModel.Schedule> scheduleArrayList;
    private SessionManager sessionManager;
    private GetScheduleListResponseModel getScheduleListResponseModel;


    private void initialization() {
        scheduleListRV = findViewById(R.id.scheduleListRV);
        noScheduleTV = findViewById(R.id.scheduleListLoginTV);
        connectionApi = Functions.getRetrofit().create(ConnectionAPI.class);
        scheduleArrayList = new ArrayList<>();
        sessionManager = new SessionManager(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_list);
        //        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initialization();

        scheduleListRV.setVisibility(View.VISIBLE);
        noScheduleTV.setVisibility(View.GONE);

        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ScheduleAdapter(this, scheduleArrayList);
        scheduleListRV.setAdapter(mAdapter);

        scheduleListRV.setLayoutManager(mLayoutManager);





        if (sessionManager.isLoggedIn()) {
            userPhone = DBFunctions.getUserInformation().getPhone();

            Thread scheduleListThread = new Thread(new GetScheduleListThread());
            scheduleListThread.start();

            Functions.ProgressDialog(this);
            Functions.showDialog();
        }else {
            scheduleListRV.setVisibility(View.GONE);
            noScheduleTV.setVisibility(View.VISIBLE);
            noScheduleTV.setText("Please Login!!!");
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            finish();
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public void goToAddScheduleActivity(View view) {
        startActivity(new Intent(ScheduleListActivity.this, ScheduleCalendarActivity.class));
//        finish();
    }

    public class GetScheduleListThread implements Runnable {

        @Override
        public void run() {
            scheduleArrayList.clear();
            RequestBody password = RequestBody.create(MultipartBody.FORM, API_ACCESS_PASSWORD);
            RequestBody user = RequestBody.create(MultipartBody.FORM, API_ACCESS_ID);
            RequestBody function = RequestBody.create(MultipartBody.FORM, API_ACCESS_FUNCTION_GET_SCHEDULE_LIST);
            RequestBody phone = RequestBody.create(MultipartBody.FORM, String.valueOf(userPhone));

            Log.d(TAG, "run: data: " + phone + "\ndataModel: " + userPhone);

            final Call<GetScheduleListResponseModel> insertScheduleCallBack = connectionApi.getAllISchedule(password, user,
                    function, phone);


            insertScheduleCallBack.enqueue(new Callback<GetScheduleListResponseModel>() {
                @Override
                public void onResponse(Call<GetScheduleListResponseModel> call, Response<GetScheduleListResponseModel> response) {
                    if (response.code() == 200) {
                        getScheduleListResponseModel = response.body();
                        String status = getScheduleListResponseModel.getStatus();
                        Log.d(TAG, "Status : " + getScheduleListResponseModel.toString());
                        // success code is 100
                        if (status.equalsIgnoreCase(API_ACCESS_SUCCESS_CODE)) {

                            if (getScheduleListResponseModel.getSchedule().size() > 0) {

                                for (GetScheduleListResponseModel.Schedule schedule : getScheduleListResponseModel.getSchedule()) {
                                    scheduleArrayList.add(schedule);
                                    Log.d(TAG, "onResponse: " + schedule.toString());
                                }

//                                mAdapter = new ScheduleAdapter(ScheduleListActivity.this, scheduleArrayList);
//                                scheduleListRV.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();

                            } else {
                                Toast.makeText(ScheduleListActivity.this, "Some thing is wrong!!!", Toast.LENGTH_SHORT).show();
                            }


                        }
                        // NO Schedule FOUND code is 101
                        else if (status.equalsIgnoreCase(NO_USER_FOUND_CODE)) {
                            String error_msg = getScheduleListResponseModel.getDetail();
                            Log.d(TAG, "onResponse: " + error_msg);
//                            Toast.makeText( OrderListActivity.this, error_msg, Toast.LENGTH_SHORT ).show();
                            scheduleListRV.setVisibility(View.GONE);
                            noScheduleTV.setVisibility(View.VISIBLE);
                            noScheduleTV.setText(error_msg);

                        } else {
                            Log.d(TAG, "onResponse: Some Is Wrong");
                        }


                    } else {
//                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Server Error response.code ===> " + response.code());
                    }
                    Log.d(TAG, "onResponse: " + response.body() + " \n\n" + response.raw() + " \n\n" + response.toString() + " \n\n" + response.errorBody());
                    hideDialog();

                }

                @Override
                public void onFailure(Call<GetScheduleListResponseModel> call, Throwable t) {

                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Log.d(TAG, "onFailure: " + t.toString());
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    hideDialog();

                }
            });
        }
    }
}
