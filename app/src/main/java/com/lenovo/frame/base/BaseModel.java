package com.lenovo.frame.base;

import java.util.List;

/**
 * @author feizai
 * @date 12/21/2020 021 10:05:39 PM
 */
public class BaseModel<T> {

    private int code;//默认成功的时候为200
    private String message;
    private T data;
    private List<T> datas;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }

    public boolean isOk() {
        return code == 0;
    }

}
