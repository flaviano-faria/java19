package com.storebackoffice.setcomparison;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

public final class HashLinkedTreeSetDemo {

    private HashLinkedTreeSetDemo() {
    }

    public static void main(String[] args) {
        demonstrateIterationOrder();
        demonstrateDuplicates();
        demonstrateNullRules();
        demonstrateTreeSetComparisonAndBounds();
    }

    private static void demonstrateIterationOrder() {
        System.out.println("=== iteration order (insert: banana, cherry, apple) ===");
        List<String> insertOrder = List.of("banana", "cherry", "apple");

        HashSet<String> hash = new HashSet<>();
        LinkedHashSet<String> linked = new LinkedHashSet<>();
        TreeSet<String> tree = new TreeSet<>();

        for (String s : insertOrder) {
            hash.add(s);
            linked.add(s);
            tree.add(s);
        }

        System.out.println("HashSet:        " + hash + "  (undefined order; do not rely on it)");
        System.out.println("LinkedHashSet:  " + linked + "  (insertion order)");
        System.out.println("TreeSet:        " + tree + "  (sorted / natural order)");
    }

    private static void demonstrateDuplicates() {
        System.out.println("=== duplicates ===");
        TreeSet<Integer> tree = new TreeSet<>(List.of(10, 10, 20));
        System.out.println("After adding 10 twice and 20 once, TreeSet size=" + tree.size() + " -> " + tree);
    }

    private static void demonstrateNullRules() {
        System.out.println("=== null ===");
        HashSet<String> hash = new HashSet<>();
        hash.add(null);
        System.out.println("HashSet allows null: " + hash);

        LinkedHashSet<String> linked = new LinkedHashSet<>();
        linked.add(null);
        System.out.println("LinkedHashSet allows null: " + linked);

        TreeSet<String> tree = new TreeSet<>();
        try {
            tree.add(null);
            System.out.println("TreeSet accepted null (unexpected for natural order)");
        } catch (NullPointerException ex) {
            System.out.println("TreeSet (natural order) throws NullPointerException on add(null): " + ex.getClass().getSimpleName());
        }
    }

    private static void demonstrateTreeSetComparisonAndBounds() {
        System.out.println("=== TreeSet: comparator, first/last, subSet ===");
        TreeSet<String> natural = new TreeSet<>(List.of("delta", "alpha", "charlie", "bravo"));
        System.out.println("Natural order:              " + natural);
        System.out.println("first() / last(): " + natural.first() + " .. " + natural.last());

        TreeSet<String> reverse = new TreeSet<>(Comparator.reverseOrder());
        reverse.addAll(List.of("delta", "alpha", "charlie"));
        System.out.println("Comparator.reverseOrder(): " + reverse);

        TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        byLength.addAll(List.of("a", "ccc", "bb", "b", "aaa"));
        System.out.println("By length, then alpha:     " + byLength);

        NavigableSet<Integer> scores = new TreeSet<>(List.of(40, 72, 55, 90, 68));
        System.out.println("Scores in [60, 85]:        " + scores.subSet(60, true, 85, true));
    }
}
