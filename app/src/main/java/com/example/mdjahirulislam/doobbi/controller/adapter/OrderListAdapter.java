package com.example.mdjahirulislam.doobbi.controller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.controller.helper.Functions;
import com.example.mdjahirulislam.doobbi.controller.helper.SessionManager;
import com.example.mdjahirulislam.doobbi.model.requestModel.InsertOrderHistoryDBModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetOrderListResponseModel;
import com.example.mdjahirulislam.doobbi.view.order.OrderDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.Realm;

import static com.example.mdjahirulislam.doobbi.controller.helper.Functions._IMAGE_BASE_URL;

public class OrderListAdapter extends RecyclerSwipeAdapter<OrderListAdapter.MyViewHolder> {

    private static final String TAG = OrderListAdapter.class.getSimpleName();

    private List<GetOrderListResponseModel.Order> orderList;
    private Context mContext;
    private InsertOrderHistoryDBModel insertOrderHistoryDBModel;
    private SessionManager sessionManager;
    private String uniqueID;

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public SwipeLayout swipeLayout;
        public TextView Delete;
//        public TextView Edit;
//        public TextView Share;
        public ImageButton btnLocation;
        public LinearLayout swipeMenuLL;

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
            super(view);

            mainLL = view.findViewById(R.id.SingleOrderItemMainLL);
            dateTV = view.findViewById(R.id.singleOrderItemDateTV);
            timeTV = view.findViewById(R.id.singleOrderItemTimeTV);
            orderIDTV = view.findViewById(R.id.singleOrderItemIDTV);
            quantityTV = view.findViewById(R.id.singleOrderItemQuantityTV);
            totalPriceTV = view.findViewById(R.id.singleOrderItemPriceTV);
            statusIV = view.findViewById(R.id.singleOrderItemStatusIV);
            statusTV = view.findViewById(R.id.singleOrderItemStatusTV);
            orderStatus = view.findViewById(R.id.singleOrderItemStatusView);

            Delete = (TextView) view.findViewById(R.id.Delete);
//            Edit = (TextView) view.findViewById(R.id.Edit);
//            Share = (TextView) view.findViewById(R.id.Share);
//            btnLocation = (ImageButton) view.findViewById(R.id.btnLocation);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe);
            swipeMenuLL = view.findViewById(R.id.swipeMenuLL);

        }
    }


    public OrderListAdapter(Context context, List<GetOrderListResponseModel.Order> orderList) {
        this.orderList = orderList;
        this.mContext = context;
        sessionManager = new SessionManager(context);
        Log.d(TAG, "OrderListAdapter: orderList ---> " + orderList.toString());


    }

    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_order_item_list_design, parent, false);

        return new OrderListAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final OrderListAdapter.MyViewHolder viewHolder, final int position) {
        final GetOrderListResponseModel.Order order = orderList.get(position);

        Log.d(TAG, "onBindViewHolder: " + order.toString());
//        holder.mainLL.getLayoutParams();
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(viewHolder.mainLL.getLayoutParams());
        LinearLayout.LayoutParams swipeLayoutParams = new LinearLayout.LayoutParams(viewHolder.swipeMenuLL.getLayoutParams());

        if (position == 0) {
            layoutParams.setMargins(0, 150, 0, 0);
            swipeLayoutParams.setMargins(0, 150, 0, 0);
            viewHolder.mainLL.setLayoutParams(layoutParams);
            viewHolder.swipeMenuLL.setLayoutParams(swipeLayoutParams);


        } else {
            layoutParams.setMargins(0, 0, 0, 0);
            viewHolder.mainLL.setLayoutParams(layoutParams);
            viewHolder.swipeMenuLL.setLayoutParams(swipeLayoutParams);


        }

        viewHolder.dateTV.setText(Functions.getDateFromCreatedAt(order.getCreateDate()));
        viewHolder.timeTV.setText(Functions.getTimeFromCreatedAt(order.getCreateDate()));
        viewHolder.orderIDTV.setText("ID-" + String.format("%04d", Integer.parseInt(order.getOrderId())));
        viewHolder.quantityTV.setText(order.getTotalItem() + " Pcs");
        viewHolder.totalPriceTV.setText("Tk. " + order.getTotalPayableAmount() + ".00");
        viewHolder.statusTV.setText(order.getStatusDetial());

        viewHolder.mainLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, OrderDetailsActivity.class).putExtra("position", orderList.get(position).getOrderId()));

            }
        });

        if (order.getStatusDetial().equalsIgnoreCase("Tempeory")) {
            Picasso.get()
                    .load(_IMAGE_BASE_URL + order.getPhone())
                    .placeholder(R.drawable.order_status_temp)
                    .into(viewHolder.statusIV);
            viewHolder.orderStatus.setBackgroundResource(R.color.colorOrderStatusTemporary);

        } else {
            // TODO: 05/12/18 add more status
            Log.d(TAG, "onBindViewHolder: Please add more status");
            Toast.makeText(mContext, "Please add more status", Toast.LENGTH_SHORT).show();
        }

        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);

        //from the left to right
//        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));

        //from the right to left
        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wraper));


        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {

            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, " Click : " + order.getOrderId() + " \n" + order.getCustomerName(), Toast.LENGTH_SHORT).show();
            }
        });

//        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Clicked on Information ", Toast.LENGTH_SHORT).show();
//            }
//        });

//        viewHolder.Share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(view.getContext(), "Clicked on Share ", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(view.getContext(), "Clicked on Edit  ", Toast.LENGTH_SHORT).show();
//            }
//        });

        viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
//                orderList.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, orderList.size());
//                mItemManger.closeAllItems();
                Toast.makeText(v.getContext(), "For Deleted Need Delete Api", Toast.LENGTH_SHORT).show();
            }
        });

        mItemManger.bindView(viewHolder.itemView, position);


    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }
}