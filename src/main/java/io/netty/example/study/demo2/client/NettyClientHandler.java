package io.netty.example.study.demo2.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.CompletableFuture;

/**
 * Date:2021/5/30
 */
@ChannelHandler.Sharable
public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //接收服务端请求数据
        String id = ((String) msg).split(":")[1];
        CompletableFuture future = FutureMapUtil.remove(id);

        //如果存在设置结果
        if(null != future){
            future.complete(((String) msg).split(":")[0]);
        }
}
}
