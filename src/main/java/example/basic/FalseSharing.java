package example.basic;

/**
 * Created by shy on 7/11/20.
 */
public class FalseSharing implements Runnable {
    public static final int NUM_THREADS = 4;
    public static final long ITERATIONS = 500L * 1000L * 1000L;
    private final int arrayIndex;

    private static VolatileLong[] longs = new VolatileLong[NUM_THREADS];
    static {
        for (int i = 0; i < longs.length; i++) {
            longs[i] = new VolatileLong();
        }
    }

    public FalseSharing(int arrayIndex) {
        this.arrayIndex = arrayIndex;
    }

    public static void main(String[] args) throws InterruptedException {
        final long start = System.currentTimeMillis();
        runTest();
        System.out.println("duration = " + (System.currentTimeMillis() - start));
    }

    private static void runTest() throws InterruptedException {
        Thread[] ts = new Thread[NUM_THREADS];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(new FalseSharing(i));
        }
        for (Thread t : ts) {
            t.start();
        }
        for (Thread t : ts) {
            t.join();
        }
    }

    @Override
    public void run() {
        long l = ITERATIONS + 1;
        while (0 != --l) {
            longs[arrayIndex].value = l;
        }
    }

    public static final class VolatileLong {
        public volatile long value = 0L;
        public long p1,p2,p3,p4,p5,p6,p7;//缓存行填充
    }
}
