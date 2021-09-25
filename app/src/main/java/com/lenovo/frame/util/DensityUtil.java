package com.lenovo.frame.util;

import android.content.Context;

/**
 * 分辨率转换类
 * @author feizai
 * @date 12/21/2020 021 10:05:39 PM
 */
public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
