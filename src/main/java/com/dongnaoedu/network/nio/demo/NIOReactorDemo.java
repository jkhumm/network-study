package com.dongnaoedu.network.nio.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class NIOReactorDemo {

    private ServerSocketChannel serverSocketChannel;
    private SubReactor[] subReactors = new SubReactor[16];// 负责处理IO读和写
    private MainReactor mainReactor;// 负责接收客户端的连接，有点类似我们的服务端，只是做完连接工作后
    private static ExecutorService workPool = Executors.newCachedThreadPool();//处理业务操作的线程


    public static void main(String[] args) throws IOException {
        NIOReactorDemo nioReactorDemo = new NIOReactorDemo();
        nioReactorDemo.newGroup();//初始化mainReactor
        nioReactorDemo.initAndRegister();//将服务端注册到select中去
        nioReactorDemo.bind();
    }


    /**
     * 初始化mainReactor和subReactor
     * @throws IOException
     */
    public void newGroup() throws IOException {
        mainReactor = new MainReactor();//用一个类来管理连接

        for (int i = 0; i < subReactors.length; i++) {
            subReactors[i] = new SubReactor();
        }
    }

    /**
     * 初始化服务端channel并注册到mainReactor中，并且启动mainReactor
     * @throws IOException
     */
    public void initAndRegister() throws IOException {
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        mainReactor.register(serverSocketChannel);//服务端注册到select中去
        mainReactor.start();
    }

    private void bind() throws IOException {
        serverSocketChannel.bind(new InetSocketAddress(8080));
    }

    /**
     * MainReactor 负责接收客户端连接
     */
    class MainReactor extends Thread {
        private Selector selector;
        AtomicInteger incr = new AtomicInteger(0);

        public MainReactor() throws IOException {
            selector = Selector.open();
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 5. 启动selector（管家）
                    selector.select();// 阻塞，直到事件通知才会返回
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        if (key.isAcceptable()) {
                            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
                            // 将客户端连接给到acceptor
                            new Acceptor(socketChannel);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //将客户端的连接分配到一个subReactor线程中，并启动subReactor线程
        class Acceptor {
            public Acceptor(SocketChannel socketChannel) throws IOException {
                socketChannel.configureBlocking(false);
                int index = incr.getAndIncrement() % subReactors.length;//取模  轮询策略
                SubReactor subReactor = subReactors[index];
                subReactor.register(socketChannel);
                subReactor.start();//可能轮询多次 启动多次
                System.out.println("收到新连接：" + socketChannel);
            }
        }

        /**
         * 将服务端channel注册到selector中，注册OP_ACCEPT事件
         * @param channel
         * @throws ClosedChannelException
         */
        public void register(ServerSocketChannel channel) throws ClosedChannelException {
            channel.register(selector, SelectionKey.OP_ACCEPT);
        }

    }

    /**
     * 负责从客户端读数据后交给工作线程去处理，和给客户端写数据
     * 解决一个select处理多个客户端请求，因某个请求耗时较长造成的堵车现象
     */
    class SubReactor extends Thread {
        private Selector selector;
        private volatile boolean running = false;

        public SubReactor() throws IOException {
            selector = Selector.open();
        }
        @Override
        public synchronized void start() {
            if (!running) {//防止多次启动造成线程重复启动的问题
                running = true;
                super.start();
            }
        }

        @Override
        public void run() {
            while (running) {
                try {
                    // 5. 启动selector（管家）
                    selector.select();// 阻塞，直到事件通知才会返回

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = selectionKeys.iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isReadable()) {// 客户端连接有数据可以读时触发
                            try {
                                SocketChannel socketChannel = (SocketChannel) key.channel();
                                new Handler(socketChannel);
                            } catch (Exception e) {
                                e.printStackTrace();
                                key.cancel();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 将客户端channel注册到selector中，注册OP_READ事件
         * @param socketChannel
         * @throws ClosedChannelException
         */
        public void register(SocketChannel socketChannel) throws ClosedChannelException {
            socketChannel.register(selector, SelectionKey.OP_READ);
        }

        class Handler {
            public Handler(SocketChannel socketChannel) throws IOException {
                ByteBuffer requestBuffer = ByteBuffer.allocate(1024);
                while (socketChannel.isOpen() && socketChannel.read(requestBuffer) != -1) {
                    // 长连接情况下,需要手动判断数据有没有读取结束 (此处做一个简单的判断: 超过0字节就认为请求结束了)
                    if (requestBuffer.position() > 0) break;
                }
                if (requestBuffer.position() == 0) return; // 如果没数据了, 则不继续后面的处理
                requestBuffer.flip();//切换至读模式
                byte[] content = new byte[requestBuffer.remaining()];
                requestBuffer.get(content);
                System.out.println(new String(content));
                System.out.println("收到数据,来自：" + socketChannel.getRemoteAddress());
                // TODO 业务操作 数据库 接口调用等等
                workPool.execute(new Runnable() {
                    @Override
                    public void run() {
                        // 处理业务
                    }
                });

                // 响应结果 200
                String response = "HTTP/1.1 200 OK\r\n" +
                        "Content-Length: 11\r\n\r\n" +
                        "Hello World";
                ByteBuffer buffer = ByteBuffer.wrap(response.getBytes());
                while (buffer.hasRemaining()) {
                    socketChannel.write(buffer);
                }
            }
        }
    }
}
