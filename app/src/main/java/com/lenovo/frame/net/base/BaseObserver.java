package com.lenovo.frame.net.base;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.lenovo.frame.net.ExceptionHandle;
import com.lenovo.frame.net.base.BaseModel;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @author feizai
 * @date 12/22/2020 022 10:49:42 AM
 */
public abstract class BaseObserver<T> implements Observer<T> {

    private Context mContext;

    public BaseObserver(Context context){
        mContext=context;
    }

    @Override
    public void onSubscribe(Disposable d) {
        Log.v("Message","请求开始");
        showDialog();
    }

    @Override
    public void onNext(T t) {
        hideDialog();
        Log.v("Message","请求到数据");
        int code = 0;
        String msg = "";
        if (t instanceof BaseModel) {
            msg = ((BaseModel) t).getMessage();
            code = ((BaseModel) t).getCode();
        }
        switch (code) {
            case 200:
                successful(t);
                break;
            default:
                defeated(t);
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.v("Message","请求出错");
        hideDialog();
        Log.e("Error",e.getMessage());
        ExceptionHandle.ResponeThrowable error;
        if (e instanceof ExceptionHandle.ResponeThrowable) {
            error = (ExceptionHandle.ResponeThrowable) e;
        } else {
            error = new ExceptionHandle.ResponeThrowable(e, ExceptionHandle.ERROR.UNKNOWN);
        }
        showErrorMessage(error);
    }

    @Override
    public void onComplete() {
        Log.v("Message","请求完成");
        hideDialog();
    }

    protected abstract void showDialog();

    protected abstract void hideDialog();

    protected abstract void successful(T t);

    protected abstract void defeated(T t);

    public abstract void onError(ExceptionHandle.ResponeThrowable e);

    public void showErrorMessage(ExceptionHandle.ResponeThrowable e) {
        onError(e);
    }
}
