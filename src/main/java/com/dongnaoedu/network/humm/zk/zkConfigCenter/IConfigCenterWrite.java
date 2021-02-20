package com.dongnaoedu.network.humm.zk.zkConfigCenter;

import java.util.Properties;

/**
 * @author heian
 * @create 2020-03-22-10:02 下午
 * @description 配置文件配置在zk固定的目录下，所以此处接口入参只需要传文件名而不是整个路径
 */
public interface IConfigCenterWrite {

    //创建一个新的配置文件
    String insertCfgFile(String fileName, Properties properties);

    //删除一个配置文件
    boolean deleteCfgFile(String fileName);

    //修改
    void updateCfgFile(String fileName,Properties properties);

    //查
    Properties selectCfgFile(String fileName);
}
