package com.mvphttp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mvphttp.presenter.Presenter;
import com.mvphttp.view.BaseActivity;
import com.mvphttp.view.IView;
import com.mvphttp.widget.ProgessDialog;

public class MainActivity extends BaseActivity<IView,Presenter> implements IView,View.OnClickListener {

    private TextView textView;
    private ProgessDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView(){
        textView=(TextView) findViewById(R.id.text);
        findViewById(R.id.btn).setOnClickListener(this);
    }

    @Override
    public void onLoadContributorStart() {
        showProgress();
    }

    @Override
    public void onLoadContribtorComplete(String topContributor) {
       //得到主线程的数据
        Message msg=new Message();
        msg.what=1;
        msg.obj=topContributor;
        handler.sendMessage(msg);
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismissProgress();
            textView.setText(msg.obj.toString());
        }
    };

    @Override
    public void onClick(View v) {
        p.setGet();
    }

    @Override
    public Presenter createPresenter() {
        return new Presenter();
    }

    @Override
    public void onNetWork() {
        Toast.makeText(getApplicationContext(),"网络未连接",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError() {
        Toast.makeText(getApplicationContext(),"数据加载失败",Toast.LENGTH_LONG).show();
        textView.setText("");
    }

    /***
     * 启动
     */
    public void showProgress()
    {
        if(dialog==null)
        {
            dialog=new ProgessDialog(MainActivity.this);
        }
        dialog.showMessage("正在加载");
    }

    /***
     * 关闭
     */
    public void  dismissProgress()
    {
        if(dialog==null)
        {
            dialog=new ProgessDialog(this);
        }
        dialog.dismiss();
    }
}
