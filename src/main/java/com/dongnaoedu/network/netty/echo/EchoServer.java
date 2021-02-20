package com.dongnaoedu.network.netty.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


public final class EchoServer {


    public static void main(String[] args) throws Exception {
        // 1、创建EventLoopGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);//类比mainReactor
        EventLoopGroup workerGroup = new NioEventLoopGroup();//类比subReactor线程组  默认大小是cpu核心数X2
        final EchoServerHandler serverHandler = new EchoServerHandler();
        try {
            // 2、创建启动器
            ServerBootstrap b = new ServerBootstrap();
            // 3、配置启动器
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //4、向流水线添加作业 ChannelHandler
                            p.addLast(serverHandler);
                        }
                    });
            //5、等待客户端连接
            ChannelFuture f = b.bind(8007).sync();// 4、启动器启动
            f.channel().closeFuture().sync();// 5、等待服务端channel关闭，不关闭则一直阻塞
        } finally {
            // 6、释放资源
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

}
