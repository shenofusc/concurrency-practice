package example.basic;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by shy on 6/29/20.
 */
public class RndTask implements Callable<Long> {
    public static final int GEN_COUNT = 10000000;
    public static final int THREAD_COUNT = 4;
    static ExecutorService exec = Executors.newFixedThreadPool(THREAD_COUNT);
    public static Random rnd = new Random(123);

    public static ThreadLocal<Random> tRnd = new ThreadLocal<Random>() {
        @Override
        protected Random initialValue() {
            return new Random(123);
        }
    };

    private int mode = 0;

    public RndTask(int mode) {
        this.mode = mode;
    }

    public Random getRandom() {
        if (mode == 0) {
            return rnd;
        } else if (mode == 1) {
            return tRnd.get();
        } else {
            return null;
        }
    }

    @Override
    public Long call() throws Exception {
        long b = System.currentTimeMillis();
        for (int i = 0; i < GEN_COUNT; i++) {
            getRandom().nextInt();
        }
        long e = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + " spend " + (e - b) + "ms");
        return e - b;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Future<Long>[] futures = new Future[THREAD_COUNT];
        for (int i = 0; i < THREAD_COUNT; i++) {
            futures[i] = exec.submit(new RndTask(0));
        }
        long totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futures[i].get();
        }
        System.out.println("多线程访问同一个Random实例：" + totalTime + "ms");

        for (int i = 0; i < THREAD_COUNT; i++) {
            futures[i] = exec.submit(new RndTask(1));
        }
        totalTime = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
            totalTime += futures[i].get();
        }
        System.out.println("使用ThreadLocal包装Random实例：" + totalTime + "ms");
        exec.shutdown();
    }
}
