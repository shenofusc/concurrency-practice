package example.pattern.producer_consumer.disruptor;

/**
 * Created by shy on 7/11/20.
 */
public class PCData {
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
