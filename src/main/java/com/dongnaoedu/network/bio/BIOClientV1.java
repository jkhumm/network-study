package com.dongnaoedu.network.bio;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

public class BIOClientV1 {
    private static Charset charset = Charset.forName("UTF-8");

    public static void main(String[] args) throws Exception {
        Socket s = new Socket("localhost", 8080);
        OutputStream out = s.getOutputStream();

        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        String msg = scanner.nextLine() + "\n";
        out.write(msg.getBytes(charset)); // 阻塞，写完成
        scanner.close();
        out.write("bye\n".getBytes());

        InputStream inputStream = s.getInputStream();
        byte[] data = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(data)) > 0) {
            System.out.println(new String(data));
        }
        s.close();
    }

}
