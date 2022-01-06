package com.lenovo.frame.base;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lenovo.frame.R;
import com.lenovo.frame.listeren.RequsetPermissionResultListener;
import com.lenovo.frame.util.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

public abstract class BaseActivity extends AppCompatActivity implements SwipeBackActivityBase, IGetPageName {

    //布局文件ID
    private int resource;

    private RequsetPermissionResultListener mRequsetPermissionResultListener;

    private CompositeDisposable mCompositeDisposable;

    private SwipeBackActivityHelper mHelper;

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;

    private Context mContext;

    public BaseActivity(int resLayout) {
        resource = resLayout;
        mContext = this;
        mCompositeDisposable = new CompositeDisposable();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        setContentView(resource);
        ButterKnife.bind(this);
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            activityResultCallback(result);
        });
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
//            permissionResultCallback(result);
        });
        requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
//            permissionsResultCallback(result);
        });
        initView();
    }

    @Override
    protected void onDestroy() {
        mCompositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    /**
     * 是否开启左滑返回 true:开启
     *
     * @param enable
     */
    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    //活动初始化自定义操作
    protected abstract void initView();

    //activity返回值的处理
    protected abstract void activityResultCallback(ActivityResult result);

    //请求权限结果回调
//    protected abstract void permissionResultCallback(Boolean result);
//
//    protected abstract void permissionsResultCallback(Map<String, Boolean> result);

    /**
     * 活动跳转，无返回值
     *
     * @param activityclass 将要跳转的活动的class
     */
    protected void startActivity(Class activityclass) {
        startActivity(new Intent(this, activityclass));
    }

    /**
     * 活动跳转，有返回值
     *
     * @param activityclass 将要跳转的活动的class
     * @param requsetCode   请求跳转的id 唯一标识符即可
     */
    @Deprecated
    protected void startActivityForResult(Class activityclass, int requsetCode) {
        startActivityForResult(new Intent(this, activityclass), requsetCode);
    }

    /**
     * 活动跳转，有返回值
     *
     * @param activityclass 将要跳转的活动的class
     */
    protected void registerForActivityResult(Class activityclass) {
        intentActivityResultLauncher.launch(new Intent(this, activityclass));
    }

    /**
     * 活动跳转，有返回值
     *
     * @param intent 将要跳转的活动的intent
     */
    protected void registerForActivityResult(Intent intent) {
        intentActivityResultLauncher.launch(intent);
    }

    /**
     * 请求单一权限
     * 过时方法
     *
     * @param id                              请求授权的id 唯一标识即可
     * @param permission                      请求的权限
     * @param requsetPermissionResultListener 请求权限结果回调监听
     */
    @Deprecated
    public void requestPermission(int id, String permission, RequsetPermissionResultListener requsetPermissionResultListener) {
        mRequsetPermissionResultListener = requsetPermissionResultListener;
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{permission}, id);
            } else {
                requsetPermissionResultListener.onAllowable(permission);
            }
        } else {
            requsetPermissionResultListener.onAllowable(permission);
        }
    }

    /**
     * 请求单一权限
     * 采用ActivityResultAPI
     *
     * @param permission 请求的权限
     * @param requsetPermissionResultListener 请求权限结果回调监听
     */
    public void requestPermission(String permission, RequsetPermissionResultListener requsetPermissionResultListener) {
        mRequsetPermissionResultListener = requsetPermissionResultListener;
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissionLauncher.launch(permission);
            } else {
                requsetPermissionResultListener.onAllowable(permission);
            }
        } else {
            requsetPermissionResultListener.onAllowable(permission);
        }
    }

    /**
     * 请求多权限
     * 过时方法
     *
     * @param id                              请求授权的id 唯一标识即可
     * @param permissions                     请求的权限组
     * @param requsetPermissionResultListener 请求权限结果回调监听
     */
    @Deprecated
    public void requestPermissions(int id, String[] permissions, RequsetPermissionResultListener requsetPermissionResultListener) {
        mRequsetPermissionResultListener = requsetPermissionResultListener;
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> result = new ArrayList<>();
            int count = 0;
            //检查是否拥有权限
            for (String permission : permissions) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    result.add(permission);
                    count++;
                }
            }
            if (count == 0) {
                requsetPermissionResultListener.onAllAllowable();
            } else {
                String[] strings = new String[count];
                result.toArray(strings);
                ActivityCompat.requestPermissions(BaseActivity.this, strings, id);
            }
        } else {
            requsetPermissionResultListener.onAllAllowable();
        }
    }

    /**
     * 请求多权限
     *
     * @param permissions 请求的权限组
     * @param requsetPermissionResultListener 请求权限结果回调监听
     */
    public void requestPermissions(String[] permissions, RequsetPermissionResultListener requsetPermissionResultListener) {
        mRequsetPermissionResultListener = requsetPermissionResultListener;
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> result = new ArrayList<>();
            int count = 0;
            //检查是否拥有权限
            for (String permission : permissions) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    result.add(permission);
                    count++;
                }
            }
            if (count == 0) {
                requsetPermissionResultListener.onAllAllowable();
            } else {

                String[] strings = new String[count];
                result.toArray(strings);
                requestPermissionsLauncher.launch(strings);
            }
        } else {
            requsetPermissionResultListener.onAllAllowable();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mRequsetPermissionResultListener == null) {
            throw new NullPointerException("RequsetPermissionResultListener is not allowed to be empty");
        }
        for (int i = 0; i < grantResults.length; i++) {
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, permissions[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (isTip) {//表明用户没有彻底禁止弹出权限请求
                    mRequsetPermissionResultListener.onDisallowable(permissions[i]);
                } else {//表明用户已经彻底禁止弹出权限请求
                    //这里一般会提示用户进入权限设置界面
                    mRequsetPermissionResultListener.onCompleteban(permissions[i]);
                }
                return;
            } else {
                mRequsetPermissionResultListener.onAllowable(permissions[i]);
            }
        }
    }

    /*获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）*/
    protected Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        return localIntent;
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    /**
     * 添加Disposable
     */
    protected void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }
}
