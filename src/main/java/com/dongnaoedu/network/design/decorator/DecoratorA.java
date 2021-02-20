package com.dongnaoedu.network.design.decorator;

/**
 * 功能增强
 */
public class DecoratorA extends Decorator {

	public DecoratorA(Component component) {
		super(component);
	}

	public String methodA() {
		return this.component.methodA() + " + A";
	}

	public int methodB() {
		return this.component.methodB() + 10;
	}
}
