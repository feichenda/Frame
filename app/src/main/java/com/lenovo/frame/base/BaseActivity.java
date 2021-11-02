package com.lenovo.frame.base;

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

import java.util.HashMap;
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
    int resource;
    //用户同意权限权限申请
    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    //用户拒绝权限申请
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();
    //用户彻底禁止权限申请
    private Map<Integer, Runnable> completebanPermissionRunnables = new HashMap<>();
    //用户同意权限权限申请时做的逻辑
    private Runnable allowableRunnable;
    //用户拒绝权限申请时做的逻辑
    private Runnable disallowableRunnable;
    //用户彻底禁止权限申请时做的逻辑
    private Runnable completebanRunable;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private SwipeBackActivityHelper mHelper;

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;

    public BaseActivity(int resLayout) {
        resource = resLayout;
        disallowableRunnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(BaseActivity.this, "您以禁止获取权限", Toast.LENGTH_SHORT).show();
//                finish();
            }
        };
        completebanRunable = new Runnable() {
            @Override
            public void run() {
                new MaterialDialog.Builder(BaseActivity.this)
                        .canceledOnTouchOutside(false)
                        .title("警告")
                        .content("跳转到设置以获取权限")
                        .icon(getResources().getDrawable(R.drawable.ic_error))
                        .positiveText("确认")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                startActivity(getAppDetailSettingIntent());
                            }
                        })
                        .negativeText("取消")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish();
                            }
                        })
                        .show();
            }
        };
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
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {});
        requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {});
        initView();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.dispose();
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
     * 请求单一权限
     *
     * @param id                   请求授权的id 唯一标识即可
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作，不能为空
     * @param disallowableRunnable 禁止权限后的操作，可以为空，为空默认操作弹Toast
     * @param completebanRunable   彻底禁止权限后的操作，可以为空，为空默认操作询问是否到设置里打开权限，确认这跳转，取消则结束activity
     */
    @Deprecated
    public void requestPermission(int id, String permission, Runnable allowableRunnable, Runnable disallowableRunnable, Runnable completebanRunable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }

        allowablePermissionRunnables.put(id, allowableRunnable);
//        disallowablePermissionRunnables.put(id, disallowableRunnable);
//        completebanPermissionRunnables.put(id, completebanRunable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }
        if (completebanRunable != null) {
            completebanPermissionRunnables.put(id, completebanRunable);
        }
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                ActivityCompat.requestPermissions(BaseActivity.this, new String[]{permission}, id);
            } else {
                allowableRunnable.run();
            }
        } else {
            allowableRunnable.run();
        }
    }

    /**
     * 请求单一权限
     *
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作，不能为空
     * @param disallowableRunnable 禁止权限后的操作，可以为空，为空默认操作弹Toast
     * @param completebanRunable   彻底禁止权限后的操作，可以为空，为空默认操作询问是否到设置里打开权限，确认这跳转，取消则结束activity
     */
    public void requestPermission(String permission, Runnable allowableRunnable, Runnable disallowableRunnable, Runnable completebanRunable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }

        allowablePermissionRunnables.put(1, allowableRunnable);
        this.allowableRunnable = allowableRunnable;
//        disallowablePermissionRunnables.put(id, disallowableRunnable);
//        completebanPermissionRunnables.put(id, completebanRunable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(1, disallowableRunnable);
            this.disallowableRunnable = disallowableRunnable;
        }
        if (completebanRunable != null) {
            completebanPermissionRunnables.put(1, completebanRunable);
            this.completebanRunable = completebanRunable;
        }
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissionLauncher.launch(permission);
            } else {
                allowableRunnable.run();
            }
        } else {
            allowableRunnable.run();
        }
    }

    /**
     * 请求单一权限
     *
     * @param id                   请求授权的id 唯一标识即可
     * @param permissions          请求的权限组
     * @param allowableRunnable    同意授权后的操作，不能为空
     * @param disallowableRunnable 禁止权限后的操作，可以为空，为空默认操作弹Toast
     * @param completebanRunable   彻底禁止权限后的操作，可以为空，为空默认操作询问是否到设置里打开权限，确认这跳转，取消则结束activity
     */
    @Deprecated
    public void requestPermissions(int id, String[] permissions, Runnable allowableRunnable, Runnable disallowableRunnable, Runnable completebanRunable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }

        allowablePermissionRunnables.put(id, allowableRunnable);
//        disallowablePermissionRunnables.put(id, disallowableRunnable);
//        completebanPermissionRunnables.put(id, completebanRunable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }
        if (completebanRunable != null) {
            completebanPermissionRunnables.put(id, completebanRunable);
        }
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] result = new String[permissions.length];
            int count = 0;
            //检查是否拥有权限
            for (String permission : permissions) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    result[count++] = permission;
                }
            }
            if (count == 0) {
                allowableRunnable.run();
            }else {
                requestPermissionsLauncher.launch(permissions);
            }
        } else {
            allowableRunnable.run();
        }
    }

    /**
     * 请求多权限
     *
     * @param permissions          请求的权限组
     * @param allowableRunnable    同意授权后的操作，不能为空
     * @param disallowableRunnable 禁止权限后的操作，可以为空，为空默认操作弹Toast
     * @param completebanRunable   彻底禁止权限后的操作，可以为空，为空默认操作询问是否到设置里打开权限，确认这跳转，取消则结束activity
     */
    public void requestPermissions(String[] permissions, Runnable allowableRunnable, Runnable disallowableRunnable, Runnable completebanRunable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }

        allowablePermissionRunnables.put(1, allowableRunnable);
        this.allowableRunnable = allowableRunnable;
//        disallowablePermissionRunnables.put(id, disallowableRunnable);
//        completebanPermissionRunnables.put(id, completebanRunable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(1, disallowableRunnable);
            this.disallowableRunnable = disallowableRunnable;
        }
        if (completebanRunable != null) {
            completebanPermissionRunnables.put(1, completebanRunable);
            this.completebanRunable = completebanRunable;
        }
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] result = new String[permissions.length];
            int count = 0;
            //检查是否拥有权限
            for (String permission : permissions) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    result[count++] = permission;
                }
            }
            if (count == 0) {
                allowableRunnable.run();
            }else {
                requestPermissionsLauncher.launch(permissions);
            }
        } else {
            allowableRunnable.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int i = 0; i < grantResults.length; i++) {
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(BaseActivity.this, permissions[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (isTip) {//表明用户没有彻底禁止弹出权限请求
                    Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
                    if (disallowRun != null) {
                        disallowRun.run();
                    } else {
                        this.disallowableRunnable.run();
                    }
                } else {//表明用户已经彻底禁止弹出权限请求
                    //这里一般会提示用户进入权限设置界面
                    Runnable completebanRun = completebanPermissionRunnables.get(requestCode);
                    if (completebanRun != null) {
                        completebanRun.run();
                    } else {
                        this.completebanRunable.run();
                    }
                }
                return;
            } else {
                Runnable allowRun = allowablePermissionRunnables.get(requestCode);
                if (allowRun != null) {
                    allowRun.run();
                    continue;
                } else if (this.allowableRunnable != null) {
                    this.allowableRunnable.run();
                    continue;
                } else {
                    throw new NullPointerException("allowRun == null");
                }
            }
        }
    }

    /*获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）*/
    private Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
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
        compositeDisposable.add(disposable);
    }
}
