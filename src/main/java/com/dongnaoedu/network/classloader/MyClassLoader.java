package com.dongnaoedu.network.classloader;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class MyClassLoader extends ClassLoader {

    public static final String file_path = "/Users/apple/Desktop/HelloWorld.class";
    public static final String classPath = "com.dongnaoedu.network.classloader.HelloWorld";

    //将class文件读入内存
    private static byte[] getClassBytes(String filePath) throws ClassNotFoundException{
        String name = file_path;
        System.out.println("文件地址：" + name);
        Path path = Paths.get(name);
        if (!path.toFile().exists()){
            throw new ClassNotFoundException ("该路径下" + filePath + "无class文件");
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream ()){
            Files.copy(path,out);
            return out.toByteArray();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Class<?> findClass(String path) throws ClassNotFoundException {
        byte[] bytes = getClassBytes(path);
        if(bytes.length == 0){
            throw new ClassNotFoundException ("找不到该文件");
        }
        return this.defineClass (classPath,bytes,0,bytes.length);//第一个参数为类名
    }
    //返回大写setName方法名
    public static String setProperty(String property){
        return "set" + property.substring (0,1).toUpperCase () + property.substring (1);
    }

    public static void main(String[] args) throws Exception{
        MyClassLoader myClassLoader = new MyClassLoader();
        System.out.println(MyClassLoader.class.getClassLoader().getParent().toString());//sun.misc.Launcher$ExtClassLoader@3feba861
        //loadClass 极为重要
        Class<?> myClass = myClassLoader.loadClass (file_path);//如果 这个类存在则不会用自定义加载器加载
        System.out.println (myClass.getClassLoader ());//com.dongnaoedu.network.classloader.MyClassLoader@3feba861
        Object obj = myClass.newInstance ();
        System.out.println (obj);//hello,java
        Map<String,String> map = new HashMap<>();
        map.put ("name","huling");
        map.put ("age","25");
        Field[] declaredFields = myClass.getDeclaredFields ();
        //只对private封装的属性进行设置属性
        for (int i=0;i<declaredFields.length;i++){
            int key = declaredFields[i].getModifiers ();//返回修饰符的参数
            if(Modifier.toString (key).equals ("private")){//对修饰符进行解码
                String property = declaredFields[i].getName ();
                Method setMeth = myClass.getMethod (MyClassLoader.setProperty (property),String.class);//获取set方法
                setMeth.invoke (obj,map.get (property));
            }
        }
        System.out.println ( myClass.getMethod ("getName",null ).invoke (obj,null));
        System.out.println (myClass.getMethod ("getAge",null).invoke (obj,null));
    }
}