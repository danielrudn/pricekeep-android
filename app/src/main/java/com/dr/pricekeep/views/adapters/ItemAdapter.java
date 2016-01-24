package com.dr.pricekeep.views.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dr.pricekeep.R;
import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.views.activities.SingleItemActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private ArrayList<Item> itemList;

    public ItemAdapter(ArrayList<Item> list) {
        this.itemList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setItem(itemList.get(position));
        Picasso.with(holder.mItemImage.getContext())
                .load(itemList.get(position).getImageURL())
                .resize(200,200)
                .centerInside().into(holder.mItemImage);
        holder.mNameText.setText(itemList.get(position).getItemName());
        holder.mPriceText.setText(itemList.get(position).getCurrentPrice());
        holder.mSiteName.setText(itemList.get(position).getSiteName());
        Calendar cal = Calendar.getInstance();
        holder.mLastCheckedText.setText(itemList.get(position).getDateLastCheckedFormatted());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameText, mPriceText, mSiteName, mLastCheckedText;
        private ImageView mItemImage;

        private Item mItem = null;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mItemImage = (ImageView)view.findViewById(R.id.list_item_image);
            mNameText = (TextView)view.findViewById(R.id.list_item_name);
            mPriceText = (TextView)view.findViewById(R.id.list_item_price);
            mSiteName = (TextView)view.findViewById(R.id.list_item_sitename);
            mLastCheckedText = (TextView)view.findViewById(R.id.last_checked_text);
        }

        public void setItem(Item item) {
            mItem = item;
        }

        @Override
        @TargetApi(21)
        public void onClick(View v) {
            final CardView card = (CardView) v.findViewById(R.id.test);
            Intent activityIntent = new Intent(v.getContext(), SingleItemActivity.class);
            activityIntent.putExtra(Item.TAG, mItem);
            // set up shared elements in transition
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity)v.getContext(),
                    Pair.create((View) card, "mainCardTransition"),
                    Pair.create((View)mItemImage, "imageTransition"),
                    Pair.create((View)mNameText,"nameTransition"),
                    Pair.create((View)mSiteName, "siteNameTransition"),
                    Pair.create((View)mPriceText, "priceTransition"),
                    Pair.create((View)mLastCheckedText, "lastCheckedTransition"));
            v.getContext().startActivity(activityIntent,
                    options.toBundle());

        }

    }
}