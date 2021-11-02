package com.lenovo.frame;

import android.Manifest;
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


    }

    @Override
    protected void activityResultCallback(ActivityResult result) {
        Intent data = result.getData();
        int resultCode = result.getResultCode();
        if (resultCode == RESULT_OK)
            Toast.makeText(FirstActivity.this, data.getStringExtra("data"), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, new Runnable() {
            @Override
            public void run() {
//                Toast.makeText(FirstActivity.this, "tongyi", Toast.LENGTH_SHORT).show();
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
        },null,null);
    }

    @Override
    public String getPageName() {
        return null;
    }
}
