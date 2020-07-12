package example.pattern.pipeline;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 并发流水线
 * Created by shy on 7/11/20.
 */
public class Div implements Runnable {
    public static BlockingQueue<Msg> queue = new LinkedBlockingQueue<>();

    @Override
    public void run() {
        while (true) {
            try {
                Msg msg = queue.take();
                msg.i = msg.i / 2;
                System.out.println(msg.exp + "=" + msg.i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
