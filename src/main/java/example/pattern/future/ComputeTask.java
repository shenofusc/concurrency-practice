package example.pattern.future;

import java.util.concurrent.Callable;

/**
 * Created by shy on 7/11/20.
 */
public class ComputeTask implements Callable<String> {
    private String param;

    public ComputeTask(String param) {
        this.param = param;
    }

    @Override
    public String call() throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(param);
            Thread.sleep(100);
        }
        return sb.toString();
    }
}
