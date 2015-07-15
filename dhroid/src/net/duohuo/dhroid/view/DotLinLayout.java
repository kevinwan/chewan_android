package net.duohuo.dhroid.view;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class DotLinLayout extends LinearLayout {

	int dotComImg;

	int dotFocusImg;

	public DotLinLayout(Context context) {
		super(context);
	}

	public DotLinLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.CENTER_VERTICAL);
	}

	/**
	 * 设置dot的背景
	 * @param com
	 * @param focus
	 */
	public void setDotImage(int com, int focus) {
		this.dotComImg = com;
		this.dotFocusImg = focus;
	}

	/**
	 * 设置dot的数量
	 * 
	 * @param count
	 */
	public void setDotCount(int count) {
		this.removeAllViews();
		for (int i = 0; i < count; i++) {
			ImageView imageView = new ImageView(getContext());
			LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			if (i > 0)
				params.leftMargin = 10;
			addView(imageView, params);
			if (i == 0)
				imageView.setImageResource(dotFocusImg);
			else
				imageView.setImageResource(dotComImg);
		}
	}

	/**
	 * 设置当前的点
	 * 
	 * @param current
	 */
	public void setCurrentFocus(int current) {
		for (int i = 0; i < getChildCount(); i++) {
			ImageView iamgeView = (ImageView) getChildAt(i);
			if (i == current)
				iamgeView.setImageResource(dotFocusImg);
			else
				iamgeView.setImageResource(dotComImg);
		}
	}

}
