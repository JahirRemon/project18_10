package com.example.mdjahirulislam.doobbi.view.order;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.OrderDetailsAdapter;
import com.example.mdjahirulislam.doobbi.controller.adapter.OrderListAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertUserDataModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetOrderDetailsResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetOrderListResponseModel;
import com.example.mdjahirulislam.doobbi.view.HomeActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_ORDER_LIST_DETAILS;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_TEMPORARY_ORDER_LIST;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.RESIZE_IMAGE_HEIGHT;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.RESIZE_IMAGE_WIDTH;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.tabNames;

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
    private ImageView qrIV;

    private InsertUserDataModel userDataModel;
    private LinearLayoutManager mLayoutManager;
    private OrderDetailsAdapter mAdapter;
    private ArrayList<GetOrderDetailsResponseModel.Item> orderDetailsList;

    private String userPhone;
    private String getOrderID;

    private ConnectionAPI connectionApi;
    private GetOrderDetailsResponseModel responseModel;



    private void initialization() {
        userProfilePictureIV = findViewById( R.id.orderDetailsUserProfilePictureIV );
        userNameTV = findViewById( R.id.orderDetailsUserNameTV );
        userAddressTV = findViewById( R.id.orderDetailsUserAddressTV );
        userMobileTV = findViewById( R.id.orderDetailsUserMobileTV );
        orderListRV = findViewById( R.id.orderDetailsListRV );
        totalPriceTV = findViewById( R.id.orderDetailsTotalPriceTV );
        totalQuantityTV = findViewById( R.id.orderDetailsTotalQuantityTV );
        userDataModel = new InsertUserDataModel(  );
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        qrIV = findViewById(R.id.orderDetailsQRIV);

        Functions.ProgressDialog(this);
        Functions.showDialog();

        orderDetailsList = new ArrayList<>();
        getOrderID = getIntent().getStringExtra("position");
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
        //        for back button on action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );

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


        String text= getOrderID; // Whatever you need to encode in the QR code
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,400,400);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrIV.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
//        todo next work
        userPhone = userDataModel.getPhone();

        mLayoutManager = new LinearLayoutManager( this );
//        mAdapter = new OrderDetailsAdapter( this, orderDetailsList );
//        orderListRV.setAdapter( mAdapter );

        orderListRV.setLayoutManager( mLayoutManager );


        Thread orderThread = new Thread( new OrderDetailsThread() );
        orderThread.start();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public class OrderDetailsThread implements Runnable{

        @Override
        public void run() {
            orderDetailsList.clear();
            RequestBody password = RequestBody.create( MultipartBody.FORM, API_ACCESS_PASSWORD );
            RequestBody user = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
            RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_GET_ORDER_LIST_DETAILS );
            RequestBody phone = RequestBody.create( MultipartBody.FORM, String.valueOf( userPhone ) );
            RequestBody orderID = RequestBody.create( MultipartBody.FORM,  getOrderID  );

            Log.d( TAG, "run: data: " + phone + "\ndataModel: " + userPhone );

            final Call<GetOrderDetailsResponseModel> getOrderDetailsResponseModelCall = connectionApi.getSingleOrderDetails( password, user,
                    function, phone, orderID );
            getOrderDetailsResponseModelCall.enqueue(new Callback<GetOrderDetailsResponseModel>() {
                @Override
                public void onResponse(Call<GetOrderDetailsResponseModel> call, Response<GetOrderDetailsResponseModel> response) {
                    if (response.code() == 200) {
                        responseModel = response.body();
                        String status = responseModel.getStatus();
                        Log.d( TAG, "Status : " + responseModel.toString() );
                        // deny code is 100
                        if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) { // Status success code = 100

                            for (GetOrderDetailsResponseModel.Item item: responseModel.getItems()){
                                orderDetailsList.add(item);
                            }

                            if (Integer.parseInt(responseModel.getTotalItem())==1) {
                                totalQuantityTV.setText("Total " + responseModel.getTotalItem() + " Item");
                            }else {
                                totalQuantityTV.setText("Total " + responseModel.getTotalItem() + " Items");
                            }
                            totalPriceTV.setText("Total Tk. "+responseModel.getTotalPayableAmount()+".00");
                            if (orderDetailsList!=null) {
                                mAdapter = new OrderDetailsAdapter(OrderDetailsActivity.this, orderDetailsList);
                                orderListRV.setAdapter(mAdapter);
                            }else {
                                Toast.makeText(OrderDetailsActivity.this, "Something is wrong!!!", Toast.LENGTH_SHORT).show();
                            }
                            hideDialog();
                        }
                        // deny code is 101
                        else if (status.equalsIgnoreCase( NO_USER_FOUND_CODE )) {
                            String error_msg = responseModel.getDetail();
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
                public void onFailure(Call<GetOrderDetailsResponseModel> call, Throwable t) {
                    Log.d( TAG, "onFailure: " + t.getLocalizedMessage() );
                    Toast.makeText(OrderDetailsActivity.this, "Something is Wrong. Please Try Again!!!", Toast.LENGTH_SHORT).show();
                    hideDialog();
                }
            });



        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // app icon in action bar clicked; goto parent activity.
        this.finish();
        return super.onOptionsItemSelected( item );
    }

}
