package io.netty.example.study.demo1.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioChannelOption;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.example.study.demo1.server.codec.OrderFrameDecoder;
import io.netty.example.study.demo1.server.codec.OrderFrameEncoder;
import io.netty.example.study.demo1.server.codec.OrderProtocolDecoder;
import io.netty.example.study.demo1.server.codec.OrderProtocolEncoder;
import io.netty.example.study.demo1.server.handler.AuthHandler;
import io.netty.example.study.demo1.server.handler.MetricsHandler;
import io.netty.example.study.demo1.server.handler.OrderServerProcessHandler;
import io.netty.example.study.demo1.server.handler.ServerIdleCheckHandler;
import io.netty.handler.flush.FlushConsolidationHandler;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.IpSubnetFilterRule;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.traffic.GlobalTrafficShapingHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.concurrent.ExecutionException;

@Slf4j
public class Server {

    public static void main(String[] args) throws InterruptedException, ExecutionException, CertificateException, SSLException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        /**
         *
         * 三种io模式，BIO\NIO\AIO
         *  NIO,reactor有三种模式
         *
         */

        //liunx系统是使用epoll，chanel为NIO模式
        serverBootstrap.channel(NioServerSocketChannel.class);
        //bio模式
//        serverBootstrap.channel(OioServerSocketChannel.class);
        //最大等待连接数
        serverBootstrap.option(NioChannelOption.SO_BACKLOG, 1024);
        //如果需要发送较小大的报文需要关闭
        serverBootstrap.childOption(NioChannelOption.TCP_NODELAY, true);
        //设置serverchannel,客户端代码只需要设置一个handler就可以了，服务端要设置两个
        serverBootstrap.handler(new LoggingHandler(LogLevel.INFO));

        //BIO模式
//        OioEventLoopGroup eventExecutors = new OioEventLoopGroup();
        //thread，主从reactor多线程模式,NIO模式
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("boss"));
        NioEventLoopGroup workGroup = new NioEventLoopGroup(0, new DefaultThreadFactory("worker"));
        UnorderedThreadPoolEventExecutor businessGroup = new UnorderedThreadPoolEventExecutor(10, new DefaultThreadFactory("business"));
        NioEventLoopGroup eventLoopGroupForTrafficShaping = new NioEventLoopGroup(0, new DefaultThreadFactory("TS"));

        try{

            serverBootstrap.group(bossGroup, workGroup);

            //metrics
            MetricsHandler metricsHandler = new MetricsHandler();

            //trafficShaping,10mb，流量整形流量整形(traffic shaping)典型作用是限制流出某一网络的某一连接的流量与突发，使这类报文以比较均匀的速度向外发送
            GlobalTrafficShapingHandler globalTrafficShapingHandler = new GlobalTrafficShapingHandler(eventLoopGroupForTrafficShaping, 10 * 1024 * 1024, 10 * 1024 * 1024);

            //ipfilter
            IpSubnetFilterRule ipSubnetFilterRule = new IpSubnetFilterRule("127.1.1.1", 16, IpFilterRuleType.REJECT);
            RuleBasedIpFilter ruleBasedIpFilter = new RuleBasedIpFilter(ipSubnetFilterRule);

            //auth
            AuthHandler authHandler = new AuthHandler();

            //ssl,自签证书，此例仅单向验证
            SelfSignedCertificate selfSignedCertificate = new SelfSignedCertificate();
            //打印出证书位置，使用抓包工具可以使用证书解密加密数据
            log.info("certificate position:" + selfSignedCertificate.certificate().toString());
            SslContext sslContext = SslContextBuilder.forServer(selfSignedCertificate.certificate(), selfSignedCertificate.privateKey()).build();

            //log
            LoggingHandler debugLogHandler = new LoggingHandler(LogLevel.DEBUG);
            LoggingHandler infoLogHandler = new LoggingHandler(LogLevel.INFO);


            //childhandler设置的niosocketchannel的属性，handle设置的是serversocketchannel
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {

                    //顺序很重要
                    ChannelPipeline pipeline = ch.pipeline();

                    //完善线程名，提高可诊断性
                    pipeline.addLast("debegLog", debugLogHandler);
                    //黑白名单
                    pipeline.addLast("ipFilter", ruleBasedIpFilter);

                    //流量整形
                    pipeline.addLast("tsHandler", globalTrafficShapingHandler);
                    //监控数据，可视化
                    pipeline.addLast("metricHandler", metricsHandler);
                    //服务端启用idle check,10s没收到数据就关闭，而客户端开启keepalive和idle check，每5s发送心跳
                    pipeline.addLast("idleHandler", new ServerIdleCheckHandler());
                    //数据加密
                    pipeline.addLast("ssl", sslContext.newHandler(ch.alloc()));
                    //tcp粘包，半包处理
                    //ByteToMessageDecoder
                    pipeline.addLast("frameDecoder", new OrderFrameDecoder());
                    //MessageToMessageEncoder
                    pipeline.addLast("frameEncoder", new OrderFrameEncoder());
                    //序列化方式
                    //MessageToMessageDecoder
                    pipeline.addLast("protocolDecoder", new OrderProtocolDecoder());
                    pipeline.addLast("protocolEncoder", new OrderProtocolEncoder());


                    //日志类抽出共享，顺序移动的话会影响打印日志的范围
                    pipeline.addLast("infolog", infoLogHandler);
                    //减少flush次数，提高吞吐量，但是提高了延迟
                    pipeline.addLast("flushEnhance", new FlushConsolidationHandler(10, true));

                    pipeline.addLast("auth", authHandler);

                    //业务处理使用独立的线程池，业务异步化，对应io密集型的优化
                    pipeline.addLast(businessGroup, new OrderServerProcessHandler());
                }
            });

            ChannelFuture channelFuture = serverBootstrap.bind(8090).sync();

            channelFuture.channel().closeFuture().sync();

        } finally{
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            businessGroup.shutdownGracefully();
            eventLoopGroupForTrafficShaping.shutdownGracefully();
        }

    }

}
