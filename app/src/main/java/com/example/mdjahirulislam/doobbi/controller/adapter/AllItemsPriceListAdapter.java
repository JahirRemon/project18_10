package com.example.mdjahirulislam.doobbi.controller.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetAllItemsPriceResponseModel;
import com.example.mdjahirulislam.doobbi.model.responseModel.GetAllItemsPriceResponseModel.Price;

import java.util.List;

public class AllItemsPriceListAdapter extends RecyclerView.Adapter<AllItemsPriceListAdapter.MyViewHolder> {

    private static final String TAG = OrderDetailsAdapter.class.getSimpleName();

    private List<Price>  mItemsList;
    private Context mContext;
    private String uniqueID;
    private int mPosition;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView itemRegularImage;
        private TextView itemRegularNameTV;
        private TextView itemRegularIronPrice;
        private TextView itemRegularWashPrice;

        private ImageView itemGoldImage;
        private TextView itemGoldNameTV;
        private TextView itemGoldIronPrice;
        private TextView itemGoldWashPrice;


        public MyViewHolder(View view) {
            super( view );

            itemRegularImage = view.findViewById( R.id.singleItemPriceListRegularItemImageIV );
            itemRegularNameTV = view.findViewById( R.id.singleItemPriceListRegularItemNameTV );
            itemRegularIronPrice = view.findViewById( R.id.singleItemPriceListRegularItemIronPriceTV );
            itemRegularWashPrice = view.findViewById( R.id.singleItemPriceListRegularItemWashPriceTV );

//            itemGoldImage = view.findViewById( R.id.singleItemPriceListGoldItemImageIV );
//            itemGoldNameTV = view.findViewById( R.id.singleItemPriceListGoldItemNameTV );
//            itemGoldIronPrice = view.findViewById( R.id.singleItemPriceListGoldItemIronPriceTV );
//            itemGoldWashPrice = view.findViewById( R.id.singleItemPriceListGoldItemWashPriceTV );


        }
    }


    public AllItemsPriceListAdapter(Context context, List<Price > orderDetailsList, int position) {
        this.mItemsList = orderDetailsList;
        this.mContext = context;
        this.mPosition = position;
//        mRealm = Realm.getDefaultInstance();
//        sessionManager = new SessionManager( context );

    }

    @Override
    public AllItemsPriceListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from( parent.getContext() )
                .inflate( R.layout.single_item_price_list_design, parent, false );

        return new AllItemsPriceListAdapter.MyViewHolder( itemView );
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final AllItemsPriceListAdapter.MyViewHolder holder, int position) {
        final GetAllItemsPriceResponseModel.Price price = mItemsList.get( position );

       holder.itemRegularNameTV.setText(price.getItemName());
       holder.itemRegularIronPrice.setText(price.getSalesPrice());
       holder.itemRegularWashPrice.setText(price.getSalesPrice());

    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }
}
