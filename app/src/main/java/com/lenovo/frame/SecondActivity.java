package com.lenovo.frame;

import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResult;

import com.lenovo.frame.base.BaseActivity;

public class SecondActivity extends BaseActivity {

    public SecondActivity() {
        super(R.layout.activity_second);
    }

    @Override
    protected void initView() {
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", "data from SecondActivity");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void activityResultCallback(ActivityResult result) {

    }

    @Override
    public String getPageName() {
        return null;
    }
}
