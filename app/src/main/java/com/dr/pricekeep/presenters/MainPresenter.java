package com.dr.pricekeep.presenters;

import android.view.MenuItem;

public interface MainPresenter {

    public void initializeViews();

    public void onNavigationItemSelected(MenuItem item);

    public void onFABClicked();

}