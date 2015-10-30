package com.gongpingjia.carplay.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity;
import com.gongpingjia.carplay.api.API2;
import com.gongpingjia.carplay.bean.User;
import com.gongpingjia.carplay.view.AnimButtonView;
import com.gongpingjia.carplay.view.dialog.ActivityDialog;

import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;

public class CarPlayUtil {

    public static void bindActiveButton2(String type, String appointmentId, Context context, View yingyao_layout, View yingyaohou) {
        final User user = User.getInstance();
        final String appID = appointmentId;
        final Context mContext = context;

        final LinearLayout yingyaoLayouts = (LinearLayout) yingyao_layout;
        final AnimButtonView yingyao = (AnimButtonView) yingyaoLayouts.getChildAt(0);
        final AnimButtonView hulue = (AnimButtonView) yingyaoLayouts.getChildAt(1);

        final LinearLayout yingyaohouLayouts = (LinearLayout) yingyaohou;
        final AnimButtonView dyanmic_one = (AnimButtonView) yingyaohouLayouts.getChildAt(0);
        final AnimButtonView dyanmic_two = (AnimButtonView) yingyaohouLayouts.getChildAt(1);

        switch (type) {
            case "应邀":
                yingyaoLayouts.setVisibility(View.VISIBLE);
                yingyaohouLayouts.setVisibility(View.GONE);
                yingyao.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(user.getPhone())) {
                            System.out.println("获取:" + user.getPhone());
                            ActivityDialog dialog = new ActivityDialog(mContext, appID);
                            dialog.setOnPickResultListener(new ActivityDialog.OnPickResultListener() {

                                @Override
                                public void onResult(int result) {
                                    if (result == 1) {
                                        yingyaoLayouts.setVisibility(View.GONE);
                                        yingyaohouLayouts.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                            dialog.show();
                        } else {
                            DhNet net = new DhNet(API2.CWBaseurl + "application/" + appID + "/process?userId=5609eb2c0cf224e7d878f693&token=67666666-f2ff-456d-a9cc-e83761749a6a");
                            net.addParam("accept", "true");
                            net.doPostInDialog(new NetTask(mContext) {
                                @Override
                                public void doInUI(Response response, Integer transfer) {
                                    if (response.isSuccess()) {
                                        yingyaoLayouts.setVisibility(View.GONE);
                                        yingyaohouLayouts.setVisibility(View.VISIBLE);
                                        System.out.println("应邀：" + response.isSuccess());
                                    }
                                }
                            });
                        }

                    }
                });
                hulue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DhNet net = new DhNet(API2.CWBaseurl + "application/" + appID + "/process?userId=5609eb2c0cf224e7d878f693&token=67666666-f2ff-456d-a9cc-e83761749a6a");
                        net.addParam("accept", "false");
                        net.doPostInDialog(new NetTask(mContext) {
                            @Override
                            public void doInUI(Response response, Integer transfer) {
                                if (response.isSuccess()) {
                                    System.out.println("忽略：" + response.isSuccess());
                                }
                            }
                        });
                    }
                });

                break;
            case "邀请中":

                yingyaohouLayouts.setVisibility(View.VISIBLE);
                yingyaoLayouts.setVisibility(View.GONE);
                dyanmic_one.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //跳转聊天
                    }
                });
                dyanmic_two.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //拨打电话
                    }
                });

                break;
            default:
                break;
        }

    }


//    public static void bindActiveButton(String type, String appointmentId, Context context, View... activeButton) {
//        View[] views = activeButton;
//        View[] viewtwo = activeButton;
//        final User user = User.getInstance();
//        final String appID = appointmentId;
//        final Context mContext = context;
//
//        switch (type) {
//            case "应邀":
//                AnimButtonView hulue = (AnimButtonView) viewtwo[0];
//                AnimButtonView yingyao = (AnimButtonView) viewtwo[1];
//                final LinearLayout yingyao_layout = (LinearLayout) viewtwo[2];
//                final LinearLayout yingyao_layouts = (LinearLayout) viewtwo[3];
//                yingyao_layout.setVisibility(View.VISIBLE);
//                yingyao_layouts.setVisibility(View.GONE);
//                yingyao.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View view) {
//                        if (TextUtils.isEmpty(user.getPhone())) {
//                            System.out.println("获取:" + user.getPhone());
//                            ActivityDialog dialog = new ActivityDialog(mContext, appID);
//                            dialog.setOnPickResultListener(new ActivityDialog.OnPickResultListener() {
//
//                                @Override
//                                public void onResult(int result) {
//                                    if (result == 1) {
//                                        yingyao_layout.setVisibility(View.GONE);
//                                        yingyao_layouts.setVisibility(View.VISIBLE);
//                                    }
//                                }
//                            });
//                            dialog.show();
//                        } else {
//                            DhNet net = new DhNet(API2.CWBaseurl + "application/" + appID + "/process?userId=5609eb2c0cf224e7d878f693&token=67666666-f2ff-456d-a9cc-e83761749a6a");
//                            net.addParam("accept", "true");
//                            net.doPostInDialog(new NetTask(mContext) {
//                                @Override
//                                public void doInUI(Response response, Integer transfer) {
//                                    if (response.isSuccess()) {
//                                        yingyao_layout.setVisibility(View.GONE);
//                                        yingyao_layouts.setVisibility(View.VISIBLE);
//                                        System.out.println("应邀：" + response.isSuccess());
//                                    }
//                                }
//                            });
//                        }
//
//                    }
//                });
//                hulue.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        DhNet net = new DhNet(API2.CWBaseurl + "application/" + appID + "/process?userId=5609eb2c0cf224e7d878f693&token=67666666-f2ff-456d-a9cc-e83761749a6a");
//                        net.addParam("accept", "false");
//                        net.doPostInDialog(new NetTask(mContext) {
//                            @Override
//                            public void doInUI(Response response, Integer transfer) {
//                                if (response.isSuccess()) {
//                                    System.out.println("忽略：" + response.isSuccess());
//                                }
//                            }
//                        });
//                    }
//                });
//
//                break;
//
//            case "邀请中":
//                AnimButtonView dyanmic_one = (AnimButtonView) views[0];
//                AnimButtonView dyanmic_two = (AnimButtonView) views[1];
//                final LinearLayout dyanmic_layout = (LinearLayout) views[2];
//                final LinearLayout dyanmic_layouts = (LinearLayout) views[3];
//                dyanmic_layout.setVisibility(View.VISIBLE);
//                dyanmic_layouts.setVisibility(View.GONE);
//                dyanmic_one.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //跳转聊天
//                    }
//                });
//                dyanmic_two.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        //拨打电话
//                    }
//                });
//                break;
//
//
//        }
//
//    }

    public static void bindSexView(String gender, View sexBg) {
        if (!TextUtils.isEmpty(gender)) {
            ImageView sexI = (ImageView) sexBg.findViewById(R.id.iv_sex);
            if (gender.equals("男")) {
                sexI.setImageResource(R.drawable.icon_man3x);
                sexBg.setBackgroundResource(R.drawable.radio_sex_man_normal);
            } else {
                sexI.setImageResource(R.drawable.icon_woman3x);
                sexBg.setBackgroundResource(R.drawable.radion_sex_woman_normal);
            }
        }
    }


//    public static void bindSexView(String gender, View sexBg) {
//        if (!TextUtils.isEmpty(gender)) {
//            if (gender.equals("男")) {
//                sexBg.setBackgroundResource(R.drawable.man);
//            } else {
//                sexBg.setBackgroundResource(R.drawable.woman);
//            }
//        }
//    }

    public static void bindDriveAge(JSONObject jo, ImageView carLogoI,
                                    TextView driveAgeT) {
        if (TextUtils.isEmpty(JSONUtil.getString(jo, "carModel"))) {
            carLogoI.setVisibility(View.GONE);
            ViewUtil.bindView(driveAgeT, "带我飞~");
        } else {
            carLogoI.setVisibility(View.VISIBLE);
            ViewUtil.bindView(driveAgeT, JSONUtil.getString(jo, "carModel")
                    + "," + JSONUtil.getString(jo, "drivingExperience") + "年驾龄");
            ViewUtil.bindNetImage((ImageView) carLogoI,
                    JSONUtil.getString(jo, "carBrandLogo"), "carlogo");
        }
    }

    public static String getStringDate(Long date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateString = formatter.format(date);

        return dateString;
    }

    public static int px2sp(float pxValue, Context context) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(float spValue, Context context) {
        float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    public static boolean getPhoto(final Activity activity,
                                   final int takePhotoCode, final int imageCode, final File tempFile) {
        final CharSequence[] items = {"相册", "拍照"};
        AlertDialog dlg = new AlertDialog.Builder(activity).setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 1) {
                            Intent getImageByCamera = new Intent(
                                    "android.media.action.IMAGE_CAPTURE");
                            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(tempFile));
                            activity.startActivityForResult(getImageByCamera,
                                    takePhotoCode);
                        } else {

                            Intent intent = new Intent(activity,
                                    PhotoSelectorActivity.class);
                            intent.putExtra(PhotoSelectorActivity.KEY_MAX, 9);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            activity.startActivityForResult(intent, imageCode);

                            // Intent getImage = new Intent(
                            // Intent.ACTION_GET_CONTENT);
                            // getImage.addCategory(Intent.CATEGORY_OPENABLE);
                            // getImage.setType("image/jpeg");
                            // activity.startActivityForResult(getImage,
                            // imageCode);
                        }
                    }
                }).create();
        Window window = dlg.getWindow();
        window.setWindowAnimations(R.style.mystyle);
        dlg.show();
        return true;
    }

    public static boolean getPhoto(final Activity activity,
                                   final int takePhotoCode, final int imageCode, final File tempFile,
                                   final int needPicNum) {
        final CharSequence[] items = {"相册", "拍照"};
        AlertDialog dlg = new AlertDialog.Builder(activity).setTitle("选择图片")
                .setItems(items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 1) {
                            Intent getImageByCamera = new Intent(
                                    "android.media.action.IMAGE_CAPTURE");
                            getImageByCamera.putExtra(MediaStore.EXTRA_OUTPUT,
                                    Uri.fromFile(tempFile));
                            activity.startActivityForResult(getImageByCamera,
                                    takePhotoCode);
                        } else {
                            Intent intent = new Intent(activity,
                                    PhotoSelectorActivity.class);
                            intent.putExtra(PhotoSelectorActivity.KEY_MAX,
                                    needPicNum);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            activity.startActivityForResult(intent, imageCode);

                            // Intent getImage = new Intent(
                            // Intent.ACTION_GET_CONTENT);
                            // getImage.addCategory(Intent.CATEGORY_OPENABLE);
                            // getImage.setType("image/jpeg");
                            // activity.startActivityForResult(getImage,
                            // imageCode);
                        }
                    }
                }).create();
        Window window = dlg.getWindow();
        window.setWindowAnimations(R.style.mystyle);
        dlg.show();
        return true;
    }


    public static String numberWithDelimiter(int num) {
        StringBuilder accum = new StringBuilder();
        int len = accum.append(num).length();
        if (len <= 3) return accum.append("m").toString();   //如果长度小于等于3不做处理
        while ((len -= 3) > 0) { //从个位开始倒序插入
            accum.insert(len, ",");
        }
        return accum.append("km").toString();
    }

}
