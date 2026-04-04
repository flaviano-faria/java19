package com.storebackoffice;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates {@link HashMap#newHashMap(int)} (Java 19+): a factory that picks
 * initial capacity from the <em>expected number of mappings</em>, so the map can
 * grow to that size without resizing/rehashing. Also contrasts the default load
 * factor with explicit {@link HashMap#HashMap(int, float)} usage.
 */
public final class HashMapNewHashMapDemo {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private HashMapNewHashMapDemo() {
    }

    public static void main(String[] args) {
        demonstrateSizedForKnownLoad();
        demonstrateVsRawConstructor();
        demonstrateLoadFactor();
    }

    /**
     * Typical use: you know how many key/value pairs you will insert (e.g. from a query or file).
     */
    private static void demonstrateSizedForKnownLoad() {
        int expectedProductCount = 10_000;
        Map<String, Integer> inventory = HashMap.newHashMap(expectedProductCount);

        for (int i = 0; i < expectedProductCount; i++) {
            inventory.put("SKU-" + i, i);
        }

        System.out.println("inventory size: " + inventory.size());
        System.out.println("sample get: " + inventory.get("SKU-42"));
    }

    /**
     * {@code newHashMap(n)} is defined for <em>mappings</em>, not raw capacity.
     * {@code new HashMap<>(n)} treats {@code n} as initial capacity (buckets), which is a lower-level
     * knob and does not mean "room for n entries" at default load factor.
     */
    private static void demonstrateVsRawConstructor() {
        int plannedEntries = 100;

        Map<String, String> a = HashMap.newHashMap(plannedEntries);
        Map<String, String> b = new HashMap<>(plannedEntries);

        for (int i = 0; i < plannedEntries; i++) {
            String key = "k" + i;
            a.put(key, "a");
            b.put(key, "b");
        }

        System.out.println("newHashMap(" + plannedEntries + ") size: " + a.size());
        System.out.println("new HashMap<>(" + plannedEntries + ") size: " + b.size());
        System.out.println(
                "Same logical size; newHashMap sizes buckets for " + plannedEntries
                        + " mappings at default load factor (fewer internal resizes than a naive capacity guess).");
    }

    /**
     * Load factor controls how full the hash table may get before it grows: roughly,
     * resize happens when {@code size > bucketCount * loadFactor}. Lower values keep
     * the table emptier (fewer collisions, more memory); higher values pack more
     * entries before a resize. {@link HashMap#newHashMap(int)} always uses the default
     * load factor ({@value #DEFAULT_LOAD_FACTOR}); use {@link HashMap#HashMap(int, float)}
     * when you need a different trade-off.
     */
    private static void demonstrateLoadFactor() {
        System.out.println("--- load factor ---");
        System.out.println(
                "Resize trigger (conceptually): size grows past bucketCount * loadFactor (capacity is rounded up by the JDK).");
        System.out.println("Default load factor: " + DEFAULT_LOAD_FACTOR);

        int entries = 48;
        int sameInitialCapacity = 64;

        HashMap<Integer, String> dense = new HashMap<>(sameInitialCapacity, 0.9f);
        HashMap<Integer, String> sparse = new HashMap<>(sameInitialCapacity, 0.5f);
        HashMap<Integer, String> factoryDefault = HashMap.newHashMap(entries);

        fillWithInts(dense, entries);
        fillWithInts(sparse, entries);
        fillWithInts(factoryDefault, entries);

        System.out.println(
                "Same " + entries + " entries, same initialCapacity=" + sameInitialCapacity + ": "
                        + "0.9f (denser) vs 0.5f (more headroom before resize).");
        System.out.println("dense.size=" + dense.size() + ", sparse.size=" + sparse.size());
        System.out.println(
                "HashMap.newHashMap(" + entries + ") uses default load factor " + DEFAULT_LOAD_FACTOR
                        + "; factoryDefault.size=" + factoryDefault.size());
    }

    private static void fillWithInts(HashMap<Integer, String> map, int count) {
        for (int i = 0; i < count; i++) {
            map.put(i, "v" + i);
        }
    }
}
