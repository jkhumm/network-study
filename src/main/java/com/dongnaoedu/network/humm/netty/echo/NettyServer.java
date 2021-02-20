package com.dongnaoedu.network.humm.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Heian
 * @time 19/07/21 23:40
 * @description：服务端
 */
public class NettyServer {

    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        // 1、创建EventLoopGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup (1);//类比mainReactor
        //事件轮训器
        EventLoopGroup workerGroup = new NioEventLoopGroup();//类比subReactor线程组  默认大小是cpu核心数X2
        final ServerHandler serverHandler = new ServerHandler();
        try {
            ServerBootstrap b = new ServerBootstrap();// 2、创建启动器
            b.group(bossGroup, workerGroup)// 3、配置启动器
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler (LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel> () {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            //每个客户端连接进
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(serverHandler);//3、向流水线添加作业 ChannelHandler
                        }
                    });
            // 等待客户端连接
            ChannelFuture f = b.bind(PORT).sync();// 4、启动器启动
            f.channel().closeFuture().sync();// 5、等待服务端channel关闭，不关闭则一直阻塞
        } finally {
            // 6.释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
