package com.dongnaoedu.network.design.observer;

import java.util.ArrayList;
import java.util.List;

public class PyqSubject implements Observable{

    private List<ObServer> fans = new ArrayList<>();

    public void pushMsh(Object msg){
        System.out.println("主题：发个朋友圈："+msg.toString());
        notifyObservers(msg);
    }

    @Override
    public void addObserver(ObServer o) {
        fans.add(o);
    }

    @Override
    public void removeObserver(ObServer o) {
        fans.remove(o);
    }

    @Override
    public void notifyObservers(Object msg) {
        for (ObServer fan : fans) {
            fan.update(msg);
        }
    }
}
