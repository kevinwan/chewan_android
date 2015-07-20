package com.gongpingjia.carplay.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.gongpingjia.carplay.R;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class ClearableEditText extends EditText implements OnFocusChangeListener, TextWatcher {

    private Drawable mClearDrawable;

    private boolean hasFocus;

    public ClearableEditText(Context context) {
        this(context, null);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mClearDrawable = getCompoundDrawables()[2];
        if (mClearDrawable == null) {
            mClearDrawable = getResources().getDrawable(R.drawable.ic_launcher);
        }

        mClearDrawable.setBounds(0, 0, mClearDrawable.getIntrinsicWidth(), mClearDrawable.getIntrinsicHeight());
        setClearIconVisible(false);
        setOnFocusChangeListener(this);
        addTextChangedListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {
                boolean touchable = (event.getX() > getWidth() - getTotalPaddingRight())
                        && (event.getX() < (getWidth() - getPaddingRight()));
                if (touchable) {
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);

    }

    /**
     * @Description
     * @author Administrator
     * @param visible
     */

    protected void setClearIconVisible(boolean visible) {
        Drawable right = visible ? mClearDrawable : null;
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], right, getCompoundDrawables()[3]);
    }

    /**
     * Description
     * 
     * @param v
     * @param hasFocus
     * @see android.view.View.OnFocusChangeListener#onFocusChange(android.view.View,
     *      boolean)
     */

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().length() > 0);
        } else {
            setClearIconVisible(false);
        }
    }

    /**
     * Description
     * 
     * @param text
     * @param start
     * @param lengthBefore
     * @param lengthAfter
     * @see android.widget.TextView#onTextChanged(java.lang.CharSequence, int,
     *      int, int)
     */

    @Override
    public void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        // TODO Auto-generated method stub
        if (hasFocus) {
            setClearIconVisible(text.length() > 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

}
