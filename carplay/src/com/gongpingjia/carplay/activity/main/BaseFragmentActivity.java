package com.gongpingjia.carplay.activity.main;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.receiver.NetReceiver;
import com.umeng.analytics.MobclickAgent;

import net.duohuo.dhroid.activity.ActivityTack;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;

public class BaseFragmentActivity extends FragmentActivity {
    public IDialog dialoger;

    public Activity self;

    Integer dialogcount = 0;

    Dialog progressdialog;

    NetReceiver netReceiver;

    private long exitTimefist = 0;
    private long exitTimesec = 0;
    private long exitTimethr = 0;
    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        dialoger = IocContainer.getShare().get(IDialog.class);
        self = this;
        ActivityTack.getInstanse().addActivity(this);

//        netReceiver = new NetReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(CONNECTIVITY_CHANGE_ACTION);
//        registerReceiver(netReceiver, filter);
    }


    /**
     * 设置标题
     */
    public void setTitle(String text) {
        TextView titleT = (TextView) findViewById(R.id.title);
        if (titleT != null) {
            titleT.setText(text);
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
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void showToast(String msg) {
        dialoger.showToastShort(getApplicationContext(), msg);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
//        if (netReceiver != null) {
//            unregisterReceiver(netReceiver);
//        }
    }

    public Dialog showProgressDialog(String msg) {
        if (TextUtils.isEmpty(msg))
            msg = getString(R.string.progress_doing);

        synchronized (dialogcount) {
            dialogcount++;
        }

        System.out.println("show" + dialogcount);

        if (progressdialog == null || !progressdialog.isShowing()) {
            progressdialog = dialoger.showProgressDialog(this, msg);
            System.out.print("progress dlg:"
                    + dialoger.getClass().getSimpleName());
        }
        return progressdialog;
    }

    /**
     * dismiss加载框
     */
    public void hidenProgressDialog() {
        synchronized (dialogcount) {
            dialogcount--;
        }
        System.out.println("hiden" + dialogcount);
        // if (dialogcount == 0) {
        if (progressdialog != null) {
            progressdialog.dismiss();
        }
        // }
    }

    /**
     * 返回列表顶部
     */
    public void backToTop(final ListView... listViews) {
        RelativeLayout labt = (RelativeLayout) findViewById(R.id.labt);
        if (labt != null) {
            labt.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    for (int i = 0; i < listViews.length; i++) {
                        dowbleClick(listViews[i], i);
                    }
                }
            });

        }
    }

    /**
     * 返回列表顶部双击响应事件
     *
     * @param listView
     */
    private void dowbleClick(ListView listView, int currnt) {
        switch (currnt) {
            case 0:
                if ((System.currentTimeMillis() - exitTimefist) > 300)
                    exitTimefist = System.currentTimeMillis();
                else
                    backTop(listView);
                break;
            case 1:
                if ((System.currentTimeMillis() - exitTimesec) > 300)
                    exitTimesec = System.currentTimeMillis();
                else
                    backTop(listView);
                break;
            case 2:
                if ((System.currentTimeMillis() - exitTimethr) > 300)
                    exitTimethr = System.currentTimeMillis();
                else
                    backTop(listView);
                break;

            default:
                break;
        }
    }

    private void backTop(ListView listView) {
        if (!listView.isStackFromBottom()) {
            listView.setStackFromBottom(true);
        }
        listView.setStackFromBottom(false);
    }
}
