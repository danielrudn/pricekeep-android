package com.dr.pricekeep.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Item implements Parcelable {

    private String itemName, updateInterval;
    private String startPrice, currentPrice;
    private UUID uuid;
    private String url, imageURL;
    private Date dateAdded, dateLastChecked;
    private boolean notify = true;

    public static final String TAG = Item.class.getSimpleName();
    public static final Parcelable.Creator<Item> CREATOR
            = new Parcelable.Creator<Item>() {

        @Override
        public Item createFromParcel(Parcel source) {
            String[] values = new String[10];
            source.readStringArray(values);
            return new Item(values[0], values[1], values[2], values[3], values[5], values[6], values[4], values[7], values[8], values[9]);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public Item(String name, String updateInterval, String startPrice,
                String currentPrice, String url, String imageURL, String uuid, String dateAdded, String dateLastChecked,
                String notify) {
        this.itemName = name;
        this.updateInterval = updateInterval;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.url = url;
        this.imageURL = imageURL;
        this.uuid = UUID.fromString(uuid);
        this.dateAdded = new Date(Long.parseLong(dateAdded));
        this.dateLastChecked = new Date(Long.parseLong(dateLastChecked));
        this.notify = Boolean.parseBoolean(notify);
    }

    public String getItemName() {
        if(itemName.length() > 50) {
            return itemName.substring(0, 50);
        }
        return itemName;
    }

    public String getSiteName() {
        String siteName = url.replaceAll("http://", "").replaceAll("https://", "").replace("m.", "");
        siteName = siteName.substring(0, siteName.indexOf("."));
        if(!siteName.contains("/") && !siteName.contains(".") && !siteName.equalsIgnoreCase("www")) {
            return siteName.toUpperCase();
        }
        
        siteName = url.substring(url.indexOf(".")+1);
        if(siteName.indexOf(".") == -1) {
            return siteName.toUpperCase();
        }
        return siteName.substring(0, siteName.indexOf(".")).toUpperCase();
    }

    public String getUpdateInterval() {
        return updateInterval;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public String getCurrentPrice() {
        return String.format("$%,.2f", Float.parseFloat(currentPrice.replaceAll("\\$", "")));
    }

    public String getUUID() {
        return uuid.toString();
    }

    public String getURL() {
        return url;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getUpdateQueuePath() {
        return "updateQueue/" + updateInterval + "/" + uuid.toString();
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public Date getDateLastChecked() {
        return dateLastChecked;
    }

    public String getDateLastCheckedFormatted() {
        int hoursResult = Calendar.getInstance().getTime().getHours() - dateLastChecked.getHours();
        int minutesResult = Calendar.getInstance().getTime().getMinutes() - dateLastChecked.getMinutes();
        int dayResult = Calendar.getInstance().getTime().getDay() - dateLastChecked.getDay();
        if(hoursResult > 0 && hoursResult < 24) {
            return hoursResult + ((hoursResult == 1) ? " hr" : " hrs");
        }
        if(minutesResult > 0 && minutesResult < 24 && hoursResult == 0) {
            return minutesResult + ((minutesResult==1) ? " min" : " mins");
        }
        return dayResult + ((dayResult==1) ? " day" : " days");
    }

    public boolean shouldNotify() {
        return notify;
    }

    public boolean hasPriceChanged() {
        return !currentPrice.replaceAll("\\$", "").equals(startPrice.replaceAll("\\$", ""));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        String[] values = new String[]{itemName, updateInterval,
        startPrice, currentPrice, uuid.toString(), url, imageURL,
                Long.toString(dateAdded.getTime()), Long.toString(dateLastChecked.getTime()),
                Boolean.toString(notify)};
        dest.writeStringArray(values);
    }
}
