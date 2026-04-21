package com.storebackoffice.structuredconcurrency;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

public final class StructuredConcurrencyDemo {

    private StructuredConcurrencyDemo() {
    }

    public static void main(String[] args) throws Exception {
        System.out.println("JEP 428 (incubator in JDK 19): StructuredTaskScope (preview on JDK 21; finalized in a later JDK).\n");
        shutdownOnFailureHappyPath();
        shutdownOnFailurePropagatesAndCancelsPeers();
        shutdownOnSuccessFirstResult();
    }

    private static void shutdownOnFailureHappyPath() throws Exception {
        System.out.println("--- ShutdownOnFailure: parallel fetches, both succeed ---");
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            StructuredTaskScope.Subtask<String> left = scope.fork(() -> delayReturn("left", 80));
            StructuredTaskScope.Subtask<String> right = scope.fork(() -> delayReturn("right", 40));
            scope.join();
            scope.throwIfFailed();
            System.out.println("combined: " + left.get() + " + " + right.get());
        }
    }

    private static void shutdownOnFailurePropagatesAndCancelsPeers() throws Exception {
        System.out.println("--- ShutdownOnFailure: one subtask fails; scope shuts down and interrupts peers ---");
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            StructuredTaskScope.Subtask<String> slow = scope.fork(() -> delayReturn("never", 5_000));
            StructuredTaskScope.Subtask<String> bad = scope.fork(() -> {
                Thread.sleep(80);
                throw new IllegalStateException("downstream unavailable");
            });
            scope.join();
            try {
                scope.throwIfFailed();
            } catch (ExecutionException e) {
                System.out.println("throwIfFailed: " + e.getCause().getClass().getSimpleName()
                        + ": " + e.getCause().getMessage());
            }
            System.out.println("slow subtask state after failure: " + slow.state());
            System.out.println("bad  subtask state after failure: " + bad.state());
        }
    }

    private static void shutdownOnSuccessFirstResult() throws Exception {
        System.out.println("--- ShutdownOnSuccess: first successful result wins ---");
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            scope.fork(() -> delayReturn("slow-wins-only-if-first", 200));
            scope.fork(() -> delayReturn("fast", 20));
            scope.join();
            System.out.println("result: " + scope.result());
        }
    }

    private static String delayReturn(String value, int millis) throws InterruptedException {
        Thread.sleep(millis);
        return value;
    }
}
