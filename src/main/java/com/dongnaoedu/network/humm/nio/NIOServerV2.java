package com.dongnaoedu.network.humm.nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 结合Selector实现非阻塞服务器
 */
public class NIOServerV2 {

    public static void main(String[] args) throws Exception {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();// 1. 创建服务端的channel对象
        serverSocketChannel.configureBlocking(false); // 设置为非阻塞模式
        Selector selector = Selector.open();// 2. 创建Selector
        SelectionKey selectionKey = serverSocketChannel.register(selector,0); // 3. 把服务端的channel注册到selector，注册accept事件
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);//register(select,事件,附件)
        serverSocketChannel.socket().bind(new InetSocketAddress(8080)); // 4. 绑定端口，启动服务
        System.out.println("启动成功");
        while (true) {
            // 5. 启动selector（管家）
            selector.select();// 阻塞，直到事件通知才会返回
            Set<SelectionKey> selectionKeys = selector.selectedKeys();//拿到所有客户端的事件
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //同一时刻只有一个事件
                if (key.isAcceptable()) {
                    SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();//强转为ServerSocketChannel
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);
                    System.out.println("收到新连接：" + socketChannel);
                    iterator.remove();
                } else if (key.isReadable()) {// 客户端连接有数据可以读时触发
                    try {
                        SocketChannel socketChannel = (SocketChannel) key.channel();// 不再是新连接，则直接强转为SocketChannel
                        ByteBuffer receivebf = ByteBuffer.allocateDirect(2048);
                        while (socketChannel.isOpen() && socketChannel.read(receivebf) != -1) {
                            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                            if (receivebf.position() > 0) break;
                        }
                        if (receivebf.position() == 0) continue; // 如果没数据了, 则不继续后面的处理
                        receivebf.flip();
                        byte[] content = new byte[receivebf.remaining()];
                        receivebf.get (content);
                        System.out.println("收到数据,来自：" + socketChannel.getRemoteAddress()+":" + new String (content));
                        // TODO 业务操作 数据库 接口调用等等  服务端类似生产者   提供数据给消费者

                        // 响应结果 200
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 11\r\n\r\n" +
                                "Hello World";
                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                        while (buffer.hasRemaining()) {
                            socketChannel.write(buffer);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }
    }
}
