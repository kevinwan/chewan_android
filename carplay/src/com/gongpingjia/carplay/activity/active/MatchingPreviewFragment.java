package com.gongpingjia.carplay.activity.active;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.view.pop.SelectPicturePop;

import de.greenrobot.event.EventBus;

/**
 * 匹配结果预览页
 * Created by Administrator on 2015/11/21.
 */
public class MatchingPreviewFragment extends CarPlayBaseFragment implements View.OnClickListener{
    static MatchingPreviewFragment instance;
    View mainV;
    TextView nickNameT,ageT,payT,transferT,locationT,distanceT;
    ImageView headAttI,carAttI,sexI,active_bgI;
    RelativeLayout sexLayoutR;
    Button changePhotoBtn,nextMatchingBtn;

    public static MatchingPreviewFragment getInstance() {
        if (instance == null) {
            instance = new MatchingPreviewFragment();
        }

        return instance;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.activity_matching_preview, null);
        EventBus.getDefault().register(this);
        initView();
        return mainV;
    }

    private void initView() {
        nickNameT = (TextView) mainV.findViewById(R.id.tv_nickname);
        sexLayoutR = (RelativeLayout) mainV.findViewById(R.id.layout_sex_and_age);
        sexI = (ImageView) mainV.findViewById(R.id.iv_sex);
        ageT = (TextView) mainV.findViewById(R.id.tv_age);
        payT = (TextView) mainV.findViewById(R.id.pay);
        transferT = (TextView) mainV.findViewById(R.id.transfer);
        locationT = (TextView) mainV.findViewById(R.id.location);
        distanceT = (TextView) mainV.findViewById(R.id.tv_distance);
        headAttI = (ImageView) mainV.findViewById(R.id.head_att);
        carAttI = (ImageView) mainV.findViewById(R.id.iv_car_logo);
        active_bgI = (ImageView) mainV.findViewById(R.id.active_bg);
        changePhotoBtn = (Button) mainV.findViewById(R.id.changephoto);
        nextMatchingBtn = (Button) mainV.findViewById(R.id.nextmatching);

        changePhotoBtn.setOnClickListener(this);
        nextMatchingBtn.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //更换照片
            case R.id.changephoto:
                final SelectPicturePop pop = SelectPicturePop.getInstance(getActivity(),0);
                //拍照
                pop.setPhotoGraphListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                    }
                });
                //相册
                pop.setAlbumListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                    }
                });
                //上传过的图片
                pop.setExistingListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                    }
                });
                pop.show();
                break;
            //继续匹配
            case R.id.nextmatching:

                break;
        }
    }
}
