package example.basic;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * 相当于一个没有数量限制的CountDownLatch
 * Created by shy on 7/11/20.
 */
public class BooleanLatch {
    private static class Sync extends AbstractQueuedSynchronizer {
        boolean isSignalled() {
            return getState() != 0;
        }

        protected int tryAcquireShared(int ignore) {
            return isSignalled() ? 1 : -1;
        }

        protected boolean tryReleaseShared(int ignore) {
            setState(1);
            return true;
        }
    }

    private final Sync sync = new Sync();
    public boolean isSignalled() {
        return sync.isSignalled();
    }
    public void signal() {
        sync.releaseShared(1);
    }
    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    public static void main(String[] args) throws InterruptedException {
        final BooleanLatch booleanLatch = new BooleanLatch();
        for (int i = 0; i < 10; i++) {
            new Thread("thread-" + i) {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName() + " is waiting");
                    try {
                        booleanLatch.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + " is signalled");
                }
            }.start();
        }
        Thread.sleep(5000);
        booleanLatch.signal();
        System.out.println("main signalled");
    }
}
