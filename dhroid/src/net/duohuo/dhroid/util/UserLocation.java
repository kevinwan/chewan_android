package net.duohuo.dhroid.util;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

public class UserLocation implements AMapLocationListener {

    private LocationManagerProxy mLocationManagerProxy;

    private String city = "南京", provice;

    private double latitude, longitude;

    private boolean islocation;

    static UserLocation instance;

    private String district = "";
    OnLocationChanged onLocationChanged;
    private static double EARTH_RADIUS = 6378.137;


    public static UserLocation getInstance() {
        if (instance == null) {
            instance = new UserLocation();
        }
        return instance;
    }

    public void init(Context context) {
        mLocationManagerProxy = LocationManagerProxy.getInstance(context);
        mLocationManagerProxy.setGpsEnable(true);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 15, this);
    }


    public void init(Context context, int locationTime) {
        mLocationManagerProxy = LocationManagerProxy.getInstance(context);
        mLocationManagerProxy.setGpsEnable(true);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次,
        // 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, locationTime, 15, this);
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public boolean isIslocation() {
        return islocation;
    }

    public void setIslocation(boolean islocation) {
        this.islocation = islocation;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    @Override
    public void onLocationChanged(Location arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }


    public OnLocationChanged getOnLocationChanged() {
        return onLocationChanged;
    }

    public void setOnLocationChanged(OnLocationChanged onLocationChanged) {
        this.onLocationChanged = onLocationChanged;
    }

    public interface OnLocationChanged {
        void change(double latitude, double longitude);
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (amapLocation != null
                && amapLocation.getAMapException().getErrorCode() == 0) {
            islocation = true;
            // 定位成功回调信息，设置相关消息
            city = amapLocation.getCity();
            if (city != null && city.contains("市")) {
                city = city.replace("市", "");
            }
            provice = amapLocation.getProvince();

            if (provice != null && provice.contains("省")) {
                provice = provice.replace("省", "");
            }

            district = amapLocation.getDistrict();

            if (longitude != 0 && latitude != 0) {
                double distance = distance(longitude, latitude, amapLocation.getLongitude(), amapLocation.getLatitude());
                Log.d("msg", "distance:" + distance);
                if (distance > 1 && onLocationChanged != null) {
                    onLocationChanged.change(amapLocation.getLatitude(), amapLocation.getLongitude());
                }
            }
            latitude = amapLocation.getLatitude();
            longitude = amapLocation.getLongitude();
        } else {
            islocation = false;
            Log.e("AmapErr", "Location ERR:"
                    + amapLocation.getAMapException().getErrorCode());
        }
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    public double distance(double long1, double lat1, double long2,
                           double lat2) {
        double a, b, R;
        R = 6378137; // 地球半径
        lat1 = lat1 * Math.PI / 180.0;
        lat2 = lat2 * Math.PI / 180.0;
        a = lat1 - lat2;
        b = (long1 - long2) * Math.PI / 180.0;
        double d;
        double sa2, sb2;
        sa2 = Math.sin(a / 2.0);
        sb2 = Math.sin(b / 2.0);
        d = 2 * R * Math.asin(Math.sqrt(sa2 * sa2 + Math.cos(lat1) * Math.cos(lat2) * sb2 * sb2));
        return d;
    }


}
