package example.basic;

/**
 * server模式下不会打印数值，但是client模式下应该会打印出来
 */
public class NoVisibility {
    private static volatile boolean ready;
    private static int number;

    private static class ReaderThread extends Thread {
        @Override
        public void run() {
            while (!ready);
            System.out.println(number);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new ReaderThread().start();
        Thread.sleep(1000);
        number = 42;
        ready = true;
        Thread.sleep(10000);
    }
}
