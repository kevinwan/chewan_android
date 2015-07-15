package net.duohuo.dhroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Gallery;


public class NormalGallery extends Gallery {

	public NormalGallery(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	private boolean isScrollingLeft(MotionEvent e1, MotionEvent e2) {
		return e2.getX() > e1.getX();
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int keyCode;
		if (isScrollingLeft(e1, e2)) {
			keyCode = KeyEvent.KEYCODE_DPAD_LEFT; // 向左移
		} else {
			keyCode = KeyEvent.KEYCODE_DPAD_RIGHT; // 右移
		}
		onKeyDown(keyCode, null);
		return false;
	}

}
