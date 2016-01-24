package com.dr.pricekeep.presenters;

import android.content.Intent;

import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.views.activities.MainActivity;
import com.dr.pricekeep.views.activities.SingleItemActivity;
import com.firebase.client.Firebase;

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
        Firebase ref = new Firebase(MainActivity.FIREBASE_URL);
        ref.child(mItem.getUpdateQueuePath()).removeValue();
        ref.child("users/" + ref.getAuth().getUid() + "/items/" + mItem.getUUID().toString()).removeValue();
        mSingleItemActivity.finish();
    }

}
