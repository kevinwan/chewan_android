package net.duohuo.dhroid.net;

import net.duohuo.dhroid.Const;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * 网络访问
 *
 * @author duohuo-jinghao
 */
public class NetUtil {

    /**
     * 同步网络访问数据
     *
     * @param url
     * @param method
     * @param params
     * @return
     * @throws IOException
     */
    public static String sync(String url, String method, Map<String, Object> params, DhNet net)
            throws IOException {
        StringBuffer sb = null;
        InputStream in = syncStream(url, method, params, net);
        if (in != null) {
            Scanner scanner = new Scanner(in);
            sb = new StringBuffer();
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            in.close();
            scanner.close();
            return new String(sb);
        }
        return null;

    }


    public static String sync(String url, String method, Map<String, Object> params)
            throws IOException {
        StringBuffer sb = null;
        InputStream in = syncStream(url, method, params, null);
        if (in != null) {
            Scanner scanner = new Scanner(in);
            sb = new StringBuffer();
            while (scanner.hasNext()) {
                sb.append(scanner.nextLine());
            }
            in.close();
            scanner.close();
            return new String(sb);
        }
        return null;

    }

    /**
     * 获取同步获取网络流
     *
     * @param url
     * @param method
     * @param params
     * @return
     * @throws IOException
     */
    public static InputStream syncStream(String url, String method, Map<String, Object> params, DhNet net)
            throws IOException {
        HttpResponse response;
        if (method.equalsIgnoreCase("POST")) {
            HttpPost httppost = new HttpPost(url);
            if (Const.postType == 1) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                for (String key : params.keySet()) {
                    if (params.get(key) != null) {
                        formparams.add(new BasicNameValuePair(key, params.get(key).toString()));
                    }
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
                httppost.setEntity(entity);
            } else {
                JSONObject jo = null;
                try {
                    jo = new JSONObject();

                    for (String key : params.keySet()) {
                        if (params.get(key) != null) {
                            Object params1 = params.get(key);
                            if (params1.toString().contains("{") && params1.toString().contains("=")) {
                                HashMap<String, Object> map = (HashMap<String, Object>) params.get(key);
                                JSONObject jo1 = new JSONObject();
                                for (String key1 : map.keySet()) {
                                    jo1.put(key1, map.get(key1));
                                }
                                jo.put(key, jo1);

                            } else {
                                jo.put(key, params1);
                            }
                        }
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                StringEntity entity = new StringEntity(jo.toString(), "UTF-8");
                entity.setContentType("application/json");
                httppost.setEntity(entity);
                // response = PostByJson(url, params);
            }

            response = HttpManager.execute(httppost);
        } else {
            if (!url.contains("?")) {
                url += "?";
            } else {
                if (!url.endsWith("&")) {
                    url += "&";
                }
            }
            HttpGet httpGet = new HttpGet(url + encodeUrl(params));
            response = HttpManager.execute(httpGet);
        }

        if (getResponseCode(response) == 200) {
            HttpEntity rentity = response.getEntity();
            if (rentity != null) {
                return rentity.getContent();
            }
        } else {
            if (net != null) {
                String errorjson = "{'success':false,'msg':'服务器异常','code':'noServiceError'}";
                Response res = new Response(errorjson);
                net.task.transfer(res, NetTask.TRANSFER_DOERROR);
            }
            return null;
        }
        return null;
    }

    public static int getResponseCode(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    /**
     * 获取get时的url
     *
     * @param params
     * @return
     */
    public static String encodeUrl(Map<String, Object> params) {
        if (params == null || params.size() == 0)
            return "";
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            if (params.get(key) != null) {
                sb.append(key.trim()).append("=").append(URLEncoder.encode((params.get(key).toString()))).append("&");
            }
        }
        return sb.toString();
    }
}