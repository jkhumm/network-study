package com.dongnaoedu.network.design.observer;
/**
 * 主题
 */
public interface Observable {

	void addObserver(ObServer o);

	void removeObserver(ObServer o);

	void notifyObservers(Object msg);
}
