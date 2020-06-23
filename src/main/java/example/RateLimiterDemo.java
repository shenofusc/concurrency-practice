package example;

import com.google.common.util.concurrent.RateLimiter;

/**
 * Created by shy on 6/23/20.
 */
public class RateLimiterDemo {
    static RateLimiter limiter = RateLimiter.create(2);

    public static class Task implements Runnable {

        public void run() {
            System.out.println(System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50; i++) {
            if (!limiter.tryAcquire()) {
                continue;
            }
            new Thread(new Task()).start();
        }
    }
}
