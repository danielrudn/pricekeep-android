package com.dr.pricekeep.presenters;

import android.content.Intent;

import com.dr.pricekeep.backend.FirebaseBackend;
import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.views.activities.SingleItemActivity;

public class SingleItemPresenterImpl implements SingleItemPresenter {

    private SingleItemActivity mSingleItemActivity;

    private Item mItem;

    public SingleItemPresenterImpl(SingleItemActivity activity) {
        this.mSingleItemActivity = activity;
    }

    @Override
    public void initialize(Intent intent) {
        mItem = intent.getParcelableExtra(Item.TAG);
        mSingleItemActivity.setTitle(mItem.getItemName());
        mSingleItemActivity.setNameText(mItem.getItemName());
        mSingleItemActivity.setPriceText(mItem.getCurrentPrice());
        mSingleItemActivity.setSiteNameText(mItem.getSiteName());
        mSingleItemActivity.setImage(mItem.getImageURL());
        mSingleItemActivity.setLastCheckedText(mItem.getDateLastCheckedFormatted());
        mSingleItemActivity.setSwitchChecked(mItem.shouldNotify());
    }

    @Override
    public void deleteItem() {
        FirebaseBackend.getInstance(mSingleItemActivity).getDatabase().deleteItem(mItem);
        mSingleItemActivity.finish();
    }

}
