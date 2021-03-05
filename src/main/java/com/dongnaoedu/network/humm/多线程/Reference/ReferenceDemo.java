package com.dongnaoedu.network.humm.多线程.Reference;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author heian
 * @create 2020-02-24-9:43 上午
 * @description
 */
public class ReferenceDemo {

   static AtomicReference<MyEntity> reference = new AtomicReference<>();

    public static void main(String[] args) {
        MyEntity oldEntity = new MyEntity();
        oldEntity.setTime(123);
        oldEntity.setSeqence(321);
        reference.set(oldEntity);
        long seqence = reference.get().getSeqence();
        seqence++;
        long time = System.currentTimeMillis();
        MyEntity newEntity = new MyEntity();
        newEntity.setSeqence(seqence);
        newEntity.setTime(time);
        while (reference.compareAndSet(oldEntity,newEntity)){
            System.out.println(reference.get().toString());
            System.out.println("CAS true");
        }
    }

}

class MyEntity{
    private long time;
    private long seqence;


    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getSeqence() {
        return seqence;
    }

    public void setSeqence(long seqence) {
        this.seqence = seqence;
    }

    @Override
    public String toString() {
        return "MyEntity{" +
                "time=" + time +
                ", seqence=" + seqence +
                '}';
    }
}