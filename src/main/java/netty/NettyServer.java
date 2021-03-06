package netty;

import static netty.NettyConfig.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.ResourceLeakDetector;

import java.util.concurrent.ExecutionException;

/**
 * <p>
 * 使用hessian2序列化消息回复NettyClient
 * </p>
 * Created by pomelo on 16/10/21.
 */
public class NettyServer {

    /**
     *
     */
    private static ServerBootstrap bootstrap;

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        bootstrap = new ServerBootstrap();
        bootstrap.group(boss,
                        worker).channel(NioServerSocketChannel.class).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new ChannelInitializer<SocketChannel>() {

                            @Override
                            public void initChannel(SocketChannel ch) throws Exception {
                                ChannelPipeline p = ch.pipeline();

//                                p.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
//                                p.addLast(new ObjectEncoder());

                                p.addLast("logger", new LoggingHandler(LogLevel.DEBUG));
                                p.addLast("timer", new IdleStateHandler(2000, 5000, 5000));
                                p.addLast("decoder", new CustomHessianDecoder());
                                p.addLast("encoder", new CustomHessianEncoder());
                                // 异步业务线程处理
                                EventLoopGroup business = new NioEventLoopGroup();
                                p.addLast(business, new NettyServerHandler());
                            }
                        });

        // Start the server.
        ChannelFuture ch = bootstrap.bind(PORT).sync().channel().closeFuture().sync();

        // 检测内存泄露
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

        System.out.println("start server success, you can start client to send massage to invoke this server");

        // Wait until the connection is closed.
        ch.channel().closeFuture().sync();

    }
}
