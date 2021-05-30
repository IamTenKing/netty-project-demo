package io.netty.example.study.demo2.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *
 * 业务处理类
 * Date:2021/5/29
 */
@ChannelHandler.Sharable//共享业务类是需要增加此注解
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      try {

          //处理请求
          System.out.println("接受到请求："+msg);
          String msgStr = (String) msg;

          String reqId = msgStr.split(":")[1];
          String resp = generatorFrame("im result",reqId);

          try {
              Thread.sleep(3000);
          }catch (Exception ex){
              ex.printStackTrace();
          }

          //回写结果
          ctx.channel().writeAndFlush(Unpooled.copiedBuffer(resp.getBytes()));
      }catch (Exception e){
          e.printStackTrace();
      }

    }

    private String generatorFrame(String msg, String reqId) {
        return msg+":"+reqId+"|";
    }
}
