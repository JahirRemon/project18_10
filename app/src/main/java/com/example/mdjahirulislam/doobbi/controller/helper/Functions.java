package com.example.mdjahirulislam.doobbi.controller.helper;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.view.authentication.RegistrationActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HP on 9/30/2017.
 */

public class Functions {
    private static final String TAG = Functions.class.getSimpleName();

    public static final int RESIZE_IMAGE_WIDTH = 600;
    public static final int RESIZE_IMAGE_HEIGHT = 300;
    public static final String _INTENT_FROM = "intentFrom";
    public static final long _PROGRESS_TIME_IN_MILLISECOND = 2500;
    public static final String DEVELOPER_KEY = "AIzaSyAEDWZUfRgWp9s6mBy_MRZ5WNHudJneEn8";
    public static final String API_ACCESS_SUCCESS_CODE = "100";
    public static final String API_ACCESS_DENY_CODE = "000";
    public static final String NO_USER_FOUND_CODE = "101";
    public static final String API_ACCESS_ID = "rajib";
    public static final String API_ACCESS_PASSWORD = "123456";

    //Function Name
    public static final String API_ACCESS_FUNCTION_ADD_USER = "addcustomer";
    public static final String API_ACCESS_FUNCTION_ADD_USER_IMAGE = "customerimage";
    public static final String API_ACCESS_FUNCTION_LOGIN = "verifycustomer";
    public static final String API_ACCESS_FUNCTION_USER_DETAILS = "getcustomer";
    public static final String API_ACCESS_FUNCTION_GET_CATEGORY = "getcategory";
    public static final String API_ACCESS_FUNCTION_GET_CATEGORY_ITEMS = "getcategoryitem";
    public static final String API_ACCESS_FUNCTION_GET_CATEGORY_ITEMS_PRICE = "getitemwiseprise";
    public static final String API_ACCESS_FUNCTION_INSERT_ORDER_LIST = "customerorder";
    public static final String API_ACCESS_FUNCTION_GET_TEMPORARY_ORDER_LIST = "getcustomerorder";
    public static final String API_ACCESS_FUNCTION_GET_ORDER_LIST_DETAILS = "getorderdetail";
    public static final String API_ACCESS_FUNCTION_GET_ITEMS_PRICE = "getitemprice";
    public static final String API_ACCESS_FUNCTION_GET_SCHEDULE_LIST = "getSchedule";
    public static final String API_ACCESS_FUNCTION_INSERT_SCHEDULE = "updateSchedule";

    public static String BASE_URL = "http://transparentgroup.net/services/services/";
    public static String _IMAGE_BASE_URL = "http://transparentgroup.net/services/services/";

    public static final int[] tabIconsWhite = {
            R.drawable.ic_tab_man_white,
            R.drawable.ic_tab_woman_white,
            R.drawable.ic_tab_child_white,
            R.drawable.ic_tab_house_hold_white,
            R.drawable.ic_tab_accessories_white


    };

    public static final int[] tabIconsAss = {
            R.drawable.ic_tab_man_ass,
            R.drawable.ic_tab_woman_ass,
            R.drawable.ic_tab_child_ass,
            R.drawable.ic_tab_house_hold_ass,
            R.drawable.ic_tab_accessories_ass


    };


    //    public static final String[] tabNames = {"No Data"};
    public static ArrayList<String> tabNames = new ArrayList<>();


    public static Retrofit getRetrofit() {

//        Gson gson = new GsonBuilder().setLenient().create();

        return new Retrofit.Builder()
                .baseUrl( BASE_URL )
                .addConverterFactory( GsonConverterFactory.create() )
                .build();
    }

    public static boolean isSamsung() {
        String manufacturer = android.os.Build.MANUFACTURER;
        if (manufacturer.toLowerCase( Locale.ENGLISH ).contains( "samsung" ))
            return true;
        else
            return false;
    }


    public static String convertDate(long inputDate) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );

        SimpleDateFormat convertDateFormat = new SimpleDateFormat( "dd MMMM, yyyy" );

        return convertDateFormat.format( inputDate );
    }

    public static String convertDateForApi(long inputDate) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );

        SimpleDateFormat convertDateFormat = new SimpleDateFormat( "dd-MM-yyyy" );

        return convertDateFormat.format( inputDate );
    }


    public static long convertMillsFromDMY(String inputDate) { // DMY= Day Month Year
//        String[] parts = inputDate.split( "." );

        SimpleDateFormat sdf = new SimpleDateFormat( "dd-MM-yyyy" );

        Date date = null;
        try {
            date = sdf.parse( inputDate );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }

    public static String convertDayFromMills(long inputDate) {

        DateFormat dayFormate = new SimpleDateFormat( "EEEE" );
        String dayFromDate = dayFormate.format( inputDate );
        Log.d( "asd", "----------:: " + dayFromDate );


        return dayFromDate;
    }

    public static String getDateFromCreatedAt(String createAt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );

        Date date = null;
        try {
            date = simpleDateFormat.parse( createAt );
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date == null) {
            return "";
        }

        SimpleDateFormat convetDateFormat = new SimpleDateFormat( "dd MMM, yyyy" );

        return convetDateFormat.format( date );

    }

    public static String getTimeFromCreatedAt(String createAt) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss" );

        Date date = null;
        try {
            date = simpleDateFormat.parse( createAt );
        } catch (ParseException e) {
            e.printStackTrace();
        }


        if (date == null) {
            return "";
        }

        SimpleDateFormat convertDateFormat = new SimpleDateFormat( "hh:mm a" );

        return convertDateFormat.format( date );
    }

    public static String convertDayFromDMY(String inputDate) { // DMY= Day Month Year   2018-04-14
        String[] parts = inputDate.split( "." );
        Calendar cal = Calendar.getInstance();
        int year = 0; // 004
        int date = 0;
        String day = new String();
        int month = 0;
        try {
            date = Integer.parseInt( parts[2] );  // 17
            month = Integer.parseInt( parts[1] ) - 1;  // 05
            year = Integer.parseInt( parts[0] );  // 05
            cal.set( year, month, date );

        } catch (Exception e) {

        }

        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getWeekdays();
        int dayOfWeek = cal.get( cal.DAY_OF_WEEK );
        if (dayOfWeek >= 1 && dayOfWeek <= 7) {

            return months[dayOfWeek];

        } else {
            return "error";
        }


    }

    public static String upperCaseFirst(String value) {

        // Convert String to char array.
        char[] array = value.toCharArray();
        // Modify first element in array.
        array[0] = Character.toUpperCase( array[0] );
        // Return string.
        return new String( array );
    }


    public static String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder( context, Locale.getDefault() );
        Log.d( TAG, "getAddress: lat: " + lat + " lng: " + lng );
        if (lat != 0 && lng != 0) {
            try {
                List<Address> addresses = geocoder.getFromLocation( lat, lng, 1 );
                Address obj = addresses.get( 0 );
                String addressLine = obj.getAddressLine( 0 );
                String add = obj.getCountryName();
                add = add + "\n" + obj.getAddressLine( 1 );
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();
                add = add + "\n" + obj.getThoroughfare();

                Log.v( TAG, "Address" + addresses );
                Log.v( TAG, "Address" + add );
                // Toast.makeText(this, "Address=>" + add,
                // Toast.LENGTH_SHORT).show();

                return addressLine;
                // TennisAppActivity.showDialog(add);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText( context, e.getMessage(), Toast.LENGTH_SHORT ).show();
                return e.getLocalizedMessage();
            }
        } else {
            return "Some Thing is Wrong";
        }
    }


    public static String getRoadNo(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder( context, Locale.getDefault() );
        Log.d( TAG, "getAddress: lat: " + lat + " lng: " + lng );
        if (lat != 0 && lng != 0) {
            try {
                List<Address> addresses = geocoder.getFromLocation( lat, lng, 1 );
                Address obj = addresses.get( 0 );
                String addressLine = obj.getAddressLine( 0 );
                String add = obj.getCountryName();
                add = add + "\n" + obj.getAddressLine( 1 );
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();
                add = add + "\n" + obj.getThoroughfare();

                Log.v( TAG, "Address" + addresses );
                Log.v( TAG, "Address" + add );
                // Toast.makeText(this, "Address=>" + add,
                // Toast.LENGTH_SHORT).show();

                return obj.getThoroughfare();
                // TennisAppActivity.showDialog(add);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Toast.makeText( context, e.getMessage(), Toast.LENGTH_SHORT ).show();
                return e.getMessage();
            }
        } else {
            return "Some Thing is Wrong";
        }
    }

    public static AnimationSet setTextAnimation() {

        Animation fadeIn = new AlphaAnimation( 0, 1 );
        fadeIn.setInterpolator( new DecelerateInterpolator() ); //add this
        fadeIn.setDuration( 1000 );

        Animation fadeOut = new AlphaAnimation( 1, 0 );
        fadeOut.setInterpolator( new AccelerateInterpolator() ); //and this
        fadeOut.setStartOffset( 1000 );
        fadeOut.setDuration( 1000 );


        AnimationSet animation = new AnimationSet( false ); //change to false
        animation.addAnimation( fadeIn );
//        animation.addAnimation(fadeOut);
//        showAddressTV.setAnimation(animation);
        return animation;

    }


    public static void showSnack(boolean isConnected, View view) {
        String message;
        int color;
        if (isConnected) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar
                .make( view, message, Snackbar.LENGTH_LONG );

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById( android.support.design.R.id.snackbar_text );
        textView.setTextColor( color );
        snackbar.show();
    }


    public static AlertDialog.Builder alertDialogBuilder;
    public static CircleProgressBar progressBar;
    public static AlertDialog alertDialog;


    public static void ProgressDialog(Context context) {


        LayoutInflater li = LayoutInflater.from( context );
        final View promptsView = li.inflate( R.layout.custom_progress_dialog, null );
        alertDialogBuilder = new AlertDialog.Builder( context );
        // set prompts.xml to alertDialog builder
        alertDialogBuilder.setView( promptsView );
        alertDialogBuilder.setCancelable( false );

        alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside( false );


        progressBar = (CircleProgressBar) promptsView.findViewById( R.id.custom_progress );

    }

    public static void showDialog() {
        if (!alertDialog.isShowing()) {
            simulateProgress();
            Log.d( TAG, "showDialog: Yes" );
        } else {
            Log.d( TAG, "showDialog: No " + alertDialog.isShowing() );
        }

    }

    public static void hideDialog() {
        try {
            if (alertDialog.isShowing()) {
                alertDialog.dismiss();
                Log.d( TAG, "hideDialog: Yes" );
            } else {
                Log.d( TAG, "hideDialog: No " + alertDialog.isShowing() );
            }
        }catch (Exception e){
            Log.d( TAG, "hideDialog: " +e.getLocalizedMessage());
        }

    }

    public static void simulateProgress() {
        Log.d( TAG, "simulateProgress: " );
        alertDialog.show();
        progressBar.setVisibility( View.VISIBLE );
        ValueAnimator animator = ValueAnimator.ofInt( 0, 100 );
        animator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();

                progressBar.setProgress( progress );
//                progressBar.sett

            }
        } );
        animator.setRepeatCount( ValueAnimator.INFINITE );
        animator.setDuration( 1500 );
        animator.start();
    }


    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_CODE_PERMISSION = 100;

    public void requestPermission(Activity context) {

        ActivityCompat.requestPermissions( context, new String[]{
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_NUMBERS}, REQUEST_LOCATION );
    }

    public static String getMyPhoneNO(Activity activity) {
        Context context = activity.getApplicationContext();
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService( Context.TELEPHONY_SERVICE );
        if (
                ActivityCompat.checkSelfPermission( context, Manifest.permission.READ_PHONE_STATE ) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission( context, Manifest.permission.CAMERA ) != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission( context, Manifest.permission.READ_EXTERNAL_STORAGE ) != MockPackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission( context, Manifest.permission.WRITE_EXTERNAL_STORAGE ) != MockPackageManager.PERMISSION_GRANTED) {
            Log.d( TAG, "getMyPhoneNO: if " );
            ActivityCompat.requestPermissions( activity, new String[]{
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_PHONE_NUMBERS,
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100 );
            return " ";

        } else {
            String mPhoneNumber = tMgr.getLine1Number();
//            Log.d( TAG, "getMyPhoneNO: 0---> " +tMgr.getNetworkType());
//            Log.d( TAG, "getMyPhoneNO: 1---> " +tMgr.getPhoneType());
//            Log.d( TAG, "getMyPhoneNO: 2---> " +tMgr.getDeviceSoftwareVersion());
//            Log.d( TAG, "getMyPhoneNO: 3---> " +tMgr.getGroupIdLevel1());
//            Log.d( TAG, "getMyPhoneNO: 4---> " +tMgr);
//            Log.d( TAG, "getMyPhoneNO: 5---> " +tMgr.);
//            Log.d( TAG, "getMyPhoneNO: 6---> " +tMgr.);

            Log.d( TAG, "getMyPhoneNO: else" + mPhoneNumber );
            return mPhoneNumber;
        }

    }


    //Check Edit Text Field is empty or not
    public static boolean isEmpty(EditText editText) {

        String input = editText.getText().toString().trim();
        return input.length() == 0;

    }

    public static void setError(EditText editText, String errorString) {

        editText.setError( errorString );

    }

    public static void clearError(EditText editText) {

        editText.setError( null );

    }

    public static void setAnimationNumber(final TextView textView, final String firstMessage, int startNumber,
                                          int endNumber, final String secondMessage, int animationSpeed) {

        ValueAnimator animator = ValueAnimator.ofInt( startNumber, endNumber );
        animator.setDuration( animationSpeed );
        animator.addUpdateListener( new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText( firstMessage + animation.getAnimatedValue().toString() + secondMessage );
            }
        } );
        animator.start();

    }


    public static class ProgressThread extends Thread {

        public void run() {

            try {
                Log.d( TAG, "run: progress thread" );
                Thread.sleep( _PROGRESS_TIME_IN_MILLISECOND );
            } catch (InterruptedException e) {

            } finally {
                hideDialog();
            }
        }

    }


}
