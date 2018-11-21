package com.example.mdjahirulislam.doobbi.view.authentication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.requestThread.GetLoginUserDetailsThread;
import com.example.mdjahirulislam.doobbi.controller.requestThread.GetUserDetailsThread;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.isEmpty;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.setError;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();

    private static String userPhone = "";
    private GetUserDetailsThread getUserDetailsThread;
    private GetLoginUserDetailsThread getLoginUserDetailsThread;

    // View

    private EditText userIdET;
    private EditText userPasswordET;
    private ImageView showPassIV;
    private boolean showPass = false;


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

        userPhone = Functions.getMyPhoneNO( this );
        if (!userPhone.isEmpty()) {
            getUserDetailsThread = new GetUserDetailsThread( this, userPhone );
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
        }else if (isEmpty( userPasswordET )) {
            setError( userPasswordET, "Enter Your Password. " );
        }else {

            getLoginUserDetailsThread = new GetLoginUserDetailsThread( this, userIdET.getText().toString(),userPasswordET.getText().toString()  );
            getLoginUserDetailsThread.run();
            Functions.ProgressDialog( this );
            Functions.showDialog();

        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d( TAG, "onPause: " + getUserDetailsThread.isAlive() );
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
}
