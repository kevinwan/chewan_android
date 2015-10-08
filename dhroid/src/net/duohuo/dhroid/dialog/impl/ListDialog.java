package net.duohuo.dhroid.dialog.impl;

import net.duohuo.dhroid.R;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ListDialog extends AlertDialog {

	TextView leftV;

	TextView rightV;

	String title;

	TextView titleV;

	OnItemClickListener itemclickListener;

	ListAdapter adapter;

	ListView listV;

	public ListDialog(Context context, String title, ListAdapter adapter,
			OnItemClickListener itemclickListener) {
		super(context);
		this.adapter = adapter;
		this.title = title;
		this.adapter = adapter;
		this.itemclickListener = itemclickListener;
	}

	public void setLeftTitleAndListener(String title,
			android.view.View.OnClickListener onClick) {
		leftV.setText(title);
		if (onClick != null) {
			leftV.setOnClickListener(onClick);
		}
	}
	
	public void setRightTitleAndListener(String title,
			android.view.View.OnClickListener onClick) {
		rightV.setText(title);
		rightV.setVisibility(View.VISIBLE);
		rightV.setOnClickListener(onClick);
	}

	public void setTitle(String title){
		titleV.setText(title);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dh_list_dialog);
		titleV = (TextView) findViewById(R.id.title);
		titleV.setText(title != null ? title : "提示");
		listV = (ListView) findViewById(R.id.list);
		listV.setAdapter(adapter);
		rightV = (TextView) findViewById(R.id.yes);
		if (itemclickListener != null) {
			listV.setOnItemClickListener(itemclickListener);
		}
		leftV = (TextView) findViewById(R.id.cancel);
		leftV.setOnClickListener(new android.view.View.OnClickListener() {
			public void onClick(View v) {
				dismiss();
			}
		});
	}
}
