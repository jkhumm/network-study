package com.dongnaoedu.network.design.decorator;

public class DecoratorSample {
	public static void main(String[] args) {
		// 装饰者成为被装饰者，通过这种方式可以装饰无数个被装饰者
		Component a = new DecoratorA(new ConcreteComponent());

		System.out.println(a.methodA());
		System.out.println(a.methodB());

		Component b = new DecoratorB(a);
		System.out.println(b.methodA());
		System.out.println(b.methodB());


	}
}
