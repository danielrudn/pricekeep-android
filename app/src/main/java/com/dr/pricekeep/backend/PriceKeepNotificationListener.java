package com.dr.pricekeep.backend;

import java.util.ArrayList;

public interface PriceKeepNotificationListener {

    public void onNotificationsLoaded(ArrayList<String> itemIds);

}
