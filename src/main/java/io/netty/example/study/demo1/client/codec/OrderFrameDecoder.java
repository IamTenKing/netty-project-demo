package io.netty.example.study.demo1.client.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.demo1.util.JsonUtil;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


//字节转message----------ByteToMessageDecoder
//一次编码器，解决tcp半包粘包问题
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    //注意initialbytestoStrip参数
    public OrderFrameDecoder() {

        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println(this.getClass().getName());
        Object decode = super.decode(ctx, in);
        System.out.println("client OrderFrameDecoder  encode内容:");

        return decode;
    }
}
