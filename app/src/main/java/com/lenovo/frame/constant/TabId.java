package com.lenovo.frame.constant;

import androidx.annotation.StringDef;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/**
 * @Author feizai
 * @Date 2021/9/29 0029  下午 8:43:11
 * @Explain
 */
@StringDef({TabId.HOME, TabId.ACGN, TabId.SMALL_VIDEO, TabId.GOLD, TabId.MINE})
@Retention(AnnotationRetention.SOURCE)
public @interface TabId {
    final static String HOME = "home";
    final static String ACGN = "acgn";
    final static String SMALL_VIDEO = "small_video";
    final static String GOLD = "gold";
    final static String MINE = "mine";
}
