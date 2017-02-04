# Android之MVC模式的使用
 <p>MVC全名是Model View Controller，是模型(model)－视图(view)－控制器(controller)的缩写，一种软件设计典范，用一种业务逻辑、数据、界面显示分离的方法组织代码，将业务逻辑聚集到一个部件里面，在改进和个性化定制界面及用户交互的同时，不需要重新编写业务逻辑。</p> 
<p>Model 层处理数据，业务逻辑等；</p> 
<p>View 层处理界面的显示结果；</p> 
<p>Controller层起到桥梁的作用，来控制V层和M层通信以此来达到分离视图显示和业务逻辑层。</p> 
<p>案例：</p> 
<pre><code class="language-java">public class MainActivity extends Activity implements Implement,OnClickListener {
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
</code></pre> 
<pre><code class="language-java">public class MainModel {

	public void loadData(TextView tv_view) {
		tv_view.setText("MVC模式在Android中的应用，Model获取数据");
	}

	/***
	 * 网络请求
	 * 
	 * @return 返回请求数据
	 */
	public Object httpData() {
		return "网络请求返回数据";
	}
}</code></pre> 
<p>&nbsp;</p> 
<p>MVC的优点：</p> 
<p>1.耦合性低。所谓耦合性就是模块代码之间的关联程度。利用MVC框架使得View（视图）层和Model（模型）层可以很好的分离，这样就达到了解耦的目的，所以耦合性低，减少模块代码之间的相互影响。</p> 
<p>2.可扩展性好。由于耦合性低，添加需求，扩展代码就可以减少修改之前的代码，降低bug的出现率。</p> 
<p>3.模块职责划分明确。主要划分层M,V,C三个模块，利于代码的维护。</p>
