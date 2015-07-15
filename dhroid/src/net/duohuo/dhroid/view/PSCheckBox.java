package net.duohuo.dhroid.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class PSCheckBox extends ImageView {
	
	boolean checked = false;
	
	OnCheckChangeListener onCheckChangeListener;
	
	int checkedImg,uncheckImg;

	public boolean isChecked() {
		return checked;
	}

	public OnCheckChangeListener getOnCheckChangeListener() {
		return onCheckChangeListener;
	}

	public void setOnCheckChangeListener(
			OnCheckChangeListener onCheckChangeListener) {
		this.onCheckChangeListener = onCheckChangeListener;
	}

	public PSCheckBox(Context context, AttributeSet attrs,int checkedImg,int uncheckImg) {
		super(context, attrs);
		setChecked(false);
		this.checkedImg = checkedImg;
		this.uncheckImg = uncheckImg;
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onCheckChangeListener == null) {
					setChecked(!checked);
				} else if (onCheckChangeListener != null) {
					if (!isChecked()) {
						setChecked(true);
						onCheckChangeListener.onChange(PSCheckBox.this, true);
					} else {
						setChecked(false);
						onCheckChangeListener.onChange(PSCheckBox.this, false);
					}
				}
			}
		});
	}
	
	public PSCheckBox(Context context) {
		super(context);
		setChecked(false);
		this.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onCheckChangeListener == null) {
					setChecked(!checked);
				} else if (onCheckChangeListener != null) {
					if (!isChecked()) {
						setChecked(true);
						onCheckChangeListener.onChange(PSCheckBox.this, true);
					} else {
						setChecked(false);
						onCheckChangeListener.onChange(PSCheckBox.this, false);
					}
				}
			}
		});
	}


	public void setChecked(boolean checked) {
		this.checked = checked;
		invalidate();
		if(checked){
			setImageResource(checkedImg);
		}else{
			setImageResource(uncheckImg);
		}
	}

	public interface OnCheckChangeListener {
		void onChange(View v, boolean checked);
	}
}
