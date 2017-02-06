package com.mvpproject.presenter;

import com.mvpproject.model.ILoginSignIn;
import com.mvpproject.model.ModelSignIn;
import com.mvpproject.view.IView;

/**
 * Created by zq on 2016/8/16.
 */

public class Presenter extends BasePresenter<IView> {

    ILoginSignIn iLoginSignIn=new ModelSignIn();

    public void setSignIn(String name,String pwd)
    {
       iLoginSignIn.onSignIn(name, pwd, new ILoginSignIn.IOnSetListenter() {
           IView view=getView();
           @Override
           public void onError(String error) {
               if(view!=null){
                    view.showToast(error);
               }
           }

           @Override
           public void onSccess(String repsonce) {
               if(view!=null){
                   view.showToast(repsonce);
               }
           }
       });
    }
}
