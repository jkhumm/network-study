package com.dongnaoedu.network.design.decorator;

/**
 * 功能增强
 */
public class DecoratorB extends Decorator {

	public DecoratorB(Component component) {
		super(component);
	}

	public String methodA() {
		return this.component.methodA() + " + B";
	}

	public int methodB() {
		return this.component.methodB() + 9;
	}
}
