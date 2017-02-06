package com.mvpproject.model;

/**
 * Created by zq on 2016/8/16.
 */

public interface ILoginSignIn {

    void onSignIn(String name,String pwd,IOnSetListenter listenter);

    interface IOnSetListenter
    {
        void onError(String error);

        void onSccess(String repsonce);
    }

}
