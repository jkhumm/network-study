package com.dongnaoedu.network.nio.demo;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * NIO selector 多路复用reactor线程模型
 */
public class NIOServerV3 {

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
    private void initMainAndSUbReactor() throws IOException {
        // 创建mainReactor线程, 只负责处理serverSocketChannel
        for (int i = 0; i < mainReactorThreads.length; i++) {
            mainReactorThreads[i] = new ReactorThread() {
                AtomicInteger incr = new AtomicInteger(0);
                @Override
                public void handler(SelectableChannel channel) throws Exception {
                    // 只做请求分发，不做具体的数据读取
                    ServerSocketChannel ch = (ServerSocketChannel) channel;
                    SocketChannel socketChannel = ch.accept();
                    socketChannel.configureBlocking(false);
                    // 收到连接建立的通知之后，分发给I/O线程继续去读取数据
                    int index = incr.getAndIncrement() % subReactorThreads.length;
                    ReactorThread workEventLoop = subReactorThreads[index];
                    workEventLoop.doStart();
                    SelectionKey selectionKey = workEventLoop.register(socketChannel);
                    selectionKey.interestOps(SelectionKey.OP_READ);
                    System.out.println(Thread.currentThread().getName() + "收到新连接 : " + socketChannel.getRemoteAddress());
                }
            };
        }

        // 创建IO线程,负责处理客户端连接以后socketChannel的IO读写
        for (int i = 0; i < subReactorThreads.length; i++) {
            subReactorThreads[i] = new ReactorThread() {
                @Override
                public void handler(SelectableChannel channel) throws IOException {
                    // work线程只负责处理IO处理，不处理accept事件
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
                    System.out.println(new String(content));
                    System.out.println(Thread.currentThread().getName() + "收到数据,来自：" + ch.getRemoteAddress());
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
                }
            };
        }

    }

    // 初始化服务端的channel,并且开启mainReactor线程
    private void regAndDisServerSocket() throws Exception {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        int index = new Random().nextInt(mainReactorThreads.length); // 随机分配mainReaeactor线程
        mainReactorThreads[index].doStart();
        SelectionKey selectionKey = mainReactorThreads[index].register(serverSocketChannel);
        selectionKey.interestOps(SelectionKey.OP_ACCEPT);
    }

    // 绑定端口
    private void bind(int port) throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(port));
        System.out.println("启动完成，端口" + port);
    }

    public static void main(String[] args) throws Exception {
        // 1、初始化服务器
        NIOServerV3 nioServerV3 = new NIOServerV3();
        // 2、创建main和sub两组线程
        nioServerV3.initMainAndSUbReactor();
        // 3、 创建serverSocketChannel，注册到mainReactor线程上的selector上
        nioServerV3.regAndDisServerSocket();
        // 4、 为serverSocketChannel绑定端口
        nioServerV3.bind(8080);
    }


    /**
     * ReactorThread:抽象类
     * 1.服务端的注册、启动
     * 2.当select监听到客户端状态：连接进来，则分配handle，做分配
     */
    abstract class ReactorThread extends Thread {

        volatile boolean running = false;
        Selector selector;
        //构造抽象方法   Selector监听到有事件后,调用这个方法
        public abstract void handler(SelectableChannel channel) throws Exception;

        private ReactorThread() throws IOException {
            selector = Selector.open();
        }

        //服务端注册
        private SelectionKey register(SelectableChannel channel) throws Exception {
            return channel.register(selector, 0,channel);
        }
        // 防止同一个线程启动多次
        private void doStart() {
            if (!running) {
                running = true;
                start();
            }
        }
        @Override
        public void run() {
            // 轮询Selector事件
            while (running) {
                try {
                    selector.select(5000);
                    // 获取查询结果
                    Set<SelectionKey> selected = selector.selectedKeys();
                    // 遍历查询结果
                    Iterator<SelectionKey> iter = selected.iterator();
                    while (iter.hasNext()) {
                        // 被封装的查询结果
                        SelectionKey key = iter.next();
                        iter.remove();
                        int readyOps = key.readyOps();
                        // 关注 Read 和 Accept两个事件
                        if ((readyOps & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT)) != 0 || readyOps == 0) {
                            try {
                                SelectableChannel channel = (SelectableChannel) key.attachment();
                                channel.configureBlocking(false);
                                handler(channel);
                                if (!channel.isOpen()) {
                                    key.cancel(); // 如果关闭了,就取消这个KEY的订阅
                                }
                            } catch (Exception ex) {
                                key.cancel(); // 如果有异常,就取消这个KEY的订阅
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }




}
