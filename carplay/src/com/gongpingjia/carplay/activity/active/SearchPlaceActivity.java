package com.gongpingjia.carplay.activity.active;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.activity.CarPlayBaseActivity;
import com.gongpingjia.carplay.adapter.SuggestionAdapter;
import com.gongpingjia.carplay.bean.SuggestionPlace;
import com.gongpingjia.carplay.view.ClearableEditText;

/*
 *@author zhanglong
 *Email:1269521147@qq.com
 */
public class SearchPlaceActivity extends CarPlayBaseActivity implements TextWatcher, OnClickListener {

    private ClearableEditText mKeyEditText;

    private ImageView mBackImageView;

    private TextView mSearchTextView;

    private ListView mSuggestionList;

    private List<SuggestionPlace> mDatas;

    private SuggestionAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_place);

        mDatas = new ArrayList<SuggestionPlace>();
        mAdapter = new SuggestionAdapter(this, mDatas);
        mSuggestionList.setAdapter(mAdapter);

        mSuggestionList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SuggestionPlace sp = mDatas.get(position);
                String key = sp.getDetails() + sp.getStyleName().toString();
                Intent it = new Intent();
                it.putExtra("key", key);
                setResult(RESULT_OK, it);
                self.finish();
            }
        });
    }

    @Override
    public void initView() {
        // TODO Auto-generated method stub
        mKeyEditText = (ClearableEditText) findViewById(R.id.et_search);
        mSearchTextView = (TextView) findViewById(R.id.tv_search);
        mBackImageView = (ImageView) findViewById(R.id.imgView_back);
        mSuggestionList = (ListView) findViewById(R.id.lv_suggestion);

        mKeyEditText.addTextChangedListener(this);
        mBackImageView.setOnClickListener(this);
        mSearchTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.imgView_back:
            finish();
            break;
        case R.id.tv_search:
            String key = mKeyEditText.getText().toString();
            Intent it = new Intent();
            it.putExtra("key", key);
            setResult(RESULT_OK, it);
            this.finish();
            break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        final String key = s.toString().trim();

        Inputtips inputTips = new Inputtips(this, new InputtipsListener() {

            @Override
            public void onGetInputtips(List<Tip> tipList, int rCode) {
                if (rCode == 0) {// 正确返回
                    mDatas.clear();
                    for (int i = 0; i < tipList.size(); i++) {
                        String name = tipList.get(i).getName();
                        SuggestionPlace sug = new SuggestionPlace();
                        sug.setDetails(tipList.get(i).getDistrict());
                        SpannableStringBuilder sb = new SpannableStringBuilder(tipList.get(i).getName());
                        int start = name.toLowerCase().indexOf(key.toLowerCase());
                        int end = start + key.length();
                        if (start != -1) {
                            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#47D1D4")), start, end,
                                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        sug.setStyleName(sb);
                        mDatas.add(sug);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        try {
            inputTips.requestInputtips(key, "");// 第一个参数表示提示关键字，第二个参数默认代表全国，也可以为城市区号
        } catch (AMapException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

}
