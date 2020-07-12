package example.pattern.future;

import com.google.common.util.concurrent.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.concurrent.Executors;

/**
 * JDK自带的Future框架只允许阻塞获取计算结果，Guava做了进一步的增强，允许使用回调函数，完全不阻塞调用线程。
 * Created by shy on 7/11/20.
 */
public class GuavaFutureDemo {
    public static void main(String[] args) {
        ListeningExecutorService exec = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(10));
        final ListenableFuture<String> future = exec.submit(new ComputeTask("X"));
        //这种方式无法处理异常情况
        /*future.addListener(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(future.get());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, MoreExecutors.directExecutor());*/
        //这种方式更加通用，可以处理异常情况
        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String result) {
                System.out.println(result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.out.println("async process failed");
            }
        }, exec);
        System.out.println("main task finished");
    }
}
