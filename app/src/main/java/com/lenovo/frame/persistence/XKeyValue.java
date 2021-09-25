package com.lenovo.frame.persistence;

import android.app.Application;

import com.lenovo.frame.constant.Key;
import com.tencent.mmkv.MMKV;

/**
 * @Author feizai
 * @Date 2021/9/25 0025  下午 8:31:08
 * @Explain
 */
public class XKeyValue {
    public void init(Application application) {
        MMKV.initialize(application);
    }

    public void putBoolean(@Key String key, Boolean value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public Boolean getBoolean(@Key String key, Boolean value) {
        return MMKV.defaultMMKV().decodeBool(key, value);
    }

    public Boolean getBoolean(@Key String key) {
        return MMKV.defaultMMKV().decodeBool(key, false);
    }

    public void putString(@Key String key, String value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public String getString(@Key String key, String value) {
        return MMKV.defaultMMKV().decodeString(key, value);
    }

    public String getString(@Key String key) {
        return MMKV.defaultMMKV().decodeString(key, "");
    }
}
