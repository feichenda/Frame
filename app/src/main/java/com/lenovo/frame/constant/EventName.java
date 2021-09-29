package com.lenovo.frame.constant;

import androidx.annotation.StringDef;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/**
 * @Author feizai
 * @Date 2021/9/29 0029  下午 8:38:26
 * @Explain
 */
@StringDef(EventName.REFRESH_HOME_LIST)
@Retention(AnnotationRetention.SOURCE)
public @interface EventName {
    static final String REFRESH_HOME_LIST = "refresh_home_list";
}
