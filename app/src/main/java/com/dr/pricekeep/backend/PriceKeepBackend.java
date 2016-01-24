package com.dr.pricekeep.backend;

import com.dr.pricekeep.backend.PriceKeepAuthListener;
import com.dr.pricekeep.backend.PriceKeepDatabase;
import com.dr.pricekeep.models.User;

public interface PriceKeepBackend {

    public void authWithPassword(String email, String password, PriceKeepAuthListener listener);

    public PriceKeepDatabase getDatabase();

    public User getUser();

    public void logout();

    public boolean isLoggedIn();

    public void loadNotifications(PriceKeepNotificationListener listener);

}
