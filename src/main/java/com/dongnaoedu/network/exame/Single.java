package com.dongnaoedu.network.exame;

/**
 * @author heian
 * @date 2021/3/15 8:02 下午
 * @description 章鱼游戏面试
 */
public final class Single {


    static class Holder{
        private static Single single = new Single();
    }

    public static Single getInstance(){
        return Holder.single;
    }

    public static void main(String[] args) {
        // 如何实现 将ab的值互相调换，但是不能通过中间变量
        int a = 1;
        int b = 10;
        b = a + b;//1 + 10
        a = b - a ;//1 + 10 - 1 a+b-a ==> a=b

        b =  b - a;  // a+b-b ==> b-a

        System.out.println(a);
        System.out.println(b);


    }


}
