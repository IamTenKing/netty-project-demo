package io.netty.example.study.demo2.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.reactivex.Flowable;
import io.reactivex.processors.ReplayProcessor;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;

/**
 * Date:2021/5/30
 */
public class RpcClient {

    //连接通道
    private volatile Channel channel;

    //请求id生成器
    private static  final AtomicLong INVOKE_ID = new AtomicLong(0);
    //启动器
    private  Bootstrap bootstrap;

    public RpcClient() {


        NioEventLoopGroup group = new NioEventLoopGroup();
        NettyClientHandler nettyClientHandler = new NettyClientHandler();
        try {
            bootstrap =  new Bootstrap();

            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //设置帧分割符，避免半包粘包
                            ByteBuf delimiter = Unpooled.copiedBuffer("|".getBytes());
                            pipeline.addLast(new DelimiterBasedFrameDecoder(1000,delimiter));
                            //自动解码为string
                            pipeline.addLast(new StringDecoder());
                            //将字符串消息自动编码
                            pipeline.addLast(new StringEncoder());

                            //添加业务处理器
                            pipeline.addLast(nettyClientHandler);


                        }
                    });
            //发起连接请求
            ChannelFuture future = bootstrap.connect("127.0.0.1", 12800).sync();

            if(future.isDone() && future.isSuccess()){
                this.channel = future.channel();
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }


    private void sendMsg(String msg){
        channel.writeAndFlush(msg);
    }


    public void close(){
        if(null !=bootstrap){
            bootstrap.group().shutdownGracefully();
        }

        if(null !=channel){
            channel.close();
        }
    }



    //根据消息和请求id 拼接消息帧
    public String generatorFrame(String msg,String reqId){
        return msg+":"+reqId+"|";

    }


    public CompletableFuture rpcAsyncCall(String msg){

        CompletableFuture<String> future = new CompletableFuture<>();
        String reqId = INVOKE_ID.getAndIncrement() + "";

        msg = generatorFrame(msg, reqId);

        this.sendMsg(msg);

        //保存future对象
        FutureMapUtil.put(reqId,future);
        return future;
    }


    public String rpcSyncCall(String msg) throws ExecutionException, InterruptedException {

        //这里 future如何和请求结果结合，是由业务handler将结果设置到futute里面
        CompletableFuture<String> future = new CompletableFuture<>();
        String reqId = INVOKE_ID.getAndIncrement() + "";

        msg = generatorFrame(msg, reqId);

        this.sendMsg(msg);

        //保存future对象
        FutureMapUtil.put(reqId,future);
        return future.get();
    }


    /**
     * 异步改成reactive编程风格
     * @param msg
     * @return
     */
    public Flowable rpcAsyncCallFlowable(String msg){
        return Flowable.defer(()->{

            final ReplayProcessor<Object> processor = ReplayProcessor.createWithSize(1);
            //具体执行rpc调用
            CompletableFuture future = rpcAsyncCall(msg);
            future.whenComplete((v,t)->{
               if(t!=null){
                   processor.onError((Throwable) t);
               } else{
                   //发射rpc返回结果
                   processor.onNext(v);
                   //结束流
                   processor.onComplete();

               }
            });

            return processor;
        });

    }






}
