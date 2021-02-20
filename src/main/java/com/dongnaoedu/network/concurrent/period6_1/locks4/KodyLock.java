package com.dongnaoedu.network.concurrent.period6_1.locks4;


public class KodyLock {
    private boolean isFair;

    public KodyLock(boolean isFair){
        this.isFair = isFair;
    }

    CommonMask mask = new CommonMask(){
        public boolean tryLock(int acquires) {
            if (isFair) {
                return tryFairLock(acquires);
            }else {
                return tryNonFairLock(acquires);
            }
        }

        public boolean tryNonFairLock(int acquires){
            //如果read count ！=0 返回false
            if (readCount.get() !=0)
                return false;

            int wct = mask.writeCount.get();     //拿到 独占锁 当前状态

            if (wct==0){
                if (mask.writeCount.compareAndSet(wct, wct + acquires)){     //通过修改state来抢锁
                    mask.owner =Thread.currentThread();  //  抢到锁后，直接修改owner为当前线程
                    return true;
                }
            }else if (mask.owner == Thread.currentThread()){
                mask.writeCount.set(wct + acquires);     //修改count值
                return true;
            }

            return false;
        }


        public boolean tryFairLock(int acquires){
            //如果read count ！=0 返回false
            if (readCount.get() !=0)
                return false;

            int wct = mask.writeCount.get();     //拿到 独占锁 当前状态

            if (wct==0){
                WaitNode node = mask.waiters.peek();
                if (node!=null &&
                        node.thread == Thread.currentThread()
                        &&mask.writeCount.compareAndSet(wct, wct + acquires)){     //通过修改state来抢锁
                    mask.owner =Thread.currentThread();  //  抢到锁后，直接修改owner为当前线程
                    return true;
                }
            }else if (mask.owner == Thread.currentThread()){
                mask.writeCount.set(wct + acquires);     //修改count值
                return true;
            }

            return false;
        }


        public boolean tryUnlock(int releases) {
            //若当前线程没有 持有独占锁
            if(mask.owner!= Thread.currentThread()){
                throw new IllegalMonitorStateException();       //抛IllegalMonitorStateException
            }

            int wc= mask.writeCount.get();
            int nextc = wc - releases;      //计算 独占锁剩余占用
            mask.writeCount.set(nextc);      //不管是否完全释放，都更新count值

            if (nextc==0){  //是否完全释放
                mask.owner = null;   //若完全释放，设置owner为null
                return true;
            }else{
                return false;
            }

        }
    };

    public void lock(){
        mask.lock();
    }

    public boolean tryLock(){
        return mask.tryLock(1);
    }

    public void unLock(){
        mask.unlock();
    }


}
