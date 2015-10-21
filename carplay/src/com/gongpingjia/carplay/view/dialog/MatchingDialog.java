package com.gongpingjia.carplay.view.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

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
    private CheckBox checkBox;
    private TextView textDestination;
    private Button btnMatch;
    private Context context;

    public MatchingDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    public MatchingDialog(Context context, List<Matching> data) {
        super(context);
        mDatas = data;
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_match_intention);

        checkBox = (CheckBox) findViewById(R.id.chk_pick);
        textDestination = (TextView) findViewById(R.id.tv_destination);
        btnMatch = (Button) findViewById(R.id.btn_match);

        gridView = (GridView) findViewById(R.id.gv_matching);
        mAdapter = new MatchingAdapter(getContext(), mDatas);
        gridView.setAdapter(mAdapter);

        btnMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean pickOrNot = checkBox.isChecked();
                if (textDestination.getText().toString().trim().length() == 0) {
                    //请选择地点
                    return;
                }
            }
        });

        textDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MateRegionDialog dlg = new MateRegionDialog(context);
                dlg.setOnMateRegionResultListener(new MateRegionDialog.OnMateRegionResultListener() {
                    @Override
                    public void onResult(String place) {
                        Toast.makeText(context, place, Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
            }
        });
    }
}
