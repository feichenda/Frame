package com.lenovo.frame.base;

import androidx.lifecycle.ViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * @Author: feizai
 * @CreateDate: 2021/10/10 下午3:54
 */
public abstract class BaseViewModel extends ViewModel implements IGetPageName {
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    /**
     * 添加Disposable
     */
    protected void addDisposable(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}
