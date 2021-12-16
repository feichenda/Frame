package com.lenovo.frame.listeren;

/**
 * Author: feizai
 * Date: 2021/12/6-0006 上午 11:43:58
 * Describe:
 */
public interface RequsetPermissionResultListener {
    void onAllowable(String[] permissions, int position);//允许

    void onDisallowable(String[] permissions, int position);//禁止

    void onCompleteban(String[] permissions, int position);//完全禁止
}
