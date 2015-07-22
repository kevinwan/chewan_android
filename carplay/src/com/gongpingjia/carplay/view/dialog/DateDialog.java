package com.gongpingjia.carplay.view.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.gongpingjia.carplay.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;

public class DateDialog
{
    DatePicker datePicker;
    
    long bendi;
    
    LinearLayout timeLayout;
    
    String time;
    
    OnDateResultListener onDateResultListener;
    
    public void show(final Context context)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.time_dialog, null);
        datePicker = (DatePicker)view.findViewById(R.id.date_picker);
        // datePicker.setMaxDate(maxDate)
        bendi = System.currentTimeMillis();
        // datePicker.setMaxDate(bendi+86400000*3);
        builder.setView(view);
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(bendi);
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);
        builder.setTitle("选取时间");
        builder.setPositiveButton("确  定", new DialogInterface.OnClickListener()
        {
            
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();
                
                String dateString =
                    new StringBuilder().append(datePicker.getYear())
                        .append("年")
                        .append(datePicker.getMonth() + 1)
                        .append("月")
                        .append(datePicker.getDayOfMonth())
                        .append("日")
                        .toString();
                
                String datef =
                    new StringBuilder().append(datePicker.getYear())
                        .append("-")
                        .append(datePicker.getMonth() + 1)
                        .append("-")
                        .append(datePicker.getDayOfMonth())
                        .append(" ")
                        .toString();
                
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                
                Date date = null;
                try
                {
                    date = df.parse(datef);
                }
                catch (ParseException e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long time = date.getTime();
                
                if (onDateResultListener != null)
                {
                    onDateResultListener.result(dateString, time);
                }
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }
    
    public OnDateResultListener getOnDateResultListener()
    {
        return onDateResultListener;
    }
    
    public void setOnDateResultListener(OnDateResultListener onDateResultListener)
    {
        this.onDateResultListener = onDateResultListener;
    }
    
    public interface OnDateResultListener
    {
        void result(String date, long datetime);
    }
}
