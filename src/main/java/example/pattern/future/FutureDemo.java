package example.pattern.future;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by shy on 7/11/20.
 */
public class FutureDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<String> future = exec.submit(new ComputeTask("X"));
        System.out.println("request finished");
        Thread.sleep(1000);
        System.out.println("data:" + future.get());
    }
}
