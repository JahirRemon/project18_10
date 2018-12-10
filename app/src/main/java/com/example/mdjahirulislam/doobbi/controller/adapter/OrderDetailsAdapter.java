package com.example.mdjahirulislam.doobbi.controller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.DBFunctions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderHistoryDBModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetOrderDetailsResponseModel;

import java.util.List;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {

    private static final String TAG = OrderDetailsAdapter.class.getSimpleName();

    private List<GetOrderDetailsResponseModel.Item>  mItemsList;
    private Context mContext;
    private Realm mRealm;
    private InsertOrderHistoryDBModel insertOrderHistoryDBModel;
    private SessionManager sessionManager;
    private String uniqueID;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView plusIV;
        private ImageView minusIV;
        private TextView quantityTV;
        private TextView priceTV;
        private TextView totalPriceTV;
        private TextView itemType;
        private LinearLayout mainView;
        private ImageView zeroIV;


        public MyViewHolder(View view) {
            super( view );


            quantityTV = view.findViewById( R.id.singleOrderDetailsItemQuantityTV );
            priceTV = view.findViewById( R.id.singleOrderDetailsRegularPriceTV );
            totalPriceTV = view.findViewById( R.id.singleOrderDetailsTotalPriceTV );
            itemType = view.findViewById( R.id.singleOrderDetailsItemNameAndTypeTV );
        }
    }


    public OrderDetailsAdapter(Context context, List<GetOrderDetailsResponseModel.Item > orderDetailsList) {
        this.mItemsList = orderDetailsList;
        this.mContext = context;
//        this.mPosition = position;
        mRealm = Realm.getDefaultInstance();
        sessionManager = new SessionManager( context );

    }

    @Override
    public OrderDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.single_order_item_details_design, parent, false );

        return new OrderDetailsAdapter.MyViewHolder( itemView );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final OrderDetailsAdapter.MyViewHolder holder, int position) {
        final GetOrderDetailsResponseModel.Item item = mItemsList.get( position );

        holder.itemType.setText( item.getItemName() + " - " + item.getServiceName() );
        holder.priceTV.setText(String.valueOf( Integer.parseInt(item.getPerItemPrice())/Integer.parseInt(item.getItemCount()))) ;
        holder.quantityTV.setText(item.getItemCount());
        holder.totalPriceTV.setText(item.getPerItemPrice());

    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }
}
