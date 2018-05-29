package netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import serialization.hessian.HessianUtil;

/**
 * Created by zhengyong on 17/5/27.
 */
public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object s) throws Exception {
        // hessian2反序列化
        if (s instanceof byte[]) {
            System.out.println("received server byte message: " + new String ((byte[]) s, "UTF-8"));
            return;
        }
        System.out.println("received server string message: " + s.toString());
    }
}
