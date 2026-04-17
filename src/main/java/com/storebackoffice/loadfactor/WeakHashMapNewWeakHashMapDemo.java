package com.storebackoffice;

import java.util.WeakHashMap;

/**
 * Demonstrates {@link WeakHashMap#newWeakHashMap(int)} (Java 19+): sizes the table for an
 * expected number of <em>mappings</em> at the default load factor. {@link WeakHashMap} entries
 * may disappear when their keys are only weakly reachable, so demos that need a stable size must
 * retain strong references to keys outside the map.
 */
public final class WeakHashMapNewWeakHashMapDemo {

    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private WeakHashMapNewWeakHashMapDemo() {
    }

    public static void main(String[] args) {
        demonstrateSizedForKnownLoad();
        demonstrateVsRawConstructor();
        demonstrateWeakKeys();
        demonstrateLoadFactor();
    }

    /**
     * Typical use: you know how many cache-like entries you may register, while keys remain strongly
     * reachable elsewhere for as long as you need the values.
     */
    private static void demonstrateSizedForKnownLoad() {
        int expectedMappings = 10_000;
        Object[] strongKeys = new Object[expectedMappings];
        WeakHashMap<Object, Integer> cache = WeakHashMap.newWeakHashMap(expectedMappings);

        for (int i = 0; i < expectedMappings; i++) {
            Object key = new Object();
            strongKeys[i] = key;
            cache.put(key, i);
        }

        System.out.println("cache size (keys held strongly): " + cache.size());
        System.out.println("sample get: " + cache.get(strongKeys[42]));
    }

    /**
     * {@code newWeakHashMap(n)} is defined for <em>mappings</em>, not raw table capacity.
     * {@code new WeakHashMap<>(n)} treats {@code n} as initial capacity only.
     */
    private static void demonstrateVsRawConstructor() {
        int plannedEntries = 100;
        Object[] strongKeys = new Object[plannedEntries];

        WeakHashMap<Object, String> a = WeakHashMap.newWeakHashMap(plannedEntries);
        WeakHashMap<Object, String> b = new WeakHashMap<>(plannedEntries);

        for (int i = 0; i < plannedEntries; i++) {
            Object key = new Object();
            strongKeys[i] = key;
            a.put(key, "a");
            b.put(key, "b");
        }

        System.out.println("newWeakHashMap(" + plannedEntries + ") size: " + a.size());
        System.out.println("new WeakHashMap<>(" + plannedEntries + ") size: " + b.size());
        System.out.println(
                "Same logical size; newWeakHashMap sizes buckets for " + plannedEntries
                        + " mappings at default load factor (fewer internal resizes than a naive capacity guess).");
    }

    /**
     * When no strong reference to a key remains, the entry may be removed after GC (exact timing is JVM-specific).
     */
    private static void demonstrateWeakKeys() {
        System.out.println("--- weak keys (GC is a hint; behavior may vary by JVM) ---");
        WeakHashMap<Object, String> map = WeakHashMap.newWeakHashMap(4);
        Object key = new Object();
        map.put(key, "payload");
        System.out.println("While key is strongly reachable, size=" + map.size());

        key = null;
        suggestGarbageCollection();
        System.out.println("After dropping the strong key reference and suggesting GC, size=" + map.size());
    }

    /**
     * {@link WeakHashMap#newWeakHashMap(int)} uses the default load factor ({@value #DEFAULT_LOAD_FACTOR});
     * use {@link WeakHashMap#WeakHashMap(int, float)} for a different trade-off.
     */
    private static void demonstrateLoadFactor() {
        System.out.println("--- load factor (WeakHashMap) ---");
        System.out.println(
                "Resize trigger (conceptually): size grows past bucketCount * loadFactor (capacity is rounded up by the JDK).");
        System.out.println("Default load factor: " + DEFAULT_LOAD_FACTOR);

        int entries = 48;
        int sameInitialCapacity = 64;

        WeakHashMap<Object, String> dense = new WeakHashMap<>(sameInitialCapacity, 0.9f);
        WeakHashMap<Object, String> sparse = new WeakHashMap<>(sameInitialCapacity, 0.5f);
        WeakHashMap<Object, String> factoryDefault = WeakHashMap.newWeakHashMap(entries);

        Object[] keepDense = fillWeakMap(dense, entries);
        Object[] keepSparse = fillWeakMap(sparse, entries);
        Object[] keepFactory = fillWeakMap(factoryDefault, entries);

        System.out.println(
                "Strong key refs retained (array lengths "
                        + keepDense.length
                        + "/"
                        + keepSparse.length
                        + "/"
                        + keepFactory.length
                        + ") so entries are not cleared during this demo.");
        System.out.println(
                "Same " + entries + " entries, same initialCapacity=" + sameInitialCapacity + ": "
                        + "0.9f (denser) vs 0.5f (more headroom before resize).");
        System.out.println("dense.size=" + dense.size() + ", sparse.size=" + sparse.size());
        System.out.println(
                "WeakHashMap.newWeakHashMap(" + entries + ") uses default load factor " + DEFAULT_LOAD_FACTOR
                        + "; factoryDefault.size=" + factoryDefault.size());
    }

    private static Object[] fillWeakMap(WeakHashMap<Object, String> map, int count) {
        Object[] keys = new Object[count];
        for (int i = 0; i < count; i++) {
            keys[i] = new Object();
            map.put(keys[i], "v" + i);
        }
        return keys;
    }

    private static void suggestGarbageCollection() {
        for (int i = 0; i < 8; i++) {
            System.gc();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }
}
