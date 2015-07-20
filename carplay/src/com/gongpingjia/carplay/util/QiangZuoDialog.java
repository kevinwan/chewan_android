package com.gongpingjia.carplay.util;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
/**
 * 成员管理  弹出框   
 * @author wang
 *
 */
public class QiangZuoDialog {
	Context context;
	String name = "";
	String age = "";
	int head = 0;
	int sex = 0;
	String txt = "";
	public QiangZuoDialog(Context context ){
		this.context=context;
	}
	public QiangZuoDialog(Context context,String txt){
		this.context=context;
		this.txt = txt;
	}
	public QiangZuoDialog(Context context,String name,String age,int head,int sex,String txt){
		this.context=context;
		this.name = name;
		this.age = age;
		this.head = head;
		this.sex = sex;
		this.txt = txt;
	}
	//座位无人时的弹出框
	public void UnmannedDialog() {

		final AlertDialog dlg = new AlertDialog.Builder(context).create();

		dlg.show();

		Window window = dlg.getWindow();

		// *** 主要就是在这里实现这种效果的.


		window.setContentView(R.layout.grad_seat_dialog);

		TextView textView = (TextView) window.findViewById(R.id.txt);
		textView.setText(txt);
		Button ok = (Button) window.findViewById(R.id.cancel);

		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				dlg.cancel();

			}

		});


		Button cancel = (Button) window.findViewById(R.id.ok);

		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				dlg.cancel();

			}

		});

	}
	//座位有人时 的弹出框
	public void SomeoneDialog() {

		final AlertDialog dlg = new AlertDialog.Builder(context).create();

		dlg.show();

		Window window = dlg.getWindow();

		// *** 主要就是在这里实现这种效果的.


		window.setContentView(R.layout.pull_down_seat);

		TextView textView = (TextView) window.findViewById(R.id.txt);
		textView.setText(txt);
		TextView textView2 = (TextView) window.findViewById(R.id.name);
		TextView textView3 = (TextView) window.findViewById(R.id.person_age);
		ImageView imageView = (ImageView) window.findViewById(R.id.head);
		RelativeLayout layout = (RelativeLayout) window.findViewById(R.id.person_sex);
		Button ok = (Button) window.findViewById(R.id.cancel);

		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				dlg.cancel();

			}

		});


		Button cancel = (Button) window.findViewById(R.id.ok);

		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				dlg.cancel();

			}

		});

	}
}
