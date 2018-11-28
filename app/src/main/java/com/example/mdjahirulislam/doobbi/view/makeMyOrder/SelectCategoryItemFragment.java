package com.example.mdjahirulislam.doobbi.view.makeMyOrder;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.SelectedCategoryItemPriceAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.ItemPriceModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetItemWisePriceResponseModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_CATEGORY_ITEMS_PRICE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.hideDialog;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.showDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectCategoryItemFragment extends Fragment {
    private static final String TAG = SelectCategoryItemFragment.class.getSimpleName();

    private static final String ARG_POSITION = "position";


    private RecyclerView mRecyclerView;
    private SelectedCategoryItemPriceAdapter mAdapter;
    private Context context;

    private ConnectionAPI connectionApi;
    private ArrayList<ItemPriceModel> itemPriceModels;
//    private ArrayList<String> itemIdList;
    private GetItemWisePriceResponseModel priceResponseModel;
    private Thread getItemWisePriceThread;
    private int itemID;

    private ImageView itemImageIV;
    private TextView regularPriceTV;
    private TextView itemTitleTV;


    private SessionManager sessionManager;

    public SelectCategoryItemFragment() {
        // Required empty public constructor
    }

    public interface onTotalPriceListener {
        public void setPrice(int price, int operator);
    }

    private onTotalPriceListener totalPriceListener;


    public static SelectCategoryItemFragment newInstance(int position) {
        SelectCategoryItemFragment fragment = new SelectCategoryItemFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_POSITION, position );
        fragment.setArguments( args );
        return fragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            totalPriceListener = (onTotalPriceListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_select_category_item, container, false );

        itemImageIV = view.findViewById( R.id.fragment_selected_item_image_IV );
        regularPriceTV = view.findViewById( R.id.singleItemPriceTagTV );
        itemTitleTV = view.findViewById( R.id.fragment_selected_item_name_TV );
        context = getActivity().getApplicationContext();
        mRecyclerView = view.findViewById( R.id.selected_category_list_view_RV );
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );
        itemPriceModels = new ArrayList<>(  );

        if (getArguments() != null) {

            itemID = getArguments().getInt( ARG_POSITION );
            Log.d( TAG, "onCreate: " + itemID );
        }

        sessionManager = new SessionManager( getContext() );

        getItemWisePriceThread = new Thread( new GetItemWisePriceThread());

        try {
            Functions.ProgressDialog( getActivity() );
            showDialog();
            getItemWisePriceThread.start();

            Log.d( TAG, "onCreateView: show dialog" );


        } catch (Exception e) {
            Log.d( TAG, "onCreate: catch " + e.getLocalizedMessage() );
        } finally {
            Log.d( TAG, "onCreate: finally ->" + getItemWisePriceThread.isAlive() );

//            getTabNameThread.interrupt();
            Log.d( TAG, "onCreate: finally " + Thread.currentThread().isAlive() );
        }

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );


//        mAdapter = new SelectedCategoryItemPriceAdapter(context, null);


    }

    class GetItemWisePriceThread implements Runnable {
        public void run() {
            Log.d( TAG, "run: thread is running..." );
            Log.d( TAG, "run: "+Thread.currentThread().isAlive() );

            RequestBody password = RequestBody.create( MultipartBody.FORM, API_ACCESS_PASSWORD );
            RequestBody user = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
            RequestBody itemID = RequestBody.create( MultipartBody.FORM, String.valueOf( SelectCategoryItemFragment.this.itemID ) );
            RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_GET_CATEGORY_ITEMS_PRICE );

//                Log.d( TAG, "run: data: " + phone + "\ndataModel: " + userPhone );

            final Call<GetItemWisePriceResponseModel> insertUserResponseModelCallBack = connectionApi.getCategoryItemWisePrice( password, user,itemID,
                    function );


            itemPriceModels.clear();
//            itemIdList.clear();
            insertUserResponseModelCallBack.enqueue( new Callback<GetItemWisePriceResponseModel>() {
                @Override
                public void onResponse(Call<GetItemWisePriceResponseModel> call, Response<GetItemWisePriceResponseModel> response) {
                    if (response.code() == 200) {
                        priceResponseModel = response.body();
                        String status = priceResponseModel.getStatus();
                        Log.d( TAG, "Status : " + priceResponseModel.toString() );
                        // deny code is 100
                        if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) { // Status success code = 100

                            for (int i = 0; i < priceResponseModel.getItem().size(); i++) {
                                GetItemWisePriceResponseModel.Item items = priceResponseModel.getItem().get( i );
                               itemPriceModels.add( new ItemPriceModel( items.getPriceId(),
                                       items.getItemId(),items.getServiceId(),items.getServiceName(),
                                       items.getSalesPrice(),items.getBasePrice(),items.getDiscountPer()) );
                            }

                            itemTitleTV.setText( priceResponseModel.getItemName());
                            regularPriceTV.setText( "Tk. "+priceResponseModel.getItem().get( 0 ).getSalesPrice() );
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                            mAdapter = new SelectedCategoryItemPriceAdapter( context, itemPriceModels );
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setAdapter(mAdapter);


                            hideDialog();
                        }
                        // deny code is 101
                        else if (status.equalsIgnoreCase( NO_USER_FOUND_CODE )) {
                            String error_msg = priceResponseModel.getDetail();
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
                public void onFailure(Call<GetItemWisePriceResponseModel> call, Throwable t) {

                    Log.d( TAG, "onFailure: " + t.getLocalizedMessage() );
                    Log.d( TAG, "onFailure: " + t.toString() );
                    Log.d( TAG, "onFailure: " + t.getMessage() );
                    hideDialog();

                }
            } );

        }
    }
}
