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
<pre class="hljs java"><span style="color:#cc7832"><span class="hljs-keyword">public</span> <span class="hljs-class"><span class="hljs-keyword">class</span> </span></span><span class="hljs-class"><span class="hljs-title">Presenter</span> </span><span style="color:#cc7832"><span class="hljs-class"><span class="hljs-keyword">extends</span> </span></span><span class="hljs-class"><span class="hljs-title">BasePresenter</span>&lt;<span class="hljs-title">IView</span>&gt; </span>{

    ILoginSignIn <span style="color:#9876aa">iLoginSignIn</span>=<span style="color:#cc7832"><span class="hljs-keyword">new</span> </span>ModelSignIn()<span style="color:#cc7832">;
</span>
<span style="color:#cc7832">    <span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">setSignIn</span></span></span><span class="hljs-function"><span class="hljs-params">(String name</span></span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-params">,</span></span></span><span class="hljs-function"><span class="hljs-params">String pwd)</span>
    </span>{
       <span style="color:#9876aa">iLoginSignIn</span>.<span style="color:#cc7833">onSignIn</span>(name<span style="color:#cc7832">, </span>pwd<span style="color:#cc7832">, <span class="hljs-keyword">new</span> </span>ILoginSignIn.IOnSetListenter() {
           IView <span style="color:#9876aa">view</span>=<span style="color:#cc7833">getView</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">           </span><span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">           </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onError</span></span></span><span class="hljs-function"><span class="hljs-params">(String error)</span> </span>{
               <span style="color:#cc7832"><span class="hljs-keyword">if</span></span>(<span style="color:#9876aa">view</span>!=<span style="color:#cc7832"><span class="hljs-keyword">null</span></span>){
                    <span style="color:#9876aa">view</span>.<span style="color:#cc7833">showToast</span>(error)<span style="color:#cc7832">;
</span><span style="color:#cc7832">               </span>}
           }

           <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">           </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onSccess</span></span></span><span class="hljs-function"><span class="hljs-params">(String repsonce)</span> </span>{
               <span style="color:#cc7832"><span class="hljs-keyword">if</span></span>(<span style="color:#9876aa">view</span>!=<span style="color:#cc7832"><span class="hljs-keyword">null</span></span>){
                   <span style="color:#9876aa">view</span>.<span style="color:#cc7833">showToast</span>(repsonce)<span style="color:#cc7832">;
</span><span style="color:#cc7832">               </span>}
           }
       })<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}
}</pre>
<p style="text-align:start">activity</p>
<pre class="hljs java"><span style="color:#cc7832"><span class="hljs-keyword">public</span> <span class="hljs-class"><span class="hljs-keyword">class</span> </span></span><span class="hljs-class"><span class="hljs-title">MainActivity</span> </span><span style="color:#cc7832"><span class="hljs-class"><span class="hljs-keyword">extends</span> </span></span><span class="hljs-class"><span class="hljs-title">BaseActivity</span>&lt;<span class="hljs-title">IView</span></span><span style="color:#cc7832"><span class="hljs-class">,</span></span><span class="hljs-class"><span class="hljs-title">Presenter</span>&gt; </span><span style="color:#cc7832"><span class="hljs-class"><span class="hljs-keyword">implements</span> </span></span><span class="hljs-class"><span class="hljs-title">IView</span></span><span style="color:#cc7832"><span class="hljs-class">,</span></span><span class="hljs-class"><span class="hljs-title">View</span>.<span class="hljs-title">OnClickListener</span></span>{

    EditText <span style="color:#9876aa">username</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>EditText <span style="color:#9876aa">password</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span><span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">protected</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onCreate</span></span></span><span class="hljs-function"><span class="hljs-params">(Bundle savedInstanceState)</span> </span>{
        <span style="color:#cc7832"><span class="hljs-keyword">super</span></span>.<span style="color:#cc7833">onCreate</span>(savedInstanceState)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">setContentView</span>(R.layout.<em>activity_main</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">initView</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">private</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">initView</span></span></span><span class="hljs-function"><span class="hljs-params">()</span>
    </span>{
        <span style="color:#9876aa">username</span>=(EditText)<span style="color:#cc7833">findViewById</span>(R.id.<em>username</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#9876aa">password</span>=(EditText)<span style="color:#cc7833">findViewById</span>(R.id.<em>password</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">findViewById</span>(R.id.<em>login</em>).<span style="color:#cc7833">setOnClickListener</span>(<span style="color:#cc7832"><span class="hljs-keyword">this</span></span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> </span></span><span class="hljs-function">Presenter </span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">createPersenter</span></span></span><span class="hljs-function"><span class="hljs-params">()</span> </span>{
        <span style="color:#cc7832"><span class="hljs-keyword">return</span> <span class="hljs-keyword">new</span> </span>Presenter()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onClick</span></span></span><span class="hljs-function"><span class="hljs-params">(View v)</span> </span>{
        String name=<span style="color:#9876aa">username</span>.<span style="color:#cc7833">getText</span>().<span style="color:#cc7833">toString</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>String pwd=<span style="color:#9876aa">password</span>.<span style="color:#cc7833">getText</span>().<span style="color:#cc7833">toString</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#9876aa">p</span>.<span style="color:#cc7833">setSignIn</span>(name<span style="color:#cc7832">,</span>pwd)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">showToast</span></span></span><span class="hljs-function"><span class="hljs-params">(String msg)</span> </span>{
        Toast.<em>makeText</em>(<span style="color:#cc7833">getApplicationContext</span>()<span style="color:#cc7832">,</span>msg<span style="color:#cc7832">,</span>Toast.<em>LENGTH_LONG</em>).<span style="color:#cc7833">show</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}
}</pre>
<p>网络请求</p>
<pre class="hljs java"><span style="color:#cc7832"><span class="hljs-keyword">public</span> <span class="hljs-class"><span class="hljs-keyword">class</span> </span></span><span class="hljs-class"><span class="hljs-title">MainActivity</span> </span><span style="color:#cc7832"><span class="hljs-class"><span class="hljs-keyword">extends</span> </span></span><span class="hljs-class"><span class="hljs-title">BaseActivity</span>&lt;<span class="hljs-title">IView</span></span><span style="color:#cc7832"><span class="hljs-class">,</span></span><span class="hljs-class"><span class="hljs-title">Presenter</span>&gt; </span><span style="color:#cc7832"><span class="hljs-class"><span class="hljs-keyword">implements</span> </span></span><span class="hljs-class"><span class="hljs-title">IView</span></span><span style="color:#cc7832"><span class="hljs-class">,</span></span><span class="hljs-class"><span class="hljs-title">View</span>.<span class="hljs-title">OnClickListener</span> </span>{

    <span style="color:#cc7832"><span class="hljs-keyword">private</span> </span>TextView <span style="color:#9876aa">textView</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">    <span class="hljs-keyword">private</span> </span>ProgessDialog <span style="color:#9876aa">dialog</span><span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span><span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">protected</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onCreate</span></span></span><span class="hljs-function"><span class="hljs-params">(Bundle savedInstanceState)</span> </span>{
        <span style="color:#cc7832"><span class="hljs-keyword">super</span></span>.<span style="color:#cc7833">onCreate</span>(savedInstanceState)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">setContentView</span>(R.layout.<em>activity_main</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">initView</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">private</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">initView</span></span></span><span class="hljs-function"><span class="hljs-params">()</span></span>{
        <span style="color:#9876aa">textView</span>=(TextView) <span style="color:#cc7833">findViewById</span>(R.id.<em>text</em>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#cc7833">findViewById</span>(R.id.<em>btn</em>).<span style="color:#cc7833">setOnClickListener</span>(<span style="color:#cc7832"><span class="hljs-keyword">this</span></span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onLoadContributorStart</span></span></span><span class="hljs-function"><span class="hljs-params">()</span> </span>{
        <span style="color:#cc7833">showProgress</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onLoadContribtorComplete</span></span></span><span class="hljs-function"><span class="hljs-params">(String topContributor)</span> </span>{
       <span style="color:#808080"><span class="hljs-comment">//得到主线程的数据</span>
</span><span style="color:#808080">        </span>Message msg=<span style="color:#cc7832"><span class="hljs-keyword">new</span> </span><span style="color:#cc7833">Message</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>msg.<span style="color:#9876aa">what</span>=<span style="color:#6897bb"><span class="hljs-number">1</span></span><span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>msg.<span style="color:#9876aa">obj</span>=topContributor<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#9876aa">handler</span>.<span style="color:#cc7833">sendMessage</span>(msg)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}
    Handler <span style="color:#9876aa">handler</span>=<span style="color:#cc7832"><span class="hljs-keyword">new</span> </span>Handler(){
        <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">        </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">handleMessage</span></span></span><span class="hljs-function"><span class="hljs-params">(Message msg)</span> </span>{
            <span style="color:#cc7832"><span class="hljs-keyword">super</span></span>.<span style="color:#cc7833">handleMessage</span>(msg)<span style="color:#cc7832">;
</span><span style="color:#cc7832">            </span><span style="color:#cc7833">dismissProgress</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">            </span><span style="color:#9876aa">textView</span>.<span style="color:#cc7833">setText</span>(msg.<span style="color:#9876aa">obj</span>.<span style="color:#cc7833">toString</span>())<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>}
    }<span style="color:#cc7832">;
</span>
<span style="color:#cc7832">    </span><span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onClick</span></span></span><span class="hljs-function"><span class="hljs-params">(View v)</span> </span>{
        <span style="color:#9876aa">p</span>.<span style="color:#cc7833">setGet</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> </span></span><span class="hljs-function">Presenter </span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">createPresenter</span></span></span><span class="hljs-function"><span class="hljs-params">()</span> </span>{
        <span style="color:#cc7832"><span class="hljs-keyword">return</span> <span class="hljs-keyword">new</span> </span>Presenter()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onNetWork</span></span></span><span class="hljs-function"><span class="hljs-params">()</span> </span>{
        Toast.<em>makeText</em>(<span style="color:#cc7833">getApplicationContext</span>()<span style="color:#cc7832">,</span><span style="color:#6a8759"><span class="hljs-string">&quot;网络未连接&quot;</span></span><span style="color:#cc7832">,</span>Toast.<em>LENGTH_LONG</em>).<span style="color:#cc7833">show</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#bbb529"><span class="hljs-meta">@Override</span>
</span><span style="color:#bbb529">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">onError</span></span></span><span class="hljs-function"><span class="hljs-params">()</span> </span>{
        Toast.<em>makeText</em>(<span style="color:#cc7833">getApplicationContext</span>()<span style="color:#cc7832">,</span><span style="color:#6a8759"><span class="hljs-string">&quot;数据加载失败&quot;</span></span><span style="color:#cc7832">,</span>Toast.<em>LENGTH_LONG</em>).<span style="color:#cc7833">show</span>()<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span><span style="color:#9876aa">textView</span>.<span style="color:#cc7833">setText</span>(<span style="color:#6a8759"><span class="hljs-string">&quot;&quot;</span></span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#629755"><span class="hljs-comment">/***
</span></span><span style="color:#629755"><span class="hljs-comment">     * 启动
</span></span><span style="color:#629755"><span class="hljs-comment">     */</span>
</span><span style="color:#629755">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span> </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">showProgress</span></span></span><span class="hljs-function"><span class="hljs-params">()</span>
    </span>{
        <span style="color:#cc7832"><span class="hljs-keyword">if</span></span>(<span style="color:#9876aa">dialog</span>==<span style="color:#cc7832"><span class="hljs-keyword">null</span></span>)
        {
            <span style="color:#9876aa">dialog</span>=<span style="color:#cc7832"><span class="hljs-keyword">new</span> </span><span style="color:#cc7833">ProgessDialog</span>(MainActivity.<span style="color:#cc7832"><span class="hljs-keyword">this</span></span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>}
        <span style="color:#9876aa">dialog</span>.<span style="color:#cc7833">showMessage</span>(<span style="color:#6a8759"><span class="hljs-string">&quot;正在加载&quot;</span></span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">    </span>}

    <span style="color:#629755"><span class="hljs-comment">/***
</span></span><span style="color:#629755"><span class="hljs-comment">     * 关闭
</span></span><span style="color:#629755"><span class="hljs-comment">     */</span>
</span><span style="color:#629755">    </span><span style="color:#cc7832"><span class="hljs-function"><span class="hljs-keyword">public</span> <span class="hljs-keyword">void</span>  </span></span><span style="color:#ffc66d"><span class="hljs-function"><span class="hljs-title">dismissProgress</span></span></span><span class="hljs-function"><span class="hljs-params">()</span>
    </span>{
        <span style="color:#cc7832"><span class="hljs-keyword">if</span></span>(<span style="color:#9876aa">dialog</span>==<span style="color:#cc7832"><span class="hljs-keyword">null</span></span>)
        {
            <span style="color:#9876aa">dialog</span>=<span style="color:#cc7832"><span class="hljs-keyword">new</span> </span><span style="color:#cc7833">ProgessDialog</span>(<span style="color:#cc7832"><span class="hljs-keyword">this</span></span>)<span style="color:#cc7832">;
</span><span style="color:#cc7832">        </span>}
        <span style="color:#9876aa">dialog</span>.<span style="color:#cc7833">dismiss</span>()<span style="color:#cc7832">;
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
<p>presenter</p>
<div class="cnblogs_code">
<pre><span style="color: #0000ff">public</span> <span style="color: #0000ff">class</span> Presenter <span style="color: #0000ff">extends</span> BasePresenter&lt;IView&gt;<span style="color: #000000"> {

    ILoginSignIn iLoginSignIn</span>=<span style="color: #0000ff">new</span><span style="color: #000000"> ModelSignIn();

    </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> setSignIn(String name,String pwd)
    {
       iLoginSignIn.onSignIn(name, pwd, </span><span style="color: #0000ff">new</span><span style="color: #000000"> ILoginSignIn.IOnSetListenter() {
           IView view</span>=<span style="color: #000000">getView();
           @Override
           </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onError(String error) {
               </span><span style="color: #0000ff">if</span>(view!=<span style="color: #0000ff">null</span><span style="color: #000000">){
                    view.showToast(error);
               }
           }

           @Override
           </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onSccess(String repsonce) {
               </span><span style="color: #0000ff">if</span>(view!=<span style="color: #0000ff">null</span><span style="color: #000000">){
                   view.showToast(repsonce);
               }
           }
       });
    }
}</span></pre>
</div>
<p>activity</p>
<div class="cnblogs_code">
<pre><span style="color: #0000ff">public</span> <span style="color: #0000ff">class</span> MainActivity <span style="color: #0000ff">extends</span> BaseActivity&lt;IView,Presenter&gt; <span style="color: #0000ff">implements</span><span style="color: #000000"> IView,View.OnClickListener{

    EditText username;
    EditText password;
    @Override
    </span><span style="color: #0000ff">protected</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onCreate(Bundle savedInstanceState) {
        </span><span style="color: #0000ff">super</span><span style="color: #000000">.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    </span><span style="color: #0000ff">private</span> <span style="color: #0000ff">void</span><span style="color: #000000"> initView()
    {
        username</span>=<span style="color: #000000">(EditText)findViewById(R.id.username);
        password</span>=<span style="color: #000000">(EditText)findViewById(R.id.password);
        findViewById(R.id.login).setOnClickListener(</span><span style="color: #0000ff">this</span><span style="color: #000000">);
    }

    @Override
    </span><span style="color: #0000ff">public</span><span style="color: #000000"> Presenter createPersenter() {
        </span><span style="color: #0000ff">return</span> <span style="color: #0000ff">new</span><span style="color: #000000"> Presenter();
    }

    @Override
    </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onClick(View v) {
        String name</span>=<span style="color: #000000">username.getText().toString();
        String pwd</span>=<span style="color: #000000">password.getText().toString();
        p.setSignIn(name,pwd);
    }

    @Override
    </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> showToast(String msg) {
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }
}</span></pre>
</div>
<p>网络请求</p>
<div class="cnblogs_code">
<pre><span style="color: #0000ff">public</span> <span style="color: #0000ff">class</span> MainActivity <span style="color: #0000ff">extends</span> BaseActivity&lt;IView,Presenter&gt; <span style="color: #0000ff">implements</span><span style="color: #000000"> IView,View.OnClickListener {

    </span><span style="color: #0000ff">private</span><span style="color: #000000"> TextView textView;
    </span><span style="color: #0000ff">private</span><span style="color: #000000"> ProgessDialog dialog;
    @Override
    </span><span style="color: #0000ff">protected</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onCreate(Bundle savedInstanceState) {
        </span><span style="color: #0000ff">super</span><span style="color: #000000">.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    </span><span style="color: #0000ff">private</span> <span style="color: #0000ff">void</span><span style="color: #000000"> initView(){
        textView</span>=<span style="color: #000000">(TextView) findViewById(R.id.text);
        findViewById(R.id.btn).setOnClickListener(</span><span style="color: #0000ff">this</span><span style="color: #000000">);
    }

    @Override
    </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onLoadContributorStart() {
        showProgress();
    }

    @Override
    </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onLoadContribtorComplete(String topContributor) {
       </span><span style="color: #008000">//</span><span style="color: #008000">得到主线程的数据</span>
        Message msg=<span style="color: #0000ff">new</span><span style="color: #000000"> Message();
        msg.what</span>=1<span style="color: #000000">;
        msg.obj</span>=<span style="color: #000000">topContributor;
        handler.sendMessage(msg);
    }
    Handler handler</span>=<span style="color: #0000ff">new</span><span style="color: #000000"> Handler(){
        @Override
        </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> handleMessage(Message msg) {
            </span><span style="color: #0000ff">super</span><span style="color: #000000">.handleMessage(msg);
            dismissProgress();
            textView.setText(msg.obj.toString());
        }
    };

    @Override
    </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onClick(View v) {
        p.setGet();
    }

    @Override
    </span><span style="color: #0000ff">public</span><span style="color: #000000"> Presenter createPresenter() {
        </span><span style="color: #0000ff">return</span> <span style="color: #0000ff">new</span><span style="color: #000000"> Presenter();
    }

    @Override
    </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onNetWork() {
        Toast.makeText(getApplicationContext(),</span>"网络未连接"<span style="color: #000000">,Toast.LENGTH_LONG).show();
    }

    @Override
    </span><span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> onError() {
        Toast.makeText(getApplicationContext(),</span>"数据加载失败"<span style="color: #000000">,Toast.LENGTH_LONG).show();
        textView.setText(</span>""<span style="color: #000000">);
    }

    </span><span style="color: #008000">/**</span><span style="color: #008000">*
     * 启动
     </span><span style="color: #008000">*/</span>
    <span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000"> showProgress()
    {
        </span><span style="color: #0000ff">if</span>(dialog==<span style="color: #0000ff">null</span><span style="color: #000000">)
        {
            dialog</span>=<span style="color: #0000ff">new</span> ProgessDialog(MainActivity.<span style="color: #0000ff">this</span><span style="color: #000000">);
        }
        dialog.showMessage(</span>"正在加载"<span style="color: #000000">);
    }

    </span><span style="color: #008000">/**</span><span style="color: #008000">*
     * 关闭
     </span><span style="color: #008000">*/</span>
    <span style="color: #0000ff">public</span> <span style="color: #0000ff">void</span><span style="color: #000000">  dismissProgress()
    {
        </span><span style="color: #0000ff">if</span>(dialog==<span style="color: #0000ff">null</span><span style="color: #000000">)
        {
            dialog</span>=<span style="color: #0000ff">new</span> ProgessDialog(<span style="color: #0000ff">this</span><span style="color: #000000">);
        }
        dialog.dismiss();
    }
}</span></pre>
</div>
<p>不要忘记在AndroidManifest.xml加权限哦！</p> 
<pre><code class="language-html">&lt;uses-permission android:name="android.permission.INTERNET"/&gt;</code></pre> 
