package com.dr.pricekeep.views.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.ChangeBounds;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.dr.pricekeep.R;
import com.dr.pricekeep.presenters.SingleItemPresenter;
import com.dr.pricekeep.presenters.SingleItemPresenterImpl;
import com.squareup.picasso.Picasso;

public class SingleItemActivity extends AppCompatActivity {

    private SingleItemPresenter mSingleItemPresenter;

    private TextView mNameText, mPriceText, mSiteName, mLastCheckedText;
    private ImageView mItemImage;
    private Switch notifySwitch;

    public SingleItemActivity() {
        super();
        mSingleItemPresenter = new SingleItemPresenterImpl(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_item);
        // initialize views
        mItemImage = (ImageView)findViewById(R.id.list_item_image);
        mNameText = (TextView)findViewById(R.id.list_item_name);
        mPriceText = (TextView)findViewById(R.id.list_item_price);
        mSiteName = (TextView)findViewById(R.id.list_item_sitename);
        mLastCheckedText = (TextView)findViewById(R.id.last_checked_text);
        notifySwitch = (Switch)findViewById(R.id.notify_switch);
        // set values
        mSingleItemPresenter.initialize(getIntent());
    }

    public void setNameText(String name) {
        mNameText.setText(name);
    }

    public void setPriceText(String price) {
        mPriceText.setText(price);
    }

    public void setSiteNameText(String siteName) {
        mSiteName.setText(siteName);
    }

    public void setLastCheckedText(String text) {
        mLastCheckedText.setText(text);
    }

    public void setImage(String url) {
        Picasso.with(this)
                .load(url)
                .resize(400, 400)
                .centerInside()
                .into(mItemImage);
    }

    public void setSwitchChecked(boolean checked) {
        notifySwitch.setChecked(checked);
    }

    public void deleteItem(View view) {
        mSingleItemPresenter.deleteItem();
    }
}