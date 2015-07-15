package net.duohuo.dhroid.bean;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;

import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.BeanUtil;
import android.content.Context;

import com.google.gson.annotations.Expose;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.field.DatabaseField;

public class Entity<T>
{
    @DatabaseField(id = true)
    String id;
    
    @Expose(serialize = false, deserialize = false)
    Context mContext;
    
    @Expose(serialize = false, deserialize = false)
    Dao<Entity<T>, String> dao;
    
    // 当前对象是否缓存
    @Expose(serialize = false, deserialize = false)
    boolean isCache;
    
    // 是否实使用缓存
    @Expose(serialize = false, deserialize = false)
    boolean cacheable = true;
    
    @Expose(serialize = false, deserialize = false)
    Object activity;
    
    @Expose(serialize = false, deserialize = false)
    public String asFieldName;
    
    public Entity(String id, Context mContext, Object activity, String asFieldName)
    {
        super();
        this.id = id;
        this.mContext = mContext;
        this.activity = activity;
        this.asFieldName = asFieldName;
        // try
        // {
        // dao = IocContainer.getShare().get(OrmLiteSqliteOpenHelper.class).getDao(this.getClass());
        // }
        // catch (SQLException e)
        // {
        // e.printStackTrace();
        // }
        
    }
    
    public Entity()
    {
        super();
    }
    
    // 获取API方法
    public String getApi(String method)
    {
        return "";
    }
    
    /**
     * 加载缓存
     */
    public void loadCache()
    {
        try
        {
            Entity pentity = dao.queryForId(id);
            BeanUtil.copyBeanWithOutNull(pentity, this);
            notifyValueChanged("Load", null);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    
    public void delete()
    {
        
    }
    
    public Dao<Entity<T>, String> getDao()
    {
        return dao;
    }
    
    public void setDao(Dao<Entity<T>, String> dao)
    {
        this.dao = dao;
    }
    
    public void load()
    {
        DhNet net = new DhNet(getApi("load"));
        net.addParam("id", id);
        net.doGet(new NetTask(mContext)
        {
            @Override
            public void doInUI(Response response, Integer transfer)
            {
                Entity resentity = response.modelFromData(Entity.this.getClass());
                BeanUtil.copyBeanWithOutNull(resentity, Entity.this);
                try
                {
                    getDao().createOrUpdate(Entity.this);
                }
                catch (SQLException e)
                {
                    e.printStackTrace();
                }
                Entity.this.isCache = false;
                notifyValueChanged("Load", response);
            }
        });
    }
    
    public void save()
    {
        
    }
    
    /**
     * 
     * @param methodname
     * @param response
     */
    public void notifyValueChanged(String methodname, Response response)
    {
        if (this.activity != null)
        {
            try
            {
                String name =
                    "on" + asFieldName.substring(0, 1).toUpperCase() + asFieldName.substring(1, asFieldName.length())
                        + methodname.substring(0, 1).toUpperCase() + methodname.substring(1);
                Method method = activity.getClass().getDeclaredMethod(name, Entity.this.getClass(), Response.class);
                method.invoke(this.activity, this, response);
            }
            catch (SecurityException e)
            {
                e.printStackTrace();
            }
            catch (NoSuchMethodException e)
            {
                e.printStackTrace();
            }
            catch (IllegalArgumentException e)
            {
            }
            catch (IllegalAccessException e)
            {
                e.printStackTrace();
            }
            catch (InvocationTargetException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public void doAction()
    {
        
    }
    
    public String getTimeline()
    {
        return this.id;
    }
}
