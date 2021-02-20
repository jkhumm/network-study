package com.dongnaoedu.network.humm.nio;


import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Heian
 * @time 19/06/16 20:00
 * @copyright(C) 2019 深圳市长亮保泰
 * 用途：
 */
public class ServerSocketChannel1 {

    private static ArrayList<SocketChannel> SocketChannelList = new ArrayList<>();//解决无法获取多个客户端连接

    public static void main(String[] args) throws Exception{
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open ();
        serverSocketChannel.configureBlocking (false);//设置为非阻塞模式
        serverSocketChannel.socket ().bind (new InetSocketAddress (8080));
        System.out.println ("服务端启动了");

        while (true){
            SocketChannel socketChannel = serverSocketChannel.accept ();//非阻塞  如果没有挂起的连接则直接返回null
            if(socketChannel != null){//因为是非阻塞的，没有连接返回null
                //tcp请求  读取响应
                System.out.println("收到新连接 : " + socketChannel.getRemoteAddress());
                socketChannel.configureBlocking (false);// 默认是阻塞的,一定要设置为非阻塞
                SocketChannelList.add (socketChannel);
            }else {
                // 没有新连接的情况下,就去处理现有连接的数据,处理完的就删除掉
               Iterator<SocketChannel> iterator = SocketChannelList.iterator ();
                while (iterator.hasNext ()){
                    SocketChannel channel = iterator.next ();//新的连接没发送消息 就会去重新遍历scoketchannnel
                    ByteBuffer receiveBf = ByteBuffer.allocate (1024);
                    if (channel.read(receiveBf) == 0) {// 等于0,代表这个通道没有数据需要处理,那就待会再处理
                        continue;
                    }
                    while (channel.isOpen () && channel.read (receiveBf) != -1){//按照1kb大小去读取socketchannel 的数据，没有返回0，不阻塞但不断做轮询
                        // 长连接情况下,需要手动判断读取数据有没有读取结束，可能数据量很大，远超过1kb (此处做一个简单的判断: 超过0字节就认为请求结束了)
                        if (receiveBf.position() > 0) break;//如果读到了，就相当于会把数据写入到 receiveBf
                    }
                    if(receiveBf.position () == 0)continue;//如果没有数据则结束此次循环，终止下面的操作
                    receiveBf.flip ();//切换至读取模式
                    byte[] bytes = new byte[receiveBf.remaining ()];
                    ByteBuffer byteBf = receiveBf.get (bytes);
                    String reveiveMsg = new String (byteBf.array (),"utf-8");
                    System.out.println ("收到"+channel.getRemoteAddress ()+"客户端发来的消息为：" + reveiveMsg);
                    //响应结果  这里随便响应一个
                    String response = "HTTP/1.1 200 OK\r\n" +
                            "Content-Length: 11\r\n\r\n" +
                            "Hello World";
                    ByteBuffer sendBf = null;
                    if ("bye".equals (reveiveMsg)){
                        sendBf = ByteBuffer.wrap ("bye".getBytes ());
                    }else {
                        sendBf = ByteBuffer.wrap (response.getBytes ());
                    }
                    while (sendBf.hasRemaining ()){
                        channel.write (sendBf);//非阻塞  继续循环等待新的连接  或者处理同一个客户端发来的请求
                    }
                }

            }
        }
        // 用到了非阻塞的API, 在设计上,和BIO可以有很大的不同.继续改进
    }

}
