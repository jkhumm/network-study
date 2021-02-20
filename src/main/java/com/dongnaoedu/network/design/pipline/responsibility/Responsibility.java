package com.dongnaoedu.network.design.pipline.responsibility;

public interface Responsibility {

	void process(Request request, ResponsibilityChain chain);
}
