package com.dongnaoedu.network.design.pipline.responsibility;

public class ResponsibilityB implements Responsibility {

	@Override
	public void process(Request request, ResponsibilityChain chain) {
		System.out.println("Responsibility-B done something...");
		chain.process(request);
	}

}
