package net.duohuo.dhroid.adapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.duohuo.dhroid.util.BeanUtil;

import android.content.Context;
import android.view.View;

public class PSAdapter extends BeanAdapter
{
    // 字段(值和view的id)
    public List<FieldMap> fields;
    
    public PSAdapter(Context context, int mResource)
    {
        super(context, mResource);
        fields = new ArrayList<FieldMap>();
    }
    
    public PSAdapter addField(String key, Integer refid)
    {
        FieldMap bigMap = new FieldMapImpl(key, refid);
        fields.add(bigMap);
        return this;
    }
    
    public PSAdapter addField(String key, Integer refid, String type)
    {
        FieldMap bigMap = new FieldMapImpl(key, refid, type);
        fields.add(bigMap);
        return this;
    }
    
    public PSAdapter addField(FieldMap fieldMap)
    {
        fields.add(fieldMap);
        return this;
    }
    
    public void addAll(JSONArray ones)
    {
        if (ones == null)
            return;
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (int i = 0; i < ones.length(); i++)
        {
            try
            {
                list.add(ones.getJSONObject(i));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        addAll(list);
    }
    
    @Override
    public void bindView(View itemV, int position, Object jo)
    {
        boolean newViewHolder = false;
        ViewHolder viewHolder = (ViewHolder)itemV.getTag();
        if (viewHolder == null)
        {
            newViewHolder = true;
            viewHolder = new ViewHolder();
            itemV.setTag(viewHolder);
        }
        
        for (Iterator<FieldMap> iterator = fields.iterator(); iterator.hasNext();)
        {
            FieldMap fieldMap = iterator.next();
            View v = null;
            if (newViewHolder)
            {
                v = itemV.findViewById(fieldMap.getRefId());
                viewHolder.put(fieldMap.getRefId(), v);
            }
            else
            {
                v = viewHolder.get(fieldMap.getRefId());
            }
            String value = null;
            Object valueobj = BeanUtil.getProperty(jo, fieldMap.getKey());
            if (valueobj != null)
            {
                value = valueobj.toString();
            }
            if (fieldMap instanceof FieldMapImpl && fixer != null)
            {
                Object gloValue = fixer.fix(value, fieldMap.getType());
                bindValue(position, v, gloValue, fixer.imageOptions(fieldMap.getType()));
            }
            else
            {
                Object ovalue = fieldMap.fix(itemV, position, value, jo);
                bindValue(position, v, ovalue, fixer.imageOptions(fieldMap.getType()));
            }
        }
    }
}
