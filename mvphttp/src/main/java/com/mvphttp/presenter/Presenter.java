package com.mvphttp.presenter;

import com.mvphttp.callback.CallBackListenter;
import com.mvphttp.modle.ILoadHttp;
import com.mvphttp.modle.ModleRequest;
import com.mvphttp.view.IView;

/**
 * Created by zq on 2016/8/16.
 */

public class Presenter extends BasePresenter<IView> {

    ILoadHttp loadHttp=new ModleRequest();

    public void setGet(){
        if (loadHttp!=null)
        {

            if (false)//验证有误网络
            {
                getView().onNetWork();
                return;
            }
            if(getView()!=null)
            {
                getView().onLoadContributorStart();
            }
            loadHttp.onGet(new CallBackListenter() {
                IView view=getView();
                @Override
                public void onDataCallBackListenter(String string) {
                    if(view!=null)
                    {
                        view.onLoadContribtorComplete(string);
                    }
                }

                @Override
                public void onError(String error) {
                    view.onError();
                }
            });
        }
    }

}
