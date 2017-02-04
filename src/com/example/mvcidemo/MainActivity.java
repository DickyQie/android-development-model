package com.example.mvcidemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/****
 * 
 * MVC模式
 * @author zq
 *
 */
public class MainActivity extends Activity implements Implement,OnClickListener {
	private TextView tv_view;
	private MainModel mModel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.th);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		mModel = new MainModel();
		tv_view = (TextView) this.findViewById(R.id.tv_view);
		findViewById(R.id.btn).setOnClickListener(this);
		findViewById(R.id.btn1).setOnClickListener(this);
	}

	@Override
	public void loadData() {
		mModel.loadData(tv_view);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn:
			loadData();
			break;
		case R.id.btn1:
			httpData();
			break;
		default:
			break;
		}
	}
	@SuppressLint("HandlerLeak") @Override
	public void httpData() {
		
		Object data=mModel.httpData();
		if(data!=null)
		{
			tv_view.setText(data.toString());
		}else{
			onError();
		}
	}
	
	
	@Override
	public void onError() {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), "加载数据失败",Toast.LENGTH_LONG).show();
	}
	

}
