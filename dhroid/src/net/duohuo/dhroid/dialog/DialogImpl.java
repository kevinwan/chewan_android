package net.duohuo.dhroid.dialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duohuo.dhroid.R;
import net.duohuo.dhroid.dialog.impl.InfoDialog;
import net.duohuo.dhroid.dialog.impl.ListDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

/***
 * IDialog 基本实现
 * 
 * @author duohuo-jinghao
 * 
 */
public class DialogImpl implements IDialog
{
    
    public Dialog showDialog(Context context, String title, String msg, final DialogCallBack dialogCallBack)
    {
        Dialog dialog = new InfoDialog(context, title, msg, dialogCallBack);
        dialog.show();
        return dialog;
    }
    
    public Dialog showItemDialog(Context context, String title, CharSequence[] items, final DialogCallBack callback)
    {
        // new AlertDialog.Builder(context)
        // .setTitle(title).setItems(items, new DialogInterface.OnClickListener(){
        //
        // public void onClick(DialogInterface dialog, int which) {
        // if(callback!=null){
        // callback.onClick(which);
        // }
        // }
        // }).show();
        List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        if (items != null)
        {
            for (int i = 0; i < items.length; i++)
            {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("name", items[i]);
                data.add(map);
            }
        }
        String[] from = {"name"};
        int[] to = {R.id.name};
        
        SimpleAdapter simpleAdapter = new SimpleAdapter(context, data, R.layout.simple_dialog_list_item, from, to);
        DialogOnItemClickListener itemClickListener = new DialogOnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id)
            {
                if (callback != null)
                {
                    callback.onClick(position);
                }
                if (this.dialog != null)
                {
                    dialog.dismiss();
                }
            }
        };
        
        Dialog dialog = showAdapterDialoge(context, title, simpleAdapter, itemClickListener);
        itemClickListener.setDialog(dialog);
        return dialog;
        
    }
    
    class DialogOnItemClickListener implements OnItemClickListener
    {
        Dialog dialog;
        
        public void setDialog(Dialog dialog)
        {
            this.dialog = dialog;
        }
        
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
        {
            
        }
        
    }
    
    public Dialog showProgressDialog(Context context, String title, String msg)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(msg);
        progressDialog.show();
        progressDialog.setCancelable(true);
        return progressDialog;
    }
    
    public Dialog showProgressDialog(Context context, String msg)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(msg);
        progressDialog.show();
        progressDialog.setCancelable(true);
        return progressDialog;
    }
    
    public Dialog showProgressDialog(Context context)
    {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        progressDialog.setCancelable(true);
        return progressDialog;
    }
    
    public void showToastLong(Context context, String msg)
    {
        // 使用同一个toast避免 toast重复显示
        Toast toast = IocContainer.getShare().get(Toast.class);
        toast.setDuration(Toast.LENGTH_LONG);
        TextView textView = new TextView(context);
        textView.setText(msg);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(15, 10, 15, 10);
        textView.setBackgroundResource(R.drawable.toast_frame);
        toast.setView(textView);
        toast.show();
    }
    
    public void showToastShort(Context context, String msg)
    {
        // 使用同一个toast避免 toast重复显示
        Toast toast = IocContainer.getShare().get(Toast.class);
        toast.setDuration(Toast.LENGTH_SHORT);
        TextView textView = new TextView(context);
        textView.setText(msg);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(15, 10, 15, 10);
        textView.setBackgroundResource(R.drawable.toast_frame);
        toast.setView(textView);
        toast.show();
    }
    
    public void showToastType(Context context, String msg, String type)
    {
        showToastLong(context, msg);
    }
    
    public Dialog showDialog(Context context, int icon, String title, String msg, DialogCallBack callback)
    {
        return showDialog(context, title, msg, callback);
    }
    
    public Dialog showAdapterDialoge(Context context, String title, ListAdapter adapter,
        OnItemClickListener itemClickListener)
    {
        Dialog dialog = new ListDialog(context, title, adapter, itemClickListener);
        dialog.show();
        return dialog;
    }
    
    @Override
    public Dialog showErrorDialog(Context context, String title, String msg, DialogCallBack callback)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
