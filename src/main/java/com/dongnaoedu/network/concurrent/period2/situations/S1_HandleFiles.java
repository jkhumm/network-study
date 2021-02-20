package com.dongnaoedu.network.concurrent.period2.situations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class S1_HandleFiles {
    public static void main(String args[]) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();

        File path = new File("D:\\Temp\\test_files");
        File[] files = path.listFiles();
        System.out.println(files.length);

/*        for (File file : files){
            handleFile(file);
        }*/


        ExecutorService pool = new ThreadPoolExecutor(
                8,
                8,
                60,
                TimeUnit.SECONDS.SECONDS,
                new LinkedBlockingQueue<Runnable>(10000));

        for (final File file : files){
            Runnable cmd = new Runnable() {
                public void run() {
                    try {
                        handleFile(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            };
            pool.submit(cmd);
        }

        System.out.println("任务提交完毕 。。。");

        pool.shutdown();
        pool.awaitTermination(10000,TimeUnit.SECONDS);


        long endTime = System.currentTimeMillis();
        System.out.println("耗时" + (endTime - startTime)/1000 + "s");

    }

    public static void handleFile(File file) throws IOException {
        int length = 0;

        FileReader fr = new FileReader(file);
        BufferedReader bf = new BufferedReader(fr);
        String str;
        // 按行读取字符串
        while ((str = bf.readLine()) != null) {
            for (char c : str.toCharArray()){
                length++;
            }
        }
        bf.close();
        fr.close();
        //System.out.println(length);
    }

}
