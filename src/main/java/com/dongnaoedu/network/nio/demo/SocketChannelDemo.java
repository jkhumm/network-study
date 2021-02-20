package com.dongnaoedu.network.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketChannelDemo {

    public static void main(String[] args) throws IOException {
        // 客户端主动发起连接的方式
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false); // 设置为非阻塞模式
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 8080));

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        socketChannel.write(byteBuffer); //  发送请求数据 – 向通道写入数据

        int bytesRead = socketChannel.read(byteBuffer); // 读取服务端返回 – 读取缓冲区的数据

        socketChannel.close(); // 关闭连接
    }


}
