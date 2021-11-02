package com.lenovo.frame;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;

import com.lenovo.frame.base.BaseActivity;

/**
 * @author feizai
 * @date 2021/5/3 0003 上午 9:16:31
 */
public class FirstActivity extends BaseActivity {

    public FirstActivity() {
        super(R.layout.activity_first);
    }

    @Override
    protected void initView() {
        setSwipeBackEnable(true);
        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForActivityResult(MainActivity.class);
            }
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForActivityResult(SecondActivity.class);
            }
        });
    }

    @Override
    public void onActivityResult (ActivityResult result) {
        Intent data = result.getData();
        int resultCode = result.getResultCode();
        if (resultCode == RESULT_OK)
            Toast.makeText(FirstActivity.this, data.getStringExtra("data"), Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getPageName() {
        return null;
    }

}
