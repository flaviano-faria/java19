# java19

Sample Maven project focused on **JDK 19-era features**: `java.util` **static factory methods** that size hash-based collections by **expected mappings or elements**, plus **language** demos such as **pattern matching for `switch`** (JEP 427 preview in 19; **final in 21** — this repo targets Java 21).

The repository name refers to the release that introduced these APIs; the toolchain compiles against **Java 21** (see `pom.xml`).

## Prerequisites

- **JDK 21** (or newer) aligned with `maven.compiler.source` / `maven.compiler.target`
- **Apache Maven 3.x**

Verify:

```bash
java -version
mvn -version
```

## Build

From the project root:

```bash
mvn -q compile
```

## Running the demos

Each demo is a class with a `public static void main(String[] args)` entry point. Run with the Exec plugin:

```bash
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.<DemoClass>
```

Or run the desired class from your IDE.

### Demo classes

| Class | API highlighted | What it shows |
|--------|-----------------|---------------|
| `HashMapNewHashMapDemo` | `HashMap.newHashMap(int numMappings)` | Pre-sizing for a known number of entries; comparison with `new HashMap<>(initialCapacity)`; default load factor vs `HashMap(int, float)`. |
| `HashSetNewHashSetDemo` | `HashSet.newHashSet(int numElements)` | Same idea for sets; load factor via `HashSet(int, float)`. |
| `LinkedHashMapNewLinkedHashMapDemo` | `LinkedHashMap.newLinkedHashMap(int numMappings)` | Pre-sizing plus **insertion-order** iteration; notes `LinkedHashMap(int, float, boolean)` for access-order. |
| `LinkedHashSetNewLinkedHashSetDemo` | `LinkedHashSet.newLinkedHashSet(int numElements)` | Pre-sizing plus **insertion-order** iteration over the set. |
| `WeakHashMapNewWeakHashMapDemo` | `WeakHashMap.newWeakHashMap(int numMappings)` | Pre-sizing; **weak keys** (entries may disappear after GC when keys are not strongly reachable); load factor demo keeps strong references to keys on purpose. |
| `patternswitch.PatternSwitchDemo` | Pattern matching for `switch` (JEP 427) | Package `com.storebackoffice.patternswitch`. **`sealed` `Payment`** with `record` permits; **type patterns**; **`case` … `when`** guards; **exhaustive** `switch` (compiler-checked). |
| `Main` | — | Minimal placeholder (`Hello, World!`). |

### Example commands

```bash
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.HashMapNewHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.HashSetNewHashSetDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.LinkedHashMapNewLinkedHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.LinkedHashSetNewLinkedHashSetDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.WeakHashMapNewWeakHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.patternswitch.PatternSwitchDemo
```

## Why these factory methods exist

For `HashMap` (and similar types), the constructor `new HashMap<>(n)` takes **`n` as initial capacity** (bucket count), not “I will store `n` key-value pairs.” With the default **load factor** (0.75), choosing the wrong `n` causes extra internal **resize/rehash** work.

The JDK 19 factories accept **`numMappings` / `numElements`**: the implementation derives a suitable initial capacity so the table can grow to about that many entries at the default load factor **without unnecessary resizing**.

Custom load factors still use the existing constructors, for example:

- `HashMap(int initialCapacity, float loadFactor)`
- `LinkedHashMap(int initialCapacity, float loadFactor)` or `(int, float, boolean accessOrder)`
- `WeakHashMap(int initialCapacity, float loadFactor)`

## Project layout

```text
java19/
├── pom.xml
├── README.md
└── src/main/java/com/storebackoffice/
    ├── Main.java
    ├── HashMapNewHashMapDemo.java
    ├── HashSetNewHashSetDemo.java
    ├── LinkedHashMapNewLinkedHashMapDemo.java
    ├── LinkedHashSetNewLinkedHashSetDemo.java
    ├── WeakHashMapNewWeakHashMapDemo.java
    └── patternswitch/
        └── PatternSwitchDemo.java
```

## References

- [JDK 19 release notes](https://www.oracle.com/java/technologies/javase/19-relnote-issues.html) (platform and `java.util` changes)
- Javadoc for each type: `HashMap.newHashMap`, `HashSet.newHashSet`, `LinkedHashMap.newLinkedHashMap`, `LinkedHashSet.newLinkedHashSet`, `WeakHashMap.newWeakHashMap`

## License

No license file is present in this repository; treat usage terms as unspecified until one is added.
