package com.lenovo.frame.constant;

import androidx.annotation.StringDef;

import kotlin.annotation.AnnotationRetention;
import kotlin.annotation.Retention;

/**
 * @Author feizai
 * @Date 2021/9/29 0029  下午 8:41:51
 * @Explain
 */
@StringDef({PageName.MAIN, PageName.HOME, PageName.ACGN, PageName.SMALL_VIDEO, PageName.GOLD, PageName.MINE})
@Retention(AnnotationRetention.SOURCE)
public @interface PageName {
    final static String MAIN = "main";
    final static String HOME = "home";
    final static String ACGN = "acgn";
    final static String SMALL_VIDEO = "small_video";
    final static String GOLD = "gold";
    final static String MINE = "mine";
}
