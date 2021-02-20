package com.dongnaoedu.network.concurrent.period6_1;

public class KodyCyclicBarrier {

    private int curCount = 0 ;
    private Object generation = new Object();

    private int parties;
    public KodyCyclicBarrier(int parties){
        this.parties = parties;
    }


    public void await(){
        synchronized (this){
            Object g = generation;

            curCount ++;
            if (curCount == parties){
                //进入下一次计数
                nextGeneration();
            }else{
                for (;;){
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (g != generation){
                        return;
                    }

                }
            }
        }

    }

    public void nextGeneration(){
        synchronized (this){
            this.notifyAll();
            this.curCount = 0;
            this.generation = new Object();
        }
    }

}
