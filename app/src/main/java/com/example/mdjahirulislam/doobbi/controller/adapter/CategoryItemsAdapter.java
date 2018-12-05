package com.example.mdjahirulislam.doobbi.controller.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mdjahirulislam.doobbi.R;
import com.example.mdjahirulislam.doobbi.model.CategoryItemsModel;

import java.util.List;

public class CategoryItemsAdapter extends RecyclerView.Adapter<CategoryItemsAdapter.MyViewHolder> {

    private List<CategoryItemsModel> mItemsList;
    private Context mContext;
    private int mPosition;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView priceTV;
        private TextView descriptionTV;
        private ImageView itemPosterIV;

        public MyViewHolder(View view) {
            super(view);
            priceTV = (TextView) view.findViewById(R.id.singleItemPriceTagTV);
            descriptionTV = (TextView) view.findViewById(R.id.singleItemDescriptionTV);
            itemPosterIV = (ImageView) view.findViewById(R.id.singleItemPosterImageIV);
        }
    }


    public CategoryItemsAdapter(Context context, List<CategoryItemsModel> itemsList, int position) {
        this.mItemsList = itemsList;
        this.mContext = context;
        this.mPosition = position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item_design, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CategoryItemsModel item = mItemsList.get(position);
        holder.priceTV.setText("Tk. "+item.getMinPrice()+".00");
        holder.descriptionTV.setText(item.getItemName());
//        holder.itemPosterIV.setImageURI( Uri.parse( item.getPosterURL() ) );
    }

    @Override
    public int getItemCount() {
        return mItemsList.size();
    }
}