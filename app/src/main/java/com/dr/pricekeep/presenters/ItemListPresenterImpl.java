package com.dr.pricekeep.presenters;

import android.content.Intent;
import android.util.Log;

import com.dr.pricekeep.backend.FirebaseBackend;
import com.dr.pricekeep.backend.DatabaseListener;
import com.dr.pricekeep.backend.PriceKeepDatabase;
import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.models.User;
import com.dr.pricekeep.views.adapters.ItemAdapter;
import com.dr.pricekeep.views.fragments.ListItemFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

public class ItemListPresenterImpl implements ItemListPresenter, DatabaseListener {

    private ListItemFragment mListItemFragment;
    private PriceKeepDatabase database;
    private User mUser;
    private ItemAdapter mItemAdapter;
    // Items that are displayed in the view
    private ArrayList<Item> itemList;

    public static final int ALL_ITEMS = 0, RECENTLY_ADDED_ITEMS = 1, PRICE_CHANGED_ITEMS = 2,
            NOTIFIED_ITEMS = 3;
    private int currentState = ALL_ITEMS;

    public ItemListPresenterImpl(ListItemFragment fragment, int currentState) {
        mListItemFragment = fragment;
        mUser = FirebaseBackend.getInstance(mListItemFragment.getActivity()).getUser();
        this.currentState = currentState;
        database = FirebaseBackend.getInstance(mListItemFragment.getActivity()).getDatabase();
        database.setListener(this);
    }

    @Override
    public void initializeItems() {
        // load items
        database.loadItems(mUser);
    }

    @Override
    public boolean isItemValid(Item mItem) {
        if(currentState == RECENTLY_ADDED_ITEMS) {
            // only show items added within the past week
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.WEEK_OF_YEAR, -1);
            return (mItem.getDateAdded().compareTo(cal.getTime()) >= 0);
        } else if(currentState == PRICE_CHANGED_ITEMS) {
            return mItem.hasPriceChanged();
        } else if(currentState == NOTIFIED_ITEMS) {
            // get intent
            Intent intent = mListItemFragment.getActivity().getIntent();
            if(intent != null) {
                return intent.hasExtra(mItem.getUUID());
            }
            // only called if intent is null
            return false;
        }
        // for all items
        return true;
    }

    @Override
    public void onItemsLoaded(Collection<Item> items) {
        itemList = new ArrayList<Item>();
        // Add only items required by the state
        for(Item item : items) {
            if(isItemValid(item)) {
                itemList.add(item);
            }
        }
        // set adapter
        if(mItemAdapter == null) {
            mItemAdapter = new ItemAdapter(itemList);
            mListItemFragment.setItemAdapter(mItemAdapter);
        } else {
            mItemAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemsLoadError(Exception e) {
        Log.d(this.getClass().getSimpleName(), "onItemsLoadError Called\nException: " + e.toString());
    }
}