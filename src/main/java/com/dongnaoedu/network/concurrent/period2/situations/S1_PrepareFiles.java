package com.dongnaoedu.network.concurrent.period2.situations;

import java.io.*;
import java.util.UUID;

public class S1_PrepareFiles {
    public static void main(String args[]) throws IOException, InterruptedException {
        File file1=new File("D:\\Temp\\test_files\\");
        if (!file1.exists()) file1.mkdir();


        String path = "D:\\Temp\\test_files\\testfile.txt";
        for (int i=0; i<5000; i++){
            System.out.println("");

            String destName =  path + "testFile" + UUID.randomUUID() + ".txt";
            copyFileUsingFileStreams(path,destName);
            System.out.println("done:" + i);
        }
    }


    private static void copyFileUsingFileStreams(String sourceName, String destName)
            throws IOException {
        File source = new File(sourceName);
        File dest = new File(destName);

        InputStream input = null;
        OutputStream output = null;
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(dest);
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }



}
