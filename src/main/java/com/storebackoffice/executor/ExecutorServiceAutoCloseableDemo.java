package com.storebackoffice.executor;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public final class ExecutorServiceAutoCloseableDemo {

    private ExecutorServiceAutoCloseableDemo() {
    }

    public static void main(String[] args) {
        System.out.println("JDK 19 library changes demonstrated here:");
        System.out.println("- ExecutorService extends AutoCloseable (try-with-resources calls shutdown + await termination).");
        System.out.println("- Thread.sleep(Duration) avoids TimeUnit math for simple delays.\n");

        AtomicInteger done = new AtomicInteger();
        try (ExecutorService pool = Executors.newFixedThreadPool(2)) {
            pool.submit(() -> work(done, Duration.ofMillis(120)));
            pool.submit(() -> work(done, Duration.ofMillis(60)));
            pool.submit(() -> work(done, Duration.ofMillis(30)));
        }
        System.out.println("Pool closed; completed tasks: " + done.get());
    }

    private static void work(AtomicInteger counter, Duration delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        counter.incrementAndGet();
    }
}
