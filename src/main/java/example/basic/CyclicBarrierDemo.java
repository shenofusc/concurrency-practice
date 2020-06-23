package example.basic;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 利用CyclicBarrier模拟将军领导士兵执行任务的过程
 */
public class CyclicBarrierDemo {
    public static class Soldier implements Runnable {
        private String soldier;
        private final CyclicBarrier barrier;

        public Soldier(String soldier, CyclicBarrier barrier) {
            this.soldier = soldier;
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                //先到的soldier在这里等待全员到齐，全员到齐（即每人调用一次await()）后自动进入下一步
                barrier.await();
                doWork();
                //工作完成后，先完成的soldier在这里等待全员完成
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        private void doWork() {
            try {
                Thread.sleep(new Random().nextInt(10) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(soldier + ":mission done");
        }
    }

    public static class BarrierRun implements Runnable {
        boolean flag;
        int N;

        public BarrierRun(boolean flag, int n) {
            this.flag = flag;
            N = n;
        }

        @Override
        public void run() {
            if (flag) {
                System.out.println("general:[soldier " + N + ", mission has done]");
            } else {
                System.out.println("general:[soldier " + N + ",gather finished]");
                flag = true;
            }
        }
    }

    public static void main(String[] args) {
        final int N = 10;
        Thread[] soldiers = new Thread[10];
        boolean flag = false;
        CyclicBarrier barrier = new CyclicBarrier(N, new BarrierRun(flag, N));
        System.out.println("gather soldiers!");
        for (int i = 0; i < N; i++) {
            System.out.println("soldier " + i + " is here!");
            soldiers[i] = new Thread(new Soldier("soldier " + i, barrier));
            soldiers[i].start();
//            if (i == 5) {
//                soldiers[0].interrupt();
//            }
        }
    }
}
