package com.example.mdjahirulislam.doobbi.controller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderHistoryDBModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetOrderListResponseModel;
import com.squareup.picasso.Picasso;

import java.util.List;
import io.realm.Realm;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions._IMAGE_BASE_URL;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {

    private static final String TAG = OrderListAdapter.class.getSimpleName();

    private List<GetOrderListResponseModel.Order> orderList;
    private Context mContext;
    private Realm mRealm;
    private InsertOrderHistoryDBModel insertOrderHistoryDBModel;
    private SessionManager sessionManager;
    private String uniqueID;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mainLL;
        private TextView dateTV;
        private TextView timeTV;
        private TextView orderIDTV;
        private TextView quantityTV;
        private TextView totalPriceTV;
        private ImageView statusIV;
        private TextView statusTV;
        private View orderStatus;




        public MyViewHolder(View view) {
            super( view );

            mainLL = view.findViewById( R.id.SingleOrderItemMainLL );
            dateTV = view.findViewById( R.id.singleOrderItemDateTV );
            timeTV = view.findViewById( R.id.singleOrderItemTimeTV );
            orderIDTV = view.findViewById( R.id.singleOrderItemIDTV );
            quantityTV = view.findViewById( R.id.singleOrderItemQuantityTV );
            totalPriceTV = view.findViewById( R.id.singleOrderItemPriceTV );
            statusIV = view.findViewById( R.id.singleOrderItemStatusIV );
            statusTV = view.findViewById( R.id.singleOrderItemStatusTV );
            orderStatus = view.findViewById( R.id.singleOrderItemStatusView );
        }
    }


    public OrderListAdapter(Context context, List<GetOrderListResponseModel.Order> orderList ) {
        this.orderList = orderList;
        this.mContext = context;
//        this.mPosition = position;
        mRealm = Realm.getDefaultInstance();
        sessionManager = new SessionManager( context );
        Log.d( TAG, "OrderListAdapter: orderList ---> " +orderList.toString());


    }

    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.single_order_item_list_design, parent, false );

        return new OrderListAdapter.MyViewHolder( itemView );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final OrderListAdapter.MyViewHolder holder, int position) {
        final GetOrderListResponseModel.Order order = orderList.get( position );

        Log.d( TAG, "onBindViewHolder: " +order.toString());
//        holder.mainLL.getLayoutParams();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( holder.mainLL.getLayoutParams());

        if (position == 0){
            layoutParams.setMargins(0, 150, 0, 0);
            holder.mainLL.setLayoutParams(  layoutParams );


        }else {
            layoutParams.setMargins(0, 0, 0, 0);
            holder.mainLL.setLayoutParams(  layoutParams );


        }

        holder.dateTV.setText( Functions.getDateFromCreatedAt( order.getCreateDate() ) );
        holder.timeTV.setText( Functions.getTimeFromCreatedAt( order.getCreateDate() ) );
        holder.orderIDTV.setText( "ID-"+String.format( "%04d",Integer.parseInt( order.getOrderId() ) ));
        holder.quantityTV.setText( order.getTotalItem()+" Pcs" );
        holder.totalPriceTV.setText("Tk. " +order.getTotalPayableAmount() +".00");
        holder.statusTV.setText( order.getStatusDetial() );

        if (order.getStatusDetial().equalsIgnoreCase( "Tempeory" )) {
            Picasso.get()
                    .load(_IMAGE_BASE_URL+order.getPhone())
                    .placeholder( R.drawable.order_status_temp )
                    .into(holder.statusIV);
            holder.orderStatus.setBackgroundResource( R.color.colorOrderStatusProcessing );

        }else {
            // TODO: 05/12/18 add more status
            Log.d( TAG, "onBindViewHolder: Please add more status" );
            Toast.makeText( mContext, "Please add more status", Toast.LENGTH_SHORT ).show();
        }

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}