package net.duohuo.dhroid.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.duohuo.dhroid.R;
import net.duohuo.dhroid.adapter.INetAdapter;
import net.duohuo.dhroid.adapter.INetAdapter.LoadSuccessCallBack;
import android.widget.AbsListView.OnScrollListener;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.ioc.IocContainer;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;

public class NetRefreshAndMoreListView extends INetRefreshAndMorelistView
{
    private Context mContext;
    
    SharedPreferences share;
    
    View refreshheadView;
    
    View footView;
    
    int showFootView = View.VISIBLE;
    
    OnEmptyDataListener onEmptyDataListener;
    
    public NetRefreshAndMoreListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        mContext = context;
        if (isInEditMode())
            return;
        share = mContext.getSharedPreferences("refreshlist", mContext.MODE_WORLD_WRITEABLE);
        init();
    }
    
    public INetAdapter getNetAdapter()
    {
        if (getAdapter() instanceof HeaderViewListAdapter)
        {
            HeaderViewListAdapter headadapter = (HeaderViewListAdapter)getAdapter();
            if (headadapter.getWrappedAdapter() instanceof INetAdapter)
            {
                INetAdapter trueAdapter = (INetAdapter)headadapter.getWrappedAdapter();
                return trueAdapter;
            }
        }
        return null;
    }
    
    @Override
    public void setAdapter(ListAdapter adapter)
    {
        super.setAdapter(adapter);
        if (adapter instanceof INetAdapter)
        {
            INetAdapter netAfapter = (INetAdapter)adapter;
            netAfapter.setOnLoadSuccess(new LoadSuccessCallBack()
            {
                @Override
                public void callBack(Response response)
                {
                    if (showFootView == View.VISIBLE)
                    {
                        ViewUtil.bindView(footView.findViewById(R.id.tips),
                            getResources().getString(R.string.listview_to_loadmore));
                        footView.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        BaseAdapter badapter = (BaseAdapter)getNetAdapter();
                        if (badapter.getCount() == 0)
                        {
                            footView.findViewById(R.id.root).setVisibility(View.GONE);
                            if (onEmptyDataListener != null)
                            {
                                onEmptyDataListener.onEmpty(true);
                            }
                        }
                        else
                        {
                            if (onEmptyDataListener != null)
                            {
                                onEmptyDataListener.onEmpty(false);
                            }
                            footView.findViewById(R.id.root).setVisibility(View.VISIBLE);
                        }
                        INetAdapter netAdapter = (INetAdapter)badapter;
                        if (netAdapter != null && !netAdapter.hasMore())
                        {
                            footView.findViewById(R.id.root).setVisibility(View.GONE);
                        }
                    }
                }
            });
        }
        // 设置emptyView
        // ViewGroup group = (ViewGroup)getParent();
        // if (group != null)
        // {
        // View view = group.findViewById(R.id.empty);
        // if (group != null)
        // setEmptyView(view);
        // }
        
    }
    
    public void footViewVisibility(int visibility)
    {
        showFootView = visibility;
        footView.findViewById(R.id.root).setVisibility(visibility);
    }
    
    private void init()
    {
        refreshheadView = LayoutInflater.from(mContext).inflate(R.layout.list_refresh_head, null);
        this.setRefreshView(refreshheadView);
        footView = LayoutInflater.from(mContext).inflate(R.layout.list_more_view, null);
        footView.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                INetAdapter trueAdapter = getNetAdapter();
                if (trueAdapter != null && trueAdapter.hasMore())
                {
                    footView.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                    ViewUtil.bindView(footView.findViewById(R.id.tips),
                        getResources().getString(R.string.listview_loading));
                    trueAdapter.showNext();
                }
                else
                {
                    footView.findViewById(R.id.root).setVisibility(View.GONE);
                    ViewUtil.bindView(footView.findViewById(R.id.tips),
                        getResources().getString(R.string.listview_nomore_data));
                    IocContainer.getShare()
                        .get(IDialog.class)
                        .showToastShort(mContext, getResources().getString(R.string.listview_nomore_data));
                }
            }
        });
        setMoreView(footView, 0);
        // this.addFooterView(footView);
        this.setOnStateChangeListener(new OnStateChangeListener()
        {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void StateChange(int state, View view)
            {
                switch (state)
                {
                    case INetRefreshAndMorelistView.STATE_RELEASE_To_REFRESH:
                    {
                        view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.array_up);
                        animation.setFillAfter(true); // 动画后位置不变
                        view.findViewById(R.id.array).startAnimation(animation);
                        ViewUtil.bindView(view.findViewById(R.id.tips),
                            getResources().getString(R.string.listview_release_to_loading));
                        ViewUtil.bindView(view.findViewById(R.id.content),
                            getString(R.string.listview_last_loading_time, share.getString("lasttime"
                                + getNetAdapter().getTag(), getString(R.string.listview_pulldown_frist_loading))));
                        break;
                    }
                    case INetRefreshAndMorelistView.STATE_PULL_To_REFRESH:
                    {
                        view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.array_down);
                        animation.setFillAfter(true); // 动画后位置不变
                        view.findViewById(R.id.array).startAnimation(animation);
                        ViewUtil.bindView(view.findViewById(R.id.tips), getString(R.string.listview_pulldown_toload));
                        break;
                    }
                    case INetRefreshAndMorelistView.STATE_DONE:
                    {
                        
                        view.findViewById(R.id.progressBar).setVisibility(View.GONE);
                        view.findViewById(R.id.array).setVisibility(View.VISIBLE);
                        SimpleDateFormat format = new SimpleDateFormat(getString(R.string.listview_time_formart));
                        share.edit()
                            .putString("lasttime" + getNetAdapter().getTag(), format.format(new Date()))
                            .commit();
                    }
                    case INetRefreshAndMorelistView.STATE_LOADING:
                    {
                        break;
                    }
                    case INetRefreshAndMorelistView.STATE_REFRESHING:
                    {
                        refreshStartTime = System.currentTimeMillis();
                        // 开始刷新
                        view.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                        view.findViewById(R.id.array).clearAnimation();
                        view.findViewById(R.id.array).setVisibility(View.GONE);
                        
                    }
                    case INetRefreshAndMorelistView.STATE_MORE_LOADING:
                    {
                        ViewUtil.bindView(footView.findViewById(R.id.tips), getString(R.string.listview_loading));
                        break;
                    }
                    case INetRefreshAndMorelistView.STATE_MORE_OK:
                    {
                        ViewUtil.bindView(footView.findViewById(R.id.tips), getString(R.string.listview_loading));
                        break;
                    }
                    default:
                        break;
                }
            }
        });
    }
    
    long refreshStartTime = 0;
    
    public String getString(int res, Object... args)
    {
        return getResources().getString(res, args);
    }
    
    @Override
    public void onRefreshComplete()
    {
        long time = System.currentTimeMillis() - refreshStartTime;
        if (time < 500)
        {
            handler.sendEmptyMessageDelayed(1, 500 - time + 100);
        }
        else
        {
            super.onRefreshComplete();
        }
        
    }
    
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            onRefreshComplete();
        }
    };
    
    public OnEmptyDataListener getOnEmptyDataListener()
    {
        return onEmptyDataListener;
    }
    
    public void setOnEmptyDataListener(OnEmptyDataListener onEmptyDataListener)
    {
        this.onEmptyDataListener = onEmptyDataListener;
    }
    
    /** 没有数据时的回调 */
    public interface OnEmptyDataListener
    {
        public void onEmpty(boolean showeEptyView);
    }
}
