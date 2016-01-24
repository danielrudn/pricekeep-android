package com.dr.pricekeep.views.fragments;

import android.os.Bundle;

import com.dr.pricekeep.presenters.ItemListPresenterImpl;

public class PriceChangedItemsFragment extends ListItemFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mItemListPresenter = new ItemListPresenterImpl(this, ItemListPresenterImpl.PRICE_CHANGED_ITEMS);
    }

}
