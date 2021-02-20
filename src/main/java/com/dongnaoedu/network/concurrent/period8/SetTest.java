package com.dongnaoedu.network.concurrent.period8;

import java.util.HashSet;
import java.util.Set;

public class SetTest {
    public static void main(String args[]){


/*        System.out.println("test1===========================");
        Set<String> set = new HashSet<>();
        set.add("kody");
        set.add("kody");
        System.out.println(set.size());*/



        System.out.println("test2===========================");
        Set<Integer> nums = new HashSet<>();
        for (int i=1; i< 100000; i++){      //将i设置为100000
            nums.add(i);
        }

        for (int i : nums){
            System.out.println(i);
        }





        //List可以保证顺序
/*        List<Integer> list = new ArrayList<>(100000);
        for (int i=1; i< 100000; i++){      //将i设置为100000
            list.add(i);
        }

        for (int i : list){
            System.out.println(i);
        }

        list.get(0);*/


/*        System.out.println("test1===========================");
        CopyOnWriteArraySet<String> set2 = new CopyOnWriteArraySet<>();
        set.add("kody");
        set.add("kody");
        System.out.println(set.size());

        System.out.println("test1===========================");
        ConcurrentSkipListSet<String> set3 = new ConcurrentSkipListSet<>();
        set.add("kody");
        set.add("kody");
        System.out.println(set.size());*/



    }
}
