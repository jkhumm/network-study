package com.dongnaoedu.network.humm.多线程.CAS;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author heian
 * @create 2020-02-22-4:14 下午
 * @description
 */
public class CSADemo1 {

    private static CSADemo1 sss = null;

    static {
        Field field = null;
        try {
            field = CSADemo1.class.getDeclaredField("sss");
            field.setAccessible(true);
            CSADemo1 o = (CSADemo1)field.get(null);
            System.out.println(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }




    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

    }


}
