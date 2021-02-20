package com.dongnaoedu.network.humm.zk.zkConfigCenter;

import com.dongnaoedu.network.humm.zk.MyZkSerialiZer;
import com.dongnaoedu.network.humm.zk.zkDistributeLock.ZKDistributeLock2;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNodeExistsException;
import org.apache.commons.lang.StringUtils;

import java.net.Inet4Address;
import java.util.*;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author heian
 * @create 2020-03-22-10:08 下午
 * @description
 */
public class ConfigCenterImpl implements IConfigCenterWrite,IConfigCenterRead {

    private String configFilePath;//配置文件地址 /distributeConfigure/cfgFile
    private String lockPath;//锁的文件地址
    private static final String default_configRootPath = "/distributeConfigure";

    private ZkClient client;

    public ConfigCenterImpl(){
        this(default_configRootPath);
    }

    public ConfigCenterImpl(String path){
        if (StringUtils.isBlank(path))
            throw new IllegalArgumentException("节点地址参数不能为空");
        configFilePath = path + "/cfgFile";
        lockPath = path + "/writeLock";
        String ip4 = "";
        try {
            ip4 = Inet4Address.getLocalHost().getHostAddress();
        }catch (Exception e){
            System.out.println("ip获取异常：" + ip4);
        }
        client = new ZkClient(ip4 + ":2183");
        client.setZkSerializer(new MyZkSerialiZer());
        if (!this.client.exists(configFilePath)) {
            try {
                this.client.createPersistent(configFilePath, true);
            } catch (ZkNodeExistsException e) {

            }
        }
    }

    @Override
    public String insertCfgFile(String fileName, Properties properties) {
        checkElement(fileName);
        //创建配置文件节点：一个文件为一个父节点；一个属性key为一个子节点,子节点的数据为value
        String parentNodePath = configFilePath + "/" + fileName;
        if (client.exists(parentNodePath))
            throw new IllegalArgumentException("文件"+parentNodePath+"已存在！");
        client.createPersistent(parentNodePath,true);
        if (properties == null) return parentNodePath;
        ZKDistributeLock2 lock = new ZKDistributeLock2(lockPath + "/" + fileName);
        lock.lock();
        for (Map.Entry<Object, Object> entry:properties.entrySet()){
            System.out.println("新增的属性为：" + entry.getKey() + "=" + entry.getValue());
            String sonNodePath = parentNodePath + "/" + entry.getKey();
            client.createPersistent(sonNodePath,entry.getValue());
        }
        lock.unlock();
        return parentNodePath;
    }

    @Override
    public boolean deleteCfgFile(String fileName) {
        checkElement(fileName);
        String parentNodePath = configFilePath + "/" + fileName;
        ZKDistributeLock2 lock = new ZKDistributeLock2(lockPath + "/" + fileName);
        lock.lock();
        boolean bool =  client.deleteRecursive(parentNodePath);
        lock.unlock();
        return bool;
    }

    @Override
    public void updateCfgFile(String fileName, Properties properties) {
        checkElement(fileName);
        if(properties == null) {throw new NullPointerException("要修改的配置项不能为空");}
        String parentNodePath = configFilePath + "/" + fileName;
        ZKDistributeLock2 lock = new ZKDistributeLock2(lockPath + "/" + fileName);
        lock.lock();
        try {
            List<String> old = client.getChildren(parentNodePath);
            //遍历传来的内容 name 看这个节点是否存在于我们原来的zk文件中
            for (Map.Entry<Object, Object> entry :properties.entrySet()){
                System.out.println("修改的属性为：" + entry.getKey() + "=" + entry.getValue());
                String newSonKey = entry.getKey().toString();
                String newSonValue = entry.getValue().toString();

                String sonNodePath = parentNodePath + "/" + newSonKey;
                if (old.contains(newSonKey)){
                    //可能修改了，也可能没动
                    String oldSonVlue = client.readData(sonNodePath);
                    if (!oldSonVlue.equals(newSonValue)){
                        //动了则修改,类似于 set /path 123
                        client.writeData(sonNodePath,newSonValue);
                    }
                    //减少复杂度，剩下的就是要删除的（新增的做了，修改和没修改的也做了）
                    old.remove(newSonKey);
                }else {
                    //zk 原来不存在的，需要新增
                    client.createPersistent(sonNodePath,newSonValue);
                }
            }
            if (!old.isEmpty()){
                for (String key : old) {
                    client.delete(parentNodePath + "/" + key);
                }
            }
        }catch (Exception e){
            System.out.println("修改异常：" + e.getMessage());
        }finally {
            lock.unlock();
        }
    }

    @Override
    public Properties selectCfgFile(String fileName) {
        String parentNodePath = "";
        //watch
        if (fileName.startsWith("/")){
            parentNodePath = fileName;
        }else {
            //普通查询
            parentNodePath = configFilePath + "/" + fileName;
        }
        checkElement(parentNodePath);
        List<String> children = client.getChildren(parentNodePath);
        if (children == null || children.isEmpty())
            return new Properties();
        Properties p = new Properties();
        for (String child : children) {
            String sonNodePath = parentNodePath + "/" + child;
            String value = client.readData(sonNodePath,true);
            p.put(child,value);
        }
        return p;
    }

    //监听机制
    @Override
    public void watchCfgFile(String fileName, ChangeHandler changeHandler) {
        if (!fileName.startsWith("/")){
            fileName = configFilePath + "/" + fileName;
        }
        final String fileNodePath = fileName;
        Properties properties = selectCfgFile(fileName);
        if (properties != null){
            int waitTime = 5;
            //合并5秒配置项变化，5秒内只触发一次处理事件
            ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(1);
            scheduled.setRemoveOnCancelPolicy(true);
            final List<ScheduledFuture<?>> futureList =  new ArrayList<>();
            for (Map.Entry<Object, Object> entry :properties.entrySet()){
                System.out.println("监控："+fileNodePath+"/"+entry.getKey().toString());
                client.subscribeDataChanges(fileNodePath+"/"+entry.getKey().toString(), new IZkDataListener() {
                    @Override
                    public void handleDataDeleted(String dataPath) throws Exception {
                        System.out.println("触发删除："+dataPath);
                        triggerHandler(futureList, scheduled, waitTime, fileNodePath, changeHandler);
                    }

                    @Override
                    public void handleDataChange(String dataPath, Object data) throws Exception {
                        System.out.println("触发修改："+dataPath);
                        triggerHandler(futureList, scheduled, waitTime, fileNodePath, changeHandler);
                    }
                });

            }
            client.subscribeChildChanges(fileNodePath, new IZkChildListener() {
                @Override
                public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                    System.out.println("触发子节点："+parentPath);
                    triggerHandler(futureList, scheduled, waitTime, fileNodePath, changeHandler);
                }
            });
        }

    }

    /**
     * 合并修改变化事件，5秒钟内发生变化的合并到一个事件进行
     * @param futureList 装有定时触发任务的列表
     * @param scheduled 定时任务执行器
     * @param waitTime 延迟时间，单位秒
     * @param fileName zk配置文件的节点
     * @param changeHandler 事件处理器
     */
    private void triggerHandler(List<ScheduledFuture<?>> futureList, ScheduledThreadPoolExecutor scheduled, int waitTime, String fileName, ChangeHandler changeHandler) {
        if(futureList != null && !futureList.isEmpty()) {
            for(int i = 0 ; i < futureList.size(); i++) {
                ScheduledFuture<?> future = futureList.get(i);
                if(future != null && !future.isCancelled() && !future.isDone()) {
                    System.out.println("----取消------");
                    future.cancel(true);
                    futureList.remove(future);
                    i--;
                }
            }
        }
        ScheduledFuture<?> future = scheduled.schedule(()->{
            Properties p = selectCfgFile(fileName);
            changeHandler.itemChange(p);
        }, waitTime, TimeUnit.SECONDS);
        futureList.add(future);
    }


    private void checkElement(String v) {
        if (v == null) throw new NullPointerException();
        if("".equals(v.trim())) {
            throw new IllegalArgumentException("不能使用空格");
        }
        if(v.startsWith(" ") || v.endsWith(" ")) {
            throw new IllegalArgumentException("前后不能包含空格");
        }
    }

    public static void main(String[] args) throws Exception {
        ConfigCenterImpl.aVoid();
    }

    public static void aVoid() throws Exception {


        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);
        List<ScheduledFuture<?>> futureList = new ArrayList<>();
        System.out.println("开始");
        if(futureList != null && !futureList.isEmpty()) {
            for(int i = 0 ; i < futureList.size(); i++) {
                ScheduledFuture<?> future = futureList.get(i);
                if(future != null && !future.isCancelled() && !future.isDone()) {
                    System.out.println("----取消------");
                    future.cancel(true);
                    futureList.remove(future);
                    i--;
                }
            }
        }
        ScheduledFuture<?> schedule = executor.schedule(() -> {
            System.out.println("任务执行1");
        }, 5, TimeUnit.SECONDS);
        futureList.add(schedule);

        TimeUnit.SECONDS.sleep(1);
        if(futureList != null && !futureList.isEmpty()) {
            for(int i = 0 ; i < futureList.size(); i++) {
                ScheduledFuture<?> future = futureList.get(i);
                if(future != null && !future.isCancelled() && !future.isDone()) {
                    System.out.println("----取消------");
                    future.cancel(true);
                    futureList.remove(future);
                    i--;
                }
            }
        }
        ScheduledFuture<?> schedule2 = executor.schedule(() -> {
            System.out.println("任务执行2");
        }, 5, TimeUnit.SECONDS);
        futureList.add(schedule2);
    }

}
