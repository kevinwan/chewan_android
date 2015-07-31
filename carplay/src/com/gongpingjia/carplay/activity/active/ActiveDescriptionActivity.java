package com.gongpingjia.carplay.activity.active;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/*
 * 活动描述
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ActiveDescriptionActivity extends CarPlayBaseActivity {

    private EditText mDesEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_active_description);

        mDesEdit = (EditText) findViewById(R.id.et_description);
        String from = getIntent().getStringExtra("description");
        if (from != null && from.length() != 0) {
            mDesEdit.setText(from);
        }

        setTitle("活动介绍");
        setRightAction("确定 ", -1, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String des = mDesEdit.getText().toString();
                if (!TextUtils.isEmpty(des) && des.length() > 20 && des.length() < 140) {
                    Intent intent = new Intent();
                    intent.putExtra("des", des);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showToast("输入内容要不少于二十字不大于140字哦！");
                }
            }
        });

    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub

    }

}
