package com.mvpproject.presenter;

import java.lang.ref.WeakReference;

/**
 * Created by zq on 2016/8/16.
 * 软引用便于销毁对象，避免OOM
 */

public class BasePresenter<T> {

    WeakReference<T> weakReference;

    public void attchView(T t)
    {
        weakReference=new WeakReference<T>(t);
    }

    public void deatchView()
    {
        if(weakReference!=null)
        {
            weakReference.clear();
            weakReference=null;
        }
    }

    protected T getView(){
        return weakReference!=null ? weakReference.get() : null;
    }

}
