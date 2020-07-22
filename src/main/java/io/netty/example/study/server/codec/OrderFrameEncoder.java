package io.netty.example.study.server.codec;


import io.netty.handler.codec.LengthFieldPrepender;


//实际上是extends MessageToMessageEncoder<ByteBuf>，所以入参需要是ByteBuf对象，这个在上一步OrderProtocolEncoder
// 已经将ResponseMessage转成ByteBuf
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super(2);
    }
}
