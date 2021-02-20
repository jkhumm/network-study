package com.dongnaoedu.network.design.observer;

public class LaoWang2 implements ObServer {

	@Override
	public void update(Object msg) {
		//拿到女神发的朋友圈
		System.out.println(getClass().getSimpleName()+" 收到该主题的消息：" + msg);
	}

}
