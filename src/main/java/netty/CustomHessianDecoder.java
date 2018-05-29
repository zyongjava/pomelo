package netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import serialization.hessian.HessianUtil;

import java.util.List;

/**
 * 参考： https://blog.csdn.net/wzq6578702/article/details/78767539
 * 自定义hessian序列化decoder
 *
 * @author: zhengyong Date: 2018/5/28 Time: 下午6:53
 */
public class CustomHessianDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {

        byte[] bs = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bs);
        String msg = new String(bs);
        System.out.println("decode invoked: received msg=" + msg);
        list.add(HessianUtil.decoder(bs));
    }
}
