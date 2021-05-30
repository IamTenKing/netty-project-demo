package io.netty.example.study.demo1.server.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.demo1.util.JsonUtil;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

//解决半包粘包
//ByteToMessageDecoder ,先走这个，转成message,所以后面执行的解码OrderProtocolDecoder需要继承extends MessageToMessageDecoder<ByteBuf>
//不能有两个解码器都继承ByteToMessageDecoder
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    public OrderFrameDecoder() {
        super(10240, 0, 2, 0, 2);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        System.out.println(this.getClass().getName());
        Object decode = super.decode(ctx, in);

        System.out.println("server OrderFrameDecoder  decode内容:");

        return decode;
    }
}
