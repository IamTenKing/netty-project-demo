package io.netty.example.study.demo1.client.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.demo1.util.JsonUtil;
import io.netty.handler.codec.LengthFieldPrepender;

import java.util.List;

//一次编码器，解决tcp半包粘包问题
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super(2);
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        System.out.println(this.getClass().getName());

        super.encode(ctx, msg, out);
        System.out.println("client OrderFrameEncoder  encode内容:");
    }
}
