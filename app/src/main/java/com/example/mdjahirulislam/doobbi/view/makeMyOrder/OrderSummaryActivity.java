package com.example.mdjahirulislam.doobbi.view.makeMyOrder;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.OrderListSummaryAdapter;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.controller.requestThread.InsertOrderListThread;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderDataModel;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderHistoryDBModel;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.view.authentication.LoginActivity;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmError;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions._INTENT_FROM;

public class OrderSummaryActivity extends AppCompatActivity implements OrderListSummaryAdapter.OnTotalPriceAndQuantityListener {

    private static final String TAG = OrderListSummaryAdapter.class.getSimpleName();

    private CircleImageView userProfilePictureIV;
    private TextView userNameTV;
    private TextView userAddressTV;
    private TextView userMobileTV;
    private TextView totalPriceTV;
    private TextView totalQuantityTV;
    private TextView orderListNotFoundTV;
    private RecyclerView orderListRV;

    private OrderListSummaryAdapter mAdapter;
    private ArrayList<InsertOrderHistoryDBModel> orderItemHistoryList;
    private ArrayList<InsertOrderDataModel> dataModelArrayList;
    private String dataModel;
    private int totalQuantity = 00;
    private int startPrice = 00;
    private int startQuantity = 00;

    private Realm mRealm;
    private SessionManager sessionManager;


    private void initialization() {
        userProfilePictureIV = findViewById( R.id.orderUserProfilePictureIV );
        userNameTV = findViewById( R.id.orderUserNameTV );
        userAddressTV = findViewById( R.id.orderUserAddressTV );
        userMobileTV = findViewById( R.id.orderUserMobileTV );
        orderListRV = findViewById( R.id.orderSummaryListRV );
        totalPriceTV = findViewById( R.id.orderSummaryTotalPriceTV );
        totalQuantityTV = findViewById( R.id.orderSummaryTotalQuantityTV );
        orderListNotFoundTV = findViewById( R.id.orderListNotFoundTV );
        orderItemHistoryList = new ArrayList<>();
        mRealm = Realm.getDefaultInstance();
        sessionManager = new SessionManager( this );
        dataModelArrayList = new ArrayList<>();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_summary );
        initialization();
        orderItemHistoryList.clear();
        startPrice = DBFunctions.getAllOrderHistoryTotalPrice();
        startQuantity = DBFunctions.getAllOrderHistoryTotalItems();

        totalQuantityTV.setText( "Total " + String.valueOf( startQuantity ) + " Items" );
        totalPriceTV.setText( "Total Tk. " + String.valueOf( startPrice ) );

        LinearLayoutManager mLayoutManager = new LinearLayoutManager( this );


        InsertUserDataModel getUserDetails = mRealm.where( InsertUserDataModel.class ).findFirst();
        RealmResults<InsertOrderHistoryDBModel> results = mRealm.where( InsertOrderHistoryDBModel.class ).findAll();

        try {
            if (results.size() > 0) {
                if (Integer.parseInt( results.get( 0 ).getItemQuantity() ) > 0) {
                    orderListRV.setVisibility( View.VISIBLE );
                    orderListNotFoundTV.setVisibility( View.GONE );
                    mRealm.beginTransaction();

                    Log.d( TAG, "onCreate: size---> " + results.size() );
                    for (int i = 0; i < results.size(); i++) {
                        orderItemHistoryList.add( results.get( i ) );
                        Log.d( TAG, "onCreate: model--->" + results.toString() );
                        totalQuantity += Integer.parseInt( results.get( i ).getItemQuantity() );

                    }
                    mRealm.commitTransaction();

                    mAdapter = new OrderListSummaryAdapter( this, orderItemHistoryList, this );
                    orderListRV.setLayoutManager( mLayoutManager );
                    orderListRV.setAdapter( mAdapter );
                } else {
                    orderListRV.setVisibility( View.GONE );
                    orderListNotFoundTV.setVisibility( View.VISIBLE );

                }
            } else {
                orderListRV.setVisibility( View.GONE );
                orderListNotFoundTV.setVisibility( View.VISIBLE );

            }
        } catch (RealmError error) {
            Log.d( TAG, "onCreate: catch--> " + error );
        }

        if (getUserDetails != null) {
            userNameTV.setText( getUserDetails.getName() );
            userMobileTV.setText( getUserDetails.getPhone() );
            userAddressTV.setText( getUserDetails.getAddress() );

            Picasso.get().load( "imagePath" + getUserDetails.getClint_image_path() )
                    .centerCrop()
                    .resize( 70, 70 )
                    .error( R.drawable.nobody )
                    .into( userProfilePictureIV );
        } else {
//            startActivity( new Intent(  this,LoginActivity.class ));
        }

    }

    public void submitTempOrder(View view) {

        if (sessionManager.isLoggedIn()) {
            dataModelArrayList = DBFunctions.getAllOrderHistoryList();
            if (dataModelArrayList.size() > 0) {
                Gson gson = new Gson();
                dataModel = gson.toJson( dataModelArrayList );
                String phone = DBFunctions.getUserInformation().getPhone();

                Log.d( TAG, "submitTempOrder: " + dataModel );
                InsertOrderListThread insertNewUserThread = new InsertOrderListThread( this, dataModel, String.valueOf( DBFunctions.getAllOrderHistoryTotalItems() ), phone );
                insertNewUserThread.start();
                Functions.ProgressDialog( this );
                Functions.showDialog();
            } else {
                view.setEnabled( false );
                Toast.makeText( this, "Select Item First!!", Toast.LENGTH_SHORT ).show();
            }
        } else {
            startActivity( new Intent( this, LoginActivity.class ).putExtra( _INTENT_FROM,OrderSummaryActivity.class.getSimpleName() ) );
            finish();
        }
    }

    @Override
    public void setPrice() {
        Functions.setAnimationNumber( totalPriceTV, "Total Tk. ", startPrice, DBFunctions.getAllOrderHistoryTotalPrice(), "", 1000 );

    }

    @Override
    public void setQuantity() {
        int lastQuantity = DBFunctions.getAllOrderHistoryTotalItems();
        Functions.setAnimationNumber( totalQuantityTV, "Total ", startQuantity, lastQuantity, " Items", 100 );


    }
}
