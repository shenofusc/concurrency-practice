package example;

/**
 * Created by shy on 6/20/20.
 */
public class InterruptWait {
    private static Object obj = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                synchronized (obj) {
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        System.out.println("current lock state:" + Thread.currentThread().isInterrupted());
                    }
                }
            }
        };
        t1.start();
        Thread.sleep(100);
        t1.interrupt();
        t1.join();
    }
}
