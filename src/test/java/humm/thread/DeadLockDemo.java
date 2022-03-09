package humm.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @date 2021/3/3 2:09 下午
 */
public class DeadLockDemo {

   static Object a = new Object();
    static Object b = new Object();


    public static void afunc(){
        synchronized (b){
            System.out.println("afunc" + "拿到b的锁");
            LockSupport.parkNanos(1000*1000*1000*2L);
            synchronized (a){
                System.out.println("afunc" + "拿到a的锁");
            }
        }
    }

    public static void bfunc(){
        synchronized (a){
            System.out.println("bfunc拿到a的锁");
            LockSupport.parkNanos(1000*1000*1000*1L);
            synchronized (b){
                System.out.println("bfunc拿到b的锁");
            }
        }
    }


    public static void main(String[] args) {
       // Callable
       // FutureTask
        new Thread(() -> {
            afunc();
        }).start();
        new Thread(() -> {
            bfunc();
        }).start();
    }

}
