package net.duohuo.dhroid.dialog.impl;

import net.duohuo.dhroid.R;
import net.duohuo.dhroid.dialog.DialogCallBack;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.InjectUtil;
import net.duohuo.dhroid.util.ViewUtil;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class InfoDialog extends AlertDialog {

	View confimV;

	View cancelV;

	String title;

	String content;

	TextView titleV;

	TextView contentV;

	ImageView iconV;

	DialogCallBack dialogCallBack;

	public InfoDialog(Context context, String title, String msg,
			DialogCallBack dialogCallBack) {
		super(context);
		this.content = msg;
		this.title = title;
		this.dialogCallBack = dialogCallBack;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dh_alert_dialog);
		InjectUtil.inject(this);
		titleV = (TextView) findViewById(R.id.title);
		titleV.setText(title != null ? title : "提示");
		contentV = (TextView) findViewById(R.id.content);
		if (content != null) {
			ViewUtil.bindView(contentV, Html.fromHtml(content));
			contentV.setMovementMethod(LinkMovementMethod.getInstance());
		}
		confimV = findViewById(R.id.confirm);

		confimV.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				if (dialogCallBack != null) {
					dialogCallBack.onClick(IDialog.YES);
				}
				dismiss();
			}
		});

		cancelV = findViewById(R.id.cancel);
		cancelV.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				if (dialogCallBack != null) {
					dialogCallBack.onClick(IDialog.CANCLE);
					dismiss();
				}
			}
		});
		if (dialogCallBack == null) {
			cancelV.setVisibility(View.GONE);
			findViewById(R.id.mid).setVisibility(View.GONE);
		}
	}

}
