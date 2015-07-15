package net.duohuo.dhroid.bean;
import java.util.ArrayList;
import java.util.List;
import net.duohuo.dhroid.Const;
import net.duohuo.dhroid.dialog.IDialog;
import net.duohuo.dhroid.net.DhNet;
import net.duohuo.dhroid.net.NetTask;
import net.duohuo.dhroid.net.Response;
import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
public class Store<T extends Entity<T>> {
	public List<Entity<T>> items;
	Context mContext;
	public DhNet dhnet;
	private int pageNo = 0;
	private int step = Const.netadapter_step_default;
	private boolean hasMore = true;
	public Integer total = 0;
	IDialog dialoger;
	public String pageParams = Const.netadapter_page_no;
	public String stepParams = Const.netadapter_step;
	Boolean isLoading = false;
	private String timelineParam = Const.netadapter_timeline;
	Class tCalzz;
	private final DataSetObservable mDataSetObservable = new DataSetObservable();
	private final Object mLock = new Object();
	
	public String getApi() {
		return "";
	}

	public Store() {
		super();
		dhnet = new DhNet(getApi());
		dhnet.setMethod(DhNet.METHOD_GET);
		items=new ArrayList<Entity<T>>();
	}

	public DhNet addParam(String key, Object value) {
		return dhnet.addParam(key, value);
	}

	NetTask nettask = new NetTask(mContext) {
		@SuppressWarnings("unchecked")
		@Override
		public void doInBackground(Response response) {
			super.doInBackground(response);
			List<Entity<T>> list=response.listFromData(tCalzz);
			response.addBundle("list", list);
		}
		@Override
		public void onErray(Response response) {
			super.onErray(response);

		}

		@Override
		public void onCancelled() {
			super.onCancelled();

		}
		@Override
		public void doInUI(Response response, Integer transfer) {
			List<Entity<T>> list = response.getBundle("list");
			if (list.size() == 0) {
				hasMore = false;
			}
			if (pageNo == 1) {
				clear();
			}
			addAll(list);
		}
	};
	
	public void clear() {
		synchronized (mLock) {
			items.clear();
		}
		notifyDataSetChanged();
	}

	public void addAll(List<Entity<T>> list){
		this.items.addAll(items);
		notifyDataSetChanged();
	}
	public void insert(int location,Entity<T> entity){
		this.items.add(location, entity);
		notifyDataSetChanged();
	}
	
	public void add(Entity<T> entity){
		this.items.add(entity);
		notifyDataSetChanged();
	}
	
	
	
	/**
	 * 
	 */
	public void next(boolean dialog) {
		synchronized (isLoading) {
			if (isLoading)
				return;
			isLoading = true;
		}
		pageNo++;
		dhnet.addParam(pageParams, pageNo);
		dhnet.addParam(stepParams, step);
		Entity<T> entity=getLastItem();
		if(entity!=null){
			dhnet.addParam(timelineParam, entity.getTimeline());
		}
		dhnet.execuse(nettask);
	}
	
	
	public Entity<T> getItem(int index){
		if(0<index&&index<items.size()){
			return items.get(index);
		}
		return null;
	}
	
	/**
	 * 获取最后一条
	 * @return
	 */
	public Entity<T> getLastItem(){
		return getItem(items.size()-1);
	}
	/**
	 * 刷新
	 */
	public void reload(){
		pageNo=0;
		next(false);	
	}
	
	/**
	 * 通知数据修改
	 */
	public void notifyDataSetChanged(){
		mDataSetObservable.notifyChanged();
	}
	/**
	 * 注册数据观察者
	 * @param observer
	 */
	public void registerDataSetObserver(DataSetObserver observer){
		mDataSetObservable.registerObserver(observer);
	}
	
}
