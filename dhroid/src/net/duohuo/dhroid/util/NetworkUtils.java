package net.duohuo.dhroid.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import net.duohuo.dhroid.ioc.IocContainer;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class NetworkUtils {
	public static final String DEFAULT_WIFI_ADDRESS = "00-00-00-00-00-00";
	public static final String WIFI = "Wi-Fi";
	public static final String TWO_OR_THREE_G = "2G/3G";
	public static final String UNKNOWN = "Unknown";

	private static String convertIntToIp(int paramInt) {
		return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "."
				+ (0xFF & paramInt >> 16) + "." + (0xFF & paramInt >> 24);
	}

	/***
	 获取当前网络类型
	 * 
	 * @param pContext
	 * @return type[0] WIFI , TWO_OR_THREE_G , UNKNOWN type[0] SubtypeName
	 */
	public static String[] getNetworkState(Context pContext) {
		String[] type = new String[2];
		type[0] = "Unknown";
		type[1] = "Unknown";
		// �鿴Ȩ��
		if (pContext.getPackageManager().checkPermission(
				"android.permission.ACCESS_NETWORK_STATE",
				pContext.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
			ConnectivityManager localConnectivityManager = (ConnectivityManager) pContext
					.getSystemService("connectivity");
			if (localConnectivityManager == null)
				return type;

			NetworkInfo localNetworkInfo1 = localConnectivityManager
					.getNetworkInfo(1);
			if ((localNetworkInfo1 != null)
					&& (localNetworkInfo1.getState() == NetworkInfo.State.CONNECTED)) {
				type[0] = "Wi-Fi";
				type[1] = localNetworkInfo1.getSubtypeName();
				return type;
			}
			NetworkInfo localNetworkInfo2 = localConnectivityManager
					.getNetworkInfo(0);
			if ((localNetworkInfo2 == null)
					|| (localNetworkInfo2.getState() != NetworkInfo.State.CONNECTED))
				type[0] = "2G/3G";
			type[1] = localNetworkInfo2.getSubtypeName();
			return type;
		}
		return type;
	}

	/***
	 *获取wifi 地址
	 * 
	 * @param pContext
	 * @return
	 */

	public static String getWifiAddress(Context pContext) {
		String address = DEFAULT_WIFI_ADDRESS;
		if (pContext != null) {
			WifiInfo localWifiInfo = ((WifiManager) pContext
					.getSystemService("wifi")).getConnectionInfo();
			if (localWifiInfo != null) {
				address = localWifiInfo.getMacAddress();
				if (address == null || address.trim().equals(""))
					address = DEFAULT_WIFI_ADDRESS;
				return address;
			}

		}
		return DEFAULT_WIFI_ADDRESS;
	}

	/***
	 *获取wifi ip地址
	 * 
	 * @param pContext
	 * @return
	 */
	public static String getWifiIpAddress(Context pContext) {
		WifiInfo localWifiInfo = null;
		if (pContext != null) {
			localWifiInfo = ((WifiManager) pContext.getSystemService("wifi"))
					.getConnectionInfo();
			if (localWifiInfo != null) {
				String str = convertIntToIp(localWifiInfo.getIpAddress());
				return str;
			}
		}
		return "";
	}

	/**
	 * 获取WifiManager
	 * 
	 * @param pContext
	 * @return
	 */
	public static WifiManager getWifiManager(Context pContext) {
		return (WifiManager) pContext.getSystemService("wifi");
	}

	/**
	 * 网络可用
	 * android:name="android.permission.ACCESS_NETWORK_STATE"/>
	 * 
	 * @param ctx
	 * @return
	 */
	public static boolean isNetworkAvailable() {
	
		ConnectivityManager cm = (ConnectivityManager) 	IocContainer.getShare().getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		return (info != null && info.isConnected());
	}
	
	public static boolean isGpsAvailable(Context ctx){

		return false;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/***
	 *  wifi状态
	 * @param pContext
	 * @return
	 */
	public static boolean isWifi(Context pContext) {
		if ((pContext != null)
				&& (getNetworkState(pContext)[0].equals("Wi-Fi"))) {
			return true;
		} else {
			return false;
		}
	}
	
	
	
	/**
     * 抓取当前的城市信息
     * 
     * @return String 城市名
     */
    public  static JSONObject getCurrentCityName(Context context){
        TelephonyManager telManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation glc = (GsmCellLocation) telManager.getCellLocation();
        JSONObject jo = null;
        if (glc != null){
            int cid = glc.getCid(); // value 基站ID号
            int lac = glc.getLac();// 写入区域代码
            String strOperator = telManager.getNetworkOperator();
            int mcc = Integer.valueOf(strOperator.substring(0, 3));// 写入当前城市代码
            int mnc = Integer.valueOf(strOperator.substring(3, 5));// 写入网络代码
            String getNumber = "";
            getNumber += ("cid:" + cid + "\n");
            getNumber += ("cid:" + lac + "\n");
            getNumber += ("cid:" + mcc + "\n");
            getNumber += ("cid:" + mnc + "\n");
            DefaultHttpClient client = new DefaultHttpClient();
            BasicHttpParams params = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(params, 20000);
            HttpPost post = new HttpPost("http://www.google.com/loc/json");
            
            try{
                JSONObject jObject = new JSONObject();
                jObject.put("version", "1.1.0");
                jObject.put("host", "maps.google.com");
                jObject.put("request_address", true);
                if (mcc == 460)
                    jObject.put("address_language", "zh_CN");
                else
                    jObject.put("address_language", "en_US");
                JSONArray jArray = new JSONArray();
                JSONObject jData = new JSONObject();
                jData.put("cell_id", cid);
                jData.put("location_area_code", lac);
                jData.put("mobile_country_code", mcc);
                jData.put("mobile_network_code", mnc);
                jArray.put(jData);
                jObject.put("cell_towers", jArray);
                StringEntity se = new StringEntity(jObject.toString());
                post.setEntity(se);
                HttpResponse resp = client.execute(post);
                BufferedReader br = null;
                if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                    br = new BufferedReader(new InputStreamReader(resp
                            .getEntity().getContent()));
                    StringBuffer sb = new StringBuffer();
                    String result = br.readLine();
                    while (result != null){
                        sb.append(getNumber);
                        sb.append(result);
                        result = br.readLine();
                    }
                    String s = sb.toString();
                    s = s.substring(s.indexOf("{"));
                    jo = new JSONObject(s);
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            catch (ClientProtocolException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }
            finally{
                post.abort();
                client = null;
            }
        }
        
        return jo;
    }
	
}