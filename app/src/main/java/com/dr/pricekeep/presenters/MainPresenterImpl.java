package com.dr.pricekeep.presenters;

import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.SystemClock;
import android.view.MenuItem;

import com.dr.pricekeep.R;
import com.dr.pricekeep.backend.FirebaseBackend;
import com.dr.pricekeep.services.NotificationBroadcastReceiver;
import com.dr.pricekeep.services.NotificationService;
import com.dr.pricekeep.views.activities.MainActivity;
import com.dr.pricekeep.views.fragments.AllItemsFragment;
import com.dr.pricekeep.views.fragments.ListItemFragment;
import com.dr.pricekeep.views.fragments.NotifiedItemsFragment;
import com.dr.pricekeep.views.fragments.PriceChangedItemsFragment;
import com.dr.pricekeep.views.fragments.RecentlyAddedItemsFragment;
import com.dr.pricekeep.views.fragments.SettingsFragment;
import com.firebase.client.DataSnapshot;

public class MainPresenterImpl implements MainPresenter {

    private MainActivity mMainActivity;
    private Fragment mCurrentFragment;

    public MainPresenterImpl(MainActivity mainActivity) {
        this.mMainActivity = mainActivity;
    }

    @Override
    public void initializeViews() {
        Intent startIntent = mMainActivity.getIntent();
        if(startIntent.getStringExtra("notification") != null) {
            changeContentFragment(new NotifiedItemsFragment());
        } else {
            changeContentFragment(new AllItemsFragment());
        }
        // set nav drawer data
        mMainActivity.setEmailText(FirebaseBackend.getInstance(mMainActivity).getUser().getEmail());
    }

    @Override
    public void onNavigationItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        mMainActivity.showFAB();
        if(itemId == R.id.nav_all_items && !(mCurrentFragment instanceof AllItemsFragment)) {
            changeContentFragment(new AllItemsFragment());
        } else if(itemId == R.id.nav_recently_added_items && !(mCurrentFragment instanceof RecentlyAddedItemsFragment)) {
            changeContentFragment(new RecentlyAddedItemsFragment());
        } else if(itemId == R.id.nav_price_changed_items && !(mCurrentFragment instanceof PriceChangedItemsFragment)) {
            changeContentFragment(new PriceChangedItemsFragment());
        } else if(itemId == R.id.nav_settings && !(mCurrentFragment instanceof SettingsFragment)) {
            mMainActivity.hideFAB();
            changeContentFragment(new SettingsFragment());
        }
    }

    @Override
    public void onFABClicked() {
        ((ListItemFragment)mCurrentFragment).handleFABClick();
    }

    private void changeContentFragment(Fragment fragment) {
        mCurrentFragment = fragment;
        FragmentManager fragmentManager = mMainActivity.getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content_layout, mCurrentFragment).commit();
    }

}
