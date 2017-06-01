package netty;

import static netty.NettyConfig.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.channel.Channel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by zhengyong on 17/5/27.
 */
public class NettyClient {

    private static Channel channel;

    public static void main(String[] args) throws Exception {
        connect(HOST, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("start client success, please enter message send to server.");
        for (;;) {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            write(line);
        }
    }

    private static void write(String msg) throws Exception {
        if (channel == null) {
            throw new NullPointerException("channel must be not null");
        }
        // Sends the received line to the server.
        ChannelFuture future = channel.writeAndFlush(msg);

        // If user typed the 'bye' command, wait until the server closes
        // the connection.
        if ("bye".equals(msg.toLowerCase())) {
            channel.closeFuture().sync();
            return;
        }
        if (future != null) {
            future.sync();
        }
    }

    private static Channel connect(String host, int port) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();

                // Add the text line codec combination first,
                // p.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));

                p.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                p.addLast(new ObjectEncoder());
                p.addLast(new LoggingHandler(LogLevel.INFO));
                p.addLast(new NettyClientHandler());
            }
        });

        bootstrap.validate();
        channel = bootstrap.connect(host, port).sync().channel();
        return channel;
    }
}
