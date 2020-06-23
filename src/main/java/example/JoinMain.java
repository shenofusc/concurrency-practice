package example;

/**
 * Thread.join()方法使用示例，本质上是让调用join()方法的main线程wait()在at线程对象实例上
 */
public class JoinMain {
    public static volatile int i = 0;
    public static class AddThread extends Thread {
        @Override
        public void run() {
            for (i = 0; i < 10000000; i++);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        AddThread at = new AddThread();
        at.start();
        at.join();
        System.out.println(i);
    }
}
