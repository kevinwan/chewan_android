package com.gongpingjia.carplay.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.gongpingjia.carplay.R;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

/**
 * Created by Administrator on 2015/10/15.
 */
public class PullToRefreshRecyclerViewVertical extends PullToRefreshBase<RecyclerViewPager> {
    LinearLayoutManager layout;


    public void setOnPageChange(OnPageChange onPageChange) {
        this.onPageChange = onPageChange;
    }

    OnPageChange onPageChange;


    public PullToRefreshRecyclerViewVertical(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerViewVertical(Context context, AttributeSet attrs) {
        super(context, attrs);


    }

    @Override
    public final Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    private RecyclerViewPager recyclerView;
    private boolean isScrollOnHeader = true;
    public boolean isScrollOnFooter = false;

    @Override
    protected RecyclerViewPager createRefreshableView(Context context, AttributeSet attrs) {
        recyclerView = new RecyclerViewPager(context, attrs);
        recyclerView.setId(R.id.RecyclerViewPager);
        layout = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setTriggerOffset(0.15f);
        recyclerView.setFlingFactor(0.25f);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLongClickable(true);

        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            Integer lastVisibleItem;
            Integer fistVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (null != fistVisibleItem) {
                    isScrollOnHeader = 0 == fistVisibleItem;
                } else {
                    isScrollOnHeader = true;
                }

                if (null != lastVisibleItem) {
                    boolean isLast = layout.getItemCount() - 1 == lastVisibleItem || layout.getItemCount() == lastVisibleItem;
                    isScrollOnFooter = newState == RecyclerView.SCROLL_STATE_IDLE && isLast;
                } else {
                    isScrollOnFooter = true;
                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                fistVisibleItem = layout.findFirstVisibleItemPosition();
                lastVisibleItem = layout.findLastVisibleItemPosition();
                int childCount = recyclerView.getChildCount();
                if (childCount == 0) {
                    return;
                }
                int height = recyclerView.getChildAt(0).getHeight();
                int padding = (recyclerView.getHeight() - height) / 2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);

                    if (onPageChange != null) {
                        onPageChange.change(v);
                    }
                    //往左 从 padding 到 -(v.getWidth()-padding) 的过程中，由大到小
                    float rate = 0;
                    ;
                    if (v.getTop() <= padding) {
                        if (v.getTop() >= padding - v.getHeight()) {
                            rate = (padding - v.getTop()) * 1f / v.getHeight();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                        v.setScaleX(1 - rate * 0.1f);

                    } else {
                        //往右 从 padding 到 recyclerView.getWidth()-padding 的过程中，由大到小
                        if (v.getTop() <= recyclerView.getHeight() - padding) {
                            rate = (recyclerView.getHeight() - padding - v.getTop()) * 1f / v.getHeight();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                        v.setScaleX(0.9f + rate * 0.1f);
                    }
                }


//                mPositionText.setText("First: " + mRecyclerView.getFirstVisiblePosition());
            }
        });

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (recyclerView.getChildCount() < 3) {
                    if (recyclerView.getChildAt(1) != null) {
                        if (recyclerView.getCurrentPosition() == 0) {
                            View v1 = recyclerView.getChildAt(1);
                            v1.setScaleY(0.9f);
                            v1.setScaleX(0.9f);
                            if (onPageChange != null) {
                                onPageChange.change(v);
                            }
                        }
                    }
                } else {
                    if (recyclerView.getChildAt(0) != null) {
                        View v0 = recyclerView.getChildAt(0);
                        v0.setScaleY(0.9f);
                        v0.setScaleX(0.9f);
                    }
                    if (recyclerView.getChildAt(2) != null) {
                        View v2 = recyclerView.getChildAt(2);
                        v2.setScaleY(0.9f);
                        v2.setScaleX(0.9f);
                    }
                }

            }
        });


        return recyclerView;
    }


    @Override
    protected boolean isReadyForPullStart() {
        return isScrollOnHeader;
    }

    @Override
    protected boolean isReadyForPullEnd() {

        return isScrollOnFooter;
    }

    public interface OnPageChange {
        void change(View currentview);
    }


}

