package com.storebackoffice;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Demonstrates {@link LinkedHashMap#newLinkedHashMap(int)} (Java 19+): like
 * {@link java.util.HashMap#newHashMap(int)}, it sizes the table for an expected
 * number of <em>mappings</em> at the default load factor, while preserving
 * {@link LinkedHashMap}'s predictable <em>insertion-order</em> iteration (unless
 * you use the access-order constructor instead).
 */
public final class LinkedHashMapNewLinkedHashMapDemo {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private LinkedHashMapNewLinkedHashMapDemo() {
    }

    public static void main(String[] args) {
        demonstrateSizedForKnownLoad();
        demonstrateVsRawConstructor();
        demonstrateInsertionOrder();
        demonstrateLoadFactor();
    }

    /**
     * Typical use: known result size and you rely on insertion-ordered keys or entries.
     */
    private static void demonstrateSizedForKnownLoad() {
        int expectedRows = 10_000;
        Map<String, Integer> rowIndexById = LinkedHashMap.newLinkedHashMap(expectedRows);

        for (int i = 0; i < expectedRows; i++) {
            rowIndexById.put("ROW-" + i, i);
        }

        System.out.println("row map size: " + rowIndexById.size());
        System.out.println("sample get: " + rowIndexById.get("ROW-42"));
    }

    /**
     * {@code newLinkedHashMap(n)} is defined for <em>mappings</em>, not raw table capacity.
     * {@code new LinkedHashMap<>(n)} uses {@code n} as initial capacity only.
     */
    private static void demonstrateVsRawConstructor() {
        int plannedEntries = 100;

        Map<String, String> a = LinkedHashMap.newLinkedHashMap(plannedEntries);
        Map<String, String> b = new LinkedHashMap<>(plannedEntries);

        for (int i = 0; i < plannedEntries; i++) {
            String key = "k" + i;
            a.put(key, "a");
            b.put(key, "b");
        }

        System.out.println("newLinkedHashMap(" + plannedEntries + ") size: " + a.size());
        System.out.println("new LinkedHashMap<>(" + plannedEntries + ") size: " + b.size());
        System.out.println(
                "Same logical size; newLinkedHashMap sizes buckets for " + plannedEntries
                        + " mappings at default load factor (fewer internal resizes than a naive capacity guess).");
    }

    /**
     * {@link LinkedHashMap} iterates in insertion order by default; the factory does not change that.
     */
    private static void demonstrateInsertionOrder() {
        System.out.println("--- insertion order ---");
        LinkedHashMap<String, Integer> ordered = LinkedHashMap.newLinkedHashMap(8);
        ordered.put("third", 3);
        ordered.put("first", 1);
        ordered.put("second", 2);

        StringJoiner joiner = new StringJoiner(", ");
        for (String key : ordered.keySet()) {
            joiner.add(key);
        }
        System.out.println("key iteration order: " + joiner + " (matches insertion order, not sort order)");
    }

    /**
     * {@link LinkedHashMap#newLinkedHashMap(int)} uses the default load factor ({@value #DEFAULT_LOAD_FACTOR});
     * use {@link LinkedHashMap#LinkedHashMap(int, float)} or
     * {@link LinkedHashMap#LinkedHashMap(int, float, boolean)} for other trade-offs (including access-order mode).
     */
    private static void demonstrateLoadFactor() {
        System.out.println("--- load factor (LinkedHashMap) ---");
        System.out.println(
                "Resize trigger (conceptually): size grows past bucketCount * loadFactor (capacity is rounded up by the JDK).");
        System.out.println("Default load factor: " + DEFAULT_LOAD_FACTOR);

        int entries = 48;
        int sameInitialCapacity = 64;

        LinkedHashMap<Integer, String> dense = new LinkedHashMap<>(sameInitialCapacity, 0.9f);
        LinkedHashMap<Integer, String> sparse = new LinkedHashMap<>(sameInitialCapacity, 0.5f);
        LinkedHashMap<Integer, String> factoryDefault = LinkedHashMap.newLinkedHashMap(entries);

        fillWithInts(dense, entries);
        fillWithInts(sparse, entries);
        fillWithInts(factoryDefault, entries);

        System.out.println(
                "Same " + entries + " entries, same initialCapacity=" + sameInitialCapacity + ": "
                        + "0.9f (denser) vs 0.5f (more headroom before resize).");
        System.out.println("dense.size=" + dense.size() + ", sparse.size=" + sparse.size());
        System.out.println(
                "LinkedHashMap.newLinkedHashMap(" + entries + ") uses default load factor " + DEFAULT_LOAD_FACTOR
                        + "; factoryDefault.size=" + factoryDefault.size());
    }

    private static void fillWithInts(LinkedHashMap<Integer, String> map, int count) {
        for (int i = 0; i < count; i++) {
            map.put(i, "v" + i);
        }
    }
}
