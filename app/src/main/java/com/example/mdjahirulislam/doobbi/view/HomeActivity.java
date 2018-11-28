package com.example.mdjahirulislam.doobbi.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.ConnectivityReceiver;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.controller.requestThread.GetTadItemThread;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetTadItemResponseModel;
import com.example.mdjahirulislam.doobbi.view.authentication.LoginActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.OrderHomeActivity;
import com.example.mdjahirulislam.doobbi.view.offers.MyOfferActivity;
import com.example.mdjahirulislam.doobbi.view.order.OrderListActivity;
import com.example.mdjahirulislam.doobbi.view.pickUpMe.PickUpLocationActivity;
import com.example.mdjahirulislam.doobbi.view.priceList.PriceListActivity;
import com.example.mdjahirulislam.doobbi.view.schedule.ScheduleListActivity;
import com.smarteist.autoimageslider.SliderLayout;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
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
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.progressBar;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = HomeActivity.class.getSimpleName();
    private SliderLayout sliderLayout;
    private LinearLayout scheduleIconLL;
    private LinearLayout makeOrderLL;
    private LinearLayout priceListLL;
    private LinearLayout pickUpMeLL;
    private LinearLayout orderListLL;
    private LinearLayout myOfferLL;


    private TextView navUserName;
    private TextView navUserMobile;

    private Realm mRealm;

    private String user_id;
    private Menu navMenuItem;

    private boolean doubleBackToExitPressedOnce = false;
    private SessionManager sessionManager;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        scheduleIconLL = findViewById( R.id.scheduleIconLL );
        makeOrderLL = findViewById( R.id.makeOrderLL );
        priceListLL = findViewById( R.id.priceListLL );
        pickUpMeLL = findViewById( R.id.pickUpMeLL );
        orderListLL = findViewById( R.id.orderListLL );
        myOfferLL = findViewById( R.id.myOfferLL );
        mRealm = Realm.getDefaultInstance();
        sessionManager = new SessionManager( this );

        final DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close );
        toggle.setDrawerIndicatorEnabled( false );
        toggle.setToolbarNavigationClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer( GravityCompat.START );
            }
        } );
        toggle.setHomeAsUpIndicator( R.drawable.ic_dashboard_white_24dp );

        drawer.addDrawerListener( toggle );
        toggle.syncState();

//        mDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

//        mDrawerToggle.setDrawerIndicatorEnabled(false);

        NavigationView navigationView = (NavigationView) findViewById( R.id.nav_view );
        navigationView.setNavigationItemSelectedListener( this );

        View headerLayout = navigationView.getHeaderView( 0 );

        navUserName = headerLayout.findViewById( R.id.navUserName );
        navUserMobile = headerLayout.findViewById( R.id.navUserMobile );
        navMenuItem = navigationView.getMenu();


        sliderLayout = findViewById( R.id.imageSlider );
        sliderLayout.setIndicatorAnimation( SliderLayout.Animations.WORM ); //set indicator animation by using SliderLayout.Animations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderLayout.setScrollTimeInSec( 1 ); //set scroll delay in seconds :


        setSliderViews();

        scheduleIconLL.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = scheduleIconLL.getWidth();
                float height = scheduleIconLL.getHeight();
                Log.d( TAG, "onTouch 1: classTimeLL ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        scheduleIconLL.setAlpha( 0.5f );
                        Log.d( TAG, "onTouch 2: classTimeLL ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            scheduleIconLL.setAlpha( 1.0f );
                            startActivity( new Intent( HomeActivity.this, ScheduleListActivity.class ) );

//
//                            InsertClassTimeDataThread insertClassTimeDataThread = new InsertClassTimeDataThread( getContext() );
//                            insertClassTimeDataThread.start();
//                            Functions.ProgressDialog(getContext());
//                            Functions.showDialog();

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        scheduleIconLL.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        scheduleIconLL.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );

        Functions.getMyPhoneNO( this );

        makeOrderLL.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = makeOrderLL.getWidth();
                float height = makeOrderLL.getHeight();
                Log.d( TAG, "onTouch 1: classTimeLL ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        makeOrderLL.setAlpha( 0.5f );
                        Log.d( TAG, "onTouch 2: classTimeLL ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            makeOrderLL.setAlpha( 1.0f );
                            startActivity( new Intent( HomeActivity.this, OrderHomeActivity.class ) );

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        makeOrderLL.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        makeOrderLL.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );


        priceListLL.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = priceListLL.getWidth();
                float height = priceListLL.getHeight();
                Log.d( TAG, "onTouch 1: classTimeLL ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        priceListLL.setAlpha( 0.5f );
                        Log.d( TAG, "onTouch 2: classTimeLL ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            priceListLL.setAlpha( 1.0f );
                            startActivity( new Intent( HomeActivity.this, PriceListActivity.class ) );

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        priceListLL.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        priceListLL.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );


        pickUpMeLL.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = pickUpMeLL.getWidth();
                float height = pickUpMeLL.getHeight();
                Log.d( TAG, "onTouch 1: classTimeLL ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pickUpMeLL.setAlpha( 0.5f );
                        Log.d( TAG, "onTouch 2: classTimeLL ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d( TAG, "onTouch: X: " + event.getX() + "\nY: " + event.getY() );
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            pickUpMeLL.setAlpha( 1.0f );
                            startActivity( new Intent( HomeActivity.this, PickUpLocationActivity.class ) );

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        pickUpMeLL.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        pickUpMeLL.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );

        orderListLL.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = orderListLL.getWidth();
                float height = orderListLL.getHeight();
                Log.d( TAG, "onTouch 1: classTimeLL ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        orderListLL.setAlpha( 0.5f );
                        Log.d( TAG, "onTouch 2: classTimeLL ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d( TAG, "onTouch: X: " + event.getX() + "\nY: " + event.getY() );
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            orderListLL.setAlpha( 1.0f );
                            startActivity( new Intent( HomeActivity.this, OrderListActivity.class ) );

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        orderListLL.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        orderListLL.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );

        myOfferLL.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = myOfferLL.getWidth();
                float height = myOfferLL.getHeight();
                Log.d( TAG, "onTouch 1: classTimeLL ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        myOfferLL.setAlpha( 0.5f );
                        Log.d( TAG, "onTouch 2: classTimeLL ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.d( TAG, "onTouch: X: " + event.getX() + "\nY: " + event.getY() );
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            myOfferLL.setAlpha( 1.0f );
                            startActivity( new Intent( HomeActivity.this, MyOfferActivity.class ) );

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        myOfferLL.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        orderListLL.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );

    }

    @Override
    protected void onResume() {
        super.onResume();
        mRealm.beginTransaction();

        final RealmResults<InsertUserDataModel> getUserInfo = mRealm.where( InsertUserDataModel.class ).findAll();

        Log.d( TAG, "onResume: " + getUserInfo.size() );

        if (0 < getUserInfo.size()) {
            for (InsertUserDataModel userDataModel : getUserInfo) {
                navUserName.setText( userDataModel.getName() );
                navUserMobile.setText( getUserInfo.get( 0 ).getPhone() );
                navMenuItem.findItem( R.id.nav_log_out ).setVisible( true );
                navMenuItem.findItem( R.id.nav_signIn ).setVisible( false );
                this.user_id = getUserInfo.get( 0 ).getClint_id();
            }
        } else {
            Log.d( TAG, "onResume: Data not found of not login" );
            navMenuItem.findItem( R.id.nav_log_out ).setVisible( false );
            navMenuItem.findItem( R.id.nav_signIn ).setVisible( true );


        }
        mRealm.commitTransaction();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        if (drawer.isDrawerOpen( GravityCompat.START )) {
            drawer.closeDrawer( GravityCompat.START );
        } else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText( this, "Please click BACK again to exit", Toast.LENGTH_SHORT ).show();

        new Handler().postDelayed( new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 1000 );
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_payment) {
            // Handle the camera action
        } else if (id == R.id.nav_payment) {

        } else if (id == R.id.nav_signIn) {
            startActivity( new Intent( HomeActivity.this, LoginActivity.class ) );

        } else if (id == R.id.nav_log_out) {
            Log.d( TAG, "onNavigationItemSelected: log out " + user_id );

            mRealm.beginTransaction();
            RealmResults<InsertUserDataModel> result = mRealm.where( InsertUserDataModel.class ).equalTo( "clint_id", user_id ).findAll();
            result.deleteAllFromRealm();


            mRealm.commitTransaction();

            Functions.ProgressDialog( this );
            sessionManager.setLogin( false );
            finish();
            startActivity( getIntent() );

        }
        DrawerLayout drawer = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawer.closeDrawer( GravityCompat.START );
        return true;
    }

    private void setSliderViews() {

        for (int i = 0; i <= 3; i++) {

            SliderView sliderView = new SliderView( this );

            switch (i) {
                case 0:
                    sliderView.setImageUrl( "https://i.imgur.com/3rNjPzP.jpg" );
                    break;
                case 1:
                    sliderView.setImageUrl( "https://i.imgur.com/8wX7Ttz.jpg" );
                    break;
                case 2:
                    sliderView.setImageUrl( "https://imgur.com/mOa6L8z.jpg" );
                    break;
                case 3:
                    sliderView.setImageUrl( "https://imgur.com/aWCAnF5.jpg" );
                    break;
            }

            sliderView.setImageScaleType( ImageView.ScaleType.CENTER_CROP );
//            sliderView.setDescription("setDescription " + (i + 1));

            final int finalI = i;
            sliderView.setOnSliderClickListener( new SliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(SliderView sliderView) {
                    Toast.makeText( HomeActivity.this, "This is slider " + (finalI + 1), Toast.LENGTH_SHORT ).show();
                }
            } );

            //at last add this view in your layout :
            sliderLayout.addSliderView( sliderView );
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.home_menu, menu );
        return true;
    }

    public void goToScheduleListActivity(final View view) {

        Log.d( TAG, "goToScheduleListActivity: " );
    }


    public void callTheAuthority(View view) {
        Log.d( TAG, "callTheAuthority: " );
        Functions.showSnack( ConnectivityReceiver.isConnected( HomeActivity.this ), view );
    }


}
