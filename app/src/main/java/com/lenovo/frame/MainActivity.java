package com.lenovo.frame;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemProperties;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;

import com.lenovo.frame.base.BaseActivity;
import com.lenovo.frame.base.BaseObserver;
import com.lenovo.frame.entity.User;
import com.lenovo.frame.listeren.RequsetPermissionResultListener;
import com.lenovo.frame.net.ExceptionHandle;
import com.lenovo.frame.net.RetrofitClient;
import com.lenovo.frame.util.LogUtil;
import com.lenovo.frame.util.MediaPlayerUtil;
import com.lenovo.frame.util.ToastUtil;

import java.io.File;
import java.util.Map;

public class MainActivity extends BaseActivity implements MediaPlayerUtil.MediaPlayerListener {

    private final static String TAG = "MainActivity";
    private Context mContext;
    private TextView path;
    private Button stop;
    private Button open;
    private Button pause;
    private Button play;
    private Button net;

    public MainActivity() {
        super(R.layout.activity_main);
        mContext = this;
    }

    @Override
    protected void initView() {
        open = findViewById(R.id.open);
        pause = findViewById(R.id.pause);
        play = findViewById(R.id.play);
        stop = findViewById(R.id.stop);
        net = findViewById(R.id.net);
        path = findViewById(R.id.path);
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, new RequsetPermissionResultListener() {

            @Override
            public void onAllAllowable() {

            }

            @Override
            public void onAllowable(String permission) {
                LogUtil.i(TAG, permission);
                open.setOnClickListener(v -> {
                    openFile();
                });
                play.setOnClickListener(v->{
                    MediaPlayerUtil.getInstance(mContext).play();
                });
                pause.setOnClickListener(v->{
                    MediaPlayerUtil.getInstance(mContext).pause();
                });
                stop.setOnClickListener(v->{
                    MediaPlayerUtil.getInstance(mContext).stop();
                });
                net.setOnClickListener(v->{
                    MediaPlayerUtil.getInstance(mContext)
                            .setDataSource("http://freetyst.nf.migu.cn/public/product09/2018/05/09/2018%E5%B9%B405%E6%9C%8807%E6%97%A517%E7%82%B931%E5%88%86%E5%86%85%E5%AE%B9%E5%87%86%E5%85%A5%E7%91%9E%E5%BC%98%E5%88%A9%E9%9F%B358%E9%A6%96/%E6%AD%8C%E6%9B%B2%E4%B8%8B%E8%BD%BD/MP3_320_16_Stero/%E5%B9%B3%E5%87%A1%E4%B9%8B%E8%B7%AF-%E5%92%8C%E5%B9%B3%2B%E5%88%98%E7%BE%8E%E5%A8%B4.mp3")
                            .setMediaPlayerListener(MainActivity.this)
                            .play();
                });
            }

            @Override
            public void onDisallowable(String permission) {
                LogUtil.i(TAG, permission);
            }

            @Override
            public void onCompleteban(String permission) {
                LogUtil.i(TAG, permission);
            }
        });
        open.setOnClickListener(v -> {
            openFile();
        });
        play.setOnClickListener(v->{
            MediaPlayerUtil.getInstance(mContext).play();
        });
        pause.setOnClickListener(v->{
            MediaPlayerUtil.getInstance(mContext).pause();
        });
        stop.setOnClickListener(v->{
            MediaPlayerUtil.getInstance(mContext).stop();
        });
        net.setOnClickListener(v->{
            MediaPlayerUtil.getInstance(mContext)
                    .setDataSource("http://freetyst.nf.migu.cn/public/product09/2018/05/09/2018%E5%B9%B405%E6%9C%8807%E6%97%A517%E7%82%B931%E5%88%86%E5%86%85%E5%AE%B9%E5%87%86%E5%85%A5%E7%91%9E%E5%BC%98%E5%88%A9%E9%9F%B358%E9%A6%96/%E6%AD%8C%E6%9B%B2%E4%B8%8B%E8%BD%BD/MP3_320_16_Stero/%E5%B9%B3%E5%87%A1%E4%B9%8B%E8%B7%AF-%E5%92%8C%E5%B9%B3%2B%E5%88%98%E7%BE%8E%E5%A8%B4.mp3")
                    .setMediaPlayerListener(this).play();
        });
    }

    public void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        registerForActivityResult(intent);
    }

    @Override
    public String getPageName() {
        return null;
    }

    @Override
    public void activityResultCallback(ActivityResult result) {
        if (result.getResultCode() == Activity.RESULT_OK) {//是否选择，没选择就不会继续
            Intent data = result.getData();
            Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
            String[] proj = {MediaStore.Images.Media.DATA};
            Cursor actualimagecursor = managedQuery(uri, proj, null, null, null);
            int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            actualimagecursor.moveToFirst();
            String img_path = actualimagecursor.getString(actual_image_column_index);
            LogUtil.d(TAG, img_path);
            path.setText(img_path);
            MediaPlayerUtil.getInstance(mContext).setDataSource(img_path).setMediaPlayerListener(this).play();
//            File file = new File(img_path);
//            Toast.makeText(MainActivity.this, file.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCompltion() {
        LogUtil.d(TAG, "onCompltion");
    }

    @Override
    public void onError() {
        LogUtil.d(TAG, "onError");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerUtil.getInstance(mContext).release();
    }
}
