package humm.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DoubleThreadDemo {
 
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition ();
 
    public void await(){
        try {
            lock.lock ();
            System.out.println (Thread.currentThread ().getName () + "线程获得锁");
            condition.await ();
            System.out.println(Thread.currentThread ().getName () + "立即执行");
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }finally {
            System.out.println (Thread.currentThread ().getName () + "线程释放锁");
            lock.unlock ();
        }
    }
 
    public void single() throws InterruptedException {
        lock.lock ();
        System.out.println (Thread.currentThread ().getName () + "线程获得锁");
        condition.signal ();
        System.out.println (Thread.currentThread ().getName () + "线程唤醒睡眠线程");
        TimeUnit.SECONDS.sleep(3);
        lock.unlock ();
        System.out.println (Thread.currentThread ().getName () + "线程释放锁");
    }
 
    public static void main(String[] args) throws InterruptedException{
        DoubleThreadDemo demo1 = new DoubleThreadDemo();
        new Thread (() -> {
            demo1.await ();
        },"A线程").start ();
        TimeUnit.SECONDS.sleep (3);

        new Thread (() -> {
            try {
                demo1.single ();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B线程").start ();
 
    }
 
}