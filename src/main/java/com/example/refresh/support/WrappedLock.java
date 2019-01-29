package com.example.refresh.support;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class WrappedLock {
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();

    public static void wrappedLock(){
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void wrappedUnLock(){
          condition.signalAll();
    }

}
