package com.example.refresh.support;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WrappedLock {
    private static final ReentrantLock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static void wrappedLock(){
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wrappedUnLock(){
        if(lock.hasQueuedThreads() && lock.hasWaiters(condition)){
            condition.signalAll();
        }
    }

}
