package com.storebackoffice.futurestate;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Future.State;

public final class FutureStateDemo {

    private FutureStateDemo() {
    }

    public static void main(String[] args) throws Exception {
        System.out.println("JDK 19: Future.state() returns " + State.class.getSimpleName()
                + " without calling get() (useful for polling / logging).\n");

        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor()) {
            demonstrateSuccess(pool);
            demonstrateFailed(pool);
            demonstrateCancelled(pool);
        }
    }

    private static void demonstrateSuccess(ExecutorService pool) throws Exception {
        Future<Integer> future = pool.submit(
                () -> {
                    Thread.sleep(150);
                    return 21;
                });
        Thread.sleep(10);
        System.out.println("while running:       state=" + future.state());
        while (!future.isDone()) {
            Thread.sleep(5);
        }
        System.out.println("completed normally: state=" + future.state() + " result=" + future.get());
    }

    private static void demonstrateFailed(ExecutorService pool) throws InterruptedException {
        Future<?> future = pool.submit(
                () -> {
                    throw new IllegalStateException("task failed");
                });
        try {
            future.get();
        } catch (ExecutionException ignored) {
            // expected
        }
        System.out.println("after failure:     state=" + future.state());
    }

    private static void demonstrateCancelled(ExecutorService pool) throws InterruptedException {
        Future<?> future = pool.submit(
                () -> {
                    Thread.sleep(60_000);
                    return null;
                });
        boolean cancelled = future.cancel(true);
        System.out.println("cancel(" + cancelled + "):      state=" + future.state());
    }
}
