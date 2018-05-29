package netty;

import com.alibaba.fastjson.JSON;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import serialization.object.Person;

import java.net.InetAddress;

/**
 * <p>
 * 使用hessian2序列化返回消息给客户端
 * </p>
 * Created by pomelo on 16/10/21.
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client connect server success.");
        ctx.write("Welcome to " + InetAddress.getLocalHost().getHostName() + "!");
        ctx.flush();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        System.out.println("client left server.");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        System.out.println("client register in server.");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
        System.out.println("client unregister server.");
    }

    /**
     * 使用hessian2序列化返回消息给客户端
     * 
     * @param channelHandlerContext
     * @param s
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object s) throws Exception {
        // 回消息给client

        System.out.println("received client message: " + s.toString());
        channelHandlerContext.write(buildString(s.toString()));
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
     * @param s 字符串
     * @return String
     */
    private String buildString(String s){
        Person person = new Person();
        person.setId(1111);
        person.setName(s);
        person.setEmail("server@qq.com");
        return JSON.toJSONString(person);
    }
}
