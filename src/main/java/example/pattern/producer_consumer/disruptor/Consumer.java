package example.pattern.producer_consumer.disruptor;

import com.lmax.disruptor.WorkHandler;

/**
 * Created by shy on 7/11/20.
 */
public class Consumer implements WorkHandler<PCData> {
    @Override
    public void onEvent(PCData event) throws Exception {
        System.out.println(Thread.currentThread().getId()
                + ":Event: --" + event.getValue() * event.getValue() + "--");
    }
}
