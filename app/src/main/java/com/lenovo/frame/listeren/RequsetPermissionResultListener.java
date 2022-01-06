package com.lenovo.frame.listeren;

/**
 * Author: feizai
 * Date: 2021/12/6-0006 上午 11:43:58
 * Describe:
 */
public interface RequsetPermissionResultListener {
    void onAllAllowable();//全部允许

    void onAllowable(String permission);//允许该权限

    void onDisallowable(String permission);//禁止该权限

    void onCompleteban(String permission);//完全禁止该权限
}
