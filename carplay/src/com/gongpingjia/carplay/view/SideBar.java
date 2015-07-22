package com.gongpingjia.carplay.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SideBar extends View {
    private char[] l;

    private SectionIndexer sectionIndexter = null;

    private ListView list;

    private TextView mDialogText;

    private Paint mPaint = new Paint();

    Bitmap mbitmap;

    Context context;

    public SideBar(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        l = new char[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
                'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '#' };
    }

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void setListView(ListView _list) {
        list = _list;
        ListAdapter ha = (ListAdapter) _list.getAdapter();
        sectionIndexter = (SectionIndexer) ha;

    }

    public void setTextView(TextView mDialogText) {
        this.mDialogText = mDialogText;
    }

    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);
        int i = (int) event.getY();

        int idx = i / (getMeasuredHeight() / l.length);
        if (idx >= l.length) {
            idx = l.length - 1;
        } else if (idx < 0) {
            idx = 0;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
            setBackgroundColor(Color.parseColor("#f5f7fa"));
            if (mDialogText != null) {
                mDialogText.setVisibility(View.VISIBLE);
                mDialogText.setText(String.valueOf(l[idx]));
                mDialogText.setTextSize(34);
            }
            if (sectionIndexter == null) {
                sectionIndexter = (SectionIndexer) list.getAdapter();
            }
            if (sectionIndexter != null) {
                int position = sectionIndexter.getPositionForSection(l[idx]);
                if (position == -1) {
                    return true;
                }
                list.setSelection(position);
            }
        } else {
            mDialogText.setVisibility(View.INVISIBLE);

        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            setBackgroundDrawable(new ColorDrawable(0x00000000));
        }
        return true;
    }

    protected void onDraw(Canvas canvas) {
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#48d1d5"));
        mPaint.setTextSize(25);
        mPaint.setStyle(Style.FILL);
        mPaint.setTextAlign(Paint.Align.CENTER);
        float widthCenter = getMeasuredWidth() / 2;
        if (l.length > 0) {
            float height = getMeasuredHeight() / l.length;
            for (int i = 0; i < l.length; i++) {
                canvas.drawText(String.valueOf(l[i]), widthCenter, (i + 1) * height, mPaint);
            }
        }
        this.invalidate();
        mPaint.reset();
        super.onDraw(canvas);
    }
}
