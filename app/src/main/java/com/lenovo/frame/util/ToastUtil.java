package com.lenovo.frame.util;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.zip.Inflater;

/**
 * Author: feizai
 * Date: 2021/12/1-0001 上午 11:11:11
 * Describe:
 */
public class ToastUtil {
    private static Context mContext;
    private Toast toast;

    private ToastUtil() {
        toast = new Toast(mContext);
    }

    public static ToastUtil getInstance(Context context) {
        if (context != null) {
            mContext = context;
        } else {
            throw new NullPointerException("context is null");
        }
        return ToastUtilHolder.sInstance;
    }

    private static class ToastUtilHolder {
        private final static ToastUtil sInstance = new ToastUtil();
    }

    public void showToast(CharSequence content) {
        Toast.makeText(mContext, content, Toast.LENGTH_SHORT).show();
    }

    public void showToast(CharSequence content, Integer duration) {
        Toast.makeText(mContext, content, duration).show();
    }

    public void setView() {
        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
//        inflater.inflate()
//        toast.setView();
    }
}
