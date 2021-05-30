package io.netty.example.study.demo1.server.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.demo1.util.JsonUtil;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.List;


//实际上是extends MessageToMessageEncoder<ByteBuf>，所以入参需要是ByteBuf对象，这个在上一步OrderProtocolEncoder
// 已经将ResponseMessage转成ByteBuf
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super(2);
    }


    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println(this.getClass().getName());

        super.encode(ctx, msg, out);

        System.out.println("server OrderFrameEncoder  encode内容:");

    }
}
