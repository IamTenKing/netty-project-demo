package io.netty.example.study.demo1.client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.demo1.common.RequestMessage;
import io.netty.example.study.demo1.util.JsonUtil;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class OrderProtocolEncoder extends MessageToMessageEncoder<RequestMessage> {


    @Override
    protected void encode(ChannelHandlerContext ctx, RequestMessage requestMessage, List<Object> out) throws Exception {
        System.out.println(this.getClass().getName());

        //注意buffer的获取方式，alloc初始时指定了方式，堆外还是内存池的方式
        ByteBuf buffer = ctx.alloc().buffer();
        requestMessage.encode(buffer);

        out.add(buffer);
        System.out.println("client OrderProtocolEncoder  encode内容:"+JsonUtil.toJson(requestMessage));
    }
}
