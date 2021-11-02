package com.lenovo.frame;

import android.content.Intent;
import android.view.View;

import androidx.activity.result.ActivityResult;

import com.lenovo.frame.base.BaseActivity;

public class MainActivity extends BaseActivity {

    public MainActivity() {
        super(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        findViewById(R.id.main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("data", "data from MainActivity");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public void onActivityResult(ActivityResult result) {

    }
}
