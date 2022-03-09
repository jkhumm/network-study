package humm.thread;

import java.util.concurrent.ConcurrentSkipListMap;

/**
 * @author heian
 * @date 2021/3/5 10:05 上午
 * @description
 */
public class test {


    public static void main(String[] args) {
        InheritableThreadLocal<String> tl = new InheritableThreadLocal<>();
        ConcurrentSkipListMap<String,String> map = new ConcurrentSkipListMap<>();
        //Proxy.newProxyInstance()
    }

}
