package com.dongnaoedu.network.humm.多线程.ReenLock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author heian
 * @create 2020-03-06-10:03 上午
 * @description 读写锁
 * 当共享锁占锁成功，独占锁无法抢锁成功；共享锁可以被多个线程占锁
 * 当独占锁占锁成功，共享锁无法占锁成功；独占锁无法被多个线程占锁
 * 不包含锁降级
 */
public class MyReadWriteLock {

    //想获取锁的线程
    private LinkedBlockingQueue<WaitNode> queue = new LinkedBlockingQueue<>();
    //被引用的线程
    private AtomicReference<Thread> reference = new AtomicReference<>();
    //计算重入得次数
    private AtomicInteger readCount = new AtomicInteger(0);//共享锁
    private AtomicInteger writeCount = new AtomicInteger(0);//独占锁

    //为了区分读线程还是写线程
    class WaitNode{
        int type = 0;   //0:独占锁的线程  1:共享锁的线程
        Thread thread = null;

        public WaitNode(Thread thread, int type){
            this.thread = thread;
            this.type = type;
        }
    }
    /**
     * 尝试获取锁（不是真的去拿锁，所以不用加入到阻塞队列）
     * 修改count 和 reference  acquire：重入的次数默认是1
     */
    public boolean tryLock(int acquires){
        if (readCount.get() != 0){
            return false;
        }
        if (writeCount.get() != 0){
            //锁被占用（可能是自己）
            if (Thread.currentThread() == reference.get()){
                writeCount.set(writeCount.get()+acquires);//单线程  无需CAS
            }
        }else {
            //非公平的实现
            if (writeCount.compareAndSet(writeCount.get(),writeCount.get()+1)){
                reference.set(Thread.currentThread());
                return true;
            }
        }
        return false;
    }

    /**
     * 抢锁（存在非公平现象）
     * 修改 queue
     */
    public void lock(){
        //锁被占用，则CAS自旋不断地去抢锁
        int arg = 1;
        if (!tryLock(arg)){
            WaitNode readNode = new WaitNode(Thread.currentThread(),0);
            queue.offer(readNode);
            //lock 是不死不休所以得用for循环，既然CAS拿不到则由轻量级锁转为重量级锁（挂起阻塞）再一次去拿锁
            for (;;){
                WaitNode peek = queue.peek();
                //队列可能一个线程，所以offer进来的或者说唤醒进来的，都会去判断是不是头部，是头部则再一次去抢锁
                if (peek != null && peek.thread == Thread.currentThread()){
                    if (tryLock(arg)){
                        queue.poll();
                        break;
                    }else {
                        //可能一进来就是头部线程或者唤醒了非头部线程，挂起
                        LockSupport.park();
                    }
                }else {
                    //不是头部线程，在队列并挂起
                    LockSupport.park();
                }
            }

        }
    }

    /**
     * 释放锁 返回布尔类型是为了仿照jdk实现
     * 修改 queue
     */
    public boolean unlock(){
        int arg = 1;
        if (tryUnlock(arg)){
            WaitNode peek = queue.peek();
            //存在队列为空可能，比如就一个抢锁的不会去加入到阻塞队列
            if (peek != null){
                LockSupport.unpark(peek.thread);
            }
            return true;
        }
        return false;
    }

    /**
     * 尝试去解锁
     * 解锁不用判断读锁占用情况：，修改count 和 reference
     */
    public boolean tryUnlock(int releases){
        if (reference.get() != Thread.currentThread()){
            throw new IllegalMonitorStateException("未能获取到锁，无法释放锁");
        }else {
            //只有是拿到锁的线程才有解锁的资格，所以此处是单线程
            int value = writeCount.get()- releases;
            writeCount.set(value);
            //当你lock多次，但是unlock一次，此时是不会释放锁，只是不阻塞罢了
            if (value == 0){
                reference.set(null);
                return true;
            }else {
                return false;
            }
        }
    }

    //读锁：独占锁  tryAcquireShared(AQS中的模板方法)
    public void lockShared(){
        int arg = 1;
        //>0 抢锁成功 <0 失败
        if (tryLockShared(arg) <0){
            WaitNode writeNode = new WaitNode(Thread.currentThread(),1);
            queue.offer(writeNode);
            for (;;){
                WaitNode headNode = queue.peek();
                if (headNode != null && headNode.thread == Thread.currentThread()){
                    //如果是头部线程，则再去抢锁，抢到了则移除，没抢到则挂起
                    if (tryLockShared(arg)>0){
                        queue.poll();
                        //移除后再去拿下一个,如果下一个也是读锁，则将其唤醒
                        WaitNode peek = queue.peek();
                        if (peek != null && peek.type == 1){
                            System.out.println("读锁被唤醒或读锁唤醒下一个线程");
                            LockSupport.unpark(peek.thread);//唤醒下一个线程，下一个线程是挂起1、挂起2
                        }
                        break;
                    }else {
                        LockSupport.park();//队列头部 挂起1
                    }
                }else {
                    LockSupport.park();//非队列头部 挂起2
                }
            }
        }
    }


    /**
     * acquireShared()
     * 尝试获取读锁
     * 只修改count 因为reference是被多个线程引用
     * 这里返回int 是为了模拟jdk源码
     */
    public int tryLockShared(int acquires){
        for (;;){
            if (writeCount.get() != 0 ){//&& Thread.currentThread() != reference.get()
                return -1;
            }else {
                int readNum = readCount.get();
                //多个读线程 CAS操作改变，可能会失败所以需要自旋for
                if (readCount.compareAndSet(readNum,readNum + acquires)){
                    return 1;
                }
            }
        }
    }

    /**
     * tryReleaseShared  这里返回布尔类型是为了仿照jdk实现
     * 解除共享锁 意味着所有的读锁都释放了
     */
    public boolean unlockShared(){
        int arg = 1;
        if (tryUnlockShared(arg)){
            //读锁释放了，则去判断队列中有无写锁
            WaitNode peek = queue.peek();
            if (peek != null){
                System.out.println("读锁唤醒下一个线程");
                LockSupport.unpark(peek.thread);//读锁执行完去唤醒下一个线程 一定是写锁
            }
            return true;
        }
        return false;
    }



    /**
     * tryReleaseShared
     * 尝试解开读锁,就直接修改readCount值即可
     * 解锁不用判断写锁占用情况：读锁重入多次，解锁1次，则count就！=0，只有当读锁的count=0，才能表明读锁释放锁成功
     */
    public boolean tryUnlockShared(int releases){
        //这里无需去唤醒读锁，因为这里你解锁也只能唤醒一个，而是应该放在lockShared方法里，全部唤醒处在头部的读锁
        for (;;){
            int readNum = readCount.get();
            int readNum2 = readNum - releases;
            if (readCount.compareAndSet(readNum,readNum2)){
                return readNum2 == 0;
            }
        }
    }





    public static void main(String[] args) {
        MyReadWriteLock lock = new MyReadWriteLock();

        new Thread(() -> {
            lock.lock();
            System.out.println("写锁开始工作");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("写锁结束工作");
            lock.unlock();
        }).start();
        new Thread(() -> {
            lock.lock();
            System.out.println("写锁开始工作");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("写锁结束工作");
            lock.unlock();
        }).start();

        new Thread(() -> {
            lock.lockShared();
            System.out.println("读锁开始工作");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("读锁结束工作");
            lock.unlockShared();
        }).start();
        new Thread(() -> {
            lock.lockShared();
            System.out.println("读锁开始工作");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("读锁结束工作");
            lock.unlockShared();
        }).start();




    }

}
