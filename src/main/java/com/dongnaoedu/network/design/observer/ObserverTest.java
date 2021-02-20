package com.dongnaoedu.network.design.observer;

public class ObserverTest {

    public static void main(String[] args) {
        PyqSubject subject = new PyqSubject();
        subject.addObserver(new LaoWang());
        subject.addObserver(new LaoWang2());
        subject.pushMsh("今天的自拍");
    }

}
