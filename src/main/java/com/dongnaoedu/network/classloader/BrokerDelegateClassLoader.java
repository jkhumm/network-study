package com.dongnaoedu.network.classloader;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author heian
 * @date 2021/2/28 8:31 下午
 * @description
 */
public class BrokerDelegateClassLoader extends ClassLoader{
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
    /**
     * 复写此方法，破坏双亲委派模型
     * @param className 类的全路径名
     * @param resolve 控制加载的类是否被链接（验证、准备、解析），为false则不会记性链接阶段的继续执行也就不会导致类的初始化
     */
    @Override
    protected Class<?> loadClass(String className, boolean resolve) throws ClassNotFoundException {
        //根据类的全路径名称进行加锁，确保多线程类只被加载一次
        synchronized (getClassLoadingLock(className)){
            //到已加载的类的缓存中查看该类是否已经被加载，如果已经加载则直返回
            Class<?> c = findLoadedClass(className);
            //如果缓存中没有加载此类，需要对其进行首次加载，如果类的全路径以java和javax开头，则直接委托给系统类加载器appClassLoader进行加载
            if (c == null) {
                if (className.startsWith("java.") || className.startsWith("javax")){
                    try {
                        c = getSystemClassLoader().loadClass(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }else {
                    //则尝试用我们的自定义类加载器进行加载
                    try {
                        c = this.findClass(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    //如果自定义加载没有完成对类的加载则委托给父类加载器进行加载或者系统类加载器进行加载
                    if (c == null) {
                        if (getParent() == null){
                            c = getSystemClassLoader().loadClass(className);
                        }else {
                            c = getParent().loadClass(className);
                        }
                    }
                }
            }
            //经过若干次尝试，如果还是无法对类进行加载，则抛出无法找到该类的异常
            if (c == null) {
                throw new ClassNotFoundException("the class " + className + "not found");
            }
            if (resolve) {
                resolveClass(c);
            }
            return c;
        }
    }

    public static void main(String[] args) throws Exception {
        BrokerDelegateClassLoader classLoader = new BrokerDelegateClassLoader();
        Class<?> aClass = classLoader.loadClass(file_path);
        System.out.println(aClass.getClassLoader());

    }

}
