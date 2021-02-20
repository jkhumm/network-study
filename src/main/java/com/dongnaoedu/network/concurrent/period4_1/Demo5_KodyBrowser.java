package com.dongnaoedu.network.concurrent.period4_1;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Scanner;

public class Demo5_KodyBrowser {
    public static void main(String args[]) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);

        //对于这一点，只要理解守护线程 和非守护线程的区别就行
        while (true){
            System.out.print("Enter an url:");
            String uri = sc.nextLine();

            asyncRequest(uri);
            Thread.currentThread().stop();
            //sendRequest(uri);
        }




    }







    public static void asyncRequest(String uri){
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                for (;;){
                    System.out.println("fasdfasd");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        th.setDaemon(true);
        th.start();
    }

    //发送一个HTTP请求
    public static void sendRequest(String uri)  {
        try {
            if (uri.length() == 0) return;
            CloseableHttpClient client = client = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet("http://" + uri);

            CloseableHttpResponse response = response = client.execute(httpGet);


            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            System.out.println(result);

            Thread.sleep(1000L);    //模拟1s中的等待
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
