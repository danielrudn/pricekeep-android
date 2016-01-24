package com.dr.pricekeep.backend;

import com.dr.pricekeep.models.Item;
import com.dr.pricekeep.models.User;

import java.util.Map;

public interface PriceKeepDatabase {

    public void setListener(DatabaseListener listener);

    public void loadItems(User currentUser);

    public void addItem(Map<String,String> itemMap);

    public void deleteItem(Item item);

}