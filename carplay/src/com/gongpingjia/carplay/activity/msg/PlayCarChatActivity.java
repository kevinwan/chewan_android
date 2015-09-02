package com.gongpingjia.carplay.activity.msg;

import java.util.ArrayList;
import java.util.List;

import net.duohuo.dhroid.adapter.FieldMap;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.adapter.PSAdapter;
import net.duohuo.dhroid.adapter.ValueFix;
import net.duohuo.dhroid.view.NetRefreshAndMoreListView;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.api.API;
import com.gongpingjia.carplay.bean.PlayCarBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 玩转车聊页面 hqh
 */
public class PlayCarChatActivity extends CarPlayBaseActivity {
	// PSAdapter adapter;
	ListView listView;
	List<PlayCarBean> list;

	MyAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_car_chat);

	}

	@Override
	public void initView() {

		setTitle("玩转车玩");

		listView = (ListView) findViewById(R.id.listview);
		adapter = new MyAdapter();
		listView.setOnItemClickListener(new MyItme());
		// adapter = new PSAdapter(self, R.layout.item_carchat_list);
		// adapter.addField("title", R.id.text_title);
		// adapter.addField("content", R.id.content, "content");
		//
		// PlayCarBean carBean = new PlayCarBean();
		// carBean.setContent("爱上点击");
		// carBean.setTitle("阿三哥");

		list = new ArrayList<PlayCarBean>();
		// list.add(carBean);
		// adapter.addAll(list);

		listView.setAdapter(adapter);
		dataInit();

	}

	public class MyItme implements AdapterView.OnItemClickListener {
		Intent intent = new Intent();

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			switch (arg2) {
			case 0:
				String deta = "\n"
						+ "车玩是所有车友互助交流的公共平台，我们来自不同的城市，成长在不同的年代，喜爱不同的文化。在车玩，都应本着善意的态度参与讨论交流。"
						+ "\n" + "\n"
						+ "首先，车玩对以下发帖内容严惩不贷。视情况予以移帖、删帖、关小黑屋、封号、封设备等处理。" + "\n"
						+ "\n" + "1.骚扰、辱骂、歧视等不尊重其他车友的行为；" + "\n"
						+ "2.重复提交低质、同质内容的刷帖行为；" + "\n" + "3.软文、广告、微商；" + "\n"
						+ "4.小黄文、小黄图，除车模外以女性身体为主题的图片；" + "\n"
						+ "5.其他会引起车友不适或违反国家法律法规的内容。" + "\n" + "\n"
						+ "其次，车玩鼓励原创，请车友尊重各车玩会的主题。与各车玩会主题无关的非原创帖，将视内容移往各会闲聊区。"
						+ "\n" + "\n" + "管理员和车玩会会长不定时对帖子进行维护操作，如有疑义，请及时联系沟通。"
						+ "\n" + "\n" ;
				intent.putExtra("detail", deta);
				intent.putExtra("title", "发布活动指南");

				intent.setClass(self, PlayCarcDetailActivity.class);
				startActivity(intent);
				break;
			case 1:
				String str = "\n" + "认证标识" + "\n" + "\n"
						+ "认证车主头像上会显示车型标志图标，认证车主可以点亮爱车车标" + "\n" + "\n" + "\n"
						+ "车主特权" + "\n" + "\n"
						+ "认证车主发布活动可以更容易吸引别人的参加，认证车主以后更容易吸引别人注意" + "\n" + "\n"
						+ "\n" + "更多特权" + "\n" + "\n" + "后续版本将不断增加认证车主特权";
				intent.putExtra("detail", str);
				intent.putExtra("title", "认证车主申请");
				intent.setClass(self, PlayCarcDetailActivity.class);
				startActivity(intent);
				break;
			case 2:
				String three = "\n" + "活动分车友活动和车玩会活动；" + "\n" + "\n"
						+ "所有用户都可以发起车友活动，每个用户最多只能发起三个未过期的活动，未指定" + "\n" + "\n"
						+ "活动时间的活动，将在15天后自动结束；" + "\n" + "\n"
						+ "车主和非车主都可以发布活动；" + "\n" + "\n" + "活动发起者拥有活动管理权限；";
				intent.putExtra("detail", three);
				intent.putExtra("title", "活动规则");
				intent.setClass(self, PlayCarcDetailActivity.class);
				startActivity(intent);
				break;
			case 3:
				String four = "\n" + "发起活动" + "\n" + "\n"
						+ "每个用户最多只能发起三个未过期的活动" + "\n" + "\n" + "\n" + "编辑活动"
						+ "\n" + "\n" + "活动开始和结束时间， 活动地点，标题，添加删除图片" + "\n"
						+ "\n" + "\n" + "邀请车友" + "\n" + "\n" + "车主可以邀请微信好友参与"
						+ "\n" + "\n" + "\n" + "管理活动及群聊" + "\n" + "\n"
						+ "可以删除未开始的活动；移除活动和群聊的参与者";
				intent.putExtra("detail", four);
				intent.putExtra("title", "活动发起者权限");
				intent.setClass(self, PlayCarcDetailActivity.class);
				startActivity(intent);
				break;
			case 4:
				String fives = "\n"
						+ "随着车玩的规模的壮大，车玩开始出现不少垃圾信息，并有爆发式增长的趋势，严重影响了大伙儿的使用和社区的发展。"
						+ "\n"
						+ "\n"
						+ "垃圾信息是指：以盈利为目的，发布影响用户体验、扰乱车玩秩序的信息的行为。过去1年多，车玩上出现了“找小姐”、“刷淘宝”、“卖动作片”、“微商”等多种垃圾信息。 "
						+ "\n"
						+ "\n"
						+ "感谢通过举报、私信等各种形式帮助我们的车友，举报可以让垃圾信息更快的处理，直接减少其他车友被骚扰的情况。";
				intent.putExtra("detail", fives);
				intent.putExtra("title", "车玩举报指南");
				intent.setClass(self, PlayCarcDetailActivity.class);
				startActivity(intent);
				break;
			case 5:
				String six = "\n" + "车友个人主页右上角（针对使用不良信息作为头像的行为）;" + "\n" + "\n"
						+ "活动“举报”按钮（针对公开发布垃圾信息的行为）;" + "\n" + "\n"
						+ "群聊及私聊状态，长按聊天内容，会弹出“举报”按钮（针对私下发布垃圾信息的行为）;" + "\n"
						+ "\n" + "同时，我们诚挚期待有更多车友加入车玩反垃圾小组，帮助我们做的更好。";
				intent.putExtra("detail", six);
				intent.putExtra("title", "车玩举报入口");
				intent.setClass(self, PlayCarcDetailActivity.class);
				startActivity(intent);
				break;

			default:
				break;
			}

		}

	}

	public void dataInit() {
		PlayCarBean carBean = new PlayCarBean();
		carBean.setTitle("1.发表活动指南");
		list.add(carBean);

		PlayCarBean carBean_two = new PlayCarBean();
		carBean_two.setTitle("2.认证车主申请");
		list.add(carBean_two);

		PlayCarBean carBean_three = new PlayCarBean();
		carBean_three.setTitle("3.活动规则");
		list.add(carBean_three);

		PlayCarBean carBean_four = new PlayCarBean();
		carBean_four.setTitle("4.活动发起权限");
		list.add(carBean_four);

		PlayCarBean carBean_fives = new PlayCarBean();
		carBean_fives.setTitle("5.车玩举报指南");
		list.add(carBean_fives);

		PlayCarBean carBean_six = new PlayCarBean();
		carBean_six.setTitle("6.车玩举报入口");
		list.add(carBean_six);

	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			ViewHolder viewHolder = null;
			if (arg1 == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater
						.from(PlayCarChatActivity.this);
				arg1 = inflater.inflate(R.layout.item_carchat_list, null);
				viewHolder.title = (TextView) arg1
						.findViewById(R.id.text_title);

				arg1.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) arg1.getTag();
			}
			viewHolder.title.setText(list.get(arg0).getTitle());

			return arg1;
		}

		class ViewHolder {
			TextView title;
		}

	}

}
