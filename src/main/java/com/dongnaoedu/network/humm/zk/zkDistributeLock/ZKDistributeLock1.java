package com.dongnaoedu.network.humm.zk.zkDistributeLock;

import com.dongnaoedu.network.humm.zk.MyZkSerialiZer;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @create 2020-03-17-10:34 下午
 * @description ZK分布式锁:实现原理 临时节点不可重名 + watch
 * 缺点：会出现惊群效应，每一次删除其他每个app都会收到消息通知
 * 备注：会在指定的一个父节点lockpath 只会创建一个节点
 */
public class ZKDistributeLock1 implements Lock {

    private ZkClient zkClient;
    private String lockPath;

    public ZKDistributeLock1(String lockPath){
        if (lockPath == null || lockPath.trim().equals("")){
            throw new IllegalArgumentException("字符串不为空");
        }
        this.lockPath = lockPath;
        zkClient = new ZkClient("192.168.0.102:2183");
        zkClient.setZkSerializer(new MyZkSerialiZer());
    }

    @Override
    public boolean tryLock() {
        try {
            //Ephemeral:短暂的  创建临时节点 不带数据
            //TODO 此处应该判断ip是不是同一个，是的话就允许重入
            zkClient.createEphemeral(lockPath);
        }catch (Exception e){
            //如果该节点被创建，则会抛出错误，则认为抢锁失败
            System.out.println(Thread.currentThread().getName() + "此node节点已被创建");
            return false;
        }
        System.out.println(Thread.currentThread().getName() + "获得分布式锁");
        return true;
    }

    @Override
    public void lock() {
        if (!tryLock()){
            try {
                waitForLock();
                lock();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private void waitForLock() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IZkDataListener zkListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o)  {
                System.out.println(Thread.currentThread().getName() + "数据被修改");
            }
            @Override
            public void handleDataDeleted(String s) {
                //ZkClient-EventThread-13-192.168.0.102:2183 线程名字  （守护线程）
                System.out.println(Thread.currentThread().getName() + " 节点被删除，唤醒另外一个线程");
                //节点被删除，有人释放了锁,唤醒阻塞的线程  （网络原因临时节点会被删除）
                countDownLatch.countDown();
            }
        };
        zkClient.subscribeDataChanges(lockPath,zkListener);
        //zkClient阻塞释放后则取消订阅
        System.out.println(Thread.currentThread().getName() + "阻塞");
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "阻塞被打断");
        zkClient.unsubscribeDataChanges(lockPath,zkListener);
    }

    public static void main(String[] args) throws InterruptedException {
        //商品 id
        String productId = "abcd";
        ZKDistributeLock1 lock = new ZKDistributeLock1("/zk/"+productId);
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "执行");
            lock.lock();
            LockSupport.parkNanos(1000000000*10L);//类比业务操作去操作被锁住的数
            lock.unlock();
        }).start();
        TimeUnit.SECONDS.sleep(1);
        //模拟 另外一个服务
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "执行");
            lock.lock();
            lock.unlock();
        }).start();

    }

    @Override
    public void unlock() {
        zkClient.delete(lockPath);
        System.out.println(Thread.currentThread().getName() + "释放分布式锁");
    }


    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

}
