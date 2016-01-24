package com.dr.pricekeep.backend;


import com.dr.pricekeep.models.User;

public interface PriceKeepAuthListener {

    public void onSuccess(User user);

    public void onFailed(Exception e);

}
