package com.dongnaoedu.network.humm.netty.echo;

import com.dongnaoedu.network.netty.echo.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author Heian
 * @time 19/07/21 23:41
 * @description：客户端
 */
public class NettyClient {
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    public static void main(String[] args) throws Exception {
        // 1、创建EventLoopGroup
        EventLoopGroup group = new NioEventLoopGroup ();//类比subReactor线程组  默认大小是cpu核心数X2
        try {
            Bootstrap b = new Bootstrap();//// 2、创建启动器 把pipeline channelahaandler eventloop
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel> () {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ClientHandler ());//3、向流水线添加作业 ChannelHandler
                        }
                    });

            // 连接服务端
            ChannelFuture f = b.connect(HOST, PORT).sync();// 4、启动器启动
            f.channel().closeFuture().sync();// 5、等待服务端channel关闭，不关闭则一直阻塞
        } finally {
            // 6.释放资源
            group.shutdownGracefully();
        }
    }
}
