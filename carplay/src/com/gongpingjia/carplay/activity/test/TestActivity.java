package com.gongpingjia.carplay.activity.test;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.bean.Matching;
import com.gongpingjia.carplay.view.dialog.MateRegionDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class TestActivity extends CarPlayBaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item_nearby);
        TextView nickName = (TextView) findViewById(R.id.tv_nickname);
        final List<Matching> datum = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Matching matching = new Matching();
            matching.setName(i + "name");
            datum.add(matching);
        }
        nickName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MatchingDialog dialog = new MatchingDialog(self,datum);
//                dialog.show();

                MateRegionDialog dlg = new MateRegionDialog(self);
                dlg.show();
            }
        });
    }

    @Override
    public void initView() {

    }
}
