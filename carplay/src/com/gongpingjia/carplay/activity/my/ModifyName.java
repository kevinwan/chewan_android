package com.gongpingjia.carplay.activity.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;

/**
 * 修改昵称
 * Created by Administrator on 2015/10/16.
 */
public class ModifyName extends CarPlayBaseActivity {
    TextView right_txt;
    EditText edit;
    String inName;
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifyname);
    }

    @Override
    public void initView() {
        setTitle("修改昵称");

        Intent intent = getIntent();
        edit = (EditText) findViewById(R.id.Newnickname);
        inName = intent.getStringExtra("name");
        edit.setText(inName);
        right_txt = (TextView) findViewById(R.id.right_text);
        right_txt.setVisibility(View.VISIBLE);
        right_txt.setText("保存");
        right_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 如果没有改动 直接关闭本页
                if (isModify()) {
//                    if (edit.getText().toString().length() > 7 || edit.getText().toString().length() == 0) {
//                        showToast("昵称不能大于8个字符或者不能为空");
//                        return;
//                    }
                    modification();

                } else {
                    finish();
                }
            }
        });


    }

    private void modification() {
        final String nickname = edit.getText().toString().trim();
        if (nickname.length() > 7 || nickname.length() == 0) {
            showToast("昵称不能大于7个字符或者不能为空");
            return;
        }
//
        DhNet net = new DhNet(API2.CWBaseurl + "/user/" + user.getUserId() + "/info?token=" + user.getToken());
        net.addParam("nickname", nickname);
        net.doPostInDialog(new NetTask(self) {

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (response.isSuccess()) {
                    showToast("昵称修改成功");
                    User user = User.getInstance();
                    user.setNickName(nickname);
                    Intent intent = getIntent();
                    intent.putExtra("nickname", edit.getText().toString());
                    setResult(self.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    /**
     * 判断资料是否有改动
     */
    private boolean isModify() {
        String name = edit.getText().toString();
//
        if (TextUtils.isEmpty(name)) {
            return false;
        }
        if (!name.equals(inName)) {
            return true;
        }
        return false;
    }
}
