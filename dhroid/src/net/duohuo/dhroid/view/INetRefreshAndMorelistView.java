package net.duohuo.dhroid.view;

import net.duohuo.dhroid.R;
import net.duohuo.dhroid.adapter.INetAdapter;
import net.duohuo.dhroid.adapter.INetAdapter.LoadSuccessCallBack;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.ViewUtil;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class INetRefreshAndMorelistView extends ListView implements
		OnScrollListener {
	public final static int STATE_RELEASE_To_REFRESH = 0;

	public final static int STATE_PULL_To_REFRESH = 1;

	public final static int STATE_RELEASE_To_More = 7;

	public final static int STATE_PULL_To_More = 8;

	// 姝ｅ湪鍒锋柊
	public final static int STATE_REFRESHING = 2;

	// 鍒锋柊瀹屾垚
	public final static int STATE_DONE = 3;

	// 鍔犺浇涓�
	public final static int STATE_LOADING = 4;

	// 鍔犺浇鏇村涓�
	public final static int STATE_MORE_LOADING = 5;

	// 鍔犺浇鏇村瀹屾垚
	public final static int STATE_MORE_OK = 6;

	// 瀹為檯鐨刾adding鐨勮窛绂讳笌鐣岄潰涓婂亸绉昏窛绂荤殑姣斾緥
	private final static int RATIO = 3;

	boolean isBack;

	private View headView;

	private View footView;

	private boolean isRecored;

	private int headContentHeight;

	private int startY;

	private int firstItemIndex;

	private int state;

	private OnRefreshListener refreshListener;

	private boolean isRefreshable;

	OnStateChangeListener onStateChangeListener;

	LoadSuccessCallBack moreLoadSuccessCallBack;

	int refreshHeight;

	// 鍓╀笅澶氬皯鍒锋柊
	Integer leaveCount;

	int moreHeight;

	private int footContentHeight;

	private boolean isBottomRecord;

	private boolean isBottom;

	public boolean loadMore = true;

	public void setRefreshHeight(int refreshHeight) {
		this.refreshHeight = refreshHeight;
	}

	// 鏄惁鍦ㄥ姞杞芥洿澶�
	Boolean isLoadingMore = false;

	public INetRefreshAndMorelistView(Context context) {
		super(context);
	}

	public INetRefreshAndMorelistView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// initView(context);
	}

	public void setIsRefreshable(boolean isRefreshable) {
		this.isRefreshable = isRefreshable;
	}

	public void initView(Context context) {
		headView = LayoutInflater.from(context).inflate(
				R.layout.list_refresh_head, null);
		headView.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		headContentHeight = headView.getMeasuredHeight();
		refreshHeight = headContentHeight;
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		addHeaderView(headView, null, false);
		setOnScrollListener(this);
		state = STATE_DONE;
		isRefreshable = true;
	}

	/**
	 * 璁剧疆 RefreshView 闇�鍦ㄥ叾浠朼ddHeadView 涔嬪墠璋冪敤
	 * 
	 * @param headView
	 */
	public void setRefreshView(View headView) {
		this.headView = headView;
		headView.measure(View.MeasureSpec.UNSPECIFIED,
				View.MeasureSpec.UNSPECIFIED);
		headContentHeight = headView.getMeasuredHeight();
		refreshHeight = headContentHeight;
		headView.setPadding(0, -1 * headContentHeight, 0, 0);
		addHeaderView(headView, null, false);
		setOnScrollListener(this);
		state = STATE_DONE;
		isRefreshable = true;
	}

	public void onScroll(AbsListView view, int firstVisiableItem,
			int allVisiable, int allItems) {

		firstItemIndex = firstVisiableItem;
		isBottom = firstItemIndex + allVisiable == allItems;
		if (this.footView != null) {
			synchronized (isLoadingMore) {
				if (allItems - (firstVisiableItem + allVisiable) <= leaveCount
						&& !isLoadingMore) {
					if (this.getAdapter() instanceof HeaderViewListAdapter) {
						HeaderViewListAdapter headadapter = (HeaderViewListAdapter) this
								.getAdapter();
						if (headadapter.getWrappedAdapter() instanceof INetAdapter) {
							INetAdapter trueAdapter = (INetAdapter) headadapter
									.getWrappedAdapter();
							if (trueAdapter.hasMore()) {
								onStateChange(STATE_MORE_LOADING, footView);
								if (moreLoadSuccessCallBack == null) {
									this.moreLoadSuccessCallBack = new LoadSuccessCallBack() {

										public void callBack(Response rs) {
											onStateChange(STATE_MORE_OK,
													footView);
											isLoadingMore = false;
										}
									};
									trueAdapter
											.setOnLoadSuccess(this.moreLoadSuccessCallBack);
								}

								ViewUtil.bindView(
										footView.findViewById(R.id.tips),
										getResources().getString(
												R.string.listview_loading));
								trueAdapter.showNext();
								isLoadingMore = true;
							}
						}
					}
				}
			}
		}
	}

	public void onScrollStateChanged(AbsListView arg0, int arg1) {

	}

	int lastheadContentHeight = 0;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (lastheadContentHeight == headView.getPaddingTop()) {
				if (state != STATE_REFRESHING && state != STATE_LOADING) {
					if (state == STATE_DONE) {
					}
					if (state == STATE_PULL_To_REFRESH) {
						state = STATE_DONE;
						changeHeaderViewByState();
					}
					if (state == STATE_RELEASE_To_REFRESH) {
						state = STATE_REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}
				isRecored = false;
				isBack = false;
			}
			;
			lastheadContentHeight = headView.getPaddingTop();
		}
	};

	public boolean onTouchEvent(MotionEvent event) {
		if (isRefreshable) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (firstItemIndex == 0 && !isRecored) {
					isRecored = true;
					startY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_UP:
				if (state != STATE_REFRESHING && state != STATE_LOADING
						&& state != STATE_MORE_LOADING) {
					if (state == STATE_DONE) {
					}
					if (state == STATE_PULL_To_REFRESH) {
						state = STATE_DONE;
						changeHeaderViewByState();
					}
					if (state == STATE_RELEASE_To_REFRESH) {
						state = STATE_REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
					if (state == STATE_PULL_To_More) {
						state = STATE_MORE_OK;
						changeHeaderViewByState();
					}
					if (state == STATE_RELEASE_To_More) {
						state = STATE_MORE_LOADING;
						changeHeaderViewByState();
						onMore();
					}
				}
				isBottomRecord = false;
				isRecored = false;
				isBack = false;
				break;
			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();
				if (!isRecored && firstItemIndex == 0) {
					isRecored = true;
					startY = tempY;
				}
				if (!isBottomRecord && isBottom) {
					isBottomRecord = true;
					startY = tempY;
				}
				if (state != STATE_REFRESHING && isRecored
						&& state != STATE_LOADING) {
					if (state == STATE_MORE_OK)
						state = STATE_DONE;
					if (state == STATE_RELEASE_To_REFRESH) {
						setSelection(0);
						if (((tempY - startY) / RATIO < refreshHeight)
								&& (tempY - startY) > 0) {
							state = STATE_PULL_To_REFRESH;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = STATE_DONE;
							changeHeaderViewByState();
						}
					}
					if (state == STATE_PULL_To_REFRESH) {
						if ((tempY - startY) / RATIO >= refreshHeight) {
							state = STATE_RELEASE_To_REFRESH;
							isBack = true;
							changeHeaderViewByState();
						} else if (tempY - startY <= 0) {
							state = STATE_DONE;
							changeHeaderViewByState();
						}
					}
					if (state == STATE_DONE) {
						if (tempY - startY > 0) {
							state = STATE_PULL_To_REFRESH;
							changeHeaderViewByState();
						}
					}
					if (state == STATE_PULL_To_REFRESH) {
						headView.setPadding(0, -1 * headContentHeight
								+ (tempY - startY) / RATIO, 0, 0);

					}
					if (state == STATE_RELEASE_To_REFRESH) {
						headView.setPadding(0, (tempY - startY) / RATIO
								- headContentHeight, 0, 0);
					}
				} else if (footView != null && state != STATE_MORE_LOADING
						&& isBottomRecord && state != STATE_LOADING) {
					if (state == STATE_DONE)
						state = STATE_MORE_OK;
					if (state == STATE_RELEASE_To_More) {
						setSelection(getAdapter().getCount() - 1);
						if (((startY - tempY) / RATIO < moreHeight)
								&& (startY - tempY) > 0) {
							state = STATE_PULL_To_More;
							changeHeaderViewByState();
						} else if (startY - tempY <= 0) {
							state = STATE_MORE_OK;
							changeHeaderViewByState();
						}
					}
					if (state == STATE_PULL_To_More) {
						if ((startY - tempY) / RATIO >= moreHeight) {
							state = STATE_RELEASE_To_More;
							isBack = true;
							changeHeaderViewByState();
						} else if (startY - tempY <= 0) {
							state = STATE_MORE_OK;
							changeHeaderViewByState();
						}
					}
					if (state == STATE_MORE_OK) {
						if (startY - tempY > 0) {
							state = STATE_PULL_To_More;
							changeHeaderViewByState();
						}
					}
					if (state == STATE_PULL_To_More) {
						footView.setPadding(0, 0, 0, 1 * footContentHeight
								+ (startY - tempY) / RATIO);

					}
					if (state == STATE_RELEASE_To_More) {
						footView.setPadding(0, 0, 0, 1 * footContentHeight
								+ (startY - tempY) / RATIO);
					}
				}

				// handler.sendEmptyMessageDelayed(0, 2000);
				break;
			}
		}
		super.onTouchEvent(event);
		return true;
	}

	private void changeHeaderViewByState() {
		switch (state) {
		case STATE_RELEASE_To_REFRESH:
			onStateChange(state, headView);
			break;
		case STATE_PULL_To_REFRESH:
			onStateChange(state, headView);
			break;
		case STATE_RELEASE_To_More:
			onStateChange(state, footView);
			break;
		case STATE_PULL_To_More:
			onStateChange(state, footView);
			break;
		// 鍔犺浇涓殑
		case STATE_REFRESHING:
			headView.setPadding(0, -(headContentHeight - refreshHeight), 0, 0);
			onStateChange(state, headView);
			break;
		case STATE_MORE_LOADING:
			footView.setPadding(0, 0, 0, (footContentHeight - moreHeight));
			onStateChange(state, headView);
			break;
		// 瀹屾垚鍔犺浇
		case STATE_DONE:
			onStateChange(state, headView);
			headView.setPadding(0, -1 * headContentHeight, 0, 0);
			break;
		case STATE_MORE_OK:
			onStateChange(state, footView);
			footView.setPadding(0, 0, 0, -1 * footContentHeight);
			break;
		}
	}

	public void onMore() {
		if (this.getRealAdapter() instanceof INetAdapter) {
			INetAdapter trueAdapter = (INetAdapter) this.getRealAdapter();
			trueAdapter.setOnTempLoadSuccess(new LoadSuccessCallBack() {
				public void callBack(Response rss) {
					onMoreComplete();
				}
			});
			if (trueAdapter.hasMore()) {

				if (loadMore) {
					trueAdapter.showNext();
				}
			} else {
				onMoreComplete();
			}
		}
	}

	/**
	 * 鍒锋柊瀹屾垚鏃惰皟鐢�
	 */
	public void onMoreComplete() {
		state = STATE_MORE_OK;
		changeHeaderViewByState();
	}

	/**
	 * 
	 * @param state
	 * @param view
	 *            褰�refresh 鏃秜iew 涓篽eadview 褰�鍔犺浇涓嬮〉鏃�view 涓篽eadView
	 */
	private void onStateChange(int state, View view) {
		if (onStateChangeListener != null) {
			onStateChangeListener.StateChange(state, view);
		}
	}

	public interface OnStateChangeListener {
		/**
		 * 
		 * @param state
		 * @param view
		 *            褰�refresh 鏃秜iew 涓篽eadview 褰�鍔犺浇涓嬮〉鏃�view 涓篽eadView
		 */
		public void StateChange(int state, View view);
	}

	/**
	 * 鑾峰彇瀹為檯鐨刟dapter
	 * 
	 * @return
	 */
	public ListAdapter getRealAdapter() {
		if (this.getAdapter() instanceof HeaderViewListAdapter) {
			HeaderViewListAdapter headadapter = (HeaderViewListAdapter) this
					.getAdapter();
			if (headadapter.getWrappedAdapter() instanceof INetAdapter) {
				ListAdapter adapter = headadapter.getWrappedAdapter();
				return adapter;
			}
		}
		return this.getAdapter();
	}

	/**
	 * 
	 * @param onStateChangeListener
	 */
	public void setOnStateChangeListener(
			OnStateChangeListener onStateChangeListener) {
		this.onStateChangeListener = onStateChangeListener;
	}

	/**
	 * 鍒锋柊瀹屾垚鏃惰皟鐢�
	 */
	public void onRefreshComplete() {
		state = STATE_DONE;
		changeHeaderViewByState();
	}

	public void setonRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
		isRefreshable = true;
	}

	public interface OnRefreshListener {
		public void onRefresh();
	}

	/**
* 
*/
	public void onRefresh() {
		if (this.getAdapter() instanceof HeaderViewListAdapter) {
			HeaderViewListAdapter headadapter = (HeaderViewListAdapter) this
					.getAdapter();
			if (headadapter.getWrappedAdapter() instanceof INetAdapter) {
				INetAdapter trueAdapter = (INetAdapter) headadapter
						.getWrappedAdapter();
				trueAdapter.setOnTempLoadSuccess(new LoadSuccessCallBack() {
					public void callBack(Response rss) {
						onRefreshComplete();
					}
				});
				trueAdapter.refresh();
			}

			if (refreshListener != null) {
				refreshListener.onRefresh();
			}

		} else if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	/**
	 * 璁剧疆鑷姩鍔犺浇鍔熻兘 璋冪敤鍚庤涓嶈addFootView
	 * 
	 * @param footView
	 * @param leaveCount
	 *            鍓╀笅澶氬皯鏃跺紑濮嬭嚜鍔ㄥ姞杞�
	 */
	public void setMoreView(View footView, Integer leaveCount) {
		// footView.measure(View.MeasureSpec.UNSPECIFIED,
		// View.MeasureSpec.UNSPECIFIED);
		// footContentHeight = footView.getMeasuredHeight();
		// moreHeight = footContentHeight;
		// footView.setPadding(0, 0, 0, -1 * footContentHeight);
		this.footView = footView;
		this.leaveCount = leaveCount;
		this.addFooterView(footView);
	}

	public void setAutoLoadCount(Integer leaveCount) {
		this.leaveCount = leaveCount;
	}

	public void setLoadMore(boolean loadMore) {
		this.loadMore = loadMore;
	}

}
