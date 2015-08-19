package com.gongpingjia.carplay.activity.chat;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ChatMapActivity extends CarPlayBaseActivity implements AMapLocationListener {

    private MapView mMapView;

    private LocationManagerProxy mLocationManager;

    private AMap aMap;

    private MarkerOptions mMarkerOption;

    private AMapLocation mCurLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_map);
    }

    @Override
    public void initView() {

        setupMap();
    }

    private void setupMap() {

        Intent it = getIntent();
        mMapView = (MapView) findViewById(R.id.mapView);
        aMap = mMapView.getMap();

        mMarkerOption = new MarkerOptions();
        mMarkerOption.icon(BitmapDescriptorFactory.defaultMarker());

        if (it.getDoubleExtra("latitude", 0) == 0) {
            // 要求定位
            mLocationManager = LocationManagerProxy.getInstance(this);
            mLocationManager.setGpsEnable(true);
            mLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork, 20000, 10, this);

            setTitle("当前位置");
            setRightAction("发送", -1, new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (null != mCurLocation) {
                        Intent it = new Intent();
                        it.putExtra("latitude", mCurLocation.getLatitude());
                        it.putExtra("longitude", mCurLocation.getLongitude());
                        it.putExtra("address", mCurLocation.getAddress());
                        self.setResult(RESULT_OK, it);
                        self.finish();
                    } else {
                        showToast("定位未成功，请稍等");
                    }
                }
            });
        } else {
            // 显示位置
            double latitude = it.getDoubleExtra("latitude", 0);
            double longitude = it.getDoubleExtra("longitude", 0);
            String address = it.getStringExtra("address");
            LatLng ll = new LatLng(latitude, longitude);
            mMarkerOption.position(ll);
            mMarkerOption.snippet(address);
            aMap.addMarker(mMarkerOption);

            aMap.animateCamera(CameraUpdateFactory.newLatLng(ll));
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(AMapLocation location) {
        if (location != null) {
            mCurLocation = location;
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            mMarkerOption.position(ll);
            mMarkerOption.snippet(location.getAddress());
            aMap.addMarker(mMarkerOption);
        }
    }

}
