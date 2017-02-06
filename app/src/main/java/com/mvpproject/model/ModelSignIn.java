package com.mvpproject.model;

/**
 * Created by zq on 2016/8/16.
 */

public class ModelSignIn implements ILoginSignIn {

    @Override
    public void onSignIn(String name, String pwd, IOnSetListenter listenter) {
        if (name.isEmpty())
        {
            if (listenter!=null)
            {
                listenter.onError("输入用户名为空");
                return;
            }
        }
        if (pwd.isEmpty())
        {
            if (listenter!=null)
            {
                listenter.onError("输入密码为空");
                return;
            }
        }
        if(name.equals("dickyqie") && pwd.equals("123456"))
        {
            if (listenter!=null)
            {
                listenter.onError("登录成功");
                return;
            }
        }else{
            if (listenter!=null)
            {
                listenter.onError("登录失败");
                return;
            }
        }
    }
}
