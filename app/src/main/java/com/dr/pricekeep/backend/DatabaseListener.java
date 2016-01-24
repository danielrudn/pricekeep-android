package com.dr.pricekeep.backend;

import com.dr.pricekeep.models.Item;

import java.util.Collection;

public interface DatabaseListener {

    public void onItemsLoaded(Collection<Item> items);

    public void onItemsLoadError(Exception e);

}
