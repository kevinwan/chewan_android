package com.gongpingjia.carplay.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.gongpingjia.carplay.ILoadSuccess;

import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.net.cache.CachePolicy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/15.
 */
public class CarPlayBaseFragment extends Fragment {

    int ignore = 0;

    int currentPageListSize = 0;

    String url;

    DhNet net;

    public boolean hasMore = true;


    Boolean isLoading = false;

    String fromWhat;
    public List<JSONObject> mVaules = null;
    private final Object mLock = new Object();

    Context self;

    public IDialog dialoger;

    NetTask nettask;
    int limit = 10;
    ILoadSuccess onLoadSuccess;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ignore = 0;
        net = new DhNet();
        net.setMethod(DhNet.METHOD_GET);
        useCache(CachePolicy.POLICY_BEFORE_AND_AFTER_NET);
        mVaules = new ArrayList<JSONObject>();
        self = getActivity();
        dialoger = IocContainer.getShare().get(IDialog.class);
        nettask = new NetTask(self) {
            @Override
            public void doInBackground(Response response) {
                // 后台处理主要是数据封装
                JSONArray array = null;
                JSONObject responseJo = response.jSON();
                if (fromWhat == null) {
                    array = response.jSONArrayFromData();
                } else {
                    array = response.jSONArrayFrom(fromWhat);
                }
                List<Object> jos = new ArrayList<Object>();
                if (array != null) {
                    for (int i = 0; i < array.length(); i++) {
                        try {
                            JSONObject jo = array.getJSONObject(i);
                            jos.add(jo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
                response.addBundle("list" + response.isCache(), jos);
                super.doInBackground(response);
            }

            @Override
            public void onErray(Response response) {
                super.onErray(response);
                ignore = ignore - currentPageListSize;
                isLoading = false;
                hasMore = false;
                if (dialoger != null) {
                    if ("noNetError".equals(response.code)
                            || "netErrorButCache".equals(response.code)) {
                    } else if ("netError".equals(response.code)) {

                        if (ignore > 0) {
                            dialoger.showToastShort(self, response.msg);
                        }
                    } else {
                        dialoger.showToastShort(self, response.msg);
                    }
                }

                if (onLoadSuccess != null) {
                    onLoadSuccess.loadSuccess();
                }

                if (onLoadSuccess != null && !response.isCache() && ignore == 0) {
                    onLoadSuccess.loadSuccessOnFirst();
                }

            }

            @Override
            public void onCancelled() {
                super.onCancelled();
            }

            @Override
            public void doInUI(Response response, Integer transfer) {
                if (!response.isSuccess() && !response.isCache()) {
                    if (!TextUtils.isEmpty(response.msg)) {
                        dialoger.showToastShort(self, response.msg);
                    }
                }
                List<JSONObject> list = response.getBundle("list"
                        + response.isCache());
                if (list.size() == 0 || list.size() < limit) {
                    if (dialoger != null) {
                        if (!response.isCache()) {
                            if (ignore > 0) {
                                dialoger.showToastShort(self,
                                        Const.netadapter_no_more);
                            }
                        }
                    }
                    hasMore = false;
                }


                if (Const.postType == 2) {
                    if (ignore == 0) {
                        clear();
                    }
                }


                if (list != null) {
                    addAll(list);
                }

                // 如果加载的收据不是来自缓存那么就不在使用缓存否者一直使用缓存
                if (!response.isCache) {
                    useCache(CachePolicy.POLICY_NOCACHE);
                    isLoading = false;
                }

                if (onLoadSuccess != null) {
                    onLoadSuccess.loadSuccess();
                }

                if (onLoadSuccess != null && !response.isCache() && ignore == 0) {
                    onLoadSuccess.loadSuccessOnFirst();
                }

                if (Const.postType == 2) {
                    if (!response.isCache()) {
                        currentPageListSize = list.size();
                        ignore = ignore + list.size();
                    }
                }

            }
        };

    }

    public void addParams(String key, Object value) {
        net.addParam(key, value);
    }

    public void setOnLoadSuccess(ILoadSuccess onLoadSuccess) {
        this.onLoadSuccess = onLoadSuccess;
    }


    public void setUrl(String url) {
        net.setUrl(url);
    }

    public void fromWhat(String fromWhat) {
        this.fromWhat = fromWhat;
    }


    public void refresh() {
        if (!isLoading) {
            hasMore = true;
            ignore = 0;
            showNext();
        }
    }

    public void refreshNoDialog() {
        if (!isLoading) {
            hasMore = true;
            ignore = 0;
            showNextNoDialog();
        }
    }


    public void showNext() {
        synchronized (isLoading) {
            if (isLoading)
                return;
            isLoading = true;
        }
        net.addParam("ignore", ignore);
        net.addParam("limit", limit);
        net.execuseInDialog("", nettask);
    }


    public void showNextNoDialog() {
        synchronized (isLoading) {
            if (isLoading)
                return;
            isLoading = true;
        }
        net.addParam("ignore", ignore);
        net.addParam("limit", limit);
        net.execuse(nettask);
    }


    /**
     * 使用默认缓存
     */
    public void useCache() {
        net.useCache(true);
    }

    public void useCache(CachePolicy policy) {
        net.useCache(policy);
    }


    public void clear() {
        synchronized (mLock) {
            mVaules.clear();
        }
    }


    public void addAll(List<JSONObject> ones) {
        synchronized (mLock) {
            mVaules.addAll(ones);
        }
    }

}
