package com.dongnaoedu.network.netty.pipeline;
/**
 * @description:链表形式调用，netty就是类似的这种形式
 */
class PipelineDemo {
    // 初始化的时候造一个head，作为责任链的开始，并没有具体的处理
   public HandlerChainContext initHead = new HandlerChainContext(new AbstractHandler() {
        @Override
        void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
            handlerChainContext.runNext(arg0);
        }
    });

    public void requestProcess(Object arg0) {
        this.initHead.handler(arg0);
    }

    public void addLast(AbstractHandler handler) {
        HandlerChainContext context = initHead;
        while (context.nextContext != null) {
            context = context.nextContext;
        }
        context.nextContext = new HandlerChainContext(handler);
    }

    public static void main(String[] args) {
        PipelineDemo pipelineChainDemo = new PipelineDemo();
        pipelineChainDemo.addLast(new Handler2()); // initHead-->new Handler2()
        pipelineChainDemo.addLast(new Handler1());// initHead-->new Handler2()-->new Handler1()
        pipelineChainDemo.addLast(new Handler1());
        pipelineChainDemo.addLast(new Handler2());
        pipelineChainDemo.requestProcess("火车呜呜呜~~");  // 发起请求

    }
}
// 要素三：handler上下文，我主要负责维护链，和链的执行
class HandlerChainContext {
    HandlerChainContext nextContext; // 下一个节点
    AbstractHandler handler;

    public HandlerChainContext(AbstractHandler handler) {
        this.handler = handler;
    }

    //运行当前chainContext类方法
    void handler(Object arg0) {
        this.handler.doHandler(this, arg0);
    }

    // 继续执行下一个
    void runNext(Object arg0) {
        if (this.nextContext != null) {
            this.nextContext.handler(arg0);
        }
    }
}

// 要素一：处理器抽象类
abstract class AbstractHandler {
    //处理器，这个处理器就做一件事情，在传入的字符串中增加一个尾巴..
    abstract void doHandler(HandlerChainContext handlerChainContext, Object arg0); // handler方法
}

// 要素二：处理器具体实现类
class Handler1 extends AbstractHandler {
    @Override
    void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
        arg0 = arg0.toString() + "..handler1的小尾巴.....";
        System.out.println("我是Handler1的实例，我在处理：" + arg0);
        handlerChainContext.runNext(arg0);// 继续执行下一个
    }
}

// 要素二：处理器具体实现类
class Handler2 extends AbstractHandler {
    @Override
    void doHandler(HandlerChainContext handlerChainContext, Object arg0) {
        arg0 = arg0.toString() + "..handler2的小尾巴.....";
        System.out.println("我是Handler2的实例，我在处理：" + arg0);
        handlerChainContext.runNext(arg0);// 继续执行下一个
    }
}


