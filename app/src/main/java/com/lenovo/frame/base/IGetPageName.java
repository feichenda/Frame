package com.lenovo.frame.base;

import com.lenovo.frame.constant.PageName;

/**
 * @Author feizai
 * @Date 2021/9/29 0029  下午 8:44:41
 * @Explain 获取页面名称通用接口
 */
public interface IGetPageName {
    @PageName
    public String getPageName();
}
