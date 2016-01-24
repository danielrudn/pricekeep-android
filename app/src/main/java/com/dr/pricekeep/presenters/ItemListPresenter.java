package com.dr.pricekeep.presenters;

import com.dr.pricekeep.models.Item;

public interface ItemListPresenter {

    public void initializeItems();

    public boolean isItemValid(Item mItem);

}