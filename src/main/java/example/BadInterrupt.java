package example;

/**
 * 线程内部不处理中断时，外部调用Thread.interrupt()方法是无效的
 */
public class BadInterrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread() {
            @Override
            public void run() {
                while (true) {
                    Thread.yield();
                }
            }
        };
        t1.start();
        Thread.sleep(2000);
        t1.interrupt();
    }
}
