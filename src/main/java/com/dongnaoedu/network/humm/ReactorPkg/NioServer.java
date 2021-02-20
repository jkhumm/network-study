package com.dongnaoedu.network.humm.ReactorPkg;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Heian
 * @time 19/06/30 16:29
 * @description：基于多Reactor线程模型的服务器
 */
public class NioServer {

    private ServerSocketChannel serverSocketChannel;
    // 1、accept处理reactor线程 (accept线程)
    private ReactorThread[] mainReactorThreads = new ReactorThread[1];
    // 2、io处理reactor线程  (I/O线程)
    private ReactorThread[] subReactorThreads = new ReactorThread[8];//性能不足点二：解决办法，创建创建多个线程来处理io，并且单个线程管理多个客户端的连接
    // 3、处理业务操作的线程
    private static ExecutorService workPool = Executors.newCachedThreadPool();//性能不足点一：解决办法，创建业务线程池


    /**
     * 初始化线程组：给mainReactorThread线程组分配数量为1个 ReactorThread线程数组（也可以多个）
     *            给subReactorThread线程组分配线程数量为8个ReactorThread线程数组
     *            二者统称为Reactor抽象类，主要的作用就是监听事件：1个是处理客户端的连接，另一个是接受客户端发出的数据，并处理；
     */
    public void initMainAndSUbReactor() throws IOException {

        // 创建mainReactor线程, 只负责处理serverSocketChannel
        for(int i=0;i<mainReactorThreads.length;i++){
            AtomicInteger atomicInteger = new AtomicInteger (0);
            //通过启动main线程通过唤醒机制去唤醒sub线程
            mainReactorThreads[i] = new ReactorThread () {
                @Override
                public void hanler(SelectableChannel channel) {
                    //当客户端连接进来后,分发给I/O线程继续去读取数据
                    try {
                        ServerSocketChannel ServerSocketChannel= (ServerSocketChannel) channel;
                        SocketChannel socketChannel = ServerSocketChannel.accept ();
                        System.out.println (Thread.currentThread ().getName () + "收到客户端连接，男朋友为：" + socketChannel.toString () );
                        socketChannel.configureBlocking (false);//客户端通道也设置为非阻塞模式
                        int index = atomicInteger.getAndIncrement () % subReactorThreads.length;
                        subReactorThreads[index].singleStart ();
                        //启动一个main线程意味着，有客户端连接进来，并且告诉subReactor时刻做好准备等待客户端发来数据并返回
                        SelectionKey socketKey = subReactorThreads[index].register (socketChannel);
                        socketKey.interestOps (SelectionKey.OP_READ);
                    } catch (IOException e) {
                        e.printStackTrace ();
                    }
                }
            };
        }

        //创建subReactor线程，只负责接收客户端数据，和响应请求
        for (int i=0;i<subReactorThreads.length;i++){
            subReactorThreads[i] = new ReactorThread () {
                @Override
                public void hanler(SelectableChannel channel) {
                    try {
                        SocketChannel ch = (SocketChannel) channel;
                        ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
                        while (ch.isOpen() && ch.read(requestBuffer) != -1) {
                            // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                            if (requestBuffer.position() > 0) break;
                        }
                        if (requestBuffer.position() == 0) return; // 如果没数据了, 则不继续后面的处理
                        requestBuffer.flip();//记得切换至读模式才能写数据
                        byte[] content = new byte[requestBuffer.limit()];
                        requestBuffer.get(content);
                        System.out.println(Thread.currentThread().getName() + "收到数据,来自：" + ch.getRemoteAddress() + new String(content));
                        // TODO 业务操作 数据库、接口...
                        workPool.submit(() -> {
                            try {
                                TimeUnit.SECONDS.sleep (1);
                                System.out.println ("selectUserById接口请求完成");
                            } catch (InterruptedException e) {
                                e.printStackTrace ();
                            }
                        });
                        // 接口返回结果 200
                        String response = "HTTP/1.1 200 OK\r\n" +
                                "Content-Length: 11\r\n\r\n" +
                                "Hello World";
                        ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                        while (buffer.hasRemaining()) {
                            ch.write(buffer);
                        }
                    }catch (Exception e){
                        e.printStackTrace ();
                    }

                }
            };
        }


    }


    // 初始化服务端的channel,并且开启mainReactor线程
    public void regAndDisServerSocket(int port) throws IOException {
        serverSocketChannel = ServerSocketChannel.open ();
        serverSocketChannel.configureBlocking (false);
        //随机分配mainReactor,并启该线程
        int index = new Random ().nextInt (mainReactorThreads.length);
        mainReactorThreads[index].singleStart ();
        SelectionKey selectionKey = mainReactorThreads[index].register (serverSocketChannel);
        selectionKey.interestOps (SelectionKey.OP_ACCEPT);//设置兴趣事件
        ServerSocket socket = serverSocketChannel.socket ();
        socket.bind (new InetSocketAddress (port));
        System.out.println ("服务器启动");
    }


    public static void main(String[] args) throws Exception{
        NioServer nioServer = new NioServer ();
        //初始化mainReactor  和   subReactor线程组
        nioServer.initMainAndSUbReactor();
        //有了两个线程组，两个线程组也都知道自己该干什么事情了，所以需要启动服务和启动mainReactor线程去调用subReactor线程
        nioServer.regAndDisServerSocket (8080);

    }

}
