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
import com.example.mdjahirulislam.doobbi.controller.helper.ItemClickListener;
import com.example.mdjahirulislam.doobbi.controller.adapter.CategoryItemsAdapter;
import com.example.mdjahirulislam.doobbi.model.CategoryItemsModel;

import java.util.ArrayList;

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
        if (getArguments() != null) {
            mPosition = getArguments().getInt( ARG_POSITION );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate( R.layout.fragment_category_list, container, false );

        mRecyclerView = view.findViewById( R.id.category_list_view_RV );
        items = new ArrayList<>(  );
        context = getActivity();


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( view, savedInstanceState );


        mRecyclerView.setLayoutManager( new GridLayoutManager( getActivity(), 2 ) );
        mRecyclerView.addItemDecoration( new GridSpacingItemDecoration( 2, dpToPx( 6 ), true ) );
        mRecyclerView.setItemAnimator( new DefaultItemAnimator() );
        mRecyclerView.setHasFixedSize( true );


        items.add( new CategoryItemsModel( "Tk.10","Only Iron" ,null));
        items.add( new CategoryItemsModel( "Tk.120","Wash and Iron" ,null));
        items.add( new CategoryItemsModel( "30 Tk","Wash and Clean" ,null));
        items.add( new CategoryItemsModel( "Tk.50","Wash and Clean" ,null));
        items.add( new CategoryItemsModel( "Tk. 20","Wash and Clean" ,null));

//        items = Constants.loadAllData();
        currentPosition = getArguments().getInt( ARG_POSITION );

//
//        Log.d( "xxxxxx", "onViewCreated: " + currentPosition+"\t item is: "+items.get( currentPosition ).toString() );
        //Use this now
//        mRecyclerView.addItemDecoration( new MaterialViewPagerHeaderDecorator() );
        mRecyclerView.setAdapter( new CategoryItemsAdapter( getContext(), items, currentPosition ) );

        mRecyclerView.addOnItemTouchListener(new ItemClickListener( getActivity(), mRecyclerView, new ItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                startActivity( new Intent( context, SelectItemActivity.class ) );
                getActivity().finish();
                Log.d( TAG, "onClick: "+position );
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        } ));


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction( uri );
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach( context );
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException( context.toString()
//                    + " must implement OnFragmentInteractionListener" );
//        }
//    }

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



    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

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
}
