package com.dongnaoedu.network.design.pipline.responsibility;

public class ResponsibilityA implements Responsibility {

	@Override
	public void process(Request request, ResponsibilityChain chain) {
		System.out.println("Responsibility-A done something...");
		chain.process(request);
	}

}
