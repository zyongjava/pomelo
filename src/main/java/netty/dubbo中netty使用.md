## dubbo远程调用过程


### 一、Invoker发送消息到服务器
>1.  首先是`FailoverClusterInvoker`的`invoke()`方法
>2. `FailoverClusterInvoker`的`doInvoke()`方法选择负载均衡(loadbalance)方式调用后续方法
>3. `InvokerWrapper`的`invoke()`方法
>4. `FutureFilter`的`invoke`方法拦截后续`invoke`方法，并设置异步或同步返回值
>5. `ListenerInvokerWrapper`的`invoke()`方法
>6. `DubboInvoker`的`doInvoke()`方法使用`HeaderExchangeClient`(netty实现)发送请求,最后采用`ExchangeCodec`的`encodeRequest()`方法序列化（默认序列化协议`hessian2`）数据
>7. `HeaderExchangeClient`调用`NettyClient`的`send()`方法
>8. `DefaultFuture`异步阻塞线程`RemotingInvocationTimeoutScan`检测是否超时，设置`callback`接收返回值

### 二、线程接收服务器返回值

>1. `NettyHandler`的`messageReceived()`方法接收消息,传递给`AllChannelHandler`处理
>2. `AllChannelHandler`的`received()`方法启动`ChannelEventRunnable`线程接收服务端`RECEIVED`事件
>3. `DecodeHandler`将结果解码(decode)
>4. `HeaderExchangeHandler`的`received()`方法接收返回结果
>5. `DefaultFuture`的`received()`方法设置`callback`返回值

`NettyHandler `设置netty client里

```
ClientBootstrap bootstrap = new ClientBootstrap(channelFactory);
// config
// @see org.jboss.netty.channel.socket.SocketChannelConfig
bootstrap.setOption("keepAlive", true);
bootstrap.setOption("tcpNoDelay", true);
bootstrap.setOption("connectTimeoutMillis", getTimeout());
final NettyHandler nettyHandler = new NettyHandler(getUrl(), this);
bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
    public ChannelPipeline getPipeline() {
        NettyCodecAdapter adapter = new NettyCodecAdapter(getCodec(), getUrl(), NettyClient.this);
        ChannelPipeline pipeline = Channels.pipeline();
        pipeline.addLast("decoder", adapter.getDecoder());
        pipeline.addLast("encoder", adapter.getEncoder());
        pipeline.addLast("handler", nettyHandler);
        return pipeline;
    }
});
```



### 三、创建远程调用（Invoker）流程图
![dubbo consumer](http://img.blog.csdn.net/20170526131416142?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvemhlbmd5b25nMTU5ODQyODU2MjM=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

### 四、参考
1. http://www.tuicool.com/articles/z6BzIn7
2. https://github.com/dangdangdotcom/dubbox



