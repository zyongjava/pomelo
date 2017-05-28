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

        if (s instanceof byte[]) {
            System.out.println("read byte: " + HessianUtil.decoder((byte[]) s));
        }
        System.out.println("read result = " + s.toString());
    }
}
