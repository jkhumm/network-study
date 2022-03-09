package humm.redis;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author heian
 * @date 2021/3/6 6:01 下午
 */
public class RedisDemo {

    static Lock lock = new ReentrantLock();
    static String redisData = null;
    static String mysqlData = null;

    public static void setDataFromRedis(String key){
        redisData = "redisData";
        System.out.println("redisUtil.set(key)");
    }

    public static Object getDataFromRedis(String key){
        return redisData;
    }

    public static Object getDataFromDb(String key){
        mysqlData = "mysqlData";
        return mysqlData;
    }

    public static Object getData(String key) {
        //从缓存中获取数据
        Object result = getDataFromRedis(key);
        if (result == null) {
            if (lock.tryLock()) {
                result = getDataFromDb(key);
                if (result != null) {
                    setDataFromRedis(key);
                }
                lock.unlock();
            }else {
                LockSupport.parkNanos(1000*1000*1000*1);
                result =  getData(key);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        new Thread(() -> {
            System.out.println(getData(""));
        }).start();
        LockSupport.parkNanos(1000*1000*100*5);
        new Thread(() -> {
            System.out.println(getData("bbb"));
        }).start();
    }

}
