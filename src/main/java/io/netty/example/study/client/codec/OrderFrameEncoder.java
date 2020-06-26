package io.netty.example.study.client.codec;


import io.netty.handler.codec.LengthFieldPrepender;

//一次编码器，解决tcp半包粘包问题
public class OrderFrameEncoder extends LengthFieldPrepender {
    public OrderFrameEncoder() {
        super(2);
    }
}
