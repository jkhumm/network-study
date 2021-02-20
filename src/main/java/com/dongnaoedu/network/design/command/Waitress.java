package com.dongnaoedu.network.design.command;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务生招待客人
 */
public class Waitress {
	private Map<String, Command> commands = new HashMap<>();
	
	public void register(String cmd, Command run) {
		commands.put(cmd, run);
	}
	/**
	 * 客人点单
	 * @param command
	 */
	public void receiver(String command) {
		System.out.println("您选择了："+command);
		Command cmd = commands.get(command);
		if(cmd == null) {
			System.out.println("没有这样的品种");
		}else {
			cmd.build();
		}
		
	}
	
	public void showMenu() {
		System.out.println("老板你好，本有以下奶茶：");
		commands.keySet().forEach((item)->{
			System.out.println("\t"+item);
		});
	}
	
}
