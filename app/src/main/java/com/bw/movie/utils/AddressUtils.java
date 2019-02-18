package com.bw.movie.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bw.movie.base.BaseActivty;
import com.bw.movie.base.MyApplication;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;

public class AddressUtils {
    public static AddressUtils addressUtils;
    private double latitude = 0;
    private double longitude = 0;
    private LocationManager locationManager;
    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption = null;
    private String city;
    private String cityCode;
    private String province;

    public static synchronized AddressUtils getAddressUtils(){
        if(addressUtils==null){
            addressUtils=new AddressUtils();
        }
        return addressUtils;
    }

    public void getAddressDetail(final Activity activity) {
        mlocationClient = new AMapLocationClient(activity);
        //设置定位监听
        mlocationClient.setLocationListener(new AMapLocationListener() {

            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                if(aMapLocation!=null){
                    if(aMapLocation.getErrorCode() == 0){
                        //获取纬度
                        double latitude = aMapLocation.getLatitude();
                        //获取经度
                        double longitude = aMapLocation.getLongitude();
                        //城市信息
                        city = aMapLocation.getCity();
                        //省信息
                        province = aMapLocation.getProvince();
                        //城市编码
                        cityCode = aMapLocation.getCityCode();
                        CityPicker.from((BaseActivty) activity).locateComplete(new LocatedCity(city, province,cityCode), LocateState.SUCCESS);
                        //String address = getAddress(longitude,latitude);
                        EventBus.getDefault().postSticky(new MessageBean("address",city));
                        EventBus.getDefault().postSticky(new MessageBean("location",new double[]{latitude,longitude}));
                    }
                }
            }
        });
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        //mLocationOption.setInterval(2000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        //启动定位
        mlocationClient.startLocation();
       /* locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            latitude = location.getLatitude();
            // 纬度
            longitude = location.getLongitude();
            // 经度
            String address = getAddress(longitude,latitude);
            EventBus.getDefault().postSticky(new MessageBean("location",new double[]{latitude,longitude}));
            EventBus.getDefault().postSticky(new MessageBean("address",address));
        }
*/
    }
    public void StopLocation(){
        mlocationClient.stopLocation();
    }
    //放入经纬度就可以了
    /*public String getAddress(double latitude, double longitude) {
        String latLongString = null;
        try {
            List<Address> addList = null;
            Geocoder ge = new Geocoder(MyApplication.getApplication());
            try {
                addList = ge.getFromLocation( longitude,latitude, 1);
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
    }*/

}
