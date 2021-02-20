package com.dongnaoedu.network.concurrent.period1;

//理解 栈封闭
public class Demo6 {
    //static Demo6 ref = null;

    public static void main(String args[]){
        //thread1
        new Thread(new Runnable() {
            public void run() {
                Demo6 demo = new Demo6();

                //ref = demo;
            }
        }).start();

        //thread2
        new Thread(new Runnable() {
            public void run() {

                //ref.toString();

            }
        }).start();


    }
}
