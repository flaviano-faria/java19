package com.storebackoffice;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Demonstrates {@link LinkedHashSet#newLinkedHashSet(int)} (Java 19+): sizes the backing
 * {@link java.util.LinkedHashMap} for an expected <em>element count</em> at the default load
 * factor. Iteration order matches insertion order (unlike {@link java.util.HashSet}).
 */
public final class LinkedHashSetNewLinkedHashSetDemo {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private LinkedHashSetNewLinkedHashSetDemo() {
    }

    public static void main(String[] args) {
        demonstrateSizedForKnownLoad();
        demonstrateVsRawConstructor();
        demonstrateInsertionOrder();
        demonstrateLoadFactor();
    }

    /**
     * Typical use: known number of distinct elements and you care about stable iteration order.
     */
    private static void demonstrateSizedForKnownLoad() {
        int expectedDistinctIds = 10_000;
        Set<String> ids = LinkedHashSet.newLinkedHashSet(expectedDistinctIds);

        for (int i = 0; i < expectedDistinctIds; i++) {
            ids.add("id-" + i);
        }

        System.out.println("ids size: " + ids.size());
        System.out.println("contains id-42: " + ids.contains("id-42"));
    }

    /**
     * {@code newLinkedHashSet(n)} is defined for <em>elements</em>, not raw table capacity.
     * {@code new LinkedHashSet<>(n)} treats {@code n} as initial capacity only.
     */
    private static void demonstrateVsRawConstructor() {
        int plannedElements = 100;

        Set<String> a = LinkedHashSet.newLinkedHashSet(plannedElements);
        Set<String> b = new LinkedHashSet<>(plannedElements);

        for (int i = 0; i < plannedElements; i++) {
            a.add("e" + i);
            b.add("e" + i);
        }

        System.out.println("newLinkedHashSet(" + plannedElements + ") size: " + a.size());
        System.out.println("new LinkedHashSet<>(" + plannedElements + ") size: " + b.size());
        System.out.println(
                "Same logical size; newLinkedHashSet sizes the backing table for " + plannedElements
                        + " elements at default load factor (fewer internal resizes than a naive capacity guess).");
    }

    /**
     * {@link LinkedHashSet} iterator order follows insertion order; {@code newLinkedHashSet} does not change that.
     */
    private static void demonstrateInsertionOrder() {
        System.out.println("--- insertion order ---");
        LinkedHashSet<String> ordered = LinkedHashSet.newLinkedHashSet(8);
        ordered.add("third");
        ordered.add("first");
        ordered.add("second");

        StringJoiner joiner = new StringJoiner(", ");
        for (String element : ordered) {
            joiner.add(element);
        }
        System.out.println("iteration order: " + joiner + " (matches insertion order, not sort order)");
    }

    /**
     * {@link LinkedHashSet#newLinkedHashSet(int)} uses the default load factor ({@value #DEFAULT_LOAD_FACTOR});
     * use {@link LinkedHashSet#LinkedHashSet(int, float)} when you need a different trade-off.
     */
    private static void demonstrateLoadFactor() {
        System.out.println("--- load factor (LinkedHashSet) ---");
        System.out.println(
                "Resize trigger (conceptually): size grows past bucketCount * loadFactor (capacity is rounded up by the JDK).");
        System.out.println("Default load factor: " + DEFAULT_LOAD_FACTOR);

        int elements = 48;
        int sameInitialCapacity = 64;

        LinkedHashSet<Integer> dense = new LinkedHashSet<>(sameInitialCapacity, 0.9f);
        LinkedHashSet<Integer> sparse = new LinkedHashSet<>(sameInitialCapacity, 0.5f);
        LinkedHashSet<Integer> factoryDefault = LinkedHashSet.newLinkedHashSet(elements);

        fillWithInts(dense, elements);
        fillWithInts(sparse, elements);
        fillWithInts(factoryDefault, elements);

        System.out.println(
                "Same " + elements + " elements, same initialCapacity=" + sameInitialCapacity + ": "
                        + "0.9f (denser) vs 0.5f (more headroom before resize).");
        System.out.println("dense.size=" + dense.size() + ", sparse.size=" + sparse.size());
        System.out.println(
                "LinkedHashSet.newLinkedHashSet(" + elements + ") uses default load factor " + DEFAULT_LOAD_FACTOR
                        + "; factoryDefault.size=" + factoryDefault.size());
    }

    private static void fillWithInts(LinkedHashSet<Integer> set, int count) {
        for (int i = 0; i < count; i++) {
            set.add(i);
        }
    }
}
