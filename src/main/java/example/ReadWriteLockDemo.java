package example;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 模拟读多写少的场景，使用读写锁时2秒左右程序即可结束。使用普通锁时由于读读之间也会冲突，程序需要20秒左右才能结束。
 */
public class ReadWriteLockDemo {
    private static Lock lock = new ReentrantLock();
    private static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    private static Lock readLock = rwLock.readLock();
    private static Lock writeLock = rwLock.writeLock();
    private int value;

    public Object handleRead(Lock lock) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(1000);
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void handleWrite(Lock lock, int index) throws InterruptedException {
        try {
            lock.lock();
            Thread.sleep(1000);
            value = index;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        final ReadWriteLockDemo demo = new ReadWriteLockDemo();
        Runnable readRunnable = new Runnable() {
            @Override
            public void run() {
                try {
//                    demo.handleRead(readLock);
                    demo.handleRead(lock);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable writeRunnable = new Runnable() {
            @Override
            public void run() {
                try {
//                    demo.handleWrite(writeLock, new Random().nextInt());
                    demo.handleWrite(lock, new Random().nextInt());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        for (int i = 0; i < 18; i++) {
            new Thread(readRunnable).start();
        }
        for (int i = 0; i < 2; i++) {
            new Thread(writeRunnable).start();
        }
    }
}
