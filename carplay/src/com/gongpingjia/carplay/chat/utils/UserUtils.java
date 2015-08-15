package com.gongpingjia.carplay.chat.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.chat.DemoHXSDKHelper;
import com.gongpingjia.carplay.chat.bean.ChatUser;
import com.gongpingjia.carplay.chat.controller.HXSDKHelper;
import com.squareup.picasso.Picasso;

public class UserUtils {
	/**
	 * 根据username获取相应user，由于demo没有真实的用户数据，这里给的模拟的数据；
	 * 
	 * @param username
	 * @return
	 */
	public static ChatUser getUserInfo(String username) {
		ChatUser user = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
				.getContactList().get(username);
		if (user == null) {
			user = new ChatUser(username);
		}

		if (user != null) {
			// demo没有这些数据，临时填充
			if (TextUtils.isEmpty(user.getNick()))
				user.setNick(username);
		}
		return user;
	}

	/**
	 * 设置用户头像
	 * 
	 * @param username
	 */
	public static void setUserAvatar(Context context, String username,
			ImageView imageView) {
		ChatUser user = getUserInfo(username);
		if (user != null && user.getAvatar() != null) {
			Picasso.with(context).load(user.getAvatar())
					.placeholder(R.drawable.head_icon).into(imageView);
		} else {
			Picasso.with(context).load(R.drawable.head_icon).into(imageView);
		}
	}

	/**
	 * 设置当前用户头像
	 */
	public static void setCurrentUserAvatar(Context context, ImageView imageView) {
		ChatUser user = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
				.getUserProfileManager().getCurrentUserInfo();
		if (user != null && user.getAvatar() != null) {
			Picasso.with(context).load(user.getAvatar())
					.placeholder(R.drawable.head_icon).into(imageView);
		} else {
			Picasso.with(context).load(R.drawable.head_icon).into(imageView);
		}
	}

	/**
	 * 设置用户昵称
	 */
	public static void setUserNick(String username, TextView textView) {
		ChatUser user = getUserInfo(username);
		if (user != null) {
			textView.setText(user.getNick());
		} else {
			textView.setText(username);
		}
	}

	/**
	 * 设置当前用户昵称
	 */
	public static void setCurrentUserNick(TextView textView) {
		ChatUser user = ((DemoHXSDKHelper) HXSDKHelper.getInstance())
				.getUserProfileManager().getCurrentUserInfo();
		if (textView != null) {
			textView.setText(user.getNick());
		}
	}

	/**
	 * 保存或更新某个用户
	 * 
	 * @param user
	 */
	public static void saveUserInfo(ChatUser newUser) {
		if (newUser == null || newUser.getUsername() == null) {
			return;
		}
		((DemoHXSDKHelper) HXSDKHelper.getInstance()).saveContact(newUser);
	}

}
