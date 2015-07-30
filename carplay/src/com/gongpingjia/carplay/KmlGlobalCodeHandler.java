/**
 * 
 */
package com.gongpingjia.carplay;

import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.GlobalCodeHandler;
import net.duohuo.dhroid.net.JSONUtil;
import net.duohuo.dhroid.net.Response;

import org.json.JSONObject;

import android.content.Context;

/**
 * 
 * @author duohuo-jinghao
 * @date 2014-10-23
 */
public class KmlGlobalCodeHandler implements GlobalCodeHandler
{
    
    Long time = 0l;
    
    @Override
    public void hanlder(Context context, Response response)
    {
        
        JSONObject jo = response.jSON();
        if (!JSONUtil.getBoolean(jo, "success"))
        {
            IocContainer.getShare().get(IDialog.class).showToastLong(context, JSONUtil.getString(jo, "msg"));
        }
        if (!response.isSuccess())
        {
            IocContainer.getShare().get(IDialog.class).showToastLong(context, response.getMsg());
            // String error = JSONUtil.getString(response.jSON(), "errorCode");
            // if ("macerror".equals(error))
            // {
            // }
            // else if ("tokenerror".equals(error))
            // {
            // }
            // else
            // {
            // String code = JSONUtil.getString(response.jSON(), "code");
            // if ("noNetError".equals(code))
            // {
            // IocContainer.getShare().get(IDialog.class).showToastLong(context, response.getMsg());
            // }
            // }
        }
    }
    
}
