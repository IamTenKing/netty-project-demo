package io.netty.example.study.demo1.client.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.example.study.demo1.common.Operation;
import io.netty.example.study.demo1.common.RequestMessage;
import io.netty.example.study.demo1.util.IdUtil;
import io.netty.example.study.demo1.util.JsonUtil;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class OperationToRequestMessageEncoder extends MessageToMessageEncoder<Operation> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Operation operation, List<Object> out) throws Exception {
        RequestMessage requestMessage = new RequestMessage(IdUtil.nextId(), operation);

        out.add(requestMessage);

        System.out.println("client OperationToRequestMessageEncoder  encode内容:");

    }
}
