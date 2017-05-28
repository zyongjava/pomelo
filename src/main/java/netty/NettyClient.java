package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.channel.Channel;
import serialization.hessian.HessianUtil;
import serialization.object.Person;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by zhengyong on 17/5/27.
 */
public class NettyClient {

    static final String    HOST = "127.0.0.1";
    static final int       PORT = 8082;

    private static Channel channel;

    public static void main(String[] args) throws Exception {
        connect(HOST, PORT);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
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
        ChannelFuture future = channel.writeAndFlush(msg + "\r\n");

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

                p.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                p.addLast(new StringDecoder());
                p.addLast(new StringEncoder());

                p.addLast(new LoggingHandler(LogLevel.INFO));
                p.addLast(new NettyClientHandler());
            }
        });

        channel = bootstrap.connect(host, port).sync().channel();
        return channel;
    }

    /**
     * 使用hessian2序列化对象
     *
     * @param s 字符串
     * @return byte[]
     * @throws Exception
     */
    private static byte[] encoder(String s) throws Exception {
        Person person = new Person();
        person.setId(1222);
        person.setName(s);
        person.setEmail("client@qq.com");
        return HessianUtil.encoder(person);
    }
}
