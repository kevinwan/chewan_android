package com.gongpingjia.carplay.activity.dynamic;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.gongpingjia.carplay.ILoadSuccess;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseFragment;
import com.gongpingjia.carplay.activity.main.MainActivity;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import net.duohuo.dhroid.adapter.PSAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Administrator on 2015/10/13.
 */
public class DynamicListFragment extends CarPlayBaseFragment implements PullToRefreshBase.OnRefreshListener<RecyclerViewPager>, ILoadSuccess {

    static DynamicListFragment instance;

    View mainV;

    ListView listV;

    PSAdapter adapter;

    View headV;
    List<EMConversation> conversationList = new ArrayList<EMConversation>();

    private boolean hidden;

    public static DynamicListFragment getInstance() {
        if (instance == null) {
            instance = new DynamicListFragment();
        }

        return instance;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mainV = inflater.inflate(R.layout.fragment_dynamiclist, null);
        initView();
        return mainV;
    }

    private void initView() {
        PullToRefreshListView pullToRefreshListView = (PullToRefreshListView) mainV.findViewById(R.id.listview);
        headV = LayoutInflater.from(getActivity()).inflate(R.layout.head_dynamic, null);
        listV = pullToRefreshListView.getRefreshableView();
        listV.addHeaderView(headV);
        adapter = new PSAdapter(getActivity(), R.layout.item_group_message2);
        listV.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();

        conversationList.clear();
        conversationList = loadConversationsWithRecentChat();
        adapter.clear();
        adapter.addAll(conversationList);
        // 更新消息未读数
        ((MainActivity) getActivity()).updateUnreadLabel();
        if (!hidden && !((MainActivity) getActivity()).isConflict) {

//            getHeadImg();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
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
                String username = conversation.getUserName();
                EMGroup group = EMGroupManager.getInstance().getGroup(username);
                if (conversation.getAllMessages().size() != 0 && group != null) {
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
     * @param usernames
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
    public void onRefresh(PullToRefreshBase<RecyclerViewPager> refreshView) {

    }


}
