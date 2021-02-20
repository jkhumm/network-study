package com.dongnaoedu.network.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BIOServerV1 {

    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("服务器启动成功");
        while (!serverSocket.isClosed()) {
            Socket request = serverSocket.accept();// 阻塞
            System.out.println("收到新连接 : " + request.toString());
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 接收数据、打印
                        InputStream inputStream = request.getInputStream(); // net + i/o
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
                        String msg;
                        while ((msg = reader.readLine()) != null) { // 没有数据，阻塞
                            if (msg.length() == 0 || "bye".equals(msg)) {
                                break;
                            }
                            System.out.println(msg);
                        }
                        System.out.println("收到数据,来自："+ request.toString());
                        OutputStream outputStream = request.getOutputStream();
                        outputStream.write("hello".getBytes());
                        outputStream.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            request.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
        serverSocket.close();
    }
}
