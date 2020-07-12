package example.pattern.sort.oddevensort;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shy on 7/12/20.
 */
public class ParallelOddEvenSort {
    private static int[] arr = {2,0,2,1,1,0};
    private static ExecutorService pool = Executors.newSingleThreadExecutor();
    private static boolean changed = true;

    private static synchronized void setChanged(boolean changed) {
        ParallelOddEvenSort.changed = changed;
    }

    private static synchronized boolean getChanged() {
        return changed;
    }

    public static class OddEvenSortTask implements Runnable {
        int i;
        CountDownLatch latch;

        public OddEvenSortTask(int i, CountDownLatch latch) {
            this.i = i;
            this.latch = latch;
        }

        @Override
        public void run() {
            if (arr[i] > arr[i + 1]) {
                int temp = arr[i];
                arr[i] = arr[i+1];
                arr[i+1]=temp;
                setChanged(true);
            }
            latch.countDown();
        }
    }

    public static void pOddEvenSort(int[] arr) throws InterruptedException {
        int start = 0;
        while (getChanged() || start == 1) {
            setChanged(false);
            CountDownLatch latch = new CountDownLatch(arr.length / 2 - (arr.length % 2 == 0 ? start : 0));
            for (int i = start; i < arr.length - 1; i++) {
                pool.submit(new ParallelOddEvenSort.OddEvenSortTask(i, latch));
            }
            latch.await();
            if (start == 0) {
                start = 1;
            } else {
                start = 0;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long start;
        for (int n : arr) {
            System.out.print(n + " ");
        }
        System.out.println();
        start = System.currentTimeMillis();
        pOddEvenSort(arr);
        System.out.println("sort cost " + (System.currentTimeMillis() - start) + "ms");
        for (int n : arr) {
            System.out.print(n + " ");
        }
    }
}
