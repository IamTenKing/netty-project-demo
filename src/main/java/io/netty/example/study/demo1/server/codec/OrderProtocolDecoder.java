package io.netty.example.study.demo1.server.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.demo1.common.RequestMessage;
import io.netty.example.study.demo1.util.JsonUtil;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;


//MessageToMessageDecoder  === ByteBuf中读取成 RequestMessage对象，存储到list中，然后可以再handler中进行处理
//这个不能extends ByteToMessageDecoder，因为OrderFrameDecoder已经继承了ByteToMessageDecoder，所以这里必须是extends MessageToMessageDecoder<ByteBuf>
public class OrderProtocolDecoder extends MessageToMessageDecoder<ByteBuf> {
    //入参ByteBuf，出参List<Object>
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> out) throws Exception {

        System.out.println(this.getClass().getName());

        RequestMessage requestMessage = new RequestMessage();
        //就是从ByteBuf中读取请求，请求体各个字段
        requestMessage.decode(byteBuf);

        out.add(requestMessage);
        System.out.println("server OrderProtocolDecoder  decode内容:"+JsonUtil.toJson(requestMessage));

    }



}
