package com.mvphttp.view;

/**
 * Created by zq on 2016/8/16.
 */

public interface IView {

    void onLoadContributorStart();

    void onLoadContribtorComplete(String topContributor);

    void onNetWork();

    void onError();


}
