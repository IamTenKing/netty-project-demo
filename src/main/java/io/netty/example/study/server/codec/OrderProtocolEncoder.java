package io.netty.example.study.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.common.ResponseMessage;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

//MessageToMessageEncoder 格式转化，消息--》消息 就是ResponseMessage --> buffer
//
public class OrderProtocolEncoder extends MessageToMessageEncoder<ResponseMessage> {


    //入参responseMessage，出参List<Object>
    @Override
    protected void encode(ChannelHandlerContext ctx, ResponseMessage responseMessage, List<Object> out) throws Exception {
        ByteBuf buffer = ctx.alloc().buffer();
        //将请求头，请求体内容写入buffer
        responseMessage.encode(buffer);

        out.add(buffer);
    }
}
