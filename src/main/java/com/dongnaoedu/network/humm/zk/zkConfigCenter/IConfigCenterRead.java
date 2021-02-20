package com.dongnaoedu.network.humm.zk.zkConfigCenter;

import java.util.Properties;

/**
 * @author heian
 * @create 2020-03-22-10:02 下午
 * @description 配置文件读取
 */
public interface IConfigCenterRead {

    //监听配置文件发生变化
    void watchCfgFile(String fileName, ChangeHandler changeHandler);

    interface ChangeHandler{
        //配置文件发生变化后，给一个完整的属性对象
        void itemChange(Properties properties);
    }

}
