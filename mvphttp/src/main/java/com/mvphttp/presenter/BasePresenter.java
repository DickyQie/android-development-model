package com.mvphttp.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by zq on 2016/8/16.
 */

public class BasePresenter<T> {

    WeakReference<T> weakReference;

    public void attachView(T t){
        weakReference=new WeakReference<T>(t);
    }

    public void deachView(){
        if (weakReference!=null)
        {
            weakReference.clear();
            weakReference=null;
        }
    }
    protected T getView(){
        return weakReference!=null ? weakReference.get() : null;
    }


}
