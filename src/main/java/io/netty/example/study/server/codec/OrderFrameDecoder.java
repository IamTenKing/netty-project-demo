package io.netty.example.study.server.codec;


import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

//解决半包粘包
//ByteToMessageDecoder ,先走这个，转成message,所以后面执行的解码OrderProtocolDecoder需要继承extends MessageToMessageDecoder<ByteBuf>
//不能有两个解码器都继承ByteToMessageDecoder
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    public OrderFrameDecoder() {
        super(10240, 0, 2, 0, 2);
    }
}
