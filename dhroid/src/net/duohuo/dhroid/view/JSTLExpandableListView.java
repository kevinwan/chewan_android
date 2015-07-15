package net.duohuo.dhroid.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

/**
 * 加强板的expandableListView �?��在外面嵌套添加个framelayout
 */
public class JSTLExpandableListView extends ExpandableListView implements OnScrollListener
{
    
    @Override
    public void setAdapter(ExpandableListAdapter adapter)
    {
        super.setAdapter(adapter);
    }
    
    private LinearLayout groupLayout;
    
    public int groupIndex = -1;
    
    FrameLayout fl;
    
    /**
     * @param context
     */
    public JSTLExpandableListView(Context context)
    {
        super(context);
        super.setOnScrollListener(this);
    }
    
    /**
     * @param context
     * @param attrs
     */
    public JSTLExpandableListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        super.setOnScrollListener(this);
    }
    
    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public JSTLExpandableListView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        super.setOnScrollListener(this);
    }
    
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
    {
        int ptp = view.pointToPosition(0, groupLayout != null ? groupLayout.getHeight() : 0);
        if (ptp != AdapterView.INVALID_POSITION)
        {
            JSTLExpandableListView qExlist = (JSTLExpandableListView)view;
            long pos = qExlist.getExpandableListPosition(ptp);
            int groupPos = ExpandableListView.getPackedPositionGroup(pos);
            int childPos = ExpandableListView.getPackedPositionChild(pos);
            if (childPos < 0)
            {
                groupPos = -1;
            }
            if (groupPos < groupIndex)
            {
                groupIndex = groupPos;
                if (groupLayout != null)
                {
                    groupLayout.removeAllViews();
                    groupLayout.setVisibility(GONE);
                    // groupLayout.getBackground().setAlpha(100);
                }
            }
            else if (groupPos > groupIndex)
            {
                fl = (FrameLayout)getParent();
                groupIndex = groupPos;
                if (groupLayout != null)
                    fl.removeView(groupLayout);
                
                groupLayout = (LinearLayout)getExpandableListAdapter().getGroupView(groupPos, true, null, null);
                groupLayout.setOnClickListener(new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        collapseGroup(groupIndex);
                    }
                });
                fl.addView(groupLayout, fl.getChildCount(), new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            }
        }
    }
    
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
    }
    
    public void removeHead()
    {
        if (groupLayout != null)
        {
            groupLayout.removeAllViews();
        }
    }
    
}
