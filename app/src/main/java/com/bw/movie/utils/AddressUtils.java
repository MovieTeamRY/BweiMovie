package com.bw.movie.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import com.bw.movie.base.MyApplication;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

public class AddressUtils {
    public static AddressUtils addressUtils;
    private double latitude = 0;
    private double longitude = 0;
    private LocationManager locationManager;

    public static synchronized AddressUtils getAddressUtils(){
        if(addressUtils==null){
            addressUtils=new AddressUtils();
        }
        return addressUtils;
    }

    public void getAddressDetail(Activity activity) {
        locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            // 经度
            longitude = location.getLongitude();
            // 纬度
            String address = getAddress(latitude, longitude);
            EventBus.getDefault().postSticky(new MessageBean("address",address));
        }


    }
    //放入经纬度就可以了
    public String getAddress(double latitude, double longitude) {
        String latLongString = null;
        try {
            List<Address> addList = null;
            Geocoder ge = new Geocoder(MyApplication.getApplication());
            try {
                addList = ge.getFromLocation(latitude, longitude, 1);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (addList != null && addList.size() > 0) {
                for (int i = 0; i < addList.size(); i++) {
                    Address ad = addList.get(i);
                    latLongString= ad.getLocality();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return latLongString;
    }

}
