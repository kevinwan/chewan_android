package com.gongpingjia.carplay.view.pop;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gongpingjia.carplay.R;

/**
 * Created by Administrator on 2015/11/21.
 */
public class SelectPicturePop  {

    Activity context;

    View contentV;

    PopupWindow pop;

    static SelectPicturePop instance;

    TextView photographT,albumT,existingT,cancelT;

    public SelectPicturePop(Activity context,int type) {
        this.context = context;
        contentV = LayoutInflater.from(context).inflate(R.layout.pop_select_picture, null);
        pop = new PopupWindow(contentV, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT, true);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop.setAnimationStyle(R.style.PopupLookAround);
        pop.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        initView(type);

    }

    //type : 0为展示 "以上传照片"
    public static SelectPicturePop getInstance(Activity context,int type) {
//        if (instance == null) {
            instance = new SelectPicturePop(context,type);
//        }
        return instance;
    }

    private void initView(int type) {
        photographT = (TextView) contentV.findViewById(R.id.photograph);
        albumT = (TextView) contentV.findViewById(R.id.album);
        existingT = (TextView) contentV.findViewById(R.id.existing);
        cancelT = (TextView) contentV.findViewById(R.id.cancel);
        cancelT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        contentV.findViewById(R.id.pop_background).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });
        if (type==0){
            existingT.setVisibility(View.VISIBLE);
        }else {
            existingT.setVisibility(View.GONE);
        }

    }

    public void show() {
        pop.showAtLocation(contentV.getRootView(), Gravity.CENTER, 0, 0);
    }
    public void dismiss(){
        pop.dismiss();
    }

    /**
     * 设置拍照点击事件
     * @param listener
     */
    public void setPhotoGraphListener(View.OnClickListener listener) {
        if (listener != null) {
            photographT.setOnClickListener(listener);
        }
    }

    /**
     * 设置拍照点击事件
     * @param listener
     */
    public void setAlbumListener(View.OnClickListener listener) {
        if (listener != null) {
            albumT.setOnClickListener(listener);
        }
    }

    /**
     * 设置拍照点击事件
     * @param listener
     */
    public void setExistingListener(View.OnClickListener listener) {
        if (listener != null) {
            existingT.setOnClickListener(listener);
        }
    }

}
