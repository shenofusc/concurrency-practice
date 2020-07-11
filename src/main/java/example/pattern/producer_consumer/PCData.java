package example.pattern.producer_consumer;

/**
 * Created by shy on 7/11/20.
 */
public class PCData {
    private final int inData;

    public PCData(int inData) {
        this.inData = inData;
    }

    public int getInData() {
        return inData;
    }

    @Override
    public String toString() {
        return "PCData{" +
                "inData=" + inData +
                '}';
    }
}
