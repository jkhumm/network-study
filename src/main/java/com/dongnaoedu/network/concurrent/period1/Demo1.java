package com.dongnaoedu.network.concurrent.period1;

public class Demo1 {

    final int NUMBER = 20;
    private static Student stu = new Student();

    public static void main(String args[]){
        int x = 500;
        int y = 100;
        int a = x/y;

        String envName = "JAVA_HOME";
        stu.age = a;

        String path = System.getenv(envName);
    }
}
