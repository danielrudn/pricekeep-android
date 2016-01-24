package com.dr.pricekeep.views.fragments;

import android.os.Bundle;

import com.dr.pricekeep.presenters.ItemListPresenterImpl;

public class RecentlyAddedItemsFragment extends ListItemFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemListPresenter = new ItemListPresenterImpl(this, ItemListPresenterImpl.RECENTLY_ADDED_ITEMS);
    }

}
