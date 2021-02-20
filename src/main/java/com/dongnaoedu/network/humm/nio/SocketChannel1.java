package com.dongnaoedu.network.humm.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * @author Heian
 * @time 19/06/16 19:37
 * @copyright(C) 2019 深圳市长亮保泰
 * 用途：
 */
public class SocketChannel1 {

    public static void main(String[] args) throws Exception{
        SocketChannel socketChannel = SocketChannel.open ();
        socketChannel.configureBlocking (false);//设置为非阻塞模式
        socketChannel.connect (new InetSocketAddress ("127.0.0.1",8080));
        while (!socketChannel.finishConnect ()){
            Thread.yield ();//没有连接则阻塞在此
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        while (true){
            if (scanner.hasNext ()){  //不输入就阻塞到此
                String sendMsg = scanner.next ();
                ByteBuffer sendBf = ByteBuffer.allocate (1024);
                sendBf.put (sendMsg.getBytes ());
                sendBf.flip ();
                while (sendBf.hasRemaining ()){
                    socketChannel.write (sendBf);//发送数据   向通道写入数据
                }
                //读取响应数据
                ByteBuffer receiveBf = ByteBuffer.allocate (1024);//默认是写模式
                while (socketChannel.isConnected () && socketChannel.read (receiveBf) != -1){//非阻塞  没有值就返回0
                    // 长连接情况下,需要手动判断读取数据有没有读取结束，可能数据量很大，远超过1kb (此处做一个简单的判断: 超过0字节就认为请求结束了)
                    if (receiveBf.position() > 0) break;//读操作会默认读到的数组存到receiveBf
                }
                receiveBf.flip ();//切换至读模式
                byte[] bytes = new byte[receiveBf.limit()];
                ByteBuffer bf= receiveBf.get (bytes);//将刚才写入的数据 按照1kb大小读取
                String receiveMsg = new String (bf.array ());
                System.out.println ("读取到的数据为：" + receiveMsg);
                if ("bye".equals (receiveMsg)){
                    break;
                }
            }
        }
        socketChannel.close ();
        scanner.close ();

    }




}



