package com.xxx.compass.base.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.xxx.compass.R;
import com.xxx.compass.base.App;

/**
 * 需要Title展示的Activity
 */
public abstract class BaseTitleActivity extends BaseActivity {

    protected abstract String initTitle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            //Title字体
            ((TextView) findViewById(R.id.main_title)).setText(initTitle());

            //返回点击事件
            findViewById(R.id.main_return).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //结束页面
                   finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
