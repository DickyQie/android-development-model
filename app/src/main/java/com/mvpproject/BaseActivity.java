package com.mvpproject;

import android.app.Activity;
import android.os.Bundle;

import com.mvpproject.presenter.BasePresenter;

/**
 * Created by zq on 2016/8/16.
 */

public abstract class BaseActivity<V,T extends BasePresenter<V>> extends Activity {

    protected T p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p=createPersenter();
        p.attchView((V)this);
    }
    public abstract T createPersenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁对象
        p.deatchView();
    }
}
