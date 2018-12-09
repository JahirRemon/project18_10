package com.example.mdjahirulislam.doobbi.view.profile;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.GPSTracker;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.example.mdjahirulislam.doobbi.view.authentication.LoginActivity;
import com.example.mdjahirulislam.doobbi.view.authentication.RegistrationActivity;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.RESIZE_IMAGE_HEIGHT;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.RESIZE_IMAGE_WIDTH;

public class ProfileViewActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = ProfileViewActivity.class.getSimpleName();

    //  view
    private CircleImageView userProfilePictureIV;
    private TextView nameTV;
    private TextView mobileNoTV;
    private TextView anotherMobileNoTV;
    private TextView emailTV;
    private TextView areaTV;
    private TextView flatNoTV;
    private TextView houseNoTV;
    private TextView roadNoTV;
    private TextView updateTV;
    private SupportMapFragment mapFragment;

    //    model
    private InsertUserDataModel userDataModel;

    //Map
    private GoogleMap mMap;
    private double latitude;
    private double longitude;


    private void initialization() {
        nameTV = findViewById(R.id.profileViewUserName);
        mobileNoTV = findViewById(R.id.profileViewUserMobile);
        anotherMobileNoTV = findViewById(R.id.profileViewUserAnotherMobile);
        emailTV = findViewById(R.id.profileViewUserEmail);
        areaTV = findViewById(R.id.profileViewUserAreaTV);
        flatNoTV = findViewById(R.id.profileViewUserFlatNo);
        houseNoTV = findViewById(R.id.profileViewUserHouseNo);
        roadNoTV = findViewById(R.id.profileViewUserRoadNo);
        updateTV = findViewById(R.id.profileViewUpdateTV);
        userProfilePictureIV = findViewById(R.id.profileViewProfileImage);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.registrationMap);
        userDataModel = new InsertUserDataModel();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);

        initialization();
        //        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userDataModel = DBFunctions.getUserInformation();

        if (userDataModel != null) {
            Log.d(TAG, "onCreate: user Model: --> " + userDataModel.toString());

            nameTV.setText(userDataModel.getName());
            mobileNoTV.setText(userDataModel.getPhone());
            anotherMobileNoTV.setText(userDataModel.getAnother_phone());
            emailTV.setText(userDataModel.getEmail());
            areaTV.setText("Area: " + userDataModel.getAddress());
            flatNoTV.setText("Flat No: " + userDataModel.getFlat_no());
            houseNoTV.setText("House No: " + userDataModel.getHouse_no());
            roadNoTV.setText("Road No: " + userDataModel.getRoad_no());
            Picasso
                    .get()
                    .load(Functions._IMAGE_BASE_URL + userDataModel.getClint_image_path())
                    .resize(RESIZE_IMAGE_WIDTH, RESIZE_IMAGE_HEIGHT) // resizes the image to these dimensions (in pixel)
                    .centerCrop()
                    .placeholder(R.drawable.nobody)
                    .into(userProfilePictureIV);
            latitude = Double.parseDouble(userDataModel.getLatitude());
            longitude = Double.parseDouble(userDataModel.getLongitude());
            mapFragment.getMapAsync(ProfileViewActivity.this);

        } else {
            Toast.makeText(this, "No User Data Found", Toast.LENGTH_SHORT).show();
            updateTV.setText("Login");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void profileShowPassword(View view) {
    }

    public void goToRegistrationActivityFromProfileViewActivity(View view) {
        if (updateTV.getText().toString().equalsIgnoreCase("login")){
            startActivity(new Intent(this, LoginActivity.class).putExtra(Functions._INTENT_FROM, TAG));
        }else {
            startActivity(new Intent(this, RegistrationActivity.class).putExtra(Functions._INTENT_FROM, TAG));
        }

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


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        final LatLng currentPosition = new LatLng( latitude, longitude );

        mMap.addMarker(new MarkerOptions().position(currentPosition).title(userDataModel.getAddress()));
        mMap.moveCamera( CameraUpdateFactory.newLatLngZoom( currentPosition, 16.3f ) );
        mMap.getUiSettings().setScrollGesturesEnabled( false );

    }
}
