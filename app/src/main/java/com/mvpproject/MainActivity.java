package com.mvpproject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mvpproject.presenter.Presenter;
import com.mvpproject.view.IView;

public class MainActivity extends BaseActivity<IView,Presenter> implements IView,View.OnClickListener{

    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView()
    {
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(this);
    }

    @Override
    public Presenter createPersenter() {
        return new Presenter();
    }

    @Override
    public void onClick(View v) {
        String name=username.getText().toString();
        String pwd=password.getText().toString();
        p.setSignIn(name,pwd);
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
}
