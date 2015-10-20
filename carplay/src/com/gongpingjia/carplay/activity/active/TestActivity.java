package com.gongpingjia.carplay.activity.active;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.view.AnimButtonView;

/**
 * Created by Administrator on 2015/10/20.
 */
public class TestActivity extends CarPlayBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
    }

    @Override
    public void initView() {
        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(new MyAdapter(self));

    }


    class MyAdapter extends BaseAdapter {


        Context context;

        LayoutInflater mLayout;

        public MyAdapter(Context context) {
            this.context = context;
            mLayout = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 20;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = mLayout.inflate(R.layout.test_item, null);
            }

            AnimButtonView b = (AnimButtonView) convertView.findViewById(R.id.invite);
            b.startScaleAnimation();

            return convertView;
        }
    }
}
