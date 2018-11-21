package com.example.mdjahirulislam.doobbi.view.pickUpMe;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.test.mock.MockPackageManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.ConnectivityReceiver;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.GPSTracker;
import com.example.mdjahirulislam.doobbi.controller.helper.MyApplication;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.authentication.RegistrationActivity;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

public class PickUpLocationActivity extends FragmentActivity implements OnMapReadyCallback,ConnectivityReceiver.ConnectivityReceiverListener {

    private static final String TAG = PickUpLocationActivity.class.getSimpleName();

    private GoogleMap mMap;
    private static final int REQUEST_CODE_PERMISSION = 2;
    private String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;

    // GPSTracker class
    private GPSTracker gps;

    private double latitude;
    private double longitude;

    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingMyLocationActionButton;
    private FloatingActionButton floatingZoomInActionButton;
    private FloatingActionButton floatingZoomOutActionButton;
    private FloatingActionButton floatingCompassActionButton;
    private TextView okTV;
    private TextView cancelTV;
    private LatLng[] myLocation;
    private boolean isConnected = false;


    private TextView showAddressTV;
    private float zoomLevel = 14.0f;

    private MapView mMapView;
    private GoogleMapOptions googleMapOptions;
    private SupportPlaceAutocompleteFragment autocompleteFragment;
    private ConnectivityReceiver connectivityReceiver;


    private void initialization() {
        mMapView = (MapView) findViewById( R.id.registrationDialogMap );
        showAddressTV = findViewById( R.id.currentLocationInfoTV );
        autocompleteFragment = (SupportPlaceAutocompleteFragment) getSupportFragmentManager()
                .findFragmentById( R.id.dialog_place_autocomplete_fragment );

        materialDesignFAM = (FloatingActionMenu) findViewById( R.id.map_floating_menu );
        floatingMyLocationActionButton = (FloatingActionButton) findViewById( R.id.floating_my_location );
        floatingZoomInActionButton = (FloatingActionButton) findViewById( R.id.floating_zoom_in );
        floatingZoomOutActionButton = (FloatingActionButton) findViewById( R.id.floating_zoom_out );
        floatingCompassActionButton = (FloatingActionButton) findViewById( R.id.floating_compass );

        okTV = findViewById( R.id.mapDialogOkTV );
        cancelTV = findViewById( R.id.mapDialogCancelTV );

        myLocation = new LatLng[1];


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        connectivityReceiver = new ConnectivityReceiver();
        isConnected = ConnectivityReceiver.isConnected( this );
        setContentView(  R.layout.map_dialog_view );

        if (isConnected) {

            findViewById( R.id.mapMainNoInternetView ).setVisibility( View.GONE );
            findViewById( R.id.mapMainView ).setVisibility( View.VISIBLE );

            initialization();
            try {
                if (ActivityCompat.checkSelfPermission( this, mPermission )
                        != MockPackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions( this, new String[]{mPermission},
                            REQUEST_CODE_PERMISSION );

                    // If any permission above not allowed by user, this condition will
//                execute every time, else your else part will work
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // create class object
            gps = new GPSTracker( PickUpLocationActivity.this );

            // check if GPS enabled
            if (gps.canGetLocation()) {

                latitude = gps.getLatitude();
                longitude = gps.getLongitude();

                // \n is for new line
//            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
            } else {
                // can't get location
                // GPS or Network is not enabled
                // Ask user to enable GPS/network in settings
                gps.showSettingsAlert();
            }


            googleMapOptions = new GoogleMapOptions();
            MapsInitializer.initialize( PickUpLocationActivity.this );

            mMapView.onCreate( savedInstanceState );
            mMapView.onResume();

            mMapView.getMapAsync( PickUpLocationActivity.this );

            // Start Auto Place Complete
            Log.d( TAG, "autoComplete: " );


            //autocompleteFragment Search text size chane
            final EditText etPlace = (EditText) autocompleteFragment.getView().findViewById( R.id.place_autocomplete_search_input );

            etPlace.setTextSize( 16.0f );

            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setCountry( "BD" )
                    .build();

            autocompleteFragment.setFilter( typeFilter );

//        autocompleteFragment.setBoundsBias(new LatLngBounds(
//                new LatLng(23.7808875, 90.2792398),
//                new LatLng(23.810332, 90.4125181)));
//

            autocompleteFragment.setOnPlaceSelectedListener( new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {

                    // TODO: Get info about the selected place.
                    Log.i( TAG, "Place: " + place.getName() );
                    mMap.animateCamera( CameraUpdateFactory.newLatLngZoom( place.getLatLng(), zoomLevel ) );

                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    Log.i( TAG, "An error occurred: " + status );
                }
            } );


            floatingMyLocationActionButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu first item clicked
                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();
                        mMap.animateCamera( CameraUpdateFactory.newLatLng( new LatLng( latitude, longitude ) ) );


                    } else {
                        // can't get location
                        // GPS or Network is not enabled
                        // Ask user to enable GPS/network in settings
                        gps.showSettingsAlert();
                    }

                }
            } );
            floatingZoomInActionButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu second item clicked


                    mMap.animateCamera( CameraUpdateFactory.zoomIn() );
                    zoomLevel = mMap.getCameraPosition().zoom;
                    Log.d( TAG, "onClick: zoomLevel :" + zoomLevel );


                }
            } );
            floatingZoomOutActionButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu third item clicked

                    mMap.animateCamera( CameraUpdateFactory.zoomOut() );
                    zoomLevel = mMap.getCameraPosition().zoom;
                    Log.d( TAG, "onClick: zoomLevel :" + zoomLevel );

                }
            } );

            floatingCompassActionButton.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    //TODO something when floating action menu first item clicked

                    CameraPosition currentPlace = new CameraPosition.Builder()
                            .target( myLocation[0] )
                            .bearing( 0f )
                            .zoom( zoomLevel )
                            .build();
                    mMap.animateCamera( CameraUpdateFactory.newCameraPosition( currentPlace ) );

                }
            } );


            okTV.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO something when click ok text view. send user location data lat & log.

                    Intent intent = new Intent( PickUpLocationActivity.this, HomeActivity.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity( intent );
                    finish();
                }
            } );

            cancelTV.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO something when click ok text view. send user location data lat & log.

                    Intent intent = new Intent( PickUpLocationActivity.this, HomeActivity.class );
                    intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity( intent );
                    finish();
                }
            } );
        } else {
            findViewById( R.id.mapMainNoInternetView ).setVisibility( View.VISIBLE );
            findViewById( R.id.mapMainView ).setVisibility( View.GONE );

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        registerReceiver(connectivityReceiver, intentFilter);

        /*register connection status listener*/
        MyApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver( connectivityReceiver );
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        myLocation[0] = new LatLng( latitude, longitude );

        mMap.setBuildingsEnabled( true );
        // Add a marker in Sydney and move the camera
//        mMap.addMarker( new MarkerOptions().position( sydney ).title( "IQ Architects" ) );
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( myLocation[0], zoomLevel ) );

        mMap.setOnCameraIdleListener( new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                myLocation[0] = mMap.getCameraPosition().target;

//                searchLatLong[0] = center[0];
                showAddressTV.setVisibility( View.VISIBLE );
                showAddressTV.setAnimation( Functions.setTextAnimation() );
                showAddressTV.setText( String.valueOf( Functions.getAddress( PickUpLocationActivity.this, myLocation[0].latitude, myLocation[0].longitude ) ) );

            }
        } );
        mMap.setOnCameraMoveStartedListener( new GoogleMap.OnCameraMoveStartedListener() {
            @Override
            public void onCameraMoveStarted(int i) {
                Log.d( TAG, "onCameraMoveStarted: " + i );

//                showAddressTV.setVisibility( View.GONE );

            }
        } );
    }


    @Override
    public void onNetworkConnectionChanged(final boolean isConnected) {
        Log.d( TAG, "onNetworkConnectionChanged: " +isConnected);
        if (isConnected){
            Log.d( TAG, "onNetworkConnectionChanged: " +isConnected);

            findViewById( R.id.mapMainNoInternetView ).setVisibility( View.GONE );
            findViewById( R.id.mapMainView ).setVisibility( View.VISIBLE );


        }else {
            Log.d( TAG, "onNetworkConnectionChanged: " +isConnected);
            findViewById( R.id.mapMainNoInternetView ).setVisibility( View.VISIBLE );
            findViewById( R.id.mapMainView ).setVisibility( View.GONE );


        }
    }
}
