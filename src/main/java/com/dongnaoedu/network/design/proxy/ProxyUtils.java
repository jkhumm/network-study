package com.dongnaoedu.network.design.proxy;

import sun.misc.ProxyGenerator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ProxyUtils {

	/**
	 * 将根据类信息 动态生成的二进制字节码保存到硬盘中，
	 * @param proxyName 为动态生成的代理类的名称 $Proxy0
	 * @param classes 需要生成动态代理类的类
	 */
	public static void generateClassFile(String proxyName,Class<?>[] interfaces) throws IOException {
		// 根据类信息和提供的代理类名称，生成字节码
		byte[] classFile = ProxyGenerator.generateProxyClass(proxyName, interfaces);
		ProxyUtils.writeToFile(interfaces[0], classFile, proxyName);
	}

	public static void writeToFile(Class<?> interfaces, byte[] classFile, String proxyName) throws IOException {
		String path = interfaces.getResource(".").getPath()  + proxyName + ".class";
		System.out.println(path);
		// 保留到硬盘中  文件不存在
		File file = new File(path);
		try(FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(classFile);
			fos.flush();
			System.out.println("代理类class文件写入成功");
		} catch (Exception e) {
			System.out.println("写文件错误");
			e.printStackTrace();
		}
	}

}