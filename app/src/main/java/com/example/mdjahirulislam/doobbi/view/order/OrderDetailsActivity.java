package com.example.mdjahirulislam.doobbi.view.order;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.OrderListAdapter;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.RESIZE_IMAGE_HEIGHT;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.RESIZE_IMAGE_WIDTH;

public class OrderDetailsActivity extends AppCompatActivity {
    private static final String TAG = OrderDetailsActivity.class.getSimpleName();


    //View
    private CircleImageView userProfilePictureIV;
    private TextView userNameTV;
    private TextView userAddressTV;
    private TextView userMobileTV;
    private TextView totalPriceTV;
    private TextView totalQuantityTV;
    private RecyclerView orderListRV;

    private InsertUserDataModel userDataModel;
    private LinearLayoutManager mLayoutManager;
    private OrderListAdapter mAdapter;



    private void initialization() {
        userProfilePictureIV = findViewById( R.id.orderDetailsUserProfilePictureIV );
        userNameTV = findViewById( R.id.orderDetailsUserNameTV );
        userAddressTV = findViewById( R.id.orderDetailsUserAddressTV );
        userMobileTV = findViewById( R.id.orderDetailsUserMobileTV );
        orderListRV = findViewById( R.id.orderDetailsListRV );
        totalPriceTV = findViewById( R.id.orderDetailsTotalPriceTV );
        totalQuantityTV = findViewById( R.id.orderDetailsTotalQuantityTV );
        userDataModel = new InsertUserDataModel(  );
//        orderListNotFoundTV = findViewById( R.id.orderListNotFoundTV );
//        orderItemHistoryList = new ArrayList<>();
//        sessionManager = new SessionManager( this );
//        dataModelArrayList = new ArrayList<>();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_order_details );
        initialization();

        //Get user details from local database
        userDataModel = DBFunctions.getUserInformation();

        userNameTV.setText( userDataModel.getName() );
        userAddressTV.setText( userDataModel.getAddress() );
        userMobileTV.setText( userDataModel.getPhone() );
        Picasso.get().load( userDataModel.getClint_image_path() )
                .placeholder( R.drawable.nobody )
                .resize( RESIZE_IMAGE_WIDTH,RESIZE_IMAGE_HEIGHT )
                .centerCrop()
                .into( userProfilePictureIV );

//        todo next work

//        mLayoutManager = new LinearLayoutManager( this );
//        mAdapter = new OrderListAdapter( this, orderList );
//        orderListRV.setAdapter( mAdapter );
//
//        orderListRV.setLayoutManager( mLayoutManager );


//        Thread temporaryOrderListThread = new Thread( new OrderListActivity.GetTemporaryOrderListThread() );
//        temporaryOrderListThread.start();

        Functions.ProgressDialog( this );
        Functions.showDialog();

        Functions.ProgressThread progressThread =  new Functions.ProgressThread() ;
        progressThread.start();

        Toast.makeText( this, "Work Not Done. Need Api Integration ", Toast.LENGTH_SHORT ).show();



    }
}
