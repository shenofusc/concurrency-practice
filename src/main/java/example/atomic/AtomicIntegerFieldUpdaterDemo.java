package example.atomic;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by shy on 20/6/30.
 */
public class AtomicIntegerFieldUpdaterDemo {
    public static class Candidate {
        int id;
        volatile int score;
    }

    public static final AtomicIntegerFieldUpdater<Candidate> scoreUpdater = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");
    public static AtomicInteger allScore = new AtomicInteger();

    public static void main(String[] args) throws InterruptedException {
        final Candidate candidate = new Candidate();
        Thread[] ts = new Thread[10000];
        for (int i = 0; i < 10000; i++) {
            ts[i] = new Thread() {
                @Override
                public void run() {
                    if (Math.random() > 0.4) {
                        scoreUpdater.incrementAndGet(candidate);
                        allScore.incrementAndGet();
                    }
                }
            };
            ts[i].start();
        }
        for (int i = 0; i < 10000; i++) {
            ts[i].join();
        }
        System.out.println("score:" + candidate.score);
        System.out.println("allScore:" + allScore);
    }
}
