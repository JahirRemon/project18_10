package com.example.mdjahirulislam.doobbi.view.authentication;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.LocationListener;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.test.mock.MockPackageManager;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.ConnectivityReceiver;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.GPSTracker;
import com.example.mdjahirulislam.doobbi.controller.helper.MyApplication;
import com.example.mdjahirulislam.doobbi.controller.requestThread.InsertNewUserPhotoThread;
import com.example.mdjahirulislam.doobbi.controller.requestThread.InsertNewUserThread;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.isEmpty;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.setError;

public class RegistrationActivity extends AppCompatActivity implements OnMapReadyCallback, ConnectivityReceiver.ConnectivityReceiverListener {


    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private static final int REQUEST_LOCATION = 1;
    private static final int REQUEST_GALLERY = 2;
    private static final int REQUEST_CAMERA = 3;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final String IMAGE_DIRECTORY = "/doobbi";
    private static final int RESIZE_IMAGE_WIDTH = 600;
    private static final int RESIZE_IMAGE_HEIGHT = 300;

    private boolean showPass = false;
    private boolean showRepeatPass = false;

    private ConnectivityReceiver connectivityReceiver;


    private String cameraPermission = Manifest.permission.CAMERA;
    private String readStoragePermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private String writeStoragePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private ScrollView registrationScrollViewSV;
    private SupportMapFragment mapFragment;
    private CircleImageView userProfilePictureIV;


    private LocationListener locationListener;
    private TelephonyManager tMgr;


    //map

    private GoogleMap mMap;
    private GPSTracker gps;
    private double latitude;
    private double longitude;
    private float zoomLevel = 14.0f;


    private Uri selectedImageURI = null;
    private String mediaPath = "noimage";
    private String imagePath = null;

    private boolean isConnected = false;


    //view

    private EditText userNameET;
    private EditText userMobileET;
    private EditText userAnotherMobileET;
    private EditText userEmailET;
    private EditText userAreaET;
    private EditText userFlatET;
    private EditText userHouseET;
    private EditText userRoadNoET;
    private EditText userPassword;
    private EditText userRepeatPassword;

    private ImageView passwordEyeIV;
    private ImageView repeatPasswordEyeIV;


    private InsertUserDataModel insertUserDataModel;

    private void initialization() {
        registrationScrollViewSV = findViewById( R.id.registrationScrollViewSV );
        userAreaET = findViewById( R.id.regAreaTV );
        userProfilePictureIV = findViewById( R.id.reg_profile_image );
        userNameET = findViewById( R.id.reg_user_name );
        userMobileET = findViewById( R.id.reg_user_mobile );
        userAnotherMobileET = findViewById( R.id.reg_user_another_mobile );
        userEmailET = findViewById( R.id.reg_user_email );
        userFlatET = findViewById( R.id.reg_user_flat_no );
        userHouseET = findViewById( R.id.reg_user_house_no );
        userRoadNoET = findViewById( R.id.reg_user_road_no );
        userPassword = findViewById( R.id.reg_user_password );
        userRepeatPassword = findViewById( R.id.reg_user_repeat_password );

        passwordEyeIV = findViewById( R.id.reg_show_or_hidden_password_eye );
        repeatPasswordEyeIV = findViewById( R.id.reg_show_or_hidden_repeat_password_eye );

        insertUserDataModel = new InsertUserDataModel();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById( R.id.registrationMap );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_registration );
        connectivityReceiver = new ConnectivityReceiver();

        isConnected = ConnectivityReceiver.isConnected( this );

        if (isConnected) {
            findViewById( R.id.regMainNoInternetView ).setVisibility( View.GONE );
            findViewById( R.id.registrationScrollViewSV ).setVisibility( View.VISIBLE );

            initialization();


            userMobileET.setText( Functions.getMyPhoneNO( this ) );

            Log.d( TAG, "onCreate: " + Functions.getMyPhoneNO( this ) );


            //test

        } else {
            findViewById( R.id.regMainNoInternetView ).setVisibility( View.VISIBLE );
            findViewById( R.id.registrationScrollViewSV ).setVisibility( View.GONE );


        }


    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction( ConnectivityManager.CONNECTIVITY_ACTION );

        registerReceiver( connectivityReceiver, intentFilter );


        /*register connection status listener*/
        MyApplication.getInstance().setConnectivityListener( this );


        gps = new GPSTracker( RegistrationActivity.this );

        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            userAreaET.setText( String.valueOf( Functions.getAddress( RegistrationActivity.this, latitude, longitude ) ) );

            mapFragment.getMapAsync( this );

            // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d( TAG, "onPause: " );
        unregisterReceiver( connectivityReceiver );
        gps.stopUsingGPS();
    }

    public void goToScheduleListActivityFromRegistrationActivity(View view) {

        if (isEmpty( userNameET )) {
            setError( userNameET, "Required Field" );
        } else if (isEmpty( userMobileET )) {
            setError( userMobileET, "Required Field" );
        } else if (isEmpty( userEmailET )) {
            setError( userEmailET, "Required Field" );
        } else if (isEmpty( userAreaET )) {
            setError( userAreaET, "Required Field" );
        } else if (isEmpty( userFlatET )) {
            setError( userFlatET, "Required Field" );
        } else if (isEmpty( userHouseET )) {
            setError( userHouseET, "Required Field" );
        } else if (isEmpty( userRoadNoET )) {
            setError( userRoadNoET, "Required Field" );
        } else if (isEmpty( userPassword )) {
            setError( userPassword, "Required Field" );
        } else if (isEmpty( userRepeatPassword )) {
            setError( userRepeatPassword, "Required Field" );
        } else

        {
//            Toast.makeText( this, "it's Work Fine", Toast.LENGTH_SHORT ).show();
            if (userPassword.getText().toString().equals( userRepeatPassword.getText().toString() )) {

                insertUserDataModel = new InsertUserDataModel( userNameET.getText().toString(), userMobileET.getText().toString(),
                        userAnotherMobileET.getText().toString(), userEmailET.getText().toString(),
                        userAreaET.getText().toString(), userFlatET.getText().toString(), userRoadNoET.getText().toString(),
                        userHouseET.getText().toString(), userAreaET.getText().toString(),
                        String.valueOf( latitude ), String.valueOf( longitude ), userPassword.getText().toString() );


                // convert java object to JSON format,
                // and returned as JSON formatted string
                Gson gson = new Gson();
                String data = gson.toJson( insertUserDataModel );
                Log.d( TAG, "goToScheduleListActivityFromRegistrationActivity: " + data );


                InsertNewUserThread insertEventThread = new InsertNewUserThread( this, data );
                insertEventThread.start();

                if (mediaPath.equalsIgnoreCase( "noImage" )) {
                    Toast.makeText( this, "You will not choose your profile picture.", Toast.LENGTH_SHORT ).show();
                    Functions.ProgressDialog( this );
                    Functions.showDialog();
                } else {
                    InsertNewUserPhotoThread insertNewUserPhotoThread = new InsertNewUserPhotoThread( this, mediaPath, insertUserDataModel.getPhone() );
                    insertNewUserPhotoThread.run();
                    Functions.ProgressDialog( this );
                    Functions.showDialog();
                }

            } else {
                userRepeatPassword.setError( "Password Not Match" );
            }
        }

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng currentPosition = new LatLng( latitude, longitude );

//        mMap.addMarker(new MarkerOptions().position(currentPosition).price("Marker in Current Position"));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( currentPosition, 16.3f ) );
        mMap.getUiSettings().setScrollGesturesEnabled( false );

        mMap.setOnMapClickListener( new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                registrationScrollViewSV.fullScroll( ScrollView.FOCUS_DOWN );

                Log.d( TAG, "onMapClick: " );


                final Dialog dialog = new Dialog( RegistrationActivity.this );
                dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
                dialog.setContentView( R.layout.map_dialog_view );
                dialog.getWindow().setLayout( WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT );
                dialog.setTitle( "Select your location" );
                dialog.setCancelable( false );
                dialog.show();


                if (isConnected) {

                    dialog.findViewById( R.id.mapMainNoInternetView ).setVisibility( View.GONE );
                    dialog.findViewById( R.id.mapMainView ).setVisibility( View.VISIBLE );
                } else {
                    dialog.findViewById( R.id.mapMainNoInternetView ).setVisibility( View.VISIBLE );
                    dialog.findViewById( R.id.mapMainView ).setVisibility( View.GONE );

                }
                final GoogleMap[] dialogMap = new GoogleMap[1];


                final MapView mMapView = (MapView) dialog.findViewById( R.id.registrationDialogMap );
                MapsInitializer.initialize( RegistrationActivity.this );

                final TextView showAddressTV = dialog.findViewById( R.id.currentLocationInfoTV );
                TextView okTV = dialog.findViewById( R.id.mapDialogOkTV );
                TextView cancelTV = dialog.findViewById( R.id.mapDialogCancelTV );
                final ImageView pinIV = dialog.findViewById( R.id.mapDialogPinIV );
                final FloatingActionButton myLocation = dialog.findViewById( R.id.floating_my_location );
                final FloatingActionButton zoomIn = dialog.findViewById( R.id.floating_zoom_in );
                final FloatingActionButton zoomOut = dialog.findViewById( R.id.floating_zoom_out );
                final FloatingActionButton compass = dialog.findViewById( R.id.floating_compass );
                final LatLng[] searchLatLong = new LatLng[1];
                final LatLng[] center = new LatLng[1];

                mMapView.onCreate( dialog.onSaveInstanceState() );
                mMapView.onResume();// needed to get the map to display immediately


                // Start Auto Place Complete
                Log.d( TAG, "autoComplete: " );

                final SupportPlaceAutocompleteFragment autocompleteFragment = (SupportPlaceAutocompleteFragment)
                        getSupportFragmentManager().findFragmentById( R.id.dialog_place_autocomplete_fragment );

                final EditText etPlace = (EditText) autocompleteFragment.getView().findViewById( R.id.place_autocomplete_search_input );

                etPlace.setTextSize( 14 );

                AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                        .setCountry( "BD" )
                        .setTypeFilter( AutocompleteFilter.TYPE_FILTER_ADDRESS )
                        .build();

                autocompleteFragment.setFilter( typeFilter );


//                final Button clereBtnPlace = (Button) autocompleteFragment.getView().findViewById(R.id.place_autocomplete_clear_button);

//                ((EditText)autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_button)).setWidth(40);

                final FragmentTransaction ft2 = getSupportFragmentManager()
                        .beginTransaction();

                autocompleteFragment.setOnPlaceSelectedListener( new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {

                        // TODO: Get info about the selected place.
                        Log.i( TAG, "Place: " + place.getName() );
                        dialogMap[0].moveCamera( CameraUpdateFactory.newLatLngZoom( place.getLatLng(), 16.8f ) );
                        searchLatLong[0] = place.getLatLng();
                        Log.d( TAG, "onPlaceSelected: " + searchLatLong[0] );

                    }

                    @Override
                    public void onError(Status status) {
                        // TODO: Handle the error.
                        Log.i( TAG, "An error occurred: " + status );
                    }
                } );


                // End Auto Place Complete


                showAddressTV.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d( TAG, "onClick: showAddressTV" );
//                        autoComplete( view );


                    }
                } );


                mMapView.getMapAsync( new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleDialogMap) {

                        dialogMap[0] = googleDialogMap;

                        Log.d( TAG, "onMapReady: dialog" );
//                        googleMap.addMarker(new MarkerOptions().position(currentPosition).price("Marker in Sydney"));
                        googleDialogMap.moveCamera( CameraUpdateFactory.newLatLngZoom( currentPosition, zoomLevel ) );

                        googleDialogMap.setOnCameraIdleListener( new GoogleMap.OnCameraIdleListener() {
                            @Override
                            public void onCameraIdle() {
                                center[0] = googleDialogMap.getCameraPosition().target;

                                searchLatLong[0] = center[0];
                                showAddressTV.setVisibility( View.VISIBLE );
                                showAddressTV.setAnimation( Functions.setTextAnimation() );
                                showAddressTV.setText( String.valueOf(
                                        Functions.getAddress( RegistrationActivity.this, center[0].latitude, center[0].longitude ) ) );

                            }
                        } );

                        googleDialogMap.setOnCameraMoveStartedListener( new GoogleMap.OnCameraMoveStartedListener() {
                            @Override
                            public void onCameraMoveStarted(int i) {
                                showAddressTV.setVisibility( View.GONE );

                            }
                        } );


                    }
                } );

//                myLocation.setOnClickListener( new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialogMap[0].moveCamera( CameraUpdateFactory.newLatLngZoom( currentPosition, 16.8f ) );
//                    }
//                } );


                myLocation.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        //TODO something when floating action menu first item clicked
                        if (gps.canGetLocation()) {

                            latitude = gps.getLatitude();
                            longitude = gps.getLongitude();
//                            mMap.animateCamera( CameraUpdateFactory.newLatLng( new LatLng(latitude,longitude)) );
                            dialogMap[0].animateCamera( CameraUpdateFactory.newLatLngZoom( currentPosition, zoomLevel ) );


                        } else {
                            // can't get location
                            // GPS or Network is not enabled
                            // Ask user to enable GPS/network in settings
                            gps.showSettingsAlert();
                        }

                    }
                } );
                zoomIn.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        //TODO something when floating action menu second item clicked


                        dialogMap[0].animateCamera( CameraUpdateFactory.zoomIn() );
                        zoomLevel = dialogMap[0].getCameraPosition().zoom;
                        Log.d( TAG, "onClick: zoomLevel :" + zoomLevel );


                    }
                } );
                zoomOut.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        //TODO something when floating action menu third item clicked

                        dialogMap[0].animateCamera( CameraUpdateFactory.zoomOut() );
                        zoomLevel = dialogMap[0].getCameraPosition().zoom;
                        Log.d( TAG, "onClick: zoomLevel :" + zoomLevel );

                    }
                } );

                compass.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        //TODO something when floating action menu first item clicked

                        CameraPosition currentPlace = new CameraPosition.Builder()
                                .target( new LatLng( latitude, longitude ) )
                                .bearing( 0f )
                                .zoom( zoomLevel )
                                .build();
                        dialogMap[0].animateCamera( CameraUpdateFactory.newCameraPosition( currentPlace ) );

                    }
                } );

                cancelTV.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ft2.remove( getSupportFragmentManager()
                                .findFragmentById( R.id.dialog_place_autocomplete_fragment ) );
                        ft2.commit();
                        dialog.dismiss();


                    }
                } );


                okTV.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( searchLatLong[0], zoomLevel ) );
                        userAreaET.setText( String.valueOf( Functions.getAddress( RegistrationActivity.this, searchLatLong[0].latitude, searchLatLong[0].longitude ) ) );
                        ft2.remove( getSupportFragmentManager()
                                .findFragmentById( R.id.dialog_place_autocomplete_fragment ) );
                        ft2.commit();
                        dialog.dismiss();
                        Log.d( TAG, "onClick:" + autocompleteFragment.isRemoving() );

                    }
                } );



            }
        } );

    }


    public void addPhoto(View view) {


        try {
            if (ActivityCompat.checkSelfPermission( this, cameraPermission ) != MockPackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission( this, readStoragePermission ) != MockPackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission( this, writeStoragePermission ) != MockPackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions( this, new String[]{cameraPermission, readStoragePermission, writeStoragePermission},
                        REQUEST_CODE_PERMISSION );

                // If any permission above not allowed by user, this condition will
//                execute every time, else your else part will work
            } else {
                Log.d( TAG, "addPhoto: " + ActivityCompat.checkSelfPermission( this, cameraPermission ) );
                Log.d( TAG, "addPhoto: " + ActivityCompat.checkSelfPermission( this, readStoragePermission ) );
                Log.d( TAG, "addPhoto: " + ActivityCompat.checkSelfPermission( this, writeStoragePermission ) );
                showPictureDialog();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder( this );
        pictureDialog.setTitle( "Select Action" );
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems( pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallery();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                } );
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Log.d( TAG, "choosePhotoFromGallery: " );
        Intent galleryIntent = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );

        galleryIntent.putExtra( Intent.EXTRA_ALLOW_MULTIPLE, false );
        startActivityForResult( galleryIntent, REQUEST_GALLERY );
    }

    private void takePhotoFromCamera() {
        Log.d( TAG, "takePhotoFromCamera: " );
        Intent takePictureIntent = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE );

        if (takePictureIntent.resolveActivity( getPackageManager() ) != null) {
            String fileName = "temp.jpg";
            ContentValues values = new ContentValues();
            values.put( MediaStore.Images.Media.TITLE, fileName );
            selectedImageURI = getContentResolver().insert( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values );
            takePictureIntent
                    .putExtra( MediaStore.EXTRA_OUTPUT, selectedImageURI );
            startActivityForResult( takePictureIntent, REQUEST_CAMERA );
        }

//        startActivityForResult(takePictureIntent, CAMERA);
//        new Intent()
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult( requestCode, resultCode, data );
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_GALLERY) {
            if (data != null) {
                selectedImageURI = data.getData();
                Log.d( TAG, "select image Uri for gallery : " + selectedImageURI );

                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query( selectedImageURI, filePathColumn, null, null, null );
//                    Cursor cursor1 = getContentResolver().query( )
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
                mediaPath = cursor.getString( columnIndex );
                Log.d( TAG, "select image String for gallery : " + mediaPath );

//                TODO : not needed image save and convert
//                    Bitmap bitmap = getResizedBitmap( MediaStore.Images.Media.getBitmap( this.getContentResolver(), selectedImageURI ), 400, 400 );
//                    imagePath = saveImage( bitmap );
//                    Toast.makeText( this, "Image Saved!", Toast.LENGTH_SHORT ).show();


//                Picasso.get().load( selectedImageURI ).into( userProfilePictureIV );
                Picasso
                        .get()
                        .load(selectedImageURI)
                        .resize(RESIZE_IMAGE_WIDTH, RESIZE_IMAGE_HEIGHT) // resizes the image to these dimensions (in pixel)
                        .centerCrop()
                        .into(userProfilePictureIV);

                cursor.close();

            }

        } else if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query( selectedImageURI, filePathColumn, null, null, null );
//                    Cursor cursor1 = getContentResolver().query( )
            assert cursor != null;
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex( filePathColumn[0] );
            mediaPath = cursor.getString( columnIndex );
//            TODO : not needed image save and convert
//                Bitmap bitmap = getResizedBitmap( MediaStore.Images.Media.getBitmap( this.getContentResolver(), selectedImageURI ), 400, 500 );
//                imagePath = saveImage( bitmap );
//                Toast.makeText( RegistrationActivity.this, "Image Saved!", Toast.LENGTH_SHORT ).show();

            Picasso
                    .get()
                    .load(selectedImageURI)
                    .resize(RESIZE_IMAGE_WIDTH, RESIZE_IMAGE_HEIGHT) // resizes the image to these dimensions (in pixel)
                    .centerCrop()
                    .into(userProfilePictureIV);

            cursor.close();

        }

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {

        int width = bm.getWidth();

        int height = bm.getHeight();

        float scaleWidth = ((float) newWidth) / width;

        float scaleHeight = ((float) newHeight) / height;

// CREATE A MATRIX FOR THE MANIPULATION

        Matrix matrix = new Matrix();

// RESIZE THE BIT MAP

        matrix.postScale( scaleWidth, scaleHeight );

// RECREATE THE NEW BITMAP

        Bitmap resizedBitmap = Bitmap.createBitmap( bm, 0, 0, width, height, matrix, false );

        return resizedBitmap;

    }


    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress( Bitmap.CompressFormat.JPEG, 50, bytes );
        String imagePath = null;

        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY );
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File( wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg" );
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream( f );
            fo.write( bytes.toByteArray() );
            MediaScannerConnection.scanFile( this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null );
            fo.close();
            Log.d( "TAG", "File Saved::--->" + f.getAbsolutePath() );

            imagePath = f.getAbsolutePath();
            return imagePath;
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return imagePath;
    }


    public void showPassword(View view) {

        Log.d( TAG, "showPassword: " + view.getTag() );
        if (view.getId() == R.id.reg_show_or_hidden_password_eye) {
            if (!showPass) {
                Log.d( TAG, "showPassword: false --> " + showPass );
                userPassword.setTransformationMethod( new HideReturnsTransformationMethod() );

                passwordEyeIV.setImageResource( R.drawable.ic_visibility_blue_24dp );
                showPass = true;
            } else {
                Log.d( TAG, "showPassword: true ---> " + showPass );

                passwordEyeIV.setImageResource( R.drawable.ic_visibility_off_ass_24dp );
                userPassword.setTransformationMethod( new PasswordTransformationMethod() );

                showPass = false;
            }
        } else if (view.getId() == R.id.reg_show_or_hidden_repeat_password_eye) {
            if (!showRepeatPass) {
                Log.d( TAG, "showPassword: 1 ---> " + view.getResources() );
                repeatPasswordEyeIV.setImageResource( R.drawable.ic_visibility_blue_24dp );
                userRepeatPassword.setTransformationMethod( new HideReturnsTransformationMethod() );
                showRepeatPass = true;
            } else {
                Log.d( TAG, "showPassword: 2 ---> " + view.getResources() );
                repeatPasswordEyeIV.setImageResource( R.drawable.ic_visibility_off_ass_24dp );
                userRepeatPassword.setTransformationMethod( new PasswordTransformationMethod() );
                showRepeatPass = false;
            }
        }


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d( TAG, "onNetworkConnectionChanged: " + isConnected );
        if (isConnected) {
            Log.d( TAG, "onNetworkConnectionChanged: " + isConnected );

            findViewById( R.id.regMainNoInternetView ).setVisibility( View.GONE );
            findViewById( R.id.registrationScrollViewSV ).setVisibility( View.VISIBLE );


        } else {
            Log.d( TAG, "onNetworkConnectionChanged: " + isConnected );
            findViewById( R.id.regMainNoInternetView ).setVisibility( View.VISIBLE );
            findViewById( R.id.registrationScrollViewSV ).setVisibility( View.GONE );


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
