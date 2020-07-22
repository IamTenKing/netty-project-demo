package io.netty.example.study.client.codec;


import io.netty.handler.codec.LengthFieldBasedFrameDecoder;


//字节转message----------ByteToMessageDecoder
//一次编码器，解决tcp半包粘包问题
public class OrderFrameDecoder extends LengthFieldBasedFrameDecoder {
    //注意initialbytestoStrip参数
    public OrderFrameDecoder() {
        super(Integer.MAX_VALUE, 0, 2, 0, 2);
    }
}
