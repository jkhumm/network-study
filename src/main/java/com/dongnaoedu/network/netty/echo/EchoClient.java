package com.dongnaoedu.network.netty.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


public final class EchoClient {


    public static void main(String[] args) throws Exception {
        // 1、创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup();//类比subReactor线程组  默认大小是cpu核心数X2
        try {
            Bootstrap b = new Bootstrap();// 2、创建启动器
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            //3、向流水线添加作业 ChannelHandler
                            p.addLast(new EchoClientHandler());
                        }
                    });

            // 4、启动器启动 连接服务端
            ChannelFuture f = b.connect("127.0.0.1", 8007).sync();
            // 5、等待服务端channel关闭，不关闭则一直阻塞
            f.channel().closeFuture().sync();
        } finally {
            // 6.释放资源
            group.shutdownGracefully();
        }
    }

}
