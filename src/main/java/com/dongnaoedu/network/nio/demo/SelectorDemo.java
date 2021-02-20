package com.dongnaoedu.network.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SelectorDemo {
    public static void main(String[] args) throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();//客户端永远是被动地，没有read或者write方法
        Selector selector = Selector.open();// 创建Selector
        serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);// serverSocketChannel注册OP_READ事件
        serverSocketChannel.socket().bind(new InetSocketAddress(8080)); // 绑定端口一定要发生在注册之后，防止你启动之后有连接进来没被监听

        while(true) {
            int readyChannels = selector.select();// 会阻塞，直到有事件触发  调用此方法，监听才开始工作，监听所有连接进来的客户端
            if(readyChannels == 0) continue;
            Set<SelectionKey> selectedKeys = selector.selectedKeys();// 获取被触发的事件集合
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();
            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if(key.isAcceptable()) {
                    SocketChannel socket = ((ServerSocketChannel) key.channel()).accept();//通过key拿到对应客户端的channel对象
                    socket.register(selector, SelectionKey.OP_READ);//先跳出此循环，返回上一个循环，当有新事件进来则（可能使我们刚注册的事件）
                    // serverSocketChannel 收到一个新连接，只能作用于ServerSocketChannel

                } else if (key.isConnectable()) {
                    // 连接到远程服务器，只在客户端异步连接时生效

                } else if (key.isReadable()) {
                    // SocketChannel 中有数据可以读

                } else if (key.isWritable()) {
                    // SocketChannel 可以开始写入数据
                }

                // 将已处理的事件移除
                keyIterator.remove();
            }
        }

    }
}
