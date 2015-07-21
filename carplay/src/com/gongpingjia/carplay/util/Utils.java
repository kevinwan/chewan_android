package com.gongpingjia.carplay.util;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.os.Environment;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * @description 常用工具方法类
 * @since 2014.1.7
 */
public class Utils
{
    
    private static final String LOG_FILE_NAME = "debug.log";
    
    private static final boolean DEBUG = true;
    
    public static void debug(String message)
    {
        if (DEBUG)
        {
            System.out.println(message);
            output(message);
        }
    }
    
    public static void debug(String tag, String message)
    {
        if (DEBUG)
        {
            Log.v(tag, message);
            output(message);
        }
    }
    
    /**
     * 获取图片缩略图地址 <br>
     * 最大边长200像素
     * 
     * @param url
     * @return
     */
    public static String getThumbUrl(String url)
    {
        return url + "!200";
    }
    
    /**
     * 获取图片缩略图地址 <br>
     * 最大边长可变
     * 
     * @param url
     * @param max
     * @return
     */
    public static String getThumbUrl(String url, int max)
    {
        return url + "!" + max;
    }
    
    private static synchronized void output(String message)
    {
        File file = new File(Environment.getExternalStorageDirectory(), LOG_FILE_NAME);
        FileOutputStream fos = null;
        DataOutputStream dos = null;
        try
        {
            fos = new FileOutputStream(file, true);
            dos = new DataOutputStream(fos);
            SimpleDateFormat sdf = new SimpleDateFormat("MM.dd HH:mm:ss", Locale.US);
            String suffix = sdf.format(new Date());
            String content = suffix + "  " + message + "\n";
            dos.writeUTF(content);
        }
        catch (IOException e)
        {
            debug(e.toString());
        }
        catch (Exception e)
        {
            debug(e.toString());
        }
        finally
        {
            try
            {
                if (dos != null)
                {
                    dos.flush();
                    dos.close();
                    dos = null;
                }
                if (fos != null)
                {
                    fos.flush();
                    fos.close();
                    fos = null;
                }
            }
            catch (IOException e)
            {
                debug(e.toString());
            }
        }
    }
    
    /**
     * 判断是否是合法的Email地址
     * 
     * @param email
     * @return
     */
    public static boolean isValidEmail(String email)
    {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher mc = pattern.matcher(email);
        return mc.matches();
    }
    
    /**
     * 判断是否是合法的手机号码
     * 
     * @note 如果运营商发布新号段，需要更新该方法
     * @param phone
     * @return
     */
    public static boolean isValidMobilePhoneNumber(String phone)
    {
        Pattern pattern = Pattern.compile("^(13[0-9]|14[3|5|7|9]|15[0-9]|170|18[0-9])\\d{8}$");
        Matcher mc = pattern.matcher(phone);
        return mc.matches();
    }
    
    /**
     * 判断是否是合法的固定电话号码
     * 
     * @param phone
     * @return
     */
    public static boolean isValidPhoneNumber(String phone)
    {
        Pattern pattern = Pattern.compile("^(\\(\\d{3,4}-)|(\\d{3,4}-)?\\d{7,8}$");
        Matcher mc = pattern.matcher(phone);
        return mc.matches();
    }
    
    /**
     * 判断是否是合法的URL
     * 
     * @param url
     * @return
     */
    public static boolean isValidURL(String url)
    {
        Pattern patterna = Patterns.WEB_URL;
        Matcher mca = patterna.matcher(url);
        return mca.matches();
    }
    
    /**
     * 是否为空
     * 
     * @param object
     * @return
     */
    public static boolean isNull(Object object)
    {
        boolean result;
        if (TextUtils.isEmpty((CharSequence)(object)))
        {
            result = true;
        }
        else
        {
            String str = String.valueOf(object);
            str = str.toLowerCase();
            result = ("null").equals(str);
        }
        return result;
    }
    
    /**
     * 如果键盘没有收回 自动关闭键盘
     * 
     * @param activity Activity
     * @param v 控件View
     */
    public static void autoCloseKeyboard(Activity activity, View v)
    {
        /** 收起键盘 */
        View view = activity.getWindow().peekDecorView();
        if (view != null && view.getWindowToken() != null)
        {
            InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    
    /**
     * 判断当前应用是否在前台
     * 
     * @param context
     * @return
     */
    public static boolean isAppForground(Context context)
    {
        ActivityManager mActivityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = mActivityManager.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext())
        {
            RunningAppProcessInfo info = i.next();
            if (info.uid == context.getApplicationInfo().uid
                && info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 是否为锁屏状态
     * 
     * @param c
     * @return
     */
    public final static boolean isScreenLocked(Context c)
    {
        KeyguardManager mKeyguardManager = (KeyguardManager)c.getSystemService(Context.KEYGUARD_SERVICE);
        return mKeyguardManager.inKeyguardRestrictedInputMode();
    }
    
    private final static String[] strDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d",
        "e", "f"};
    
    // 返回形式为数字跟字符串
    private static String byteToArrayString(byte bByte)
    {
        int iRet = bByte;
        // System.out.println("iRet="+iRet);
        if (iRet < 0)
        {
            iRet += 256;
        }
        int iD1 = iRet / 16;
        int iD2 = iRet % 16;
        return strDigits[iD1] + strDigits[iD2];
    }
    
    // 转换字节数组为16进制字串
    private static String byteToString(byte[] bByte)
    {
        StringBuffer sBuffer = new StringBuffer();
        for (int i = 0; i < bByte.length; i++)
        {
            sBuffer.append(byteToArrayString(bByte[i]));
        }
        return sBuffer.toString();
    }
    
    public static String getMD5(String str)
    {
        String resultString = null;
        try
        {
            resultString = new String(str);
            MessageDigest md = MessageDigest.getInstance("MD5");
            // md.digest() 该函数返回值为存放哈希值结果的byte数组
            resultString = byteToString(md.digest(str.getBytes()));
        }
        catch (NoSuchAlgorithmException ex)
        {
            ex.printStackTrace();
        }
        return resultString;
    }
    
    /**
     * 唤醒屏幕，如果当前锁屏了
     */
    @SuppressLint("Wakelock")
    public static void notifyScreen(Context context)
    {
        if (!isScreenLocked(context))
        {
            return;
        }
        KeyguardManager km = (KeyguardManager)context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardLock kl = km.newKeyguardLock("unLock");
        kl.disableKeyguard(); // 解锁
        PowerManager pm = (PowerManager)context.getSystemService(Context.POWER_SERVICE);// 获取电源管理器对象
        PowerManager.WakeLock wl =
            pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
        wl.acquire(5000);// 点亮屏幕
        // wl.release();//释放
    }
    
    /**
     * WebView
     * 
     * @param html
     * @return
     */
    public static String formatHtmlData(String html)
    {
        String templates =
            "<html><head><meta content=\"initial-scale=1.0,user-scalable=yes,minimum-scale=1.0,maximum-scale=1.0,width=device-width\" name=\"viewport\" /> <style type=\"text/css\">"
                + "body {word-break:break-all;background-color:transparent;font-size:16px;line-height:1.5;color:#3E3E3E;padding:8px 8px 0;padding-bottom:10px;background-color:#fff;font-family:Helvetica,STHeiti STXihei,Microsoft JhengHei,Microsoft YaHei,Tohoma,Arial;} "
                + "iframe{width:100%;margin:10px auto 10px auto;}img{width:100%; margin:10px 0 10px 0;height:auto;} .brand-title{ height:40px; vertical-align:super;} .brand-title img{width:40px; margin:0px;} "
                + ".brand-title span{color:#1974D2;font-size:18px; vertical-align:super;} .goods-title{border-top:solid 1px #EEEEEE; padding-top:10px; margin-top:10px;} "
                + ".goods-title .title{font-size:16px; color:#1F1F1F} .goods-title .time{color:#c8c8c8;font-size:12px;}</style></head><body>"
                + html + "</body></html>";
        
        // MWMLog.d("", "templates="+templates);
        return templates;
    }
    
//    /**
//     * 日志
//     * 
//     * @param msg
//     */
//    public static void LogD(String msg)
//    {
//        if (Const.IS_DEBUG)
//        {
//            Log.w("GONGPINGJIA", msg);
//        }
//    }
//    
//    /**
//     * 错误日志
//     * 
//     * @param msg
//     */
//    public static void LogE(String msg)
//    {
//        Log.e("GONGPINGJIA", msg);
//    }
//    
//    /**
//     * 运行中设置ViewPager滑行速度
//     * 
//     * @param viewPager
//     * @param mDuration
//     */
//    public static void flySetViewPagerDuration(ViewPager viewPager, int mDuration)
//    {
//        try
//        {
//            Field mScroller;
//            mScroller = ViewPager.class.getDeclaredField("mScroller");
//            mScroller.setAccessible(true);
//            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
//            scroller.setFixedDuration(mDuration);
//            mScroller.set(viewPager, scroller);
//        }
//        catch (NoSuchFieldException e)
//        {
//        }
//        catch (IllegalArgumentException e)
//        {
//        }
//        catch (IllegalAccessException e)
//        {
//        }
//    }
//    
//    /***
//     * 获取缓存目录
//     * 
//     * @return
//     * @throws IOException
//     */
//    public static String getCacheDir()
//        throws IOException
//    {
//        if (getSDPath().equals(""))
//        {
//            throw new IOException("无SD卡，不进行磁盘缓存");
//        }
//        String dir = getSDPath() + "/gongpingjia/" + Const.CACHE_DIR + "/";
//        File file = new File(dir);
//        if (!file.exists())
//        {
//            file.mkdirs();
//        }
//        String substr = dir.substring(0, 4);
//        if (substr.equals("/mnt"))
//        {
//            dir = dir.replace("/mnt", "");
//        }
//        return dir;
//    }
    
    /**
     * 获取SD卡目录
     * 
     * @return
     */
    public static String getSDPath()
    {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        if (sdCardExist)
        {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
        }
        if (sdDir != null)
        {
            return sdDir.getAbsolutePath();
        }
        else
        {
            return "";
        }
    }
    
    /**
     * 将URL HASH算法成短字符串
     * 
     * @param url
     * @return
     */
    public static String hashUrl(String url)
    {
        return MD5(url);
    }
    
    /**
     * 32位MD5加密算法
     * 
     * @param str
     * @return
     */
    public static String MD5(String str)
    {
        MessageDigest md5 = null;
        try
        {
            md5 = MessageDigest.getInstance("MD5");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
        char[] charArray = str.toCharArray();
        byte[] byteArray = new byte[charArray.length];
        for (int i = 0; i < charArray.length; i++)
        {
            byteArray[i] = (byte)charArray[i];
        }
        byte[] md5Bytes = md5.digest(byteArray);
        StringBuffer hexValue = new StringBuffer();
        for (int i = 0; i < md5Bytes.length; i++)
        {
            int val = ((int)md5Bytes[i]) & 0xff;
            if (val < 16)
            {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }
        return hexValue.toString();
    }
    
    /**
     * 是否是数字
     * 
     * @param str
     * @return
     */
    public static boolean isNumber(String str)
    {
        return str.matches("\\d+");
    }
    
    public static int compare_date(String DATE1, String DATE2)
    {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime())
            {
                System.out.println("dt1 在dt2前");
                return 1;
            }
            else if (dt1.getTime() < dt2.getTime())
            {
                System.out.println("dt1在dt2后");
                return -1;
            }
            else
            {
                return 0;
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
        return 0;
    }
    
    /***
     * 获取车龄
     * 
     * @return
     */
    public static List<String> getAgeList()
    {
        List<String> list = new ArrayList<String>();
        list.add("不限");
        list.add("1年以内");
        list.add("1-3年");
        list.add("3-5年");
        list.add("5-8年");
        list.add("8年以上");
        return list;
    }
    
    /***
     * 获取里程
     * 
     * @return
     */
    public static List<String> getLichengList()
    {
        List<String> list = new ArrayList<String>();
        list.add("不限");
        list.add("0-3万公里");
        list.add("3-8万公里");
        list.add("8-15万公里");
        list.add("15-22万公里");
        list.add("22-30万公里");
        list.add("30万公里以上");
        return list;
    }
    
    public static List<String> getLichengValue()
    {
        List<String> list = new ArrayList<String>();
        list.add("");
        list.add("1.5");
        list.add("5.5");
        list.add("11.5");
        list.add("18.5");
        list.add("26");
        list.add("30");
        return list;
    }
    
    /***
     * 获取价格
     * 
     * @return
     */
    public static List<String> getPriceList()
    {
        List<String> list = new ArrayList<String>();
        list.add("不限");
        list.add("3万元以内");
        list.add("3-5万元");
        list.add("5-8万元");
        list.add("8-10万元");
        list.add("10-15万元");
        list.add("15-20万元");
        list.add("20-30万元");
        list.add("30-50万元");
        list.add("50万以上");
        return list;
    }
    
    /***
     * 获取排序
     * 
     * @return
     */
    public static List<String> getSortList()
    {
        List<String> list = new ArrayList<String>();
        list.add("价格升序");
        list.add("价格降序");
        list.add("最新鲜");
        list.add("里程少");
        return list;
    }
    
    /***
     * 查询车况描述
     * 
     * @return
     */
    public static List<List<String>> getCarStatusData()
    {
        List<List<String>> list = new ArrayList<List<String>>();
        
        List<String> excellentlist = new ArrayList<String>();
        excellentlist.add("没有出过任何事故");
        excellentlist.add("有完整的4S店保养记录");
        excellentlist.add("全车仅少量剐蹭痕迹需要补漆");
        excellentlist.add("车辆内饰轻微磨损或少量污渍");
        excellentlist.add("发动机，变速箱没有任何问题");
        excellentlist.add("电子设备全部正常");
        
        List<String> goodlist = new ArrayList<String>();
        goodlist.add("仅发生过少量小碰擦事故，只伤及车辆表面");
        goodlist.add("有完整或部分4S店保养记录");
        goodlist.add("全车需补漆不超过4处，局部钣金不超过3处");
        goodlist.add("车辆内饰磨损或污渍不超过4处，使用故障不超过1处");
        goodlist.add("发动机，变速箱能正常工作，没有维修过");
        goodlist.add("电子设备最多不超过1处故障");
        
        List<String> fairlist = new ArrayList<String>();
        fairlist.add("发生过一些轻微撞击事故，更换或修复过零部件，但不伤及车辆骨架");
        fairlist.add("有部分4S店保养记录或无保养记录");
        fairlist.add("全车有多处剐蹭痕迹需要喷漆或做钣金修复");
        fairlist.add("车辆内饰有多处明显的磨损和污渍，需要进行整理清洗");
        fairlist.add("发动机，变速箱未大修过，允许做过轻微维修");
        fairlist.add("部分电子设备存在故障");
        
        List<String> badlist = new ArrayList<String>();
        badlist.add("发生过严重撞击事故伤及车辆骨架，或是泡过水、被烧过");
        badlist.add("有部分4S店保养记录或无保养记录");
        badlist.add("全车有大量剐蹭痕迹需要喷漆或做钣金修复");
        badlist.add("车辆内饰磨损严重或有大量污渍，需要进行翻新整理");
        badlist.add("发动机，变速箱大修过或需要更换");
        badlist.add("大量电子设备存在故障");
        
        list.add(excellentlist);
        list.add(goodlist);
        list.add(fairlist);
        list.add(badlist);
        
        return list;
    }
    
    public static List<String> getCarStatusCountData()
    {
        List<String> list = new ArrayList<String>();
        list.add("15%的车属于优秀车况");
        list.add("50%的车属于较好车况");
        list.add("25%的车属于一般车况");
        list.add("10%的车属于较差车况");
        return list;
    }
    
    /**
     * 计算车龄
     * 
     * @param years
     */
    public static String getCarYear(String years)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String y = "";
        
        if (years.equals("不限"))
        {
            y = "";
        }
        else if (years.equals("8年以上"))
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.YEAR, -8);
            y = "2000-" + (sdf.format(cal.getTime()));
        }
        else if (years.equals("1年以内"))
        {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.YEAR, -1);
            y = (sdf.format(cal.getTime())) + "-" + (sdf.format(new Date()));
        }
        else
        {
            int index = years.lastIndexOf("年");
            String str = years.substring(0, index);
            String[] stra = str.split("-");
            Calendar minCal = Calendar.getInstance();
            minCal.setTime(new Date());
            minCal.add(Calendar.YEAR, -Integer.parseInt(stra[0]));
            
            Calendar maxCal = Calendar.getInstance();
            maxCal.setTime(new Date());
            maxCal.add(Calendar.YEAR, -Integer.parseInt(stra[1]));
            
            y = (sdf.format(minCal.getTime())) + "-" + (sdf.format(maxCal.getTime()));
        }
        
        return y;
    }
}
