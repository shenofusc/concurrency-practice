package example.tool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join框架使用示例
 */
public class CountTask extends RecursiveTask<Long> {
    private static final int THRESHOLD = 100;
    private int begin;
    private int end;

    public CountTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    protected Long compute() {
        long sum = 0;
        if (end - begin < THRESHOLD) {
            for (int i = begin; i <= end; i++) {
                sum += i;
            }
        } else {
            int mid = begin + (end - begin) / 2;
            CountTask left = new CountTask(begin, mid);
            CountTask right = new CountTask(mid + 1, end);
            left.fork();
            right.fork();
            sum += left.join();
            sum += right.join();
        }
        return sum;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool pool = new ForkJoinPool();
        CountTask task = new CountTask(0, 2000);
        ForkJoinTask<Long> result = pool.submit(task);
        System.out.println(result.get());
    }
}
