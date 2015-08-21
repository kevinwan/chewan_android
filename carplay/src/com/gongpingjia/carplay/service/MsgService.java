package com.gongpingjia.carplay.service;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.User;

import de.greenrobot.event.EventBus;

public class MsgService extends Service {
	Timer mTimer;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				getMsgCount();

			}
		}, 0, 30 * 60 * 1000);
	}

	private void getMsgCount() {
		User user = User.getInstance();
		try {
			String result = NetUtil.sync(
					API.CWBaseurl + "/user/" + user.getUserId()
							+ "/message/count?token=" + user.getToken(), "GET",
					null);
			try {
				JSONObject jo = new JSONObject(result);
				if (jo.getInt("result") == 0) {
					System.out.println("发送");
					JSONObject data = JSONUtil.getJSONObject(jo, "data");
					if (data != null) {
						EventBus.getDefault().post(data);
					}

				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
