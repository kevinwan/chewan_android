package com.gongpingjia.carplay.view.dialog;

import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.format.DateUtils;

import com.gongpingjia.carplay.view.BaseAlertDialog;
import com.gongpingjia.carplay.view.DateTimePicker;
import com.gongpingjia.carplay.view.DateTimePicker.OnDateTimeChangedListener;

public class DateTimePickerDialog extends BaseAlertDialog implements OnClickListener
{
    private DateTimePicker mDateTimePicker;
    
    private Calendar mDate = Calendar.getInstance();
    
    private OnDateTimeSetListener mOnDateTimeSetListener;
    
    @SuppressWarnings("deprecation")
    public DateTimePickerDialog(Context context, long date)
    {
        super(context);
        mDateTimePicker = new DateTimePicker(context);
        setView(mDateTimePicker);
        mDateTimePicker.setOnDateTimeChangedListener(new OnDateTimeChangedListener()
        {
            @Override
            public void onDateTimeChanged(DateTimePicker view, int year, int month, int day, int hour, int minute)
            {
                mDate.set(Calendar.YEAR, year);
                mDate.set(Calendar.MONTH, month);
                mDate.set(Calendar.DAY_OF_MONTH, day);
                mDate.set(Calendar.HOUR_OF_DAY, hour);
                mDate.set(Calendar.MINUTE, minute);
                mDate.set(Calendar.SECOND, 0);
                updateTitle(mDate.getTimeInMillis());
            }
        });
        
        setButton("确定", this);
        setButton2("取消", (OnClickListener)null);
        mDate.setTimeInMillis(date);
        updateTitle(mDate.getTimeInMillis());
    }
    
    public interface OnDateTimeSetListener
    {
        void OnDateTimeSet(AlertDialog dialog, long date);
    }
    
    private void updateTitle(long date)
    {
        int flag =
            DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_WEEKDAY
                | DateUtils.FORMAT_SHOW_TIME;
        setTitle(DateUtils.formatDateTime(this.getContext(), date, flag));
    }
    
    public void setOnDateTimeSetListener(OnDateTimeSetListener callBack)
    {
        mOnDateTimeSetListener = callBack;
    }
    
    public void onClick(DialogInterface arg0, int arg1)
    {
        if (mOnDateTimeSetListener != null)
        {
            mOnDateTimeSetListener.OnDateTimeSet(this, mDate.getTimeInMillis());
        }
    }
}
