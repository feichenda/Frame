package com.lenovo.frame.constant;

import androidx.annotation.IntDef;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/**
 * @Author feizai
 * @Date 2021/9/29 0029  下午 8:40:27
 * @Explain
 */
@IntDef({LoadMoreState.GONE, LoadMoreState.LOADING, LoadMoreState.ERROR, LoadMoreState.NO_NETWORK, LoadMoreState.NO_MORE})
@Retention(AnnotationRetention.SOURCE)
public @interface LoadMoreState {
    /**
     * 隐藏
     */
        final static int GONE = 0;

    /**
     * 正在加载
     */
        final static int LOADING = 1;

    /**
     * 加载异常
     */
        final static int ERROR = 2;

    /**
     * 无网络
     */
        final static int NO_NETWORK = 3;

    /**
     * 没有更多
     */
        final static int NO_MORE = 4;
}
