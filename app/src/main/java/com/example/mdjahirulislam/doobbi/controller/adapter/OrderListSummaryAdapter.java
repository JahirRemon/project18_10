package com.example.mdjahirulislam.doobbi.controller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.ItemPriceModel;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderHistoryDBModel;
import com.example.mdjahirulislam.doobbi.view.authentication.LoginActivity;
import com.example.mdjahirulislam.doobbi.view.makeMyOrder.OrderSummaryActivity;

import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions._INTENT_FROM;

public class OrderListSummaryAdapter extends RecyclerView.Adapter<OrderListSummaryAdapter.MyViewHolder> {

    private static final String TAG = OrderListSummaryAdapter.class.getSimpleName();

    private List<InsertOrderHistoryDBModel> mItemsList;
    private Context mContext;
    private Realm mRealm;
    private InsertOrderHistoryDBModel insertOrderHistoryDBModel;
    private SessionManager sessionManager;
    private String uniqueID;
    private OnTotalPriceAndQuantityListener onTotalPriceAndQuantityListener;

    public interface OnTotalPriceAndQuantityListener {
        void setPrice();

        void setQuantity();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView plusIV;
        private ImageView minusIV;
        private TextView quantityTV;
        private TextView priceTV;
        private TextView totalPriceTV;
        private TextView itemType;


        public MyViewHolder(View view) {
            super( view );

            plusIV = view.findViewById( R.id.singlePlusIV );
            minusIV = view.findViewById( R.id.singleMinusIV );
            quantityTV = view.findViewById( R.id.singleQuantityTV );
            priceTV = view.findViewById( R.id.singlePriceTV );
            totalPriceTV = view.findViewById( R.id.singleTotalPriceTV );
            itemType = view.findViewById( R.id.singleItemTypeTV );
        }
    }


    public OrderListSummaryAdapter(Context context, List<InsertOrderHistoryDBModel> itemsList, OnTotalPriceAndQuantityListener onTotalPriceAndQuantityListener) {
        this.mItemsList = itemsList;
        this.mContext = context;
//        this.mPosition = position;
        mRealm = Realm.getDefaultInstance();
        sessionManager = new SessionManager( context );
        try {
            this.onTotalPriceAndQuantityListener = onTotalPriceAndQuantityListener;
        } catch (ClassCastException e) {
            Log.d( TAG, "SelectedCategoryItemPriceAdapter: " + e.getLocalizedMessage() );
            throw new ClassCastException( context.toString() + " must implement onSomeEventListener" );
        }
    }

    @Override
    public OrderListSummaryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.single_oder_list_design, parent, false );

        return new OrderListSummaryAdapter.MyViewHolder( itemView );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final OrderListSummaryAdapter.MyViewHolder holder, int position) {
        final InsertOrderHistoryDBModel item = mItemsList.get( position );
        final int[] quantity = {0};
        final int[] totalPrice = {0};
        int regularPrice = 0;

        if (Integer.parseInt( item.getItemQuantity() ) != 0) {
            regularPrice = Integer.parseInt( item.getTotalPrice() ) / Integer.parseInt( item.getItemQuantity() );
            totalPrice[0] = regularPrice * quantity[0];
        } else {
            Toast.makeText( mContext, "No Item Selected", Toast.LENGTH_SHORT ).show();
        }


        holder.itemType.setText( item.getServiceName() );
        holder.priceTV.setText( String.valueOf( regularPrice ) );

        mRealm.beginTransaction();

        InsertOrderHistoryDBModel getQuantity = mRealm.where( InsertOrderHistoryDBModel.class )
                .equalTo( "itemID", item.getItemID() )
                .and()
                .equalTo( "serviceID", item.getServiceID() )
                .findFirst();
        try {

            if (getQuantity != null) {
                quantity[0] = Integer.parseInt( getQuantity.getItemQuantity() );
                totalPrice[0] = Integer.parseInt( getQuantity.getTotalPrice() );

            }

        } finally {
            holder.quantityTV.setText( String.valueOf( quantity[0] ) );
            holder.totalPriceTV.setText( String.valueOf( totalPrice[0] ) );
        }

        mRealm.commitTransaction();


        final int finalRegularPrice = regularPrice;
        holder.plusIV.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = holder.plusIV.getWidth();
                float height = holder.plusIV.getHeight();
//                Log.d( TAG, "onTouch 1: plusIV ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.plusIV.setAlpha( 0.5f );
//                        Log.d( TAG, "onTouch 2: plusIV ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {

                            holder.plusIV.setAlpha( 1.0f );

                            Log.d( TAG, "onTouch: is login? -->" + sessionManager.isLoggedIn() + "-----" +
                                    ">>>>> " + sessionManager.getUserId() );
                            if (!sessionManager.isLoggedIn()) {
                                Intent intent = new Intent( mContext, LoginActivity.class );
                                intent.putExtra( _INTENT_FROM, OrderSummaryActivity.class.getSimpleName() );
                                mContext.startActivity( intent );
                            } else {
                                quantity[0]++;
                                totalPrice[0] = finalRegularPrice * quantity[0];
                                holder.quantityTV.setText( String.valueOf( quantity[0] ) );
                                holder.totalPriceTV.setText( String.valueOf( totalPrice[0] ) );


                                uniqueID = UUID.randomUUID().toString();

                                RealmResults<InsertOrderHistoryDBModel> getResult = mRealm.where( InsertOrderHistoryDBModel.class )
                                        .equalTo( "itemID", item.getItemID() )
                                        .and().equalTo( "serviceID", item.getServiceID() )
                                        .findAll();

                                Log.d( TAG, "onResume: " + getResult.size() );


                                insertOrderHistoryDBModel = new InsertOrderHistoryDBModel();
                                insertOrderHistoryDBModel.setUserID( sessionManager.getUserId() );
                                insertOrderHistoryDBModel.setItemID( item.getItemID() );
                                insertOrderHistoryDBModel.setServiceID( item.getServiceID() );
                                insertOrderHistoryDBModel.setItemQuantity( String.valueOf( quantity[0] ) );
                                insertOrderHistoryDBModel.setTotalPrice( String.valueOf( totalPrice[0] ) );

                                if (0 < getResult.size()) {
                                    DBFunctions.updateOrderHistory( insertOrderHistoryDBModel, getResult.get( 0 ).get_id() );
                                    Log.d( TAG, "onTouch: find serviceId----> update history---> " + insertOrderHistoryDBModel.toString() );

                                } else {

                                    Log.d( TAG, "onResume: id--> " + uniqueID + " \nData not found new data inset " + insertOrderHistoryDBModel.toString() );
                                    DBFunctions.addOrderHistory( insertOrderHistoryDBModel, uniqueID );
                                }
                                onTotalPriceAndQuantityListener.setPrice();
                                onTotalPriceAndQuantityListener.setQuantity();
                            }

                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        holder.plusIV.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        holder.plusIV.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );

        final int finalRegularPrice1 = regularPrice;
        holder.minusIV.setOnTouchListener( new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                float width = holder.minusIV.getWidth();
                float height = holder.minusIV.getHeight();
//                Log.d( TAG, "onTouch 1: classTimeLL ---> " + event.getAction() );
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        holder.minusIV.setAlpha( 0.5f );
//                        Log.d( TAG, "onTouch 2: classTimeLL ---> " + MotionEvent.ACTION_DOWN );
                        break;
                    case MotionEvent.ACTION_UP:
                        if (event.getX() < width && event.getY() < height && event.getY() > 0) {
                            holder.minusIV.setAlpha( 1.0f );

                            Log.d( TAG, "onTouch: is login? -->" + sessionManager.isLoggedIn() + "-----" +
                                    ">>>>> " + sessionManager.getUserId() );
                            if (!sessionManager.isLoggedIn()) {
                                Intent intent = new Intent( mContext, LoginActivity.class );
                                intent.putExtra( _INTENT_FROM, OrderSummaryActivity.class.getSimpleName() );
                                mContext.startActivity( intent );
                            } else {

                                if (quantity[0] > 0) {
                                    quantity[0]--;
                                } else {
                                    quantity[0] = 0;
                                }
                                totalPrice[0] = finalRegularPrice * quantity[0];
                                holder.quantityTV.setText( String.valueOf( quantity[0] ) );
                                holder.totalPriceTV.setText( String.valueOf( totalPrice[0] ) );

                                uniqueID = UUID.randomUUID().toString();

                                RealmResults<InsertOrderHistoryDBModel> getResult = mRealm.where( InsertOrderHistoryDBModel.class )
                                        .equalTo( "itemID", item.getItemID() )
                                        .and().equalTo( "serviceID", item.getServiceID() )
                                        .findAll();

                                Log.d( TAG, "onResume: " + getResult.size() );


                                insertOrderHistoryDBModel = new InsertOrderHistoryDBModel();
                                insertOrderHistoryDBModel.setUserID( sessionManager.getUserId() );
                                insertOrderHistoryDBModel.setItemID( item.getItemID() );
                                insertOrderHistoryDBModel.setServiceID( item.getServiceID() );
                                insertOrderHistoryDBModel.setItemQuantity( String.valueOf( quantity[0] ) );
                                insertOrderHistoryDBModel.setTotalPrice( String.valueOf( totalPrice[0] ) );

                                if (0 < getResult.size()) {
                                    DBFunctions.updateOrderHistory( insertOrderHistoryDBModel, getResult.get( 0 ).get_id() );
                                    Log.d( TAG, "onTouch: find serviceId----> update history---> " + insertOrderHistoryDBModel.toString() );

                                } else {

                                    Log.d( TAG, "onResume: id--> " + uniqueID + " \nData not found new data inset " + insertOrderHistoryDBModel.toString() );
                                    DBFunctions.addOrderHistory( insertOrderHistoryDBModel, uniqueID );
                                }
                                onTotalPriceAndQuantityListener.setPrice();
                                onTotalPriceAndQuantityListener.setQuantity();
                            }
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        holder.minusIV.setAlpha( 1.0f );
                        break;
                    case MotionEvent.ACTION_MOVE:
                        holder.minusIV.setAlpha( 1.0f );
                        break;
                    default:
                        break;
                }
                return true;
            }
        } );
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }
}