package com.gongpingjia.carplay.photo.domain;

import java.util.List;

import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity.OnLocalAlbumListener;
import com.gongpingjia.carplay.activity.main.PhotoSelectorActivity.OnLocalReccentListener;
import com.gongpingjia.carplay.photo.controller.AlbumController;
import com.gongpingjia.carplay.photo.model.AlbumModel;
import com.gongpingjia.carplay.photo.model.PhotoModel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;


@SuppressLint("HandlerLeak")
public class PhotoSelectorDomain {

	private AlbumController albumController;

	public PhotoSelectorDomain(Context context) {
		albumController = new AlbumController(context);
	}

	public void getReccent(final OnLocalReccentListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<PhotoModel> photos = albumController.getCurrent();
				Message msg = new Message();
				msg.obj = photos;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/** ��ȡ����б� */
	public void updateAlbum(final OnLocalAlbumListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				listener.onAlbumLoaded((List<AlbumModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<AlbumModel> albums = albumController.getAlbums();
				Message msg = new Message();
				msg.obj = albums;
				handler.sendMessage(msg);
			}
		}).start();
	}

	/** ��ȡ��������µ�������Ƭ��Ϣ */
	public void getAlbum(final String name, final OnLocalReccentListener listener) {
		final Handler handler = new Handler() {
			@SuppressWarnings("unchecked")
			@Override
			public void handleMessage(Message msg) {
				listener.onPhotoLoaded((List<PhotoModel>) msg.obj);
			}
		};
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<PhotoModel> photos = albumController.getAlbum(name);
				Message msg = new Message();
				msg.obj = photos;
				handler.sendMessage(msg);
			}
		}).start();
	}

}
