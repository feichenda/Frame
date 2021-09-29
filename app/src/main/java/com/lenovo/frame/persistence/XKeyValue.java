package com.lenovo.frame.persistence;

import android.app.Application;
import android.os.Parcelable;

import androidx.collection.ArraySet;

import com.lenovo.frame.constant.Key;
import com.tencent.mmkv.MMKV;

import java.util.Set;

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

    public void putInt(@Key String key, int value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public int getInt(@Key String key, int value) {
        return MMKV.defaultMMKV().decodeInt(key, value);
    }

    public int getInt(@Key String key) {
        return MMKV.defaultMMKV().decodeInt(key, 0);
    }

    public void putFloat(@Key String key, float value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public float getFloat(@Key String key, float value) {
        return MMKV.defaultMMKV().decodeFloat(key, value);
    }

    public float getFloat(@Key String key) {
        return MMKV.defaultMMKV().decodeFloat(key, 0f);
    }

    public void putLong(@Key String key, long value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public long getLong(@Key String key, long value) {
        return MMKV.defaultMMKV().decodeLong(key, value);
    }

    public long getLong(@Key String key) {
        return MMKV.defaultMMKV().decodeLong(key, 0L);
    }

    public void putDouble(@Key String key, double value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public double getDouble(@Key String key, double value) {
        return MMKV.defaultMMKV().decodeDouble(key, value);
    }

    public double getDouble(@Key String key) {
        return MMKV.defaultMMKV().decodeDouble(key, 0.0);
    }

    public void putByteArray(@Key String key, byte[] value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public byte[] getByteArray(@Key String key, byte[] value) {
        return MMKV.defaultMMKV().decodeBytes(key, value);
    }

    public byte[] getByteArray(@Key String key) {
        return MMKV.defaultMMKV().decodeBytes(key, new byte[0]);
    }

    public void putStringSet(@Key String key, Set<String> value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public Set<String> getStringSet(@Key String key, Set<String> value) {
        return MMKV.defaultMMKV().decodeStringSet(key, value);
    }

    public Set<String> getStringSet(@Key String key) {
        return MMKV.defaultMMKV().decodeStringSet(key, new ArraySet<String>());
    }

    public void putParcelable(@Key String key, Parcelable value) {
        MMKV.defaultMMKV().encode(key, value);
    }

    public <T extends Parcelable> T getParcelable(@Key String key, Class<T> tClass) {
        return MMKV.defaultMMKV().decodeParcelable(key, tClass);
    }
}
