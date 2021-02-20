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

public class NIOClientV2 {

    public static void main(String[] args) throws Exception {
        SocketChannel socketChannel = SocketChannel.open();
        Selector selector = Selector.open();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
        socketChannel.connect(new InetSocketAddress("localhost", 8080));//非阻塞会立即返回

        while (true) {
            selector.select();//开启管家
            Set<SelectionKey> selectionKeys = selector.selectedKeys();//可读 可写 连接成功
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();
                if (selectionKey.isConnectable()) {
                    try {
                        if (socketChannel.finishConnect()) {
                            System.out.println("连接成功-" + socketChannel);
                            //ByteBuffer buffer = ByteBuffer.allocateDirect(2048);
                            //selectionKey.attach(buffer); // attach 类似于我们发邮件中的附件 也可以不传，这里只是为了演示此功能
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
                    ByteBuffer sendbf = ByteBuffer.allocate (1024);
                    Scanner scanner = new Scanner(System.in);
                    System.out.print("请输入：");
                    String msg = scanner.next ();
                    //scanner.close();//这里不能关闭 具体参考https://www.cnblogs.com/qingyibusi/p/5812725.html
                    sendbf.put(msg.getBytes());
                    sendbf.flip ();//在写入数据后，一定要切换至读模式
/*
                    如果我不做那个flip切换到写读式，那么它默写模式，假认是设我写了一个1，那么position 就是1  limit1024,capacity也是1024，这样通过socketchannel写入通道内的就是
                    位置1到1024，那肯定是数据为空的，如果我切换至写，那么position就变成了0，kimit就变成了1，那socketchannel写入通道的就是0到1
*/
                    while (sendbf.hasRemaining()) {
                        socketChannel.write(sendbf);
                    }
                    selectionKey.interestOps(SelectionKey.OP_READ);// 切换到感兴趣的事件
                } else if (selectionKey.isReadable()) {// 可以开始读数据
                    System.out.println("收到服务端响应:");
                    ByteBuffer receivebf = ByteBuffer.allocate(1024);
                    while (socketChannel.isOpen() && socketChannel.read(receivebf) != -1) {//没有数据，就不断轮询  不能说是阻塞
                        // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                        if (receivebf.position() > 0) break;
                    }
                    receivebf.flip();//切换至读取模式
                    byte[] content = new byte[receivebf.remaining()];
                    ByteBuffer bf = receivebf.get (content);
                    System.out.println("收到服务端端数据：" + socketChannel +new String(bf.array (),"utf-8"));
                    selectionKey.interestOps(SelectionKey.OP_WRITE);
                }
            }
        }
    }

}
