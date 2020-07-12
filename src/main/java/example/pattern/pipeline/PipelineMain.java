package example.pattern.pipeline;

/**
 * Created by shy on 7/11/20.
 */
public class PipelineMain {
    public static void main(String[] args) {
        new Thread(new Plus()).start();
        new Thread(new Multiply()).start();
        new Thread(new Div()).start();

        for (int i = 1; i <= 1000; i++) {
            for (int j = 1; j <= 1000; j++) {
                Msg msg = new Msg();
                msg.i = i;
                msg.j = j;
                msg.exp = "((" + i + "+" + j + ")*" + i + ")/2";
                Plus.queue.add(msg);
            }
        }
    }
}
