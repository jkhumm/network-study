package humm.thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author heian
 * @date 2021/3/3 2:29 下午
 * @description 三个线程依次输出 每个线程输出count次
 */
public class OrderThreadDemo {

    static int count = 10;
    ReentrantLock lock = new ReentrantLock();
    Condition conditionA = lock.newCondition();
    Condition conditionB = lock.newCondition();
    Condition conditionC = lock.newCondition();

    public void funcA() throws InterruptedException {
        int numA = 0;
        lock.lock();
        while (numA < count){
            TimeUnit.SECONDS.sleep(2);
            System.out.println(Thread.currentThread().getName() + "   " + ++numA);
            conditionB.signal();//唤醒B线程
            conditionA.await();//释放A的锁,执行下面代码必须是a再次去抢锁成功，也就是a会再次抢锁而不用lock方法去抢
            //System.out.println("a陷入阻塞被唤醒");
        }
        lock.unlock();
    }

    public void funcB() throws InterruptedException {
        int numB = 0;
        lock.lock();
        while (numB < count){
            TimeUnit.SECONDS.sleep(2);
            System.out.println(Thread.currentThread().getName() + "   " + ++numB);
            conditionC.signal();
            conditionB.await();
            //System.out.println("b陷入阻塞被唤醒");
        }
        lock.unlock();
    }

    public void funcC() throws InterruptedException {
        int numC = 0;
        lock.lock();
        while (numC < count){
            TimeUnit.SECONDS.sleep(2);
            System.out.println(Thread.currentThread().getName() + "   " + ++numC);
            conditionA.signal();
            conditionC.await();
           //System.out.println("c陷入阻塞被唤醒");
        }
        lock.unlock();
    }

    public static void main(String[] args) throws InterruptedException {
        OrderThreadDemo demo = new OrderThreadDemo();
        new Thread(() -> {
            try {
                demo.funcA();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A线程").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                demo.funcB();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"B线程").start();

        TimeUnit.SECONDS.sleep(1);

        new Thread(() -> {
            try {
                demo.funcC();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"C线程").start();


    }

}
