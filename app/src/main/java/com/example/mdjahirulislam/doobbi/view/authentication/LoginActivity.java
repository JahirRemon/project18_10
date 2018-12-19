package com.example.mdjahirulislam.doobbi.view.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.ScheduleAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.controller.requestThread.GetUserDetailsThread;
import com.example.mdjahirulislam.doobbi.controller.requestThread.InsertNewUserThread;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetUserDetailsResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.OrderHomeActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.OrderSummaryActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.SelectItemActivity;
import com.example.mdjahirulislam.doobbi.view.order.OrderListActivity;
import com.example.mdjahirulislam.doobbi.view.profile.ProfileViewActivity;
import com.example.mdjahirulislam.doobbi.view.schedule.ScheduleCalendarActivity;
import com.example.mdjahirulislam.doobbi.view.schedule.ScheduleListActivity;
import com.example.mdjahirulislam.doobbi.view.schedule.ScheduleSummaryActivity;

import io.realm.Realm;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_LOGIN;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions._INTENT_FROM;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.isEmpty;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.setError;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static String userPhone = "";
    private GetUserDetailsThread getUserDetailsThread;
    private Thread getLoginUserDetailsThread;

    // View

    private EditText userIdET;
    private EditText userPasswordET;
    private ImageView showPassIV;
    private boolean showPass = false;
    private String from;
    private Long getDate;
    private String getTime;


    private void initialization() {
        userIdET = findViewById( R.id.login_user_name );
        userPasswordET = findViewById( R.id.login_user_password );
        showPassIV = findViewById( R.id.login_show_or_hidden_password_eye );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialization();
        from = getIntent().getStringExtra( _INTENT_FROM );
        userPhone = Functions.getMyPhoneNO( this );

        if (from.equalsIgnoreCase(ScheduleSummaryActivity.class.getSimpleName())){
            getDate = getIntent().getLongExtra("date",00);
            getTime = getIntent().getStringExtra("time");
        }
        getUserDetailsThread = new GetUserDetailsThread( this, userPhone,from );

        if (!userPhone.isEmpty()) {
            getUserDetailsThread.run();
            Functions.ProgressDialog( this );
            Functions.showDialog();
        } else {
            Log.d( TAG, "onCreate: no number found" );
        }


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
           intentFunction();
        }
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    private void intentFunction() {
        Intent intent = null;
        if (from != null) {
            if (from.equalsIgnoreCase(ScheduleSummaryActivity.class.getSimpleName())) {
                intent = new Intent(this, ScheduleSummaryActivity.class);

            } else if (from.equalsIgnoreCase(HomeActivity.class.getSimpleName())) {
                intent = new Intent(this, HomeActivity.class);

            } else if (from.equalsIgnoreCase(OrderListActivity.class.getSimpleName())) {
                intent = new Intent(this, OrderListActivity.class);

            }else if (from.equalsIgnoreCase(OrderSummaryActivity.class.getSimpleName())) {
                intent = new Intent(this, OrderSummaryActivity.class);

            }else if (from.equalsIgnoreCase(ProfileViewActivity.class.getSimpleName())) {
                intent = new Intent(this, ProfileViewActivity.class);

            } else {
                intent = new Intent(this, HomeActivity.class);
            }
        }else {
            intent = new Intent(this, HomeActivity.class);

        }

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }
    public void goToRegistrationActivityFormLoginActivity(View view) {
        startActivity( new Intent( this, RegistrationActivity.class ).putExtra(_INTENT_FROM,TAG) );
    }

    public void clickOnLogin(View view) {

        if (isEmpty( userIdET )) {
            setError( userIdET, "Enter Your Mobile No. " );
        } else if (isEmpty( userPasswordET )) {
            setError( userPasswordET, "Enter Your Password. " );
        } else {

            getLoginUserDetailsThread = new Thread( new GetLoginUserDetailsThread( this, userIdET.getText().toString(), userPasswordET.getText().toString() ) );
            getLoginUserDetailsThread.start();
            Functions.ProgressDialog( this );
            Functions.showDialog();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

//        Log.d( TAG, "onPause: " + getLoginUserDetailsThread.isAlive() );
    }

    public void showPassword(View view) {
    }

    public void loginShowPassword(View view) {
        if (!showPass) {
            Log.d( TAG, "showPassword: false --> " + showPass );
            userPasswordET.setTransformationMethod( new HideReturnsTransformationMethod() );

            showPassIV.setImageResource( R.drawable.ic_visibility_blue_24dp );
            showPass = true;
        } else {
            Log.d( TAG, "showPassword: true ---> " + showPass );

            showPassIV.setImageResource( R.drawable.ic_visibility_off_ass_24dp );
            userPasswordET.setTransformationMethod( new PasswordTransformationMethod() );

            showPass = false;
        }
    }


    public class GetLoginUserDetailsThread implements Runnable {


        private ConnectionAPI connectionApi;
        private Context context;
        private String userPhone;
        private String userPassword;
        private GetUserDetailsResponseModel userDetailsResponseModel;
        private InsertUserDataModel userDetailsModelDB;

        private SessionManager sessionManager;

        private Realm mRealm = null;


        public GetLoginUserDetailsThread(Context context, String userPhone, String userPassword) {
            this.context = context;
            connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
            this.userPhone = userPhone;
            this.userPassword = userPassword;
            sessionManager = new SessionManager( context );
        }

        @Override
        public void run() {
            try {

                RequestBody password = RequestBody.create( MultipartBody.FORM, API_ACCESS_PASSWORD );
                RequestBody user = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
                RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_LOGIN );
                RequestBody phone = RequestBody.create( MultipartBody.FORM, String.valueOf( userPhone ) );
                RequestBody userPass = RequestBody.create( MultipartBody.FORM, String.valueOf( userPassword ) );

                Log.d( TAG, "run: data: " + phone + "\ndataModel: " + userPhone );

                final Call<GetUserDetailsResponseModel> insertUserResponseModelCallBack = connectionApi.getLogin( password, user,
                        function, phone, userPass );


                insertUserResponseModelCallBack.enqueue( new Callback<GetUserDetailsResponseModel>() {
                    @Override
                    public void onResponse(Call<GetUserDetailsResponseModel> call, Response<GetUserDetailsResponseModel> response) {
                        if (response.code() == 200) {
                            userDetailsResponseModel = response.body();
                            if (userDetailsResponseModel!=null) {
                                String status = userDetailsResponseModel.getStatus();
                                Log.d(TAG, "Status : " + userDetailsResponseModel.toString());
                                // deny code is 100
                                if (status.equalsIgnoreCase(API_ACCESS_SUCCESS_CODE)) { // Status success code = 100

                                    mRealm = Realm.getDefaultInstance();

                                    mRealm.beginTransaction();

                                    userDetailsModelDB = mRealm.createObject(InsertUserDataModel.class);
                                    userDetailsModelDB.setClint_id(userDetailsResponseModel.getCid());
                                    userDetailsModelDB.setName(userDetailsResponseModel.getCustomerName());
                                    userDetailsModelDB.setPhone(userDetailsResponseModel.getPhone());
                                    userDetailsModelDB.setEmail(userDetailsResponseModel.getEmail());
                                    userDetailsModelDB.setAddress(userDetailsResponseModel.getAddress());
                                    userDetailsModelDB.setClint_image_path(userDetailsResponseModel.getFileLink());
                                    userDetailsModelDB.setFlat_no(userDetailsResponseModel.getFlatNo());
                                    userDetailsModelDB.setRoad_no(userDetailsResponseModel.getRoadNo());
                                    userDetailsModelDB.setHouse_no(userDetailsResponseModel.getHouseNo());
                                    userDetailsModelDB.setArea(userDetailsResponseModel.getArea());
                                    userDetailsModelDB.setLatitude(userDetailsResponseModel.getLatitude());
                                    userDetailsModelDB.setLongitude(userDetailsResponseModel.getLongitude());

                                    mRealm.commitTransaction();


                                    sessionManager.setLogin(true);
                                    sessionManager.setUserID(userDetailsResponseModel.getCid());


                                    Intent myIntent = new Intent();
                                    if (from.equalsIgnoreCase(HomeActivity.class.getSimpleName())) {
                                        myIntent = new Intent(context, HomeActivity.class);

                                    } else if (from.equalsIgnoreCase(OrderHomeActivity.class.getSimpleName())) {
                                        myIntent = new Intent(context, OrderHomeActivity.class);

                                    } else if (from.equalsIgnoreCase(SelectItemActivity.class.getSimpleName())) {
                                        myIntent = new Intent(context, SelectItemActivity.class);

                                    } else if (from.equalsIgnoreCase(OrderSummaryActivity.class.getSimpleName())) {
                                        myIntent = new Intent(context, OrderSummaryActivity.class);

                                    } else if (from.equalsIgnoreCase(OrderListActivity.class.getSimpleName())) {
                                        myIntent = new Intent(context, OrderListActivity.class);

                                    } else if (from.equalsIgnoreCase(ProfileViewActivity.class.getSimpleName())) {
                                        myIntent = new Intent(context, ProfileViewActivity.class);

                                    } else if (from.equalsIgnoreCase(ScheduleSummaryActivity.class.getSimpleName())) {
                                        myIntent = new Intent(context, ScheduleSummaryActivity.class);
                                        myIntent.putExtra("dateTimeMills",getDate);
                                        myIntent.putExtra("time",getTime);
                                        myIntent.putExtra(_INTENT_FROM,TAG);

                                    }
                                    context.startActivity(myIntent);
                                    finish();


                                }
                                // deny code is 101
                                else if (status.equalsIgnoreCase(NO_USER_FOUND_CODE)) {
                                    String error_msg = userDetailsResponseModel.getDetail();
                                    Log.d(TAG, "onResponse: " + error_msg);
                                } else if (status.equalsIgnoreCase("102")) {
                                    Log.d(TAG, "onResponse: " + userDetailsResponseModel.getStatus());
                                    Toast.makeText(context, "" + userDetailsResponseModel.getDetail(), Toast.LENGTH_SHORT).show();
                                    userPasswordET.setText("");
                                    userPasswordET.setFocusable(true);
                                } else {
                                    Log.d(TAG, "onResponse: Some Is Wrong");

                                }
                            }else {
                                Toast.makeText(context, "Please Try again something is wrong!!!", Toast.LENGTH_SHORT).show();
                            }

                        } else {
//                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                            Log.d( TAG, "onResponse: Server Error response.code ===> " + response.code() );
                        }
                        Log.d( TAG, "onResponse: " + response.body() + " \n\n" + response.raw() + " \n\n" + response.toString() + " \n\n" + response.errorBody() );
                        hideDialog();

                    }

                    @Override
                    public void onFailure(Call<GetUserDetailsResponseModel> call, Throwable t) {
                        Toast.makeText(context, t.getLocalizedMessage()+" Try Again!!!", Toast.LENGTH_SHORT).show();
                        Log.d( TAG, "onFailure: " + t.getLocalizedMessage() );
                        Log.d( TAG, "onFailure: " + t.toString() );
                        Log.d( TAG, "onFailure: " + t.getMessage() );
                        hideDialog();

                    }
                } );
            } finally {

                if (mRealm != null) {
                    mRealm.close();
                }
            }

        }

    }
}
