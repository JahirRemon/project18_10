package com.example.mdjahirulislam.doobbi.view.priceList;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.AllItemsPriceListAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetAllItemsPriceResponseModel;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.CategoryListFragment;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_CATEGORY_ITEMS;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_ITEMS_PRICE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;
import static com.smarteist.autoimageslider.IndicatorView.utils.DensityUtils.dpToPx;

/**
 * A simple {@link Fragment} subclass.
 */
public class PriceListFragment extends Fragment {
    private static final String TAG = PriceListFragment.class.getSimpleName();


    private static final String ARG_POSITION = "position";
    private ConnectionAPI connectionApi;
    private RecyclerView mRecyclerView;
    private ArrayList<GetAllItemsPriceResponseModel.Price> priceArrayList;
    private GetAllItemsPriceResponseModel itemResponseModel;
    private int categoryID = 0;


    public PriceListFragment() {
        // Required empty public constructor
    }

    public static PriceListFragment newInstance(int position) {
        PriceListFragment fragment = new PriceListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_price_list, container, false);
        mRecyclerView = view.findViewById(R.id.priceListFragmentListRV);
        priceArrayList = new ArrayList<>();
        connectionApi = Functions.getRetrofit().create(ConnectionAPI.class);
        if (getArguments() != null) {

            categoryID = getArguments().getInt( ARG_POSITION );
            Log.d( TAG, "onCreate: " + categoryID );
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mRecyclerView.addItemDecoration(new CategoryListFragment.GridSpacingItemDecoration(2, dpToPx(16), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);

        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );

        Thread priceThread = new Thread( new getAllItemsPriceThread() );

        try {
            priceThread.start();
        } catch (Exception e) {
            Log.d( TAG, "onCreate: " + e.getLocalizedMessage() );
        } finally {
            Log.d( TAG, "onCreate: finally" + priceThread.isAlive() );
            Log.d( TAG, "onCreate: finally " + Thread.currentThread().isAlive() );
//            hideDialog();
        }


    }


    class getAllItemsPriceThread implements Runnable {
        public void run() {

            RequestBody password = RequestBody.create(MultipartBody.FORM, API_ACCESS_PASSWORD);
            RequestBody user = RequestBody.create(MultipartBody.FORM, API_ACCESS_ID);
            RequestBody function = RequestBody.create(MultipartBody.FORM, API_ACCESS_FUNCTION_GET_ITEMS_PRICE);
//            RequestBody categoryID = RequestBody.create(MultipartBody.FORM, String.valueOf(PriceListFragment.this.categoryID));

            final Call<GetAllItemsPriceResponseModel> itemsPriceResponseModelCall = connectionApi.getAllItemsPrice(password, user, function);

            itemsPriceResponseModelCall.enqueue(new Callback<GetAllItemsPriceResponseModel>() {
                @Override
                public void onResponse(Call<GetAllItemsPriceResponseModel> call, Response<GetAllItemsPriceResponseModel> response) {
                    if (response.code() == 200) {
                        itemResponseModel = response.body();
                        String status = itemResponseModel.getStatus();
                        Log.d(TAG, "Status : " + itemResponseModel.toString());
                        // deny code is 100
                        if (status.equalsIgnoreCase(API_ACCESS_SUCCESS_CODE)) { // Status success code = 100

                            priceArrayList.clear();
                            for (GetAllItemsPriceResponseModel.Price price : itemResponseModel.getPrice()) {
                                priceArrayList.add(price);
                            }
                            mRecyclerView.setAdapter(new AllItemsPriceListAdapter(getContext(), priceArrayList, PriceListFragment.this.categoryID));
//                            hideDialog();
                        }
                        // deny code is 101
                        else if (status.equalsIgnoreCase(NO_USER_FOUND_CODE)) {
                            String error_msg = itemResponseModel.getDetail();
                            Log.d(TAG, "onResponse: " + error_msg);
                        } else {
                            Log.d(TAG, "onResponse: Some Is Wrong");
                        }


                    } else {
//                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onResponse: Server Error response.code ===> " + response.code());
                    }
                    Log.d(TAG, "onResponse: " + response.body() + " \n\n" + response.raw() + " \n\n" + response.toString() + " \n\n" + response.errorBody());
//                    hideDialog();


                }

                @Override
                public void onFailure(Call<GetAllItemsPriceResponseModel> call, Throwable t) {

                    Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
                    Log.d(TAG, "onFailure: " + t.toString());
                    Log.d(TAG, "onFailure: " + t.getMessage());
//                    hideDialog();

                }
            });

        }
    }

}
