package com.mvphttp.view;

import android.app.Activity;
import android.os.Bundle;

import com.mvphttp.presenter.BasePresenter;

/**
 * Created by zq on 2016/8/16.
 */

public abstract class BaseActivity<V,T extends BasePresenter<V>> extends Activity
{
    protected T p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p=createPresenter();
        p.attachView((V)this);
    }

    public abstract T createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        p.deachView();
    }
}