package com.storebackoffice;

import java.util.HashSet;
import java.util.Set;

/**
 * Demonstrates {@link HashSet#newHashSet(int)} (Java 19+): a factory that sizes the
 * backing table for an <em>expected element count</em>, reducing rehashing when you
 * know how many distinct values you will add. {@link HashSet} is backed by a
 * {@link java.util.HashMap}, so load-factor behavior matches map-based hashing.
 */
public final class HashSetNewHashSetDemo {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private HashSetNewHashSetDemo() {
    }

    public static void main(String[] args) {
        demonstrateSizedForKnownLoad();
        demonstrateVsRawConstructor();
        demonstrateLoadFactor();
    }

    /**
     * Typical use: you know how many distinct elements you will insert (e.g. unique IDs from a batch).
     */
    private static void demonstrateSizedForKnownLoad() {
        int expectedDistinctTags = 10_000;
        Set<String> tags = HashSet.newHashSet(expectedDistinctTags);

        for (int i = 0; i < expectedDistinctTags; i++) {
            tags.add("tag-" + i);
        }

        System.out.println("tags size: " + tags.size());
        System.out.println("contains tag-42: " + tags.contains("tag-42"));
    }

    /**
     * {@code newHashSet(n)} is defined for <em>elements</em>, not raw hash table capacity.
     * {@code new HashSet<>(n)} treats {@code n} as initial capacity of the backing map.
     */
    private static void demonstrateVsRawConstructor() {
        int plannedElements = 100;

        Set<String> a = HashSet.newHashSet(plannedElements);
        Set<String> b = new HashSet<>(plannedElements);

        for (int i = 0; i < plannedElements; i++) {
            a.add("e" + i);
            b.add("e" + i);
        }

        System.out.println("newHashSet(" + plannedElements + ") size: " + a.size());
        System.out.println("new HashSet<>(" + plannedElements + ") size: " + b.size());
        System.out.println(
                "Same logical size; newHashSet sizes the backing table for " + plannedElements
                        + " elements at default load factor (fewer internal resizes than a naive capacity guess).");
    }

    /**
     * Same load-factor trade-offs as {@link HashMap}: {@link HashSet#newHashSet(int)} uses the
     * default load factor ({@value #DEFAULT_LOAD_FACTOR}); use {@link HashSet#HashSet(int, float)}
     * when you need a different trade-off.
     */
    private static void demonstrateLoadFactor() {
        System.out.println("--- load factor (HashSet) ---");
        System.out.println(
                "Resize trigger (conceptually): size grows past bucketCount * loadFactor (capacity is rounded up by the JDK).");
        System.out.println("Default load factor: " + DEFAULT_LOAD_FACTOR);

        int elements = 48;
        int sameInitialCapacity = 64;

        HashSet<Integer> dense = new HashSet<>(sameInitialCapacity, 0.9f);
        HashSet<Integer> sparse = new HashSet<>(sameInitialCapacity, 0.5f);
        HashSet<Integer> factoryDefault = HashSet.newHashSet(elements);

        fillWithInts(dense, elements);
        fillWithInts(sparse, elements);
        fillWithInts(factoryDefault, elements);

        System.out.println(
                "Same " + elements + " elements, same initialCapacity=" + sameInitialCapacity + ": "
                        + "0.9f (denser) vs 0.5f (more headroom before resize).");
        System.out.println("dense.size=" + dense.size() + ", sparse.size=" + sparse.size());
        System.out.println(
                "HashSet.newHashSet(" + elements + ") uses default load factor " + DEFAULT_LOAD_FACTOR
                        + "; factoryDefault.size=" + factoryDefault.size());
    }

    private static void fillWithInts(HashSet<Integer> set, int count) {
        for (int i = 0; i < count; i++) {
            set.add(i);
        }
    }
}
