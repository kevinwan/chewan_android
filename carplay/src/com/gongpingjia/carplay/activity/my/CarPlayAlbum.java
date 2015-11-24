package com.gongpingjia.carplay.activity.my;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.CarPlayAlbumAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CarPlayAlbum extends CarPlayBaseActivity {
    private RecyclerView recyclerView;
    CarPlayAlbumAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_play_album);
//        EventBus.getDefault().register(this);
    }

    @Override
    public void initView() {
        setTitle("车玩相册");
        String [] photoUrl = getIntent().getStringArrayExtra("photoUrl");
        String [] photoId = getIntent().getStringArrayExtra("photoId");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(self);
//        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(self,3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mAdapter = new CarPlayAlbumAdapter(self);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);

        List<JSONObject> album = new ArrayList<JSONObject>();

        if (photoUrl!=null&&photoId!=null){
            for (int i=0;i<photoUrl.length;i++){
                try {
                    JSONObject jo = new JSONObject();
                    jo.put("url", photoUrl[i]);
                    jo.put("id", photoId[i]);
                    album.add(jo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        mAdapter.setData(album);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

//    public void onEventMainThread(JSONArray albumJsa) {
//        List<JSONObject> album = new ArrayList<JSONObject>();
//        if (albumJsa != null) {
//            for (int i = 0; i < albumJsa.length(); i++) {
//                try {
//                    album.add(albumJsa.getJSONObject(i));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            mAdapter.setData(album);
//        }
//    }
}
