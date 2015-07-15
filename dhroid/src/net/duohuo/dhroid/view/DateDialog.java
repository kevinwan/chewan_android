package net.duohuo.dhroid.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.duohuo.dhroid.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;


public class DateDialog
{
    DatePicker datePicker;
    
    long bendi;
    
    LinearLayout timeLayout;
    
    String time;
    
    OnDateResultListener onDateResultListener;
    
    public void show(final Context context,final String dateformat)
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
                
                String format = dateformat==null?"/":dateformat;
                String  dateString= new StringBuilder().append(datePicker.getYear()).append(format)  
                        .append(datePicker.getMonth()+1).append(format) 
                        .append(datePicker.getDayOfMonth()).append(" ").toString() ;
                
                SimpleDateFormat df = new SimpleDateFormat("yyyy"+format+"MM"+format+"dd");
                
                Date date = null;
                try {
                    date = df.parse(dateString);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                long time=date.getTime();
                
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
        void result(String date,long datetime);
    }
}
