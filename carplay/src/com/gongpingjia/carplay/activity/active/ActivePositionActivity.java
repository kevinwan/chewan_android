package com.gongpingjia.carplay.activity.active;

import java.util.List;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.view.ClearableEditText;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ActivePositionActivity extends CarPlayBaseActivity implements OnMarkerClickListener, InfoWindowAdapter,
        OnPoiSearchListener {

    private MapView mMapView;

    private AMap aMap;

    private PoiSearch mPoiSearch;

    private PoiSearch.Query mQuery;

    private PoiResult mPoiResult;

    private ProgressDialog mProgressDlg;

    private ClearableEditText mSearchEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_position);

        mSearchEdit = (ClearableEditText) findViewById(R.id.et_search);

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        aMap = mMapView.getMap();

        mSearchEdit.setOnEditorActionListener(new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                // TODO Auto-generated method stub

                if ((actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
                        && event != null) {
                    showProgressDlg();
                    mQuery = new PoiSearch.Query(mSearchEdit.getText().toString(), "", "南京");
                    mQuery.setPageSize(10);
                    mQuery.setPageNum(0);// 设置查第一页

                    mPoiSearch = new PoiSearch(ActivePositionActivity.this, mQuery);
                    mPoiSearch.setOnPoiSearchListener(ActivePositionActivity.this);
                    mPoiSearch.searchPOIAsyn();
                    return true;
                }
                return false;
            }
        });
    }

    public void showProgressDlg() {
        if (mProgressDlg == null) {
            mProgressDlg = new ProgressDialog(this);
            mProgressDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDlg.setIndeterminate(false);
            mProgressDlg.setCancelable(false);
            mProgressDlg.setMessage("正在搜索......");
            mProgressDlg.show();

        }
    }

    public void hideProgressDlg() {
        if (mProgressDlg != null) {
            mProgressDlg.hide();
        }
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPoiSearched(PoiResult result, int rCode) {
        // TODO Auto-generated method stub
        hideProgressDlg();

        if (rCode == 0) {
            if (result != null && result.getQuery() != null) {
                // 搜索poi的结果
                if (result.getQuery().equals(mQuery)) {// 是否同一条
                    mPoiResult = result;
                    List<PoiItem> poiItems = result.getPois();
                    // 当搜索不到poiitem数据时，会返回含有搜索关键字的城市信息
                    List<SuggestionCity> suggestionCities = result.getSearchSuggestionCitys();

                    if (poiItems != null && poiItems.size() > 0) {
                        aMap.clear();
                        PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
                        poiOverlay.removeFromMap();
                        poiOverlay.addToMap();
                        poiOverlay.zoomToSpan();
                    } else if (suggestionCities != null && suggestionCities.size() > 0) {
                        Toast.makeText(this, "在下列城市搜索到结果" + suggestionCities.toString(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "暂无搜索结果", Toast.LENGTH_SHORT).show();
                    }

                }
            } else {
                Toast.makeText(this, "暂无搜索结果", Toast.LENGTH_SHORT).show();
            }
        } else if (rCode == 27) {
            Toast.makeText(this, "网络出问题啦", Toast.LENGTH_SHORT).show();
        } else if (rCode == 32) {
            Toast.makeText(this, "api key error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "出现未知异常", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View getInfoContents(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public View getInfoWindow(Marker arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO Auto-generated method stub
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

}
