package com.gongpingjia.carplay.activity.active;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gongpingjia.carplay.R;

/**
 * Created by Administrator on 2015/10/24.
 */
public class Test extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Test.this, "点击", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.test1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Test.this, "点击1", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
