package com.lenovo.frame.base;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lenovo.frame.R;
import com.lenovo.frame.listeren.RequsetPermissionResultListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author feizai
 * @date 12/21/2020 021 9:44:14 PM
 */
public abstract class BaseFragment extends Fragment implements IGetPageName {

    int resLayout;
    //用户同意权限权限申请
    public Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    //用户拒绝权限申请
    public Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();
    //用户彻底禁止权限申请
    public Map<Integer, Runnable> completebanPermissionRunnables = new HashMap<>();

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private RequsetPermissionResultListener mRequsetPermissionResultListener;

    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<String[]> requestPermissionsLauncher;

    public BaseFragment(int resLayout){
        this.resLayout=resLayout;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(resLayout,container,false);
        initView(view);
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        ButterKnife.bind(this,view);
        intentActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            activityResultCallback(result);
        });
        requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), result -> {
        });
        requestPermissionsLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        });
//        sendDateToFragment();
        return view;
    }

    protected abstract void initView(View view);

    //activity返回值的处理
    protected abstract void activityResultCallback(ActivityResult result);

    /**
     * 活动跳转，无返回值
     *
     * @param activityclass 将要跳转的活动的class
     */
    protected void startActivity(Class activityclass) {
        startActivity(new Intent(getActivity(), activityclass));
    }

    /**
     * 活动跳转，有返回值
     *
     * @param activityclass 将要跳转的活动的class
     * @param requsetCode   请求跳转的id 唯一标识符即可
     */
    @Deprecated
    protected void startActivityForResult(Class activityclass, int requsetCode) {
        startActivityForResult(new Intent(getActivity(), activityclass), requsetCode);
    }

    /**
     * 活动跳转，有返回值
     *
     * @param activityclass 将要跳转的活动的class
     */
    protected void registerForActivityResult(Class activityclass) {
        intentActivityResultLauncher.launch(new Intent(getActivity(), activityclass));
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
     * 方法过时
     * @param id                                请求授权的id 唯一标识即可
     * @param permission                        请求的权限
     * @param requsetPermissionResultListener   请求权限结果回调监听
     */
    @Deprecated
    public void requestPermission(int id, String permission, RequsetPermissionResultListener requsetPermissionResultListener) {
        mRequsetPermissionResultListener = requsetPermissionResultListener;
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, id);
            } else {
                requsetPermissionResultListener.onAllowable(new String[]{permission},1);
            }
        } else {
            requsetPermissionResultListener.onAllowable(new String[]{permission},1);
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
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissionLauncher.launch(permission);
            } else {
                requsetPermissionResultListener.onAllowable(new String[]{permission}, 1);
            }
        } else {
            requsetPermissionResultListener.onAllowable(new String[]{permission}, 1);
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
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    result.add(permission);
                    count++;
                }
            }
            if (count == 0) {
                requsetPermissionResultListener.onAllowable(permissions, permissions.length - 1);
            } else {
                String[] strings = new String[count];
                result.toArray(strings);
                ActivityCompat.requestPermissions(getActivity(), strings, id);
            }
        } else {
            requsetPermissionResultListener.onAllowable(permissions, permissions.length - 1);
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
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(getContext().getApplicationContext(), permission);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    //申请权限
                    result.add(permission);
                    count++;
                }
            }
            if (count == 0) {
                requsetPermissionResultListener.onAllowable(permissions, permissions.length);
            } else {

                String[] strings = new String[count];
                result.toArray(strings);
                requestPermissionsLauncher.launch(strings);
            }
        } else {
            requsetPermissionResultListener.onAllowable(permissions, permissions.length);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (mRequsetPermissionResultListener == null) {
            throw new NullPointerException("RequsetPermissionResultListener is not allowed to be empty");
        }
        for (int i = 0; i < grantResults.length; i++) {
            boolean isTip = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[i]);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (isTip) {//表明用户没有彻底禁止弹出权限请求
                    mRequsetPermissionResultListener.onDisallowable(permissions, i);
                } else {//表明用户已经彻底禁止弹出权限请求
                    //这里一般会提示用户进入权限设置界面
                    mRequsetPermissionResultListener.onCompleteban(permissions, i);
                }
                return;
            } else {
                mRequsetPermissionResultListener.onAllowable(permissions, i);
            }
        }
    }

    /*获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）*/
    protected Intent getAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.FROYO) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getActivity().getPackageName());
        }
        return localIntent;
    }

    @Override
    public void onDestroy() {
        compositeDisposable.dispose();
        super.onDestroy();
    }

    /**
     * 添加Disposable
     */
    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
