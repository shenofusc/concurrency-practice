package example.atomic;

import example.other.JmhSample;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * Created by shy on 20/6/30.
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class LockFreeVectorTest {
    private LockFreeVector<String> lockFreeVector = new LockFreeVector<>();
    private Vector<String> vector = new Vector<>();

    @Setup
    public void setup() {
        for (int i = 0; i < 1000000; i++) {
            lockFreeVector.put("Index-" + i);
            vector.add("Index-" + i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureLockFreeVectorPut() throws InterruptedException {
        LockFreeVector<String> localLockFreeVector = new LockFreeVector<>();
        for (int i = 0; i < 1000000; i++) {
            localLockFreeVector.put("Index-" + i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureVectorPut() throws InterruptedException {
        Vector<String> localVector = new Vector<>();
        for (int i = 0; i < 1000000; i++) {
            localVector.add("Index-" + i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureLockFreeVectorGet() throws InterruptedException {
        for (int i = 0; i < 1000000; i++) {
            String str = lockFreeVector.get(i);
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @OutputTimeUnit(TimeUnit.MICROSECONDS)
    public void measureVectorGet() throws InterruptedException {
        for (int i = 0; i < 1000000; i++) {
            String str = vector.get(i);
        }
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(LockFreeVectorTest.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();
    }
}
