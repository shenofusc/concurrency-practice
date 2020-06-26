package example.executor;

import java.util.concurrent.*;

/**
 * submit()方法内部重新封装了任务，不会抛出异常，调用get()方法时才会抛出。
 */
public class DivTask implements Runnable {
    int a,b;

    public DivTask(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        double re = a/b;
        System.out.println(re);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ThreadPoolExecutor pools = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                0L, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>());
        for (int i = 0; i < 5; i++) {
            pools.submit(new DivTask(100, i));
//            pools.execute(new DivTask(100, i));
        }
    }
}
