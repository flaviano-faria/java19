package com.storebackoffice.virtualthreads;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

public final class VirtualThreadsDemo {

    private static final int TASK_COUNT = 2_000;

    private VirtualThreadsDemo() {
    }

    public static void main(String[] args) throws InterruptedException {
        System.out.println("JEP 425 (preview in JDK 19): virtual threads are final in JDK 21.");
        System.out.println(
                "Running " + TASK_COUNT + " short tasks on Executors.newVirtualThreadPerTaskExecutor() (~1 ms sleep each)...\n");

        CountDownLatch latch = new CountDownLatch(TASK_COUNT);
        Instant start = Instant.now();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < TASK_COUNT; i++) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(Duration.ofMillis(1));
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        latch.countDown();
                    }
                });
            }
            latch.await();
        }

        Duration elapsed = Duration.between(start, Instant.now());
        System.out.println("All " + TASK_COUNT + " tasks finished in " + elapsed.toMillis() + " ms");
        System.out.println("(Many blocking virtual threads share few OS carrier threads.)\n");

        Thread v = Thread.ofVirtual()
                .name("demo-vt-", 0)
                .unstarted(() -> System.out.println(
                        "Thread.ofVirtual(): name=" + Thread.currentThread().getName()
                                + ", isVirtual=" + Thread.currentThread().isVirtual()));
        v.start();
        v.join();
    }
}
