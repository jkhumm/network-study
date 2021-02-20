package com.dongnaoedu.network.humm.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Arrays;

//ChannelHandler的子类:  ChannelInboundHandlerAdapter-->ChannelInboundHandler-->ChannelHandler
public class ClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;
    final int SIZE = 256;

    //实例化时向通道内1-256数字的字节数据
    public ClientHandler() {
        firstMessage = Unpooled.buffer(SIZE);
        for (int i = 0; i <256; i++) {// Arrays.toString(firstMessage.array())
            firstMessage.writeByte((byte)i);
        }
        System.out.println ("写入的数据为：" + Arrays.toString(firstMessage.array()));
    }

    //写出数据事件 出站
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("给服务器发送数据：" + firstMessage);//刚启动的时候会执行一次
        ctx.writeAndFlush(firstMessage);
    }

    //写数据事件  出站
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception{
        Thread.sleep (1000);
        System.out.println("收到服务端数还给服务器：" + msg);//处理handle。类比handle（）方法，监听发过来的消息
        ctx.write(msg);
    }

    //刷新数据到网络事件 出站
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    //关闭事件  出站
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
