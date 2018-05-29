package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import serialization.hessian.HessianUtil;

/**
 * 自定义hessian序列化encoder
 *
 * @author: zhengyong Date: 2018/5/28 Time: 下午6:55
 */
public class CustomHessianEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String msg, ByteBuf byteBuf) throws Exception {
        System.out.println("encode invoked: send msg=" + msg);
        byteBuf.writeBytes(HessianUtil.encoder(msg));
    }
}
