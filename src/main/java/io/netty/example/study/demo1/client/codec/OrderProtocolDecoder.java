package io.netty.example.study.demo1.client.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.demo1.common.ResponseMessage;
import io.netty.example.study.demo1.util.JsonUtil;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

//
//序列化器
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {
        System.out.println(this.getClass().getName());
        ResponseMessage responseMessage = new ResponseMessage();
        responseMessage.decode(byteBuf);

        out.add(responseMessage);
        System.out.println("client OrderProtocolDecoder  decode内容:"+JsonUtil.toJson(responseMessage));

    }
}
