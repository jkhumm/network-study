package com.dongnaoedu.network.humm.zk.zkDemo;

import com.dongnaoedu.network.humm.zk.MyZkSerialiZer;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

import java.util.List;
import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @create 2020-03-16-11:12 下午
 * @description
 */
public class ZkClientDemo {

    public static void main(String[] args) {
        //内部已经帮我们连接了
        ZkClient zkClient = new ZkClient("192.168.0.102:2183");
        zkClient.setZkSerializer(new MyZkSerialiZer());
        //创建持久化节点 前提是/zk 目录必须存在
        zkClient.create("/zk/hmm1","hmm", CreateMode.PERSISTENT);

        //对该节点下的子节点进行监听
        zkClient.subscribeChildChanges("/zk/hmm1", new IZkChildListener() {
            @Override
            public void handleChildChange(String parentPath, List<String> list) throws Exception {
                // create /zk/app3/app3-son1 son1   多了个节点
                System.out.println(parentPath + "子节点发生变化");
            }
        });

        //对该节点下的数据进行监听
        zkClient.subscribeDataChanges("/zk/hmm1", new IZkDataListener() {
            @Override
            public void handleDataChange(String parentPath, Object o) throws Exception {
                //set /zk/hmm newhmm

                System.out.println(parentPath + "发生变化了，变化成为：" + o + "线程类型" + Thread.currentThread().isDaemon());
            }

            @Override
            public void handleDataDeleted(String parentPath) throws Exception {
                //delete /zk/app3
                System.out.println(parentPath + "被删除"+Thread.currentThread().isDaemon());
            }
        });

        while (Thread.activeCount()>=1){
            LockSupport.parkNanos(1000000000*100L);
        }
    }
}

