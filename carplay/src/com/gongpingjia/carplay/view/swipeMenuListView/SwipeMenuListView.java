package com.gongpingjia.carplay.view.swipeMenuListView;

import java.util.Date;

import net.duohuo.dhroid.R;
import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * 
 * @author baoyz
 * @date 2014-8-18
 * 
 */
public class SwipeMenuListView extends ListView implements OnScrollListener
{
    
    private static final int TOUCH_STATE_NONE = 0;
    
    private static final int TOUCH_STATE_X = 1;
    
    private static final int TOUCH_STATE_Y = 2;
    
    private int MAX_Y = 5;
    
    private int MAX_X = 3;
    
    private float mDownX;
    
    private float mDownY;
    
    private int mTouchState;
    
    private int mTouchPosition;
    
    private SwipeMenuLayout mTouchView;
    
    private OnSwipeListener mOnSwipeListener;
    
    private SwipeMenuCreator mMenuCreator;
    
    private OnMenuItemClickListener mOnMenuItemClickListener;
    
    private Interpolator mCloseInterpolator;
    
    private Interpolator mOpenInterpolator;
    
    private final static int RELEASE_To_REFRESH = 0;
    
    private final static int PULL_To_REFRESH = 1;
    
    private final static int REFRESHING = 2;
    
    private final static int DONE = 3;
    
    private LayoutInflater inflater;
    
    private LinearLayout headView; // 头部
    
    private TextView tipsTextview;// 下拉刷新
    
    private TextView lastUpdatedTextView;// 最新更新
    
    private ImageView arrowImageView;// 箭头
    
    private ProgressBar progressBar;// 刷新进度条
    
    private RotateAnimation animation;// 旋转特效 刷新中箭头翻转 向下变向上
    
    private RotateAnimation reverseAnimation;
    
    // 用于保证startY的值在一个完整的touch事件中只被记录一次
    private boolean isRecored;
    
    private int headContentWidth;// 头部宽度
    
    private int headContentHeight;// 头部高度
    
    private int startY;// 高度起始位置，用来记录与头部距离
    
    private int firstItemIndex;// 列表中首行索引，用来记录其与头部距离
    
    private int state;// 下拉刷新中、松开刷新中、正在刷新中、完成刷新
    
    private boolean isBack;
    
    public OnRefreshListener refreshListener;// 刷新监听
    
    private final static String TAG = "abc";
    
    private View footView;
    
    OnLoadaMoreListener onLoadaMoreListener;
    
    public SwipeMenuListView(Context context)
    {
        super(context);
        init(context);
    }
    
    public SwipeMenuListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        init(context);
    }
    
    public SwipeMenuListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(context);
    }
    
    private void init(Context context)
    {
        MAX_X = dp2px(MAX_X);
        MAX_Y = dp2px(MAX_Y);
        mTouchState = TOUCH_STATE_NONE;
        
        inflater = LayoutInflater.from(context);
        
        headView = (LinearLayout)inflater.inflate(R.layout.list_refresh_head, null);// listview拼接headview
        footView = inflater.inflate(R.layout.list_more_view, null);
        footView.setVisibility(View.GONE);
        
        footView.setOnClickListener(new OnClickListener()
        {
            
            @Override
            public void onClick(View v)
            {
                if (onLoadaMoreListener != null)
                {
                    onLoadaMoreListener.onLoadMore();
                }
            }
        });
        
        arrowImageView = (ImageView)headView.findViewById(R.id.array);// headview中各view
        arrowImageView.setMinimumWidth(50);
        arrowImageView.setMinimumHeight(50);
        progressBar = (ProgressBar)headView.findViewById(R.id.progressBar);// headview中各view
        tipsTextview = (TextView)headView.findViewById(R.id.tips);// headview中各view
        lastUpdatedTextView = (TextView)headView.findViewById(R.id.content);// headview中各view
        
        measureView(headView);
        headContentHeight = headView.getMeasuredHeight();// 头部高度
        headContentWidth = headView.getMeasuredWidth();// 头部宽度
        
        headView.setPadding(0, -1 * headContentHeight, 0, 0);// setPadding(int left, int top, int right, int bottom)
        headView.invalidate();// Invalidate the whole view
        
        addHeaderView(headView);// 添加进headview
        addFooterView(footView);
        
        setOnScrollListener(this);// 滚动监听
        
        animation =
            new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);// 特效animation设置
        
        reverseAnimation =
            new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(250);
        reverseAnimation.setFillAfter(true);// 特效reverseAnimation设置
    }
    
    public void onScroll(AbsListView arg0, int firstVisiableItem, int arg2,// 滚动事件
        int arg3)
    {
        firstItemIndex = firstVisiableItem;// 得到首item索引
    }
    
    public void onScrollStateChanged(AbsListView arg0, int arg1)
    {
    }
    
    @Override
    public void setAdapter(ListAdapter adapter)
    {
        super.setAdapter(new SwipeMenuAdapter(getContext(), adapter)
        {
            @Override
            public void createMenu(SwipeMenu menu)
            {
                if (mMenuCreator != null)
                {
                    mMenuCreator.create(menu);
                }
            }
            
            @Override
            public void onItemClick(SwipeMenuView view, SwipeMenu menu, int index)
            {
                if (mOnMenuItemClickListener != null)
                {
                    mOnMenuItemClickListener.onMenuItemClick(view.getPosition(), menu, index);
                }
                if (mTouchView != null)
                {
                    mTouchView.smoothCloseMenu();
                }
            }
        });
    }
    
    public void setCloseInterpolator(Interpolator interpolator)
    {
        mCloseInterpolator = interpolator;
    }
    
    public void setOpenInterpolator(Interpolator interpolator)
    {
        mOpenInterpolator = interpolator;
    }
    
    public Interpolator getOpenInterpolator()
    {
        return mOpenInterpolator;
    }
    
    public Interpolator getCloseInterpolator()
    {
        return mCloseInterpolator;
    }
    
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev)
    {
        return super.onInterceptTouchEvent(ev);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev)
    {
        if (ev.getAction() != MotionEvent.ACTION_DOWN && mTouchView == null)
            return super.onTouchEvent(ev);
        int action = MotionEventCompat.getActionMasked(ev);
        action = ev.getAction();
        switch (action)
        {
            case MotionEvent.ACTION_DOWN:
                if (firstItemIndex == 0 && !isRecored)
                {// 如果首item索引为0，且尚未记录startY,则在下拉时记录之，并执行isRecored = true;
                    startY = (int)ev.getY();
                    isRecored = true;
                    
                    // Log.v(TAG, "在down时候记录当前位置‘");
                }
                
                int oldPos = mTouchPosition;
                mDownX = ev.getX();
                mDownY = ev.getY();
                mTouchState = TOUCH_STATE_NONE;
                
                mTouchPosition = pointToPosition((int)ev.getX(), (int)ev.getY());
                
                if (mTouchPosition == oldPos && mTouchView != null && mTouchView.isOpen())
                {
                    mTouchState = TOUCH_STATE_X;
                    mTouchView.onSwipe(ev);
                    return true;
                }
                
                View view = getChildAt(mTouchPosition - getFirstVisiblePosition());
                
                if (mTouchView != null && mTouchView.isOpen())
                {
                    mTouchView.smoothCloseMenu();
                    mTouchView = null;
                    return super.onTouchEvent(ev);
                }
                if (view instanceof SwipeMenuLayout)
                {
                    mTouchView = (SwipeMenuLayout)view;
                }
                if (mTouchView != null)
                {
                    mTouchView.onSwipe(ev);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int tempY = (int)ev.getY();
                if (!isRecored && firstItemIndex == 0)
                {// 如果首item索引为0，且尚未记录startY,则在拖动时记录之，并执行isRecored = true;
                 // Log.v(TAG, "在move时候记录下位置");
                    isRecored = true;
                    startY = tempY;
                }
                if (state != REFRESHING && isRecored)
                {// 如果状态不是正在刷新，且已记录startY：tempY为拖动过程中一直在变的高度，startY为拖动起始高度
                 // 可以松手去刷新了
                    if (state == RELEASE_To_REFRESH)
                    {// 如果状态是松开刷新
                     // 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
                        if ((tempY - startY < headContentHeight)// 如果实时高度大于起始高度，且两者之差小于头部高度，则状态设为下拉刷新
                            && (tempY - startY) > 0)
                        {
                            state = PULL_To_REFRESH;
                            changeHeaderViewByState();
                            
                            // Log.v(TAG, "由松开刷新状态转变到下拉刷新状态");
                        }
                        // 一下子推到顶了
                        else if (tempY - startY <= 0)
                        {// 如果实时高度小于等于起始高度了，则说明到顶了，状态设为完成刷新
                            state = DONE;
                            changeHeaderViewByState();
                            
                            // Log.v(TAG, "由松开刷新状态转变到done状态");
                        }
                        // 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
                        else
                        {// 如果当前拖动过程中既没有到下拉刷新的地步，也没有到完成刷新（到顶）的地步，则保持松开刷新状态
                         // 不用进行特别的操作，只用更新paddingTop的值就行了
                        }
                    }
                    // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                    if (state == PULL_To_REFRESH)
                    {// 如果状态是下拉刷新
                     // 下拉到可以进入RELEASE_TO_REFRESH的状态
                        if (tempY - startY >= headContentHeight)
                        {// 如果实时高度与起始高度之差大于等于头部高度，则状态设为松开刷新
                            state = RELEASE_To_REFRESH;
                            isBack = true;
                            changeHeaderViewByState();
                            
                            // Log.v(TAG, "由done或者下拉刷新状态转变到松开刷新");
                        }
                        // 上推到顶了
                        else if (tempY - startY <= 0)
                        {// 如果实时高度小于等于起始高度了，则说明到顶了，状态设为完成刷新
                            state = DONE;
                            changeHeaderViewByState();
                            
                            // Log.v(TAG, "由DOne或者下拉刷新状态转变到done状态");
                        }
                    }
                    
                    // done状态下
                    if (state == DONE)
                    {// 如果状态是完成刷新
                        if (tempY - startY > 0)
                        {// 如果实时高度大于起始高度了，则状态设为下拉刷新
                            state = PULL_To_REFRESH;
                            changeHeaderViewByState();
                        }
                    }
                    
                    // 更新headView的size
                    if (state == PULL_To_REFRESH)
                    {// 如果状态是下拉刷新，更新headview的size ?
                        headView.setPadding(0, -1 * headContentHeight + (tempY - startY), 0, 0);
                        headView.invalidate();
                    }
                    
                    // 更新headView的paddingTop
                    if (state == RELEASE_To_REFRESH)
                    {// 如果状态是松开刷新，更新 headview的paddingtop ?
                        headView.setPadding(0, tempY - startY - headContentHeight, 0, 0);
                        headView.invalidate();
                    }
                }
                
                float dy = Math.abs((ev.getY() - mDownY));
                float dx = Math.abs((ev.getX() - mDownX));
                if (mTouchState == TOUCH_STATE_X)
                {
                    if (mTouchView != null)
                    {
                        mTouchView.onSwipe(ev);
                    }
                    getSelector().setState(new int[] {0});
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                else if (mTouchState == TOUCH_STATE_NONE)
                {
                    if (Math.abs(dy) > MAX_Y)
                    {
                        mTouchState = TOUCH_STATE_Y;
                    }
                    else if (dx > MAX_X)
                    {
                        mTouchState = TOUCH_STATE_X;
                        if (mOnSwipeListener != null)
                        {
                            mOnSwipeListener.onSwipeStart(mTouchPosition);
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (state != REFRESHING)
                {// 手松开有4个状态：下拉刷新、松开刷新、正在刷新、完成刷新。如果当前不是正在刷新
                    if (state == DONE)
                    {// 如果当前是完成刷新，什么都不做
                    }
                    if (state == PULL_To_REFRESH)
                    {// 如果当前是下拉刷新，状态设为完成刷新（意即下拉刷新中就松开了，实际未完成刷新），执行changeHeaderViewByState()
                        state = DONE;
                        changeHeaderViewByState();
                        
                        // Log.v(TAG, "由下拉刷新状态，到done状态");
                    }
                    if (state == RELEASE_To_REFRESH)
                    {// 如果当前是松开刷新，状态设为正在刷新（意即松开刷新中松开手，才是真正地刷新），执行changeHeaderViewByState()
                        state = REFRESHING;
                        changeHeaderViewByState();
                        onRefresh();// 真正刷新，所以执行onrefresh，执行后状态设为完成刷新
                        
                        // Log.v(TAG, "由松开刷新状态，到done状态");
                    }
                }
                
                isRecored = false;// 手松开，则无论怎样，可以重新记录startY,因为只要手松开就认为一次刷新已完成
                isBack = false;
                
                if (mTouchState == TOUCH_STATE_X)
                {
                    if (mTouchView != null)
                    {
                        mTouchView.onSwipe(ev);
                        if (!mTouchView.isOpen())
                        {
                            mTouchPosition = -1;
                            mTouchView = null;
                        }
                    }
                    if (mOnSwipeListener != null)
                    {
                        mOnSwipeListener.onSwipeEnd(mTouchPosition);
                    }
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(ev);
                    return true;
                }
                break;
        
        }
        return super.onTouchEvent(ev);
    }
    
    public void smoothOpenMenu(int position)
    {
        if (position >= getFirstVisiblePosition() && position <= getLastVisiblePosition())
        {
            View view = getChildAt(position - getFirstVisiblePosition());
            if (view instanceof SwipeMenuLayout)
            {
                mTouchPosition = position;
                if (mTouchView != null && mTouchView.isOpen())
                {
                    mTouchView.smoothCloseMenu();
                }
                mTouchView = (SwipeMenuLayout)view;
                mTouchView.smoothOpenMenu();
            }
        }
    }
    
    private int dp2px(int dp)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources()
            .getDisplayMetrics());
    }
    
    public void setMenuCreator(SwipeMenuCreator menuCreator)
    {
        this.mMenuCreator = menuCreator;
    }
    
    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener)
    {
        this.mOnMenuItemClickListener = onMenuItemClickListener;
    }
    
    public void setOnSwipeListener(OnSwipeListener onSwipeListener)
    {
        this.mOnSwipeListener = onSwipeListener;
    }
    
    public static interface OnMenuItemClickListener
    {
        void onMenuItemClick(int position, SwipeMenu menu, int index);
    }
    
    public static interface OnSwipeListener
    {
        void onSwipeStart(int position);
        
        void onSwipeEnd(int position);
    }
    
    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState()
    {
        switch (state)
        {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                
                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);
                
                tipsTextview.setText("松开刷新");
                
                // Log.v(TAG, "当前状态，松开刷新");
                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack)
                {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);
                    
                    tipsTextview.setText("下拉刷新");
                }
                else
                {
                    tipsTextview.setText("下拉刷新");
                }
                // Log.v(TAG, "当前状态，下拉刷新");
                break;
            
            case REFRESHING:
                
                headView.setPadding(0, 0, 0, 0);
                headView.invalidate();
                
                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText("正在刷新...");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                
                // Log.v(TAG, "当前状态,正在刷新...");
                break;
            case DONE:
                headView.setPadding(0, -1 * headContentHeight, 0, 0);
                headView.invalidate();
                
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.xlistview_arrow);
                tipsTextview.setText("下拉刷新");
                lastUpdatedTextView.setVisibility(View.VISIBLE);
                
                // Log.v(TAG, "当前状态，done");
                break;
        }
    }
    
    public void setonRefreshListener(OnRefreshListener refreshListener)
    {
        this.refreshListener = refreshListener;
    }
    
    public interface OnRefreshListener
    {
        public void onRefresh();
    }
    
    public OnLoadaMoreListener getOnLoadaMoreListener()
    {
        return onLoadaMoreListener;
    }
    
    public void setOnLoadaMoreListener(OnLoadaMoreListener onLoadaMoreListener)
    {
        this.onLoadaMoreListener = onLoadaMoreListener;
    }
    
    public interface OnLoadaMoreListener
    {
        public void onLoadMore();
    }
    
    public void onRefreshComplete()
    {
        state = DONE;
        lastUpdatedTextView.setText("最近更新:" + DateFormat.format("yyyy-MM-dd hh:mm:ss", new Date()));// 刷新完成时，头部提醒的刷新日期
        changeHeaderViewByState();
        footView.setVisibility(View.VISIBLE);
    }
    
    private void onRefresh()
    {
        if (refreshListener != null)
        {
            refreshListener.onRefresh();
        }
    }
    
    public void removeFootView()
    {
        footView.setVisibility(View.GONE);
    }
    
    // 此方法直接照搬自网络上的一个下拉刷新的demo，此处是“估计”headView的width以及height
    private void measureView(View child)
    {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null)
        {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0)
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        }
        else
        {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }
}
