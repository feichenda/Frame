package com.lenovo.frame.util;

import android.util.Base64;


/**
 * @author feizai
 * @date 12/14/2020 014 3:39:38 PM
 */
public class Base64Util {

    /**
     * 给字符串加密
     * @param text
     * @return
     */
    public static String encode(String text) {
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("\n","");
    }

    /**
     * 将加密后的字符串进行解密
     * @param encodedText
     * @return
     */
    public static String decode(String encodedText) {
        return new String(Base64.decode(encodedText.getBytes(), Base64.DEFAULT));
    }

}
