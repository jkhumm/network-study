package com.dongnaoedu.network.design.decorator;

/**
 * 被装饰者（相当于内衣）
 */
public class ConcreteComponent implements Component {
	public String methodA() {
		return "concrete-object";
	}

	public int methodB() {
		return 100;
	}
}
