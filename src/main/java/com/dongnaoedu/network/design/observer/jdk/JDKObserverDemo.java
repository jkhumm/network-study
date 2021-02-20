package com.dongnaoedu.network.design.observer.jdk;

import java.util.Observable;
import java.util.Observer;

/**
 * JDK Observable与Observer的介绍。<br/>
 * <ul>
 * Observable存在的问题：
 * <li>Observable是一个类，也没有实现接口。
 * <li>主题必须继承自它，如果主题想继承另外的类，这会是一个问题。限制它的复用潜力。
 * </ul>
 *
 */
public class JDKObserverDemo {

	public static void main(String[] args) {
		Observable subject = new Observable() {
			@Override
			public void notifyObservers(Object arg) {
				setChanged();
				super.notifyObservers(arg);
			}
		};

		Observer observer1 = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				System.out.println("观察者1收到通知被更新了..." + arg);
			}
		};

		Observer observer2 = new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				System.out.println("观察者2收到通知被更新了..." + arg);
			}
		};

		subject.addObserver(observer1);
		subject.addObserver(observer2);

		//第一次发布通知
		subject.notifyObservers("你想传递的内容一 女神发朋友圈");
		//第二次发布通知
		subject.notifyObservers("你想传递的内容二 女神发朋友圈");

	}
}
