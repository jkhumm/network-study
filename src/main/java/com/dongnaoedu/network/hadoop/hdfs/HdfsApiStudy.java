package com.dongnaoedu.network.hadoop.hdfs;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

/**
 * @author heian
 * @date 2022/3/23 4:47 下午
 */
public class HdfsApiStudy {


    @Test
    public void getFileSystem1() throws IOException {
        Configuration configuration = new Configuration();
        // 指定我们使用的文件系统
        configuration.set("fs-defaultFS","hdfs://node01:8082/");// node01 ==> ip 需要配置hosts的映射
        // 获取指定的文件系统，相当于获取了主节点中所有的元数据信息
        FileSystem fileSystem = FileSystem.get(configuration);
        System.out.println(fileSystem.toString());
    }

    @Test
    public void getFileSystem2() throws Exception {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8082/"),configuration);
        System.out.println(fileSystem.toString());
    }

    @Test
    public void getFileSystem3() throws Exception {
        Configuration configuration = new Configuration();
        configuration.set("fs-defaultFS","hdfs://node01:8082/");
        FileSystem fileSystem = FileSystem.newInstance(configuration);
        System.out.println(fileSystem.toString());
    }

    @Test
    public void getFileSystem4() throws Exception {
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.newInstance(new URI("hdfs://node01:8082/"),configuration,"root");// 伪造身份去访问文件
        System.out.println(fileSystem.toString());
    }

    @Test
    public void listMyFiles() throws IOException {
        Configuration configuration = new Configuration();
        configuration.set("fs-defaultFS","hdfs://node01:8082/");
        FileSystem fileSystem = FileSystem.get(configuration);


        RemoteIterator<LocatedFileStatus> remoteIterator = fileSystem.listFiles(new Path("/"), true);
        while (remoteIterator.hasNext()){
            LocatedFileStatus next = remoteIterator.next();
            // 文件存储路径
            String filePath = next.getPath().toString();
            // block 存储信息
            BlockLocation[] blockLocations = next.getBlockLocations();
            for (BlockLocation blockLocation : blockLocations) {
                // block副本存储的位置
                String[] hosts = blockLocation.getHosts();
            }
        }

        // 创建文件夹
        if (fileSystem.mkdirs(new Path("/hello/test"))) {
            System.out.println("mkdir success");
        }
        // 创建文件
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/a.txt"));
        // 方式一：下载文件到本地
        FSDataInputStream inputStream = fileSystem.open(new Path("/exist.txt"));
        FileOutputStream outputStream = new FileOutputStream(new File("E://exist.txt"));
        IOUtils.copy(inputStream, outputStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);

        // 方式二：下载文件到本地
        fileSystem.copyFromLocalFile(new Path("/exist.txt"),new Path("E://exist.txt"));

        // 文件上传
        fileSystem.copyFromLocalFile(new Path("E://exist.txt"),new Path("/exist.txt"));

        // 本地小文件合并上传到hdfs
        FSDataOutputStream out = fileSystem.create(new Path("/big.xml"));
        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration());
        FileStatus[] fileStatuses = localFileSystem.listStatus(new Path("file:////E:\\existDir"));
        for (FileStatus fileStatus : fileStatuses) {
            FSDataInputStream in = localFileSystem.open(fileStatus.getPath());
            IOUtils.copy(in,out);
            IOUtils.closeQuietly(in);
        }
        IOUtils.closeQuietly(out);
        localFileSystem.close();
        
        fileSystem.close();
    }


}
