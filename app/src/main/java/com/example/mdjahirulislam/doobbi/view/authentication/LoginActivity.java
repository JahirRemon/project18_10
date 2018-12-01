package com.example.mdjahirulislam.doobbi.view.authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
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


    private void initialization() {
        userIdET = findViewById( R.id.login_user_name );
        userPasswordET = findViewById( R.id.login_user_password );
        showPassIV = findViewById( R.id.login_show_or_hidden_password_eye );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login );
        initialization();
        from = getIntent().getStringExtra( _INTENT_FROM );
        getUserDetailsThread = new GetUserDetailsThread( this, userPhone );

        userPhone = Functions.getMyPhoneNO( this );
        if (!userPhone.isEmpty()) {
            getUserDetailsThread.run();
            Functions.ProgressDialog( this );
            Functions.showDialog();
        } else {
            Log.d( TAG, "onCreate: no number found" );
        }


    }

    public void goToRegistrationActivityFormLoginActivity(View view) {
        startActivity( new Intent( this, RegistrationActivity.class ) );
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

        private final String TAG = GetLoginUserDetailsThread.class.getSimpleName();

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
                            String status = userDetailsResponseModel.getStatus();
                            Log.d( TAG, "Status : " + userDetailsResponseModel.toString() );
                            // deny code is 100
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

                                }
                                context.startActivity( myIntent );
                                finish();


                            }
                            // deny code is 101
                            else if (status.equalsIgnoreCase( NO_USER_FOUND_CODE )) {
                                String error_msg = userDetailsResponseModel.getDetail();
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
                    public void onFailure(Call<GetUserDetailsResponseModel> call, Throwable t) {

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
