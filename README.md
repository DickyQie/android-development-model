# Android之MVP模式实现登录和网络数据加载
<h2>MVP简介</h2> 
<p style="text-align:start">相信大家对 MVC 都是比较熟悉了：M-Model-模型、V-View-视图、C-Controller-控制器，MVP作为MVC的演化版本，也是作为用户界面（用户层）的实现模式，那么类似的MVP所对应的意义：M-Model-模型、V-View-视图、P-Presenter-表示器。</p> 
<p style="text-align:start">MVC详见博客： <a href="https://my.oschina.net/zhangqie/blog/831622" rel="nofollow">Android之MVC模式的使用 </a></p> 
<span id="OSC_h3_2"></span>
<h3><span style="color:#008080">Model</span></h3> 
<p style="text-align:start"><span style="background-color:rgb(255, 255, 255); color:rgb(33, 33, 33)">Model 是用户界面需要显示数据的抽象，也可以理解为从业务数据（结果）那里到用户界面的抽象。</span></p> 
<span id="OSC_h3_3"></span>
<h3><strong><span style="color:#008080">View</span></strong></h3> 
<p style="text-align:start"><span style="background-color:rgb(255, 255, 255); color:rgb(33, 33, 33)">视图这一层体现的很轻薄，负责显示数据、提供友好界面跟用户交互就行。MVP下Activity和Fragment体现在了这一层，Activity一般也就做加载UI视图、设置监听再交由Presenter处理的一些工作，所以也就需要持有相应Presenter的引用。</span></p> 
<span id="OSC_h3_4"></span>
<h3><strong><span style="color:#008080">Presenter</span></strong></h3> 
<p><span style="background-color:rgb(255, 255, 255); color:rgb(33, 33, 33)">Presenter这一层处理着程序各种逻辑的分发，收到View层UI上的反馈命令、定时命令、系统命令等指令后分发处理逻辑交由业务层做具体的业务操作，然后将得到的 Model 给 View 显示。</span></p> 
<p><strong><span style="color:#B22222">MVC</span></strong>和<span style="color:#B22222"><strong>MVP</strong></span>的区别</p> 
<p style="text-align:start">在&nbsp;<span style="color:#B22222"><strong>MVC</strong>&nbsp;</span>中：</p> 
<ul> 
 <li><strong>View</strong>&nbsp;可以与&nbsp;<strong>Model</strong>&nbsp;直接交互；</li> 
 <li><strong>Controller</strong>&nbsp;可以被多个&nbsp;<strong>View</strong>&nbsp;共享；</li> 
 <li><strong>Controller</strong>&nbsp;可以决定显示哪个&nbsp;<strong>View</strong>&nbsp;。</li> 
</ul> 
<p style="text-align:start">在&nbsp;<span style="color:#B22222"><strong>MVP</strong>&nbsp;</span>中：</p> 
<ul> 
 <li><strong>View</strong>&nbsp;不直接与&nbsp;<strong>Model</strong>&nbsp;交互；</li> 
 <li><strong>Presenter</strong>&nbsp;与&nbsp;<strong>View</strong>&nbsp;通过接口来交互，更有利于添加单元测试；</li> 
 <li>通常&nbsp;<strong>View</strong>&nbsp;与&nbsp;<strong>Presenter</strong>&nbsp;是一对一的，但复杂的&nbsp;<strong>View</strong>&nbsp;可能绑定多个&nbsp;<strong>Presenter</strong>&nbsp;来处理；</li> 
 <li>Presenter 也可以直接进行&nbsp;<strong>View</strong>&nbsp;上的渲染。</li> 
</ul> 
<p>模仿登录案例：（<span style="color:#4B0082">demo中包含登录和网络数据请求</span>）</p> 
<p>model</p> 
<pre><code class="language-java">public class ModelSignIn implements ILoginSignIn {

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
        if(name.equals("dickyqie") &amp;&amp; pwd.equals("123456"))
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
}</code></pre> 
<p style="text-align:start">presenter</p> 
<pre><code class="language-java">public class Presenter extends BasePresenter&lt;IView&gt; {

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
</code></pre> 
<p style="text-align:start">activity</p> 
<pre><code class="language-javascript">public class MainActivity extends BaseActivity&lt;IView,Presenter&gt; implements IView,View.OnClickListener{

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
}</code></pre> 
<p>网络请求</p> 
<pre><code class="language-java">public class MainActivity extends BaseActivity&lt;IView,Presenter&gt; implements IView,View.OnClickListener {

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
}</code></pre> 
<p>不要忘记在AndroidManifest.xml加权限哦！</p> 
<pre><code class="language-html">&lt;uses-permission android:name="android.permission.INTERNET"/&gt;</code></pre> 
<span id="OSC_h3_5"></span>
<h3><span style="color:#B22222"><strong>由于代码太多，完整代码未给出，源码直接下载即可</strong></span></h3> 
