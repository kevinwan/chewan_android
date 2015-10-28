package com.gongpingjia.carplay.activity.dynamic;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.activity.chat.ChatActivity;
import com.gongpingjia.carplay.activity.main.MainActivity2;
import com.gongpingjia.carplay.activity.my.AttentionMeActivity;
import com.gongpingjia.carplay.activity.my.DynamicActivity;
import com.gongpingjia.carplay.activity.my.InterestedPersonActivity;
import com.gongpingjia.carplay.activity.my.MySubscriberActivity2;
import com.gongpingjia.carplay.activity.my.OfficialMessageActivity;
import com.gongpingjia.carplay.activity.my.SubscribeActivity2;
import com.gongpingjia.carplay.activity.my.VisitorsActivity;
import com.gongpingjia.carplay.adapter.FragmentMsgAdapter;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/13.
 */
public class DynamicListFragment extends CarPlayBaseFragment implements PullToRefreshBase.OnRefreshListener<ListView>, ILoadSuccess, View.OnClickListener {

    static DynamicListFragment instance;

    View mainV;

    ListView listV;
    PullToRefreshListView pullToRefreshListView;

    private FragmentMsgAdapter mAdapter;

    List<EMConversation> conversationList = new ArrayList<>();


    private boolean hidden;

    public static DynamicListFragment getInstance() {
        if (instance == null) {
            instance = new DynamicListFragment();
        }

        return instance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mainV = inflater.inflate(R.layout.fragment_dynamiclist, null);
        initView();
        return mainV;
    }

    private void initView() {
        pullToRefreshListView = (PullToRefreshListView) mainV.findViewById(R.id.listview);
        pullToRefreshListView.setOnRefreshListener(this);

        listV = pullToRefreshListView.getRefreshableView();
        mAdapter = new FragmentMsgAdapter(getActivity());
        listV.setAdapter(mAdapter);
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int currentPosition = position - 1;
                int type = mAdapter.getItemViewType(currentPosition);
                EMConversation conversation = mAdapter.getItem(currentPosition);
                String username = conversation.getUserName();
                Intent intent = null;
                switch (type) {
                    //感兴趣的人
                    case 0:
                        intent = new Intent(getActivity(), InterestedPersonActivity.class);
                        break;
                    case 1:
                        //车玩官方
                        if (username.equals("OfficialAdmin")) {
                            intent = new Intent(getActivity(), ChatActivity.class);
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                            intent.putExtra("activityId", "");
                            intent.putExtra("userId", username);
                            //谁关注我
                        } else if (username.equals("SubscribeAdmin")) {
                            intent = new Intent(getActivity(), MySubscriberActivity2.class);
                            //最近访客
                        } else if (username.equals("UserViewAdmin")) {
                            intent = new Intent(getActivity(), VisitorsActivity.class);
                            //活动动态
                        } else if (username.equals("ActivityStateAdmin")) {
                            intent = new Intent(getActivity(), DynamicActivity.class);
                        }
                        break;
                    case 2:
                        intent = new Intent(getActivity(), ChatActivity.class);
                        if (conversation.isGroup()) {
                            // it is group chat
                            EMGroup group = EMGroupManager.getInstance().getGroup(username);
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
                            intent.putExtra("activityId", "");
                            intent.putExtra("groupId", group.getGroupId());
                        } else {
                            intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
                            intent.putExtra("activityId", "");
                            intent.putExtra("userId", username);

                        }
                        break;
                }
                startActivity(intent);
                conversation.resetUnreadMsgCount();
                ((MainActivity2) getActivity()).updateUnreadLabel();
                if (!hidden && !((MainActivity2) getActivity()).isConflict) {
                    ((MainActivity2) getActivity()).updateUnreadLabel();
//        if (!hidden && !((MainActivity) getActivity()).isConflict) {
//            getHeadImg();
//        }
                }
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        conversationList.clear();
        conversationList = loadConversationsWithRecentChat();
        mAdapter.setGroupMessageData(conversationList);
        // 更新消息未读数
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(EMMessage msg) {
        conversationList.clear();
        conversationList = loadConversationsWithRecentChat();
        mAdapter.setGroupMessageData(conversationList);
        // mAdapter.setData(jo);
    }

    private List<EMConversation> loadConversationsWithRecentChat() {
        // 获取所有会话，包括陌生人
        Hashtable<String, EMConversation> conversations = EMChatManager
                .getInstance().getAllConversations();
        // 过滤掉messages size为0的conversation
        /**
         * 如果在排序过程中有新消息收到，lastMsgTime会发生变化 影响排序过程，Collection.sort会产生异常
         * 保证Conversation在Sort过程中最后一条消息的时间不变 避免并发问题
         */
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
//                String username = conversation.getUserName();
//                EMGroup group = EMGroupManager.getInstance().getGroup(username);
                if (conversation.getAllMessages().size() != 0) {
                    // if(conversation.getType() !=
                    // EMConversationType.ChatRoom){
                    sortList.add(new Pair<Long, EMConversation>(conversation
                            .getLastMessage().getMsgTime(), conversation));
                    // }
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        return list;
    }


    /**
     * 根据最后一条消息的时间排序
     *
     * @param conversationList
     */
    private void sortConversationByLastChatTime(
            List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList,
                new Comparator<Pair<Long, EMConversation>>() {
                    @Override
                    public int compare(final Pair<Long, EMConversation> con1,
                                       final Pair<Long, EMConversation> con2) {

                        if (con1.first == con2.first) {
                            return 0;
                        } else if (con2.first > con1.first) {
                            return 1;
                        } else {
                            return -1;
                        }
                    }

                });
    }


    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadSuccessOnFirst() {

    }


    @Override
    public void onClick(View v) {
        Intent it;
        switch (v.getId()) {
            case R.id.avtivity_dynamic:
                it = new Intent(getActivity(), DynamicActivity.class);
                startActivity(it);
                break;
            case R.id.visit:
//                it = new Intent(getActivity(), DynamicActivity.class);
//                startActivity(it);
                break;
            case R.id.attentionme:
                it = new Intent(getActivity(), AttentionMeActivity.class);
                startActivity(it);
                break;
            case R.id.interested_people:
                it = new Intent(getActivity(), InterestedPersonActivity.class);
                startActivity(it);
                break;
            case R.id.official:
                it = new Intent(getActivity(), OfficialMessageActivity.class);
                startActivity(it);
                break;
        }
    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
        conversationList.clear();
        conversationList = loadConversationsWithRecentChat();
        mAdapter.setGroupMessageData(conversationList);
        pullToRefreshListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                pullToRefreshListView.onRefreshComplete();
            }
        }, 1000);

    }


}
