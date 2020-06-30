package example.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by shy on 6/30/20.
 */
public class AtomicIntegerDemo {
    static AtomicInteger i = new AtomicInteger();

    public static class AddThread implements Runnable {
        @Override
        public void run() {
            for (int k = 0; k < 10000; k++) {
                i.incrementAndGet();
            }
        }
    }

    private static Object obj = new Object();

    public static class WrapInteger {
        int number;

        public WrapInteger(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }
    }

    public static class LockAddThread implements Runnable {
        private WrapInteger wi;

        public LockAddThread(WrapInteger wi) {
            this.wi = wi;
        }

        @Override
        public void run() {
            synchronized (obj) {
                int number = wi.getNumber();
                for (int k = 0; k < 10000; k++) {
                    number += 1;
                }
                wi.setNumber(number);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread[] threads = new Thread[10];
        for (int k = 0; k < 10; k++) {
            threads[k] = new Thread(new AddThread());
        }
        for (int k = 0; k < 10; k++) {
            threads[k].start();
        }
        for (int k = 0; k < 10; k++) {
            threads[k].join();
        }
        System.out.println(i);
    }
}
