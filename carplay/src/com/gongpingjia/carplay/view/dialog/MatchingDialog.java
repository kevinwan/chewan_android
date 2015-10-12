package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.widget.GridView;

import com.gongpingjia.carplay.R;
import com.gongpingjia.carplay.adapter.MatchingAdapter;
import com.gongpingjia.carplay.bean.Matching;
import com.gongpingjia.carplay.view.BaseAlertDialog;

import java.util.List;

/**
 * Created by Administrator on 2015/10/12.
 */
public class MatchingDialog extends BaseAlertDialog {

    private GridView gridView;
    private List<Matching> mDatas;
    private MatchingAdapter mAdapter;

    public MatchingDialog(Context context, int theme) {
        super(context, theme);
    }

    public MatchingDialog(Context context, List<Matching> data) {
        super(context);
        mDatas = data;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_match_intention);
        gridView = (GridView) findViewById(R.id.gv_matching);
        mAdapter = new MatchingAdapter(getContext(), mDatas);
        gridView.setAdapter(mAdapter);
    }
}
