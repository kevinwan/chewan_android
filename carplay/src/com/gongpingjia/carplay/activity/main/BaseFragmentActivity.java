package com.gongpingjia.carplay.activity.main;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.umeng.analytics.MobclickAgent;

public class BaseFragmentActivity extends FragmentActivity {
    public IDialog dialoger;

    public Activity self;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        dialoger = IocContainer.getShare().get(IDialog.class);
        self = this;
        initTitleBar();
        ActivityTack.getInstanse().addActivity(this);
    }

    private void initTitleBar() {
        View backV = findViewById(R.id.backLayout);
        if (backV != null) {
            backV.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    finish();
                }
            });
        }
    }

    /** 左边返回按钮为空 */
    public void setLeftIconGone() {
        ImageView backV = (ImageView) findViewById(R.id.back);
        if (backV != null) {
            backV.setVisibility(View.GONE);
        }
    }

    /** 设置标题 */
    public void setTitle(String text) {
        TextView titleT = (TextView) findViewById(R.id.title);
        if (titleT != null) {
            titleT.setText(text);
        }
    }

    /** 设置左边的点击事件 */
    public void setLeftAction(int leftid, String leftText, OnClickListener listener) {
        ImageView backV = (ImageView) findViewById(R.id.back);
        if (backV != null) {
            if (leftid == -1) {
                backV.setVisibility(View.VISIBLE);
            } else if (leftid == -2) {
                backV.setVisibility(View.GONE);
            } else {
                backV.setVisibility(View.VISIBLE);
                backV.setImageResource(leftid);
            }
            if (listener != null) {
                View backLV = findViewById(R.id.backLayout);
                if (backLV != null) {
                    backLV.setOnClickListener(listener);
                }
            }
        }

        TextView leftTextT = (TextView) findViewById(R.id.left_text);
        if (leftText != null) {
            if (leftTextT != null) {
                leftTextT.setText(leftText);
                leftTextT.setVisibility(View.VISIBLE);
            }
        } else {
            leftTextT.setVisibility(View.GONE);
        }
    }

    /** 设置右面的点击事件 */
    public void setRightAction(String text, int id, OnClickListener listener) {
        TextView rightT = (TextView) findViewById(R.id.right_text);
        if (rightT != null) {
            if (text != null) {
                rightT.setVisibility(View.VISIBLE);
                rightT.setText(text);
                rightT.setOnClickListener(listener);
            } else {
                rightT.setVisibility(View.GONE);
            }
        }
        ImageView rightI = (ImageView) findViewById(R.id.right_icon);
        if (rightI != null) {
            if (id != -1) {
                rightI.setVisibility(View.VISIBLE);
                rightI.setImageResource(id);
                rightI.setOnClickListener(listener);
            } else {
                rightI.setVisibility(View.GONE);
            }
        }

    }

    /** 设置右面显示或者隐藏 */
    public void setRightVISIBLEOrGone(int type) {
        TextView rightT = (TextView) findViewById(R.id.right_text);
        if (rightT != null) {
            rightT.setVisibility(type);
        }

        ImageView rightI = (ImageView) findViewById(R.id.right_icon);
        if (rightI != null) {
            rightI.setVisibility(type);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        // TODO Auto-generated method stub
        super.startActivity(intent);
        // modalInAnim();
    }

    @Override
    public void finish() {
        ActivityTack.getInstanse().removeActivity(this);
        super.finish();
    }

    public void finishWithoutAnim() {
        super.finish();
    }

    
    @Override
    public void onPause()
    {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    
    @Override
    public void onResume()
    {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    
    public void showToast(String msg) {
		dialoger.showToastShort(getApplicationContext(), msg);
	}
    
}
