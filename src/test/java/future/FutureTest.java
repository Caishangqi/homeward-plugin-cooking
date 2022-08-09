package future;

import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

public class FutureTest {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        System.out.println(testFuture1());

    }

    public static String testFuture1() throws ExecutionException, InterruptedException {

        Future<String> future = returnAStupidFuture();
        String s = future.get();
        return s;


    }

    private static Future<String> returnAStupidFuture() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(new Callable<>() {

            @Override
            public String call() throws Exception {
                return "abc";
            }
        });

        return future;

    }

}
