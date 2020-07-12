package example.pattern.sort.shellsort;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by shy on 7/12/20.
 */
public class ShellSortTask implements Runnable {
    private static int[] arr = {2,0,2,1,1,0};
    private int i = 0;
    private int h = 0;
    private CountDownLatch latch;
    private static ExecutorService pool = Executors.newSingleThreadExecutor();

    public ShellSortTask(int i, int h, CountDownLatch latch) {
        this.i = i;
        this.h = h;
        this.latch = latch;
    }

    public static void pShellSort(int[] arr) throws InterruptedException {
        int h = 1;
        CountDownLatch latch = null;
        while (h <= arr.length / 3) {
            h = h*3+1;
        }
        while (h > 0) {
            System.out.println("h=" + h);
            if (h >= 4) {
                latch = new CountDownLatch(arr.length -h);//???
            }
            for (int i = h; i < arr.length; i++) {
                if (h >= 4) {
                    pool.submit(new ShellSortTask(i, h, latch));
                } else {
                    if (arr[i] < arr[i - h]) {
                        int tmp = arr[i];
                        int j=i-h;
                        while (j >= 0 && arr[j] > tmp) {
                            arr[j+h]=arr[j];
                            j-=h;
                        }
                        arr[j+h]=tmp;
                    }
                }
            }
            latch.await();
            h=(h-1)/3;
        }
    }

    @Override
    public void run() {
        if (arr[i] < arr[i - h]) {
            int tmp = arr[i];
            int j=i-h;
            while (j >= 0 && arr[j] > tmp) {
                arr[j+h]=arr[j];
                j-=h;
            }
            arr[j+h]=tmp;
        }
        latch.countDown();
    }

    public static void main(String[] args) throws InterruptedException {
        long start;
        for (int n : arr) {
            System.out.print(n + " ");
        }
        System.out.println();
        start = System.currentTimeMillis();
        pShellSort(arr);
        System.out.println("sort cost " + (System.currentTimeMillis() - start) + "ms");
        for (int n : arr) {
            System.out.print(n + " ");
        }
    }
}
