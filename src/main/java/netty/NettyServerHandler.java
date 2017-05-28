package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import serialization.hessian.HessianUtil;
import serialization.object.Person;

import java.net.InetAddress;

/**
 * Created by pomelo on 16/10/21.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelActive ");
        // Send greeting for a new connection.
        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object s) throws Exception {
        // 回消息给client
        System.out.println("read client message: " + s.toString());
        channelHandlerContext.write("server message: " + s.toString() + "\r\n");
        channelHandlerContext.flush();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 使用hessian2序列化对象
     * 
     * @param s 字符串
     * @return byte[]
     * @throws Exception
     */
    private byte[] encoder(String s) throws Exception {
        Person person = new Person();
        person.setName(s);
        person.setEmail("server@qq.com");
        return HessianUtil.encoder(person);
    }
}
