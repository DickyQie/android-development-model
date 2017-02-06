# Android之MVP模式实现登录和网络数据加载
h2>MVP简介</h2> 
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
<pre><span style="color:#cc7832">public class </span>Presenter <span style="color:#cc7832">extends </span>BasePresenter&lt;IView&gt; {

    ILoginSignIn <span style="color:#9876aa">iLoginSignIn</span>=<span style="color:#cc7832">new </span>ModelSignIn()<span style="color:#cc7832">;
</span>
<span style="color:#cc7832">    public void </span><span style="color:#ffc66d">setSignIn</span>(String name<span style="color:#cc7832">,</span>String pwd)
    {
       <span style="color:#9876aa">iLoginSignIn</span>.<span style="color:#cc7833">onSignIn</span>(name<span style="color:#cc7832">, </span>pwd<span style="color:#cc7832">, new </span>ILoginSignIn.IOnSetListenter() {
           IView <span style="color:#9876aa">view</span>=<span style="color:#cc7833">getView</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">           </span><span style="color:#bbb529">@Override
</span><span style="color:#bbb529">           </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">onError</span>(String error) {
               <span style="color:#cc7832">if</span>(<span style="color:#9876aa">view</span>!=<span style="color:#cc7832">null</span>){
                    <span style="color:#9876aa">view</span>.<span style="color:#cc7833">showToast</span>(error)<span style="color:#cc7832">;
</span><span style="color:#cc7832">               </span>}
           }

           <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">           </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">onSccess</span>(String repsonce) {
               <span style="color:#cc7832">if</span>(<span style="color:#9876aa">view</span>!=<span style="color:#cc7832">null</span>){
                   <span style="color:#9876aa">view</span>.<span style="color:#cc7833">showToast</span>(repsonce)<span style="color:#cc7832">;
</span><span style="color:#cc7832">               </span>}
           }
       })<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}
}</pre> 
<p style="text-align:start">activity</p> 
<pre><span style="color:#cc7832">public class </span>MainActivity <span style="color:#cc7832">extends </span>BaseActivity&lt;IView<span style="color:#cc7832">,</span>Presenter&gt; <span style="color:#cc7832">implements </span>IView<span style="color:#cc7832">,</span>View.OnClickListener{

    EditText <span style="color:#9876aa">username</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>EditText <span style="color:#9876aa">password</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span><span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">protected void </span><span style="color:#ffc66d">onCreate</span>(Bundle savedInstanceState) {
        <span style="color:#cc7832">super</span>.<span style="color:#cc7833">onCreate</span>(savedInstanceState)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">setContentView</span>(R.layout.<em>activity_main</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">initView</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#cc7832">private void </span><span style="color:#ffc66d">initView</span>()
    {
        <span style="color:#9876aa">username</span>=(EditText)<span style="color:#cc7833">findViewById</span>(R.id.<em>username</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#9876aa">password</span>=(EditText)<span style="color:#cc7833">findViewById</span>(R.id.<em>password</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">findViewById</span>(R.id.<em>login</em>).<span style="color:#cc7833">setOnClickListener</span>(<span style="color:#cc7832">this</span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public </span>Presenter <span style="color:#ffc66d">createPersenter</span>() {
        <span style="color:#cc7832">return new </span>Presenter()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">onClick</span>(View v) {
        String name=<span style="color:#9876aa">username</span>.<span style="color:#cc7833">getText</span>().<span style="color:#cc7833">toString</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>String pwd=<span style="color:#9876aa">password</span>.<span style="color:#cc7833">getText</span>().<span style="color:#cc7833">toString</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#9876aa">p</span>.<span style="color:#cc7833">setSignIn</span>(name<span style="color:#cc7832">,</span>pwd)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">showToast</span>(String msg) {
        Toast.<em>makeText</em>(<span style="color:#cc7833">getApplicationContext</span>()<span style="color:#cc7832">,</span>msg<span style="color:#cc7832">,</span>Toast.<em>LENGTH_LONG</em>).<span style="color:#cc7833">show</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}
}</pre> 
<p>网络请求</p> 
<pre><span style="color:#cc7832">public class </span>MainActivity <span style="color:#cc7832">extends </span>BaseActivity&lt;IView<span style="color:#cc7832">,</span>Presenter&gt; <span style="color:#cc7832">implements </span>IView<span style="color:#cc7832">,</span>View.OnClickListener {

    <span style="color:#cc7832">private </span>TextView <span style="color:#9876aa">textView</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">    private </span>ProgessDialog <span style="color:#9876aa">dialog</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span><span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">protected void </span><span style="color:#ffc66d">onCreate</span>(Bundle savedInstanceState) {
        <span style="color:#cc7832">super</span>.<span style="color:#cc7833">onCreate</span>(savedInstanceState)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">setContentView</span>(R.layout.<em>activity_main</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">initView</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#cc7832">private void </span><span style="color:#ffc66d">initView</span>(){
        <span style="color:#9876aa">textView</span>=(TextView) <span style="color:#cc7833">findViewById</span>(R.id.<em>text</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">findViewById</span>(R.id.<em>btn</em>).<span style="color:#cc7833">setOnClickListener</span>(<span style="color:#cc7832">this</span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">onLoadContributorStart</span>() {
        <span style="color:#cc7833">showProgress</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">onLoadContribtorComplete</span>(String topContributor) {
       <span style="color:#808080">//得到主线程的数据
</span><span style="color:#808080">        </span>Message msg=<span style="color:#cc7832">new </span><span style="color:#cc7833">Message</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>msg.<span style="color:#9876aa">what</span>=<span style="color:#6897bb">1</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>msg.<span style="color:#9876aa">obj</span>=topContributor<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#9876aa">handler</span>.<span style="color:#cc7833">sendMessage</span>(msg)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}
    Handler <span style="color:#9876aa">handler</span>=<span style="color:#cc7832">new </span>Handler(){
        <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">        </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">handleMessage</span>(Message msg) {
            <span style="color:#cc7832">super</span>.<span style="color:#cc7833">handleMessage</span>(msg)<span style="color:#cc7832">;
</span><span style="color:#cc7832">            </span><span style="color:#cc7833">dismissProgress</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">            </span><span style="color:#9876aa">textView</span>.<span style="color:#cc7833">setText</span>(msg.<span style="color:#9876aa">obj</span>.<span style="color:#cc7833">toString</span>())<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>}
    }<span style="color:#cc7832">;
</span>
<span style="color:#cc7832">    </span><span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">onClick</span>(View v) {
        <span style="color:#9876aa">p</span>.<span style="color:#cc7833">setGet</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public </span>Presenter <span style="color:#ffc66d">createPresenter</span>() {
        <span style="color:#cc7832">return new </span>Presenter()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">onNetWork</span>() {
        Toast.<em>makeText</em>(<span style="color:#cc7833">getApplicationContext</span>()<span style="color:#cc7832">,</span><span style="color:#6a8759">"网络未连接"</span><span style="color:#cc7832">,</span>Toast.<em>LENGTH_LONG</em>).<span style="color:#cc7833">show</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529">@Override
</span><span style="color:#bbb529">    </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">onError</span>() {
        Toast.<em>makeText</em>(<span style="color:#cc7833">getApplicationContext</span>()<span style="color:#cc7832">,</span><span style="color:#6a8759">"数据加载失败"</span><span style="color:#cc7832">,</span>Toast.<em>LENGTH_LONG</em>).<span style="color:#cc7833">show</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#9876aa">textView</span>.<span style="color:#cc7833">setText</span>(<span style="color:#6a8759">""</span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#629755">/***
</span><span style="color:#629755">     * 启动
</span><span style="color:#629755">     */
</span><span style="color:#629755">    </span><span style="color:#cc7832">public void </span><span style="color:#ffc66d">showProgress</span>()
    {
        <span style="color:#cc7832">if</span>(<span style="color:#9876aa">dialog</span>==<span style="color:#cc7832">null</span>)
        {
            <span style="color:#9876aa">dialog</span>=<span style="color:#cc7832">new </span><span style="color:#cc7833">ProgessDialog</span>(MainActivity.<span style="color:#cc7832">this</span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>}
        <span style="color:#9876aa">dialog</span>.<span style="color:#cc7833">showMessage</span>(<span style="color:#6a8759">"正在加载"</span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#629755">/***
</span><span style="color:#629755">     * 关闭
</span><span style="color:#629755">     */
</span><span style="color:#629755">    </span><span style="color:#cc7832">public void  </span><span style="color:#ffc66d">dismissProgress</span>()
    {
        <span style="color:#cc7832">if</span>(<span style="color:#9876aa">dialog</span>==<span style="color:#cc7832">null</span>)
        {
            <span style="color:#9876aa">dialog</span>=<span style="color:#cc7832">new </span><span style="color:#cc7833">ProgessDialog</span>(<span style="color:#cc7832">this</span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>}
        <span style="color:#9876aa">dialog</span>.<span style="color:#cc7833">dismiss</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}
}</pre> 
<p>不要忘记在AndroidManifest.xml加权限哦！</p> 
<pre><code class="language-html">&lt;uses-permission android:name="android.permission.INTERNET"/&gt;</code></pre> 
