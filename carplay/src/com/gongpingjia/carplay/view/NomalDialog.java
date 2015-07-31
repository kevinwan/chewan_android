package com.gongpingjia.carplay.view;

import net.duohuo.dhroid.dialog.DialogCallBack;
import net.duohuo.dhroid.dialog.DialogImpl;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.util.ViewUtil;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.view.dialog.NetErrorDialog;

public class NomalDialog extends DialogImpl
{
    
    @Override
    public Dialog showProgressDialog(Context context, String msg)
    {
        LoadingDialogNew dialog = new LoadingDialogNew(context, msg);
        // dialog.setCancelable(true);
        dialog.show();
        return dialog;
    }
    
    @Override
    public Dialog showProgressDialog(Context context)
    {
        return showProgressDialog(context, "", "");
    }
    
    @Override
    public Dialog showDialog(Context context, String title, String msg, DialogCallBack dialogCallBack)
    {
        
        return showDialog(context, 0, title, msg, dialogCallBack);
    }
    
    @Override
    public void showToastLong(Context context, String msg)
    {
        if (!TextUtils.isEmpty(msg))
        {
            Toast toast = IocContainer.getShare().get(Toast.class);
            toast.setDuration(Toast.LENGTH_SHORT);
            View toastV = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            ViewUtil.bindView(toastV.findViewById(R.id.text), msg);
            toast.setView(toastV);
            toast.show();
        }
    }
    
    @Override
    public void showToastShort(Context context, String msg)
    {
        if (!TextUtils.isEmpty(msg))
        {
            Toast toast = IocContainer.getShare().get(Toast.class);
            toast.setDuration(Toast.LENGTH_SHORT);
            View toastV = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
            ViewUtil.bindView(toastV.findViewById(R.id.text), msg);
            toast.setView(toastV);
            toast.show();
        }
    }
    
    @Override
    public void showToastType(Context context, String msg, String type)
    {
        if (!TextUtils.isEmpty(msg))
        {
            super.showToastType(context, msg, type);
        }
    }
    
    @Override
    public Dialog showAdapterDialoge(Context context, String title, ListAdapter adapter,
        OnItemClickListener itemClickListener)
    {
        return super.showAdapterDialoge(context, title, adapter, itemClickListener);
        
    }
    
    public Dialog showErrorDialog(Context context, String title, String msg, DialogCallBack callback)
    {
        NetErrorDialog dialog = new NetErrorDialog(context, title, msg);
        dialog.show();
        
        return dialog;
    }
    
}
