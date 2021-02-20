package com.dongnaoedu.network.concurrent.period2.situations;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Scanner;

public class S2_KodyBrowser {
    public static void main(String args[]) throws IOException, InterruptedException {
        Scanner sc = new Scanner(System.in);


        while (true){
            System.out.print("Enter an url:");
            String uri = sc.nextLine();

            asyncRequest(uri);
            //sendRequest(uri);
        }
    }

    public static void asyncRequest(String uri){
        final String myUri = uri;
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendRequest(myUri);
            }
        }).start();
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
