package example.pattern.producer_consumer.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * Created by shy on 7/11/20.
 */
public class PCDataFactory implements EventFactory<PCData> {
    @Override
    public PCData newInstance() {
        return new PCData();
    }
}
