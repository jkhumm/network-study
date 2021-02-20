package com.dongnaoedu.network.humm.zk.zkDistributeLock;

import com.dongnaoedu.network.humm.zk.MyZkSerialiZer;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;

import java.net.Inet4Address;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @create 2020-03-17-10:34 下午
 * @description ZK分布式锁:实现原理 取号+最小号+ watch  类似银行取排队小票
 * 备注：临时节点死掉了会释放   会在指定的一个父节点lockpath  下很多临时顺序的临时子节点
 */
public class ZKDistributeLock2 implements Lock {
    //在父节点下创建临时子节点   记住自己当前多少号，关注比自己小的那个号
    private ZkClient zkClient;
    private String lockPath;
    private ThreadLocal<String> currentPath = new ThreadLocal<>();//当前序号
    private ThreadLocal<String> beforePath = new ThreadLocal<>();//我前面的一个序号
    private ThreadLocal<Integer> count = new ThreadLocal<>();

    public ZKDistributeLock2(String lockPath){
        if (lockPath == null || lockPath.trim().equals("")){
            throw new IllegalArgumentException("字符串不为空");
        }
        this.lockPath = lockPath;
        String ip4 = "";
        try {
            ip4 = Inet4Address.getLocalHost().getHostAddress();
        }catch (Exception e){
            System.out.println("ip获取异常：" + ip4);
        }
        zkClient = new ZkClient(ip4 + ":2183");
        zkClient.setZkSerializer(new MyZkSerialiZer());
        if (!this.zkClient.exists(lockPath)){
            try {
                //创建一个持久节点
               this.zkClient.createPersistent(lockPath,true);
            }catch (Exception e){
                System.out.println("异常：节点已存在");
            }
        }
    }

    @Override
    public boolean tryLock() {
        try {
            if (this.currentPath.get() == null || !zkClient.exists(this.currentPath.get())){
                System.out.println(Thread.currentThread().getName() + "尝试获取分布式锁" + this.currentPath.get());
                String path = zkClient.createEphemeralSequential(lockPath+"/","locked");//创建临时顺序节点
                currentPath.set(path);
                count.set(0);
            }
            //获得所有的子节点
            List<String> children = this.zkClient.getChildren(lockPath);
            Collections.sort(children);
            //判断当前节点是否是最小的  如果不是则说明我前面还有人
            if (currentPath.get().equals(lockPath + "/" + children.get(0))){
                //重入次数+1
                count.set(count.get() +1);
                System.out.println(Thread.currentThread().getName() +  "获得分布式锁");
                return true;
            }
            //如果不是，则我把排在我前面的节点找出
            for (int i = 0; i < children.size(); i++) {
                if (currentPath.get().substring(lockPath.length()+1).equals(children.get(i))){
                    beforePath.set(lockPath + "/" + children.get(i-1));
                    System.out.println(Thread.currentThread().getName() + "找到前一个节点" + beforePath.get());
                }
            }
            return false;
        }catch (Exception e){
            //如果该节点被创建，则会抛出错误，则认为抢锁失败
            System.out.println(Thread.currentThread().getName() + "此node节点已被创建");
            e.printStackTrace();
            return false;
        }
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

    @Override
    public void unlock() {
        System.out.println(Thread.currentThread().getName() + "释放分布式锁");
        if (count.get() > 1){
            //重入次数-1
            count.set(count.get() -1);
        }
        if (currentPath.get() != null){
            zkClient.delete(currentPath.get());
            currentPath.set(null);
            count.set(0);
        }
    }

    private void waitForLock() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        IZkDataListener zkListener = new IZkDataListener() {
            @Override
            public void handleDataChange(String s, Object o)  {
                //do nothing
                System.out.println(Thread.currentThread().getName() + "数据被修改");
            }
            @Override
            public void handleDataDeleted(String s) {
                System.out.println(Thread.currentThread().getName() + "节点被删除，唤醒另外一个线程");
                //节点被删除，有人释放了锁,唤醒阻塞的线程  （网络原因临时节点会被删除）
                countDownLatch.countDown();
            }
        };
        zkClient.subscribeDataChanges(beforePath.get(),zkListener);
        if (this.zkClient.exists(this.beforePath.get())){
            countDownLatch.await();
        }
        zkClient.unsubscribeDataChanges(beforePath.get(),zkListener);
    }

    public static void main(String[] args) throws InterruptedException {
        //商品 id
        String productId = "abcd";
        ZKDistributeLock2 lock = new ZKDistributeLock2("/zk/"+productId);
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
    public void lockInterruptibly() throws InterruptedException {

    }
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }
    @Override
    public Condition newCondition() {
        return null;
    }

}
