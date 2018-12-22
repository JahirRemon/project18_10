package com.example.mdjahirulislam.doobbi.view.schedule;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.ScheduleAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.responseModel.InsertResponseModel;
import com.example.mdjahirulislam.doobbi.view.authentication.LoginActivity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_DENY_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_INSERT_SCHEDULE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions._INTENT_FROM;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;

public class ScheduleSummaryActivity extends AppCompatActivity {
    private static final String TAG = ScheduleSummaryActivity.class.getSimpleName();

    private TextView dateTV;
    private TextView dayTV;
    private TextView timeTV;
    private TextView confirmTV;

    private long dateTimeMills;
    private String getTime;
    private String getDate;
    private String userPhone;

    private ConnectionAPI connectionApi;
    private InsertResponseModel responseModel;
    private SessionManager sessionManager;
    private String from;

    private void initialization() {

        dateTV = findViewById(R.id.summaryScheduleDateTV);
        dayTV = findViewById(R.id.summaryScheduleDayTV);
        timeTV = findViewById(R.id.summaryScheduleTimeTV);
        confirmTV = findViewById(R.id.scheduleConfirmTV);
        connectionApi = Functions.getRetrofit().create(ConnectionAPI.class);
        sessionManager = new SessionManager(this);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_summary);

        initialization();
        //        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        from = getIntent().getStringExtra(_INTENT_FROM);

        if (from != null) {
            if (from.equalsIgnoreCase(ScheduleAdapter.class.getSimpleName())) {
                confirmTV.setVisibility(View.GONE);

                getTime = getIntent().getStringExtra("time");
                String dateString = getIntent().getStringExtra("date");
                dateTimeMills = Functions.convertMillsFromDMY(dateString);
            } else {
                confirmTV.setVisibility(View.VISIBLE);

                dateTimeMills = getIntent().getLongExtra("dateTimeMills", 00);
                getTime = getIntent().getStringExtra("time");
            }
        }

        getDate = Functions.convertDateForApi(dateTimeMills);
        Log.d(TAG, "onCreate: date --> " + getDate + " Time --> " + getTime);

        dateTV.setText(Functions.convertDate(dateTimeMills));
        dayTV.setText(Functions.convertDayFromMills(dateTimeMills));
        timeTV.setText(getTime);

    }

    public void goToScheduleListActivityFromScheduleSummary(View view) {
        if (sessionManager.isLoggedIn()) {
            userPhone = DBFunctions.getUserInformation().getPhone();
            Thread insertSchedule = new Thread(new InsertScheduleThread());
            insertSchedule.start();
            Functions.ProgressDialog(this);
            Functions.showDialog();
        } else {
            startActivity(new Intent(ScheduleSummaryActivity.this, LoginActivity.class)
                    .putExtra(_INTENT_FROM, TAG)
                    .putExtra("date", dateTimeMills)
                    .putExtra("time", getTime));

        }


    }

    @Override
    public void onBackPressed() {
        intentFunction();
    }

    private void intentFunction() {
        Intent intent = null;
        if (from != null) {
            if (from.equalsIgnoreCase(ScheduleAdapter.class.getSimpleName())) {
                intent = new Intent(this, ScheduleListActivity.class);

            } else if (from.equalsIgnoreCase(ScheduleCalendarActivity.class.getSimpleName())) {
                intent = new Intent(this, ScheduleListActivity.class);

            } else {
                intent = new Intent(this, ScheduleListActivity.class);
            }
        } else {
            intent = new Intent(this, ScheduleListActivity.class);

        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            intentFunction();
//            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    public class InsertScheduleThread implements Runnable {

        @Override
        public void run() {
            RequestBody password = RequestBody.create(MultipartBody.FORM, API_ACCESS_PASSWORD);
            RequestBody user = RequestBody.create(MultipartBody.FORM, API_ACCESS_ID);
            RequestBody function = RequestBody.create(MultipartBody.FORM, API_ACCESS_FUNCTION_INSERT_SCHEDULE);
            RequestBody phone = RequestBody.create(MultipartBody.FORM, String.valueOf(userPhone));
            RequestBody date = RequestBody.create(MultipartBody.FORM, String.valueOf(getDate));
            RequestBody time = RequestBody.create(MultipartBody.FORM, String.valueOf(getTime));

            Log.d(TAG, "run: data: " + phone + "\ndataModel: " + userPhone);

            final Call<InsertResponseModel> insertScheduleCallBack = connectionApi.insertSchedule(password, user,
                    function, phone, date, time);


            insertScheduleCallBack.enqueue(new Callback<InsertResponseModel>() {
                @Override
                public void onResponse(Call<InsertResponseModel> call, Response<InsertResponseModel> response) {
                    if (response.code() == 200) {
                        responseModel = response.body();
                        String status = responseModel.getStatus().toString();
                        Log.d(TAG, "Status : " + status);
                        if (status.equalsIgnoreCase(API_ACCESS_SUCCESS_CODE)) {
                            Intent myIntent = new Intent(ScheduleSummaryActivity.this, ScheduleListActivity.class);
                            startActivity(myIntent);
                            finish();
                            Functions.hideDialog();
                            Toast.makeText(ScheduleSummaryActivity.this, "Schedule Insert Successful :) ", Toast.LENGTH_SHORT).show();
                        } else if (status.equalsIgnoreCase(API_ACCESS_DENY_CODE)) {
                            String error_msg = responseModel.getDetail();
                            Log.d(TAG, "onResponse: " + error_msg);
                            Toast.makeText(ScheduleSummaryActivity.this, "" + error_msg, Toast.LENGTH_SHORT).show();
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
                public void onFailure(Call<InsertResponseModel> call, Throwable t) {

                    Toast.makeText(ScheduleSummaryActivity.this, "" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Log.d(TAG, "onFailure: " + t.toString());
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    hideDialog();

                }
            });
        }
    }
}
