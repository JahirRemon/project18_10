package com.example.mdjahirulislam.doobbi.view.makeMyOrder;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.adapter.TabPageAdapter;
import com.example.mdjahirulislam.doobbi.controller.connectionInterface.ConnectionAPI;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.ItemClickListener;
import com.example.mdjahirulislam.doobbi.controller.adapter.CategoryItemsAdapter;
import com.example.mdjahirulislam.doobbi.model.CategoryItemsModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetCategoryItemResponseModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_FUNCTION_GET_CATEGORY_ITEMS;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_ID;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_PASSWORD;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.API_ACCESS_SUCCESS_CODE;
import static com.example.mdjahirulislam.doobbi.controller.helper.Functions.NO_USER_FOUND_CODE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryListFragment extends Fragment {

    private static final String TAG = CategoryListFragment.class.getSimpleName();


    private static final String ARG_POSITION = "position";
    private int mPosition;
    private RecyclerView mRecyclerView;
    private static int currentPosition = 0;
    private ArrayList<CategoryItemsModel> items;
    private Context context;

    private ConnectionAPI connectionApi;
    private GetCategoryItemResponseModel itemResponseModel;
    private Thread getCategoryItemThread;


    private OnFragmentInteractionListener mListener;

    public CategoryListFragment() {
        // Required empty public constructor
    }


    public static CategoryListFragment newInstance(int position) {
        CategoryListFragment fragment = new CategoryListFragment();
        Bundle args = new Bundle();
        args.putInt( ARG_POSITION, position );
        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        context = getActivity().getApplicationContext();

        if (getArguments() != null) {

            mPosition = getArguments().getInt( ARG_POSITION );
            Log.d( TAG, "onCreate: " + mPosition );
        }
        connectionApi = Functions.getRetrofit().create( ConnectionAPI.class );

        getCategoryItemThread = new Thread( new GetCategoryItemThread() );

        try {
            getCategoryItemThread.start();
        } catch (Exception e) {
            Log.d( TAG, "onCreate: " + e.getLocalizedMessage() );
        } finally {
            Log.d( TAG, "onCreate: finally" + getCategoryItemThread.isAlive() );
            Log.d( TAG, "onCreate: finally " + Thread.currentThread().isAlive() );
//            hideDialog();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_category_list, container, false );

        mRecyclerView = view.findViewById( R.id.category_list_view_RV );
        items = new ArrayList<>();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );


        mRecyclerView.setLayoutManager( new GridLayoutManager( getActivity(), 2 ) );
        mRecyclerView.addItemDecoration( new GridSpacingItemDecoration( 2, dpToPx( 6 ), true ) );
        mRecyclerView.setItemAnimator( new DefaultItemAnimator() );
        mRecyclerView.setHasFixedSize( true );

        currentPosition = getArguments().getInt( ARG_POSITION );

        mRecyclerView.addOnItemTouchListener( new ItemClickListener( getActivity(), mRecyclerView, new ItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity( new Intent( context, SelectItemActivity.class ).putExtra( "itemsModel", items ).putExtra( "position", position ) );
                getActivity().finish();
                Log.d( TAG, "onClick: " + position );
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ) );


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public static class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition( view ); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round( TypedValue.applyDimension( TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics() ) );
    }


    class GetCategoryItemThread implements Runnable {
        public void run() {

            RequestBody password = RequestBody.create( MultipartBody.FORM, API_ACCESS_PASSWORD );
            RequestBody user = RequestBody.create( MultipartBody.FORM, API_ACCESS_ID );
            RequestBody function = RequestBody.create( MultipartBody.FORM, API_ACCESS_FUNCTION_GET_CATEGORY_ITEMS );
            RequestBody categoryID = RequestBody.create( MultipartBody.FORM, String.valueOf( mPosition ) );

            final Call<GetCategoryItemResponseModel> insertUserResponseModelCallBack = connectionApi.getCategoryItem( password, user, categoryID,
                    function );

            insertUserResponseModelCallBack.enqueue( new Callback<GetCategoryItemResponseModel>() {
                @Override
                public void onResponse(Call<GetCategoryItemResponseModel> call, Response<GetCategoryItemResponseModel> response) {
                    if (response.code() == 200) {
                        itemResponseModel = response.body();
                        String status = itemResponseModel.getStatus();
                        Log.d( TAG, "Status : " + itemResponseModel.toString() );
                        // deny code is 100
                        if (status.equalsIgnoreCase( API_ACCESS_SUCCESS_CODE )) { // Status success code = 100

                            items.clear();
                            for (int i = 0; i < itemResponseModel.getCategory().size(); i++) {
                                GetCategoryItemResponseModel.Category itemsModel = itemResponseModel.getCategory().get( i );

                                items.add( new CategoryItemsModel( itemsModel.getCategoryId(),
                                        itemsModel.getItemId(),
                                        itemsModel.getItemName(),
                                        itemsModel.getMinPrice(),
                                        String.valueOf( itemsModel.getImage() ) ) );
//                                tabIdList.add( tadItemResponseModel.getCategory().get( i ).getId() );
                            }

                            mRecyclerView.setAdapter( new CategoryItemsAdapter( getContext(), items, currentPosition ) );
//                            hideDialog();
                        }
                        // deny code is 101
                        else if (status.equalsIgnoreCase( NO_USER_FOUND_CODE )) {
                            String error_msg = itemResponseModel.getDetail();
                            Log.d( TAG, "onResponse: " + error_msg );
                        } else {
                            Log.d( TAG, "onResponse: Some Is Wrong" );
                        }


                    } else {
//                    Toast.makeText(context, "Server Error", Toast.LENGTH_SHORT).show();
                        Log.d( TAG, "onResponse: Server Error response.code ===> " + response.code() );
                    }
                    Log.d( TAG, "onResponse: " + response.body() + " \n\n" + response.raw() + " \n\n" + response.toString() + " \n\n" + response.errorBody() );
//                    hideDialog();


                }

                @Override
                public void onFailure(Call<GetCategoryItemResponseModel> call, Throwable t) {

                    Log.d( TAG, "onFailure: " + t.getLocalizedMessage() );
                    Log.d( TAG, "onFailure: " + t.toString() );
                    Log.d( TAG, "onFailure: " + t.getMessage() );
//                    hideDialog();

                }
            } );

        }
    }
}
