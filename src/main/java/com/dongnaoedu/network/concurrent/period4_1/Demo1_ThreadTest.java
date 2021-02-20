package com.dongnaoedu.network.concurrent.period4_1;


import com.dongnaoedu.network.concurrent.period1.Student;

public class Demo1_ThreadTest {
    public static void main(String args[]){
/*        Thread th = new Thread(){
            @Override
            public void run() {
                System.out.println("run method Overrided...");
            }
        };
        th.start();*/

        //通过自定义方式，可实现向线程传参
        MyThread myThread = new MyThread(new Student ());
        myThread.start();


    }


}

class MyThread extends Thread{
    private Student stu;

    public MyThread(Student stu){
        this.stu = stu;
    }

    @Override
    public void run() {
        System.out.println(stu.name);
        System.out.println(stu.age);
    }
}
