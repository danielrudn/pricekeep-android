package com.dr.pricekeep.backend;

import android.content.Context;
import android.util.Log;

import com.dr.pricekeep.factories.ItemFactory;
import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.models.User;
import com.dr.pricekeep.views.activities.MainActivity;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class FirebaseDatabase implements PriceKeepDatabase, ValueEventListener {

    private Firebase ref;
    private DatabaseListener listener = null;
    private Collection<Item> itemList = null;

    public FirebaseDatabase(Context context) {
        Firebase.setAndroidContext(context);
        ref = new Firebase(MainActivity.FIREBASE_URL);
    }

    public FirebaseDatabase(Context context, DatabaseListener listener) {
        this(context);
        this.listener = listener;
    }

    /*
     *  PriceKeepDatabase methods
     */

    @Override
    public void setListener(DatabaseListener listener) {
        this.listener = listener;
    }

    @Override
    public void loadItems(User currentUser) {
        if(ref.getAuth().getUid().equals(currentUser.getUUID())) {
            if(itemList == null) {
                ref.child("users/" + currentUser.getUUID() + "/items")
                        .addListenerForSingleValueEvent(this);
            } else {
                if(listener != null) {
                    listener.onItemsLoaded(itemList);
                }
            }
        } else {
            if(listener != null) {
                listener.onItemsLoadError(new Exception("INVALID USER"));
            }
        }
    }

    @Override
    public void addItem(Map<String, String> itemMap) {
        // TODO: add completion listener to setValue to add the item to the collection.
        ref.child("addQueue/" + UUID.randomUUID().toString()).setValue(itemMap);
    }

    @Override
    public void deleteItem(Item item) {
        ref.child(item.getUpdateQueuePath()).removeValue();
        ref.child("users/" + ref.getAuth().getUid() + "/items/" + item.getUUID().toString()).removeValue();
    }

    /*
     *  ValueEventListener methods
     */

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if(listener != null) {
            itemList = new ArrayList<Item>();
            for(DataSnapshot item : dataSnapshot.getChildren()) {
                itemList.add(ItemFactory.fromDataSnapshot(item));
            }
            Log.d("TEST", "loaded items from backend");
            listener.onItemsLoaded(itemList);
        }
    }

    @Override
    public void onCancelled(FirebaseError firebaseError) {
        if(listener != null) {
            listener.onItemsLoadError(firebaseError.toException());
        }
    }

}
