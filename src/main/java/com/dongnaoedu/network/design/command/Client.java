package com.dongnaoedu.network.design.command;

import java.util.Scanner;

public class Client {

	public static void main(String[] args) {
		Waitress waiter = new Waitress();
		waiter.register("烧仙草", new ShaoxiancaoMilk());
		waiter.register("原味奶茶", new TasteMilk());
		waiter.register("木瓜奶茶", new PawpawMilk());

		waiter.showMenu();
		Scanner scanner = new Scanner(System.in);
		System.out.println("请选择：");
		// 发送内容
		String command = scanner.nextLine();
		waiter.receiver(command);
		scanner.close();
	}
	


	
}
