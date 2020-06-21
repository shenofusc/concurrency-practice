package examples;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition.await()和Condition.signal()示例，await()后线程进入等待队列，signal()之后线程进入同步队列
 */
public class ConditionTest {
    public static ReentrantLock lock = new ReentrantLock();
    public static Condition conditionA = lock.newCondition();
    public static Condition conditionB = lock.newCondition();
    public static Condition conditionC = lock.newCondition();
    public static int index = 0;

    public static class ThreadA extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    System.out.println("threa A:" + ++index);
                    conditionB.signal();
                    conditionA.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class ThreadB extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    System.out.println("threa B:" + ++index);
                    conditionC.signal();
                    conditionB.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static class ThreadC extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    lock.lock();
                    System.out.println("threa C:" + ++index);
                    conditionA.signal();
                    conditionC.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadA threadA = new ThreadA();
        ThreadB threadB = new ThreadB();
        ThreadC threadC = new ThreadC();

        threadC.start();
        Thread.sleep(1000);
        threadA.start();
        Thread.sleep(1000);
        threadB.start();
    }
}
