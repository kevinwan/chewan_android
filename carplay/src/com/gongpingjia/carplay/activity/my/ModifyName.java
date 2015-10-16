package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;

/**
 *
 * 修改昵称
 * Created by Administrator on 2015/10/16.
 */
public class ModifyName extends CarPlayBaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpersonal);
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        EditText edit = (EditText) findViewById(R.id.modify_name);
        edit.setText(intent.getStringExtra("name"));
        View backV = findViewById(R.id.backLayout);
        if (backV != null) {
            backV.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {


                        finish();


                }
            });
        }

    }
}
