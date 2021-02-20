package com.dongnaoedu.network.bio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

public class BIOClient {
    private static Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 8080);
        OutputStream out = s.getOutputStream();

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String msg = scanner.nextLine() + "\r\n";
        out.write(msg.getBytes(charset)); // 阻塞，写完成
        out.flush();
        scanner.close();


        InputStream inputStream = s.getInputStream(); // net + i/o
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        while ((msg = reader.readLine()) != null) { // 没有数据，阻塞
            if (msg.length() == 0) {
                break;
            }
            System.out.println(msg);
        }
        s.close();
    }

}
