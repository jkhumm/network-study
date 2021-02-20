package com.dongnaoedu.network.humm.netty;

/**
 * @author Heian
 * @time 19/07/20 21:18
 * @description：链表形式调用，netty就是类似的这种形式
 */
public class PipelineDemo {
    //因为只有HandlerChainContext里有工序实现这个属性，所以只有通过这个HandlerChainContext来看它是否有
    HandlerChainContext headContext = new HandlerChainContext (new AbstractHandler () {
        @Override
        void doHandler(HandlerChainContext chainContext, Object args) {
            chainContext.runNext (args);
        }
    });

    /**
     * 从第一个节点是headContext，它这个类保存的工序为handler1,所以先会执行工序一的具体实现，如果该节点的还有下个节点，
     * 则继续执行下个节点的方法，类似于递归执行，所以每个工序的方法都能得到执行
     */
    void doProcessHandler(Object args){
        this.headContext.runCurrent (args);
    }

    //增加工序
    public void addCircle(AbstractHandler abstractHandler){
        //每次添加一个对象，都会从头到尾添加一遍，保证添加的元素是处于最尾部
        HandlerChainContext context = headContext;
        while (context.chainContextNext != null){
            context = context.chainContextNext;
        }
        //把传入的工序赋值给下一个节点
       context.chainContextNext = new HandlerChainContext (abstractHandler);

    }

    public static void main(String[] args) {
        PipelineDemo pipeline = new PipelineDemo ();
        //此时头部节点 已经加载出来（类的初始化：局部变量通过关键字new加载）
        pipeline.addCircle (new Handler1 ());// header1
        pipeline.addCircle (new Handler2 ());// header1
        pipeline.addCircle (new Handler2 ());// header1
        //分别执行每一个节点所包含的工序实现方法的信息
        pipeline.doProcessHandler ("火车头");


    }



}

/**
 * 下面就写一个字符串的叠加
 */

//第一步：定义处理器抽象类(就是工序的抽象) 和 负责维护链和链的执行
    //工序的抽象:参数1：chainContext  参数2：传入的参数
abstract class AbstractHandler{
    abstract void doHandler(HandlerChainContext chainContext,Object args);
}
    //节点，承上启下负责A流程--》B流程的执行
class HandlerChainContext{
    //既然承上启下，就必须要知道下一个工序和处理工序的实现
    HandlerChainContext chainContextNext;
    AbstractHandler abstractHandler;

    //构造方法：切换上下文只需要关心抽象工序即可
    public HandlerChainContext(AbstractHandler abstractHandler){
        this.abstractHandler = abstractHandler;
    }
    //运行当前节点信息
    void runCurrent(Object args){
        this.abstractHandler.doHandler (this,args);
    }

    //节点运行下一个具体实现的方法,并且保存节点信息
    void runNext(Object args){
        if (this.chainContextNext != null){
            this.chainContextNext.runCurrent (args);
        }
    }
}
//第二步：工序的实现类
class Handler1 extends AbstractHandler{
    @Override
    void doHandler(HandlerChainContext chainContext, Object args) {
        args = args.toString () + "我是工序一   ";
        System.out.println ("One:" + args);
        chainContext.runNext (args);
    }
}
class Handler2 extends AbstractHandler{
    @Override
    void doHandler(HandlerChainContext chainContext, Object args) {
        args = args.toString () + "我是工序二   ";
        System.out.println ("Two:" + args);
        chainContext.runNext (args);
    }
}