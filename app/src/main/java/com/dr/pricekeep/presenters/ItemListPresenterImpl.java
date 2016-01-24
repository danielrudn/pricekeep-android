package com.dr.pricekeep.presenters;

import android.content.Intent;
import android.os.SystemClock;

import com.dr.pricekeep.factories.ItemFactory;
import com.dr.pricekeep.factories.UserFactory;
import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.models.User;
import com.dr.pricekeep.views.activities.MainActivity;
import com.dr.pricekeep.views.adapters.ItemAdapter;
import com.dr.pricekeep.views.fragments.ListItemFragment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ItemListPresenterImpl implements ItemListPresenter {

    private ListItemFragment mListItemFragment;
    private Firebase mRef;
    private User mUser;
    private ItemAdapter mItemAdapter;
    // Items that are displayed in the view
    private ArrayList<Item> itemList;

    public static final int ALL_ITEMS = 0, RECENTLY_ADDED_ITEMS = 1, PRICE_CHANGED_ITEMS = 2,
            NOTIFIED_ITEMS = 3;
    private int currentState = ALL_ITEMS;

    public ItemListPresenterImpl(ListItemFragment fragment, int currentState) {
        mListItemFragment = fragment;
        mRef = new Firebase(MainActivity.FIREBASE_URL);
        mUser = UserFactory.fromAuthData(mRef.getAuth());
        this.currentState = currentState;
    }

    @Override
    public void initializeItems() {
        // load items
        mRef.child("users/" + mUser.getUUID() + "/items").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                itemList = new ArrayList<Item>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Item mItem = ItemFactory.fromDataSnapshot(data);
                    if(isItemValid(mItem)) {
                        itemList.add(mItem);
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
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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
}