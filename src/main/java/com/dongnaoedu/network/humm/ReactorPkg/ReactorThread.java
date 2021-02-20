package com.dongnaoedu.network.humm.ReactorPkg;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @author Heian
 * @time 19/06/30 15:20
 * @description：作为服务端的事件监听器，监听客户端的连接和客户端的发送的数据以及及时响应
 */
abstract class ReactorThread extends Thread {

    volatile boolean running = false;
    Selector selector;

    //Selector监听到有事件后,调用这个方法
    public abstract void hanler(SelectableChannel channel);

    public ReactorThread() throws IOException {
        selector = Selector.open ();
        System.out.println (selector.toString ());
    }
    /**
     * 服务端的注册
     * 注册两次需要用到：服务端启动注册到select和接受接受客户端连接注册accept事件
     * @param :ops=0表示注册的事件可以自定义  attachment:channel因为
     */
    public SelectionKey register(SelectableChannel channel) throws ClosedChannelException {
        return channel.register (selector,0,channel);
    }



    //每个线程启动默认是false,启动后为true,便不会再次启动
    public void singleStart() {
        if (!running){//防止线程轮询超过一轮多次启动
            running = true;
           start ();
        }
    }

    @Override
    public void run() {
        //当此线程启动的时候，说明就有业务要处理了，此线程可能作为subReactor线程一样监听多个客户端的连接
        while (running){
            try {
                selector.select ();//超过1s无返回值，变打断阻塞
                Set<SelectionKey> keys = selector.selectedKeys ();
                Iterator<SelectionKey> it = keys.iterator ();
                while (it.hasNext ()){
                    SelectionKey key = it.next ();
                    it.remove ();
                    int ops = key.readyOps ();
                    if ( (ops & (SelectionKey.OP_READ | SelectionKey.OP_ACCEPT )) != 0 ){
                        //这时候注册的可能是连接连接事件则是则是socketchannel  如果是服务端启动注册的则是serverSocketchannel
                         SelectableChannel channel = (SelectableChannel)key.attachment ();
                         //SelectableChannel channel1 = key.channel ();也可以不通过附件拿，这样register方法第三个参数也可以不传
                        channel.configureBlocking (false);
                        //连接进来后则要去处理对应的业务逻辑（mainReactor 和 subReactor）
                        try {
                            hanler (channel);
                        } catch (Exception e) {
                            e.printStackTrace ();
                        }
                        if (!channel.isOpen ())
                            key.channel ();/// 如果关闭了,就取消这个KEY的订阅
                    }

                }
            } catch (IOException e) {
                e.printStackTrace ();
            }

        }
    }

    public static void main(String[] args) throws Exception{
        //每次启动一个线程都会分配一个Selector
        ReactorThread reactorThread = new ReactorThread () {
            @Override
            public void hanler(SelectableChannel channel) {
                System.out.println (1);
            }
        };
        ReactorThread reactorThread1 = new ReactorThread () {
            @Override
            public void hanler(SelectableChannel channel) {
                System.out.println (2);
            }
        };
    }


}
