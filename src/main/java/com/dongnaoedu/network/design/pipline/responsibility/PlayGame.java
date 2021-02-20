package com.dongnaoedu.network.design.pipline.responsibility;

public class PlayGame {
	public static void main(String[] args) {
		ResponsibilityChain chain = new ResponsibilityChain();
		chain.register(new ResponsibilityA());
		chain.register(new ResponsibilityB());

		chain.process(new Request() {
		});
	}
}
