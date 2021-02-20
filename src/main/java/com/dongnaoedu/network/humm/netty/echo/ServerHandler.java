package com.dongnaoedu.network.humm.netty.echo;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author Heian
 * @time 19/07/21 23:40
 * @description：服务端添加需要入栈的
 */
//ChannelHandler接口的子类  ChannelInboundHandlerAdapter-->ChannelInboundHandler-->ChannelHandler
public class ServerHandler extends ChannelInboundHandlerAdapter {

    //读取通道内消息，并返回给客户端
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        System.out.println("收到客户端数据，还给客户端：" + msg);
        ctx.write(msg);
    }

    //读取完成刷新
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    //异常则关闭ChannelHandlerContext连接
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }


}
