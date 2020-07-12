package example.pattern.pipeline;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by shy on 7/11/20.
 */
public class Plus implements Runnable {
    public static BlockingQueue<Msg> queue = new LinkedBlockingQueue<>();
    @Override
    public void run() {
        while (true) {
            try {
                Msg msg = queue.take();
                msg.j = msg.i + msg.j;
                Multiply.queue.add(msg);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
