package com.dongnaoedu.network.humm.zk.zkConfigCenter;

import java.util.Properties;
import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @create 2020-03-22-11:10 下午
 * @description
 */
public class Test {


    public static void main(String[] args) throws InterruptedException {
        IConfigCenterWrite write = new ConfigCenterImpl();
        String fileName = "application.properties";
        write.deleteCfgFile(fileName);
        //创建配置文件
        Properties properties = new Properties();
        properties.put("my.girl.name", "chenLing");
        properties.put("my.girl.age", "26");
        String s = write.insertCfgFile(fileName, properties);
        System.out.println("new file: "+s);

        new Thread(() -> {
            IConfigCenterRead read = new ConfigCenterImpl();
            read.watchCfgFile(fileName, new IConfigCenterRead.ChangeHandler() {
                @Override
                public void itemChange(Properties properties) {
                    System.out.println(Thread.currentThread().getName() + " 监听到数据发生变化");
                }
            });
        }).start();
        LockSupport.parkNanos(1000000000*3L);
        //修改  新增  删除
        Properties properties2 = new Properties();
        properties2.put("my.girl.name", "chenLing2");
        properties2.put("my.girl.age", "18");
        properties2.put("my.girl.location", "深圳");
        write.updateCfgFile(fileName,properties2);
        //因为监听属于守护线程
        Thread.currentThread().join();

    }



}
