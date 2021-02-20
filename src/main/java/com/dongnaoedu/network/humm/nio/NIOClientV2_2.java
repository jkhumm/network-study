package com.dongnaoedu.network.humm.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class NIOClientV2_2 {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("localhost", 8080));//非阻塞会立即返回

        while (true) {
            selector.select();//开启管家
            Set<SelectionKey> selectionKeys = selector.selectedKeys();//同一时刻可能会有其他客户端连接进来，所以存在多个key
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isConnectable()) {
                    try {
                        if (socketChannel.finishConnect()) {
                            System.out.println("连接成功-" + socketChannel);
                            //ByteBuffer buffer = ByteBuffer.allocateDirect(2048);
                            //selectionKey.attach(buffer); // attach 类似于我们发邮件中的附件 也可以不传，也可以使用
                            selectionKey.interestOps(SelectionKey.OP_WRITE);//连接成功了，将事件切换至写事件
                            //socketChannel.register (selector,SelectionKey.OP_WRITE,buffer);  //这个也可以  上面两段代码等于这一段
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                } else if (selectionKey.isWritable()) {// 可以开始写数据
                    //ByteBuffer buf = (ByteBuffer) selectionKey.attachment();
                    //buf.clear();//取到这个附件，将其清空  这里没必要写  这是为了演示下
                    ByteBuffer buf = ByteBuffer.allocate (2048);
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("请输入：");
                    String msg = scanner.next();
                    scanner.close();
                    buf.put(msg.getBytes());
                    buf.flip ();
                    while (buf.hasRemaining()) {
                        socketChannel.write(buf);
                    }
                    selectionKey.interestOps(SelectionKey.OP_READ);// 切换到感兴趣的事件
                } else if (selectionKey.isReadable()) {// 可以开始读数据
                    System.out.println("收到服务端响应:");
                    ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
                    while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {//没有数据，就不断轮询  不能说是阻塞
                        // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                        if (requestBuffer.position() > 0) break;
                    }
                    requestBuffer.flip();//切换至读取模式
                    byte[] content = new byte[requestBuffer.remaining()];
                    requestBuffer.get(content);
                    System.out.println(new String(content));
                    //selectionKey.interestOps(SelectionKey.OP_WRITE);

                }
            }
        }
    }

}
