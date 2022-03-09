package humm.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author heian
 * @date 2021/3/3 2:29 下午
 * @description 三个线程依次输出 每个线程输出count次
 */
public class OrderThreadDemo2 {

    static int count = 10;
    ReentrantLock lock = new ReentrantLock();
    static List<Thread> list = new ArrayList<>(3);

    public void funcA() throws InterruptedException {
        int numA = 0;
        while (numA < count){
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            System.out.println(Thread.currentThread().getName() + "   " + ++numA);
            LockSupport.unpark(list.get(1));//唤醒B线程
            lock.unlock();//释放锁资源
            LockSupport.park();//阻塞不会释放锁  被唤醒后再次执行不会自动抢锁跟condition.await不同
        }
    }

    public void funcB() throws InterruptedException {
        int numB = 0;
        while (numB < count){
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            System.out.println(Thread.currentThread().getName() + "   " + ++numB);
            LockSupport.unpark(list.get(2));
            lock.unlock();
            LockSupport.park();//阻塞
        }
    }

    public void funcC() throws InterruptedException {
        int numC = 0;
        while (numC < count){
            lock.lock();
            TimeUnit.SECONDS.sleep(1);
            System.out.println(Thread.currentThread().getName() + "   " + ++numC);
            LockSupport.unpark(list.get(0));
            lock.unlock();
            LockSupport.park();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        OrderThreadDemo2 demo2 = new OrderThreadDemo2();
        Thread threadA = new Thread(() -> {
            try {
                demo2.funcA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A线程");
        Thread threadB = new Thread(() -> {
            try {
                demo2.funcB();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B线程");
        Thread threadC = new Thread(() -> {
            try {
                demo2.funcC();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"C线程");

        list.add(threadA);list.add(threadB);list.add(threadC);
        for (int i = 0; i < 3; i++) {
            list.get(i).start();
            TimeUnit.SECONDS.sleep(1);
        }


    }

}
