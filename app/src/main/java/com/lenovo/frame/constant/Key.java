package com.lenovo.frame.constant;

import androidx.annotation.StringDef;


import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/**
 * @Author feizai
 * @Date 2021/9/25 0025  下午 8:45:03
 * @Explain
 */
@StringDef(Key.XXX)
@Retention(AnnotationRetention.SOURCE)
public @interface Key {
    final static String XXX = "xxx";
}
