#### Android之MVP模式实现登录和网络数据加载
<h5>MVP简介</h5> 
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

