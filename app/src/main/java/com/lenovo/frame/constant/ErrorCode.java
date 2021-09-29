package com.lenovo.frame.constant;

import androidx.annotation.IntDef;


import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;


/**
 * @Author feizai
 * @Date 2021/9/29 0029  下午 8:34:18
 * @Explain
 */
@IntDef({ErrorCode.OK, ErrorCode.UNAUTHORIZED, ErrorCode.CUSTOM_FIRST, ErrorCode.VALUE_IS_NULL})
@Retention(AnnotationRetention.SOURCE)
public @interface ErrorCode {
    final static int OK = 200;
    final static int UNAUTHORIZED = 401;
    final static int CUSTOM_FIRST = 600;
    final static int VALUE_IS_NULL = CUSTOM_FIRST + 1;
}
