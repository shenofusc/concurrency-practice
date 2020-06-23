package example.executor;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 1.以固定周期运行任务时，如果任务执行需要的时间大于设定的周期时间，那么上次任务结束后会立刻开始执行下一次任务。
 * 2.延迟指定时间后运行任务时，不论任务执行需要多少时间，都会在上次任务结束后延迟指定时间才开始执行下一次任务。
 */
public class SchedulePoolDemo {
    public static class MyJob implements Runnable {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getId() + " is running");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new MyJob(), 0L, 2000L, TimeUnit.MILLISECONDS);
//        exec.scheduleWithFixedDelay(new MyJob(), 0L, 2000L, TimeUnit.MILLISECONDS);
    }
}
