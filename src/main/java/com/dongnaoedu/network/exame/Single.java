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
        int a = 1;
        int b = 10;

        b = a + b;//1 + 10
        a = b - a ;//1 + 10 - 1;

        b =  b - a;

        System.out.println(a);
        System.out.println(b);


    }


}
