package com.dr.pricekeep.factories;

import com.dr.pricekeep.models.Item;

import com.dr.pricekeep.models.Item;
import com.firebase.client.DataSnapshot;

public class ItemFactory {

    private ItemFactory() { }

    public static Item fromDataSnapshot(DataSnapshot data) {
        return new Item(data.child("name").getValue().toString(),
                data.child("updateInterval").getValue().toString(),
                data.child("startPrice").getValue().toString(),
                data.child("currentPrice").getValue().toString(),
                data.child("url").getValue().toString(),
                data.child("imgURL").getValue().toString(),
                data.getKey().toString(),
                data.child("dateAdded").getValue().toString(),
                data.child("dateChecked").getValue().toString(),
                data.child("notify").getValue().toString());
    }

}