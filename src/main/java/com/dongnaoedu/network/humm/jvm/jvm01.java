package com.dongnaoedu.network.humm.jvm;

/**
 * @author heian
 * @create 2020-03-15-3:37 下午
 * @description 栈帧 组成
 */
public class jvm01 {

    private static String name = "jack";

    public int  compare(){
        int a = 1;
        int b = 2;
        return b-a;
    }

    public static void main(String[] args) {
        jvm01 j = new jvm01();
        j.compare();
        System.out.println("开始");//对代码进行反汇编 javap -c jvm01.class >jvm.txt
    }
}
