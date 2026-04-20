package com.storebackoffice.recordpatterns;

public final class RecordPatternsDemo {

    private RecordPatternsDemo() {
    }

    public static void main(String[] args) {
        System.out.println("JEP 405 (preview in JDK 19): record patterns are final in JDK 21.\n");
        instanceofWithRecordPattern();
        switchWithNestedRecordPatterns();
        varInRecordPattern();
    }

    private static void instanceofWithRecordPattern() {
        System.out.println("--- instanceof + record pattern ---");
        Object value = new Point(3, 4);
        if (value instanceof Point(int x, int y)) {
            System.out.println("destructured: x=" + x + " y=" + y + " distance^2=" + (x * x + y * y));
        }
    }

    private static void switchWithNestedRecordPatterns() {
        System.out.println("--- switch + nested record patterns + when ---");
        Object[] samples = {
                new Segment(new Point(1, 2), new Point(1, 8)),
                new Segment(new Point(0, 0), new Point(3, 4)),
                "not a segment",
        };
        for (Object sample : samples) {
            String line = switch (sample) {
                case Segment(Point(int x1, int y1), Point(int x2, int y2)) when x1 == x2 ->
                        "vertical segment at x=" + x1;
                case Segment(Point a, Point b) -> "segment " + a + " -> " + b;
                default -> String.valueOf(sample);
            };
            System.out.println(line);
        }
    }

    private static void varInRecordPattern() {
        System.out.println("--- var in record pattern ---");
        Object value = new Point(7, -2);
        if (value instanceof Point(var px, var py)) {
            System.out.println("var bindings: px=" + px + " py=" + py);
        }
    }
}

record Point(int x, int y) {
}

record Segment(Point from, Point to) {
}
