package com.gongpingjia.carplay.util;

import com.gongpingjia.carplay.R;

import android.view.View;

public class CarPlayUtil
{
    public static void bindSexView(String gender, View sexBg)
    {
        if (gender.equals("男"))
        {
            sexBg.setBackgroundResource(R.drawable.man);
        }
        else
        {
            sexBg.setBackgroundResource(R.drawable.woman);
        }
    }
}
