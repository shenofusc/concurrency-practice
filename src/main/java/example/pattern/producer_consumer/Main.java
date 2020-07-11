package example.pattern.producer_consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shy on 7/11/20.
 */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<PCData> queue = new LinkedBlockingQueue<>(10);
        Producer p1 = new Producer(queue);
        Producer p2 = new Producer(queue);
        Producer p3 = new Producer(queue);
        Consumer c1 = new Consumer(queue);
        Consumer c2 = new Consumer(queue);
        Consumer c3 = new Consumer(queue);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(p1);
        exec.execute(p2);
        exec.execute(p3);
        exec.execute(c1);
        exec.execute(c2);
        exec.execute(c3);
        Thread.sleep(10000);
        p1.stop();
        p2.stop();
        p3.stop();
        Thread.sleep(3000);
        exec.shutdown();
    }
}
