# java19

| | |
|--|--|
| **Coordinates** | `com.storebackoffice:java19:1.0-SNAPSHOT` |
| **JDK** | **21** (`maven.compiler.source` / `target` in `pom.xml`) |
| **Build** | Apache Maven, **no third-party dependencies** |

Small **executable demos** for **JDK 19–era `java.util` factory methods** (still current on newer JDKs), a **set comparison** walkthrough, **virtual threads** (JEP 425, preview in 19; **final in 21**), and **pattern matching for `switch`** (JEP 427, preview in 19; **final in 21**). This repo targets **Java 21** and does not use `--enable-preview`.

---

## What is in this repository

| Area | Location on disk | Java package | Focus |
|------|------------------|--------------|--------|
| **Collection factories & load factor** | `src/main/java/com/storebackoffice/loadfactor/` | **`com.storebackoffice`** | `HashMap.newHashMap`, `HashSet.newHashSet`, `LinkedHashMap.newLinkedHashMap`, `LinkedHashSet.newLinkedHashSet`, `WeakHashMap.newWeakHashMap` vs raw `initialCapacity` and custom **load factor** constructors. |
| **HashSet / LinkedHashSet / TreeSet** | `src/main/java/com/storebackoffice/setcomparison/` | **`com.storebackoffice.setcomparison`** | Iteration order, duplicates, `null`, `Comparator`, `NavigableSet` (`HashLinkedTreeSetDemo`). |
| **Virtual threads** | `src/main/java/com/storebackoffice/virtualthreads/` | **`com.storebackoffice.virtualthreads`** | `Executors.newVirtualThreadPerTaskExecutor()`, `Thread.ofVirtual()` (`VirtualThreadsDemo`). |
| **Pattern `switch`** | `src/main/java/com/storebackoffice/patternswitch/` | **`com.storebackoffice.patternswitch`** | `sealed` + `record` + type patterns + `when` + exhaustiveness (`PatternSwitchDemo`). |

**Note:** Classes under `loadfactor/` still declare `package com.storebackoffice;`—the folder is only for filesystem organization. Fully qualified names for those mains **do not** include `.loadfactor`.

---

## Prerequisites

- **JDK 21+**
- **Maven 3.x**

```bash
java -version
mvn -version
```

## Compile

```bash
mvn -q compile
```

Output: `target/classes`.

## Run a demo

Each listed type has `public static void main(String[] args)`.

**Maven Exec** (plugin version resolved when you run the goal):

```bash
mvn -q compile exec:java -Dexec.mainClass=<fully.qualified.ClassName>
```

**Plain `java`** (after `mvn -q compile`):

```bash
java -cp target/classes <fully.qualified.ClassName>
```

### All entry points (`main`)

| Fully qualified class | Role |
|-----------------------|------|
| `com.storebackoffice.HashMapNewHashMapDemo` | `HashMap.newHashMap`, capacity vs mappings, load factor |
| `com.storebackoffice.HashSetNewHashSetDemo` | `HashSet.newHashSet`, load factor |
| `com.storebackoffice.LinkedHashMapNewLinkedHashMapDemo` | `LinkedHashMap.newLinkedHashMap`, insertion order, load factor |
| `com.storebackoffice.LinkedHashSetNewLinkedHashSetDemo` | `LinkedHashSet.newLinkedHashSet`, insertion order, load factor |
| `com.storebackoffice.WeakHashMapNewWeakHashMapDemo` | `WeakHashMap.newWeakHashMap`, weak keys, GC, load factor |
| `com.storebackoffice.setcomparison.HashLinkedTreeSetDemo` | `HashSet` vs `LinkedHashSet` vs `TreeSet` |
| `com.storebackoffice.virtualthreads.VirtualThreadsDemo` | Virtual threads (JEP 425): many blocking tasks, one executor |
| `com.storebackoffice.patternswitch.PatternSwitchDemo` | Pattern matching for `switch` (JEP 427) |
| `com.storebackoffice.Main` | Minimal placeholder |

### Copy-paste (`exec:java`)

```bash
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.HashMapNewHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.HashSetNewHashSetDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.LinkedHashMapNewLinkedHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.LinkedHashSetNewLinkedHashSetDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.WeakHashMapNewWeakHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.setcomparison.HashLinkedTreeSetDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.virtualthreads.VirtualThreadsDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.patternswitch.PatternSwitchDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.Main
```

---

## Why `newHashMap(n)` (and friends) exist

`new HashMap<>(n)` uses **`n` as initial table capacity** (buckets), not “I will store `n` entries.” With default **load factor** `0.75`, a poorly chosen `n` causes extra **resize / rehash** work.

The JDK 19 factories take **`numMappings`** / **`numElements`** and pick a capacity suited to about that many entries at the default load factor.

For a **different load factor**, use constructors such as `HashMap(int, float)`, `LinkedHashMap(int, float, boolean)`, `WeakHashMap(int, float)`, etc.

---

## Source tree

```text
src/main/java/com/storebackoffice/
├── loadfactor/
│   ├── Main.java
│   ├── HashMapNewHashMapDemo.java
│   ├── HashSetNewHashSetDemo.java
│   ├── LinkedHashMapNewLinkedHashMapDemo.java
│   ├── LinkedHashSetNewLinkedHashSetDemo.java
│   └── WeakHashMapNewWeakHashMapDemo.java
├── setcomparison/
│   └── HashLinkedTreeSetDemo.java
├── virtualthreads/
│   └── VirtualThreadsDemo.java
└── patternswitch/
    └── PatternSwitchDemo.java
```

---

## More JDK 19 JEPs (good follow-ups, not all implemented here)

| JEP (19) | Topic | Fit for this repo |
|----------|--------|-------------------|
| **405** | Record patterns | Add `recordpatterns` demo: nested deconstruction in `instanceof` / `switch` (final in 21). |
| **428** | Structured concurrency | `StructuredTaskScope` in `java.util.concurrent` (API evolved after 19; good for a small fan-out / failure demo on 21). |
| **424** | Foreign Function & Memory | Native interop without JNI; needs **preview** on 21 or **JDK 22+** for finalized FFM—best as a **Maven profile**. |
| **426** | Vector API | `jdk.incubator.vector`; needs **`--add-modules`** and is hardware-specific. |

Virtual threads and pattern `switch` are the lowest-friction “big” 19-era features on **Java 21** with no extra JVM flags.

---

## References

- [JDK 19 release notes](https://www.oracle.com/java/technologies/javase/19-relnote-issues.html)
- [JEP 425: Virtual Threads](https://openjdk.org/jeps/425) (preview in 19; final in 21)
- [JEP 427: Pattern Matching for switch](https://openjdk.org/jeps/427)
- Java 21+ Javadoc: `Executors.newVirtualThreadPerTaskExecutor`, `Thread.ofVirtual`, `HashMap.newHashMap`, `HashSet.newHashSet`, `LinkedHashMap.newLinkedHashMap`, `LinkedHashSet.newLinkedHashSet`, `WeakHashMap.newWeakHashMap`, `TreeSet`, `NavigableSet`

## Encoding

Use **UTF-8** for `README.md` and sources. **Do not** save the README as UTF-16 (Git / IDE diffs will show spurious `NUL` characters).

## License

No `LICENSE` file is present; treat redistribution terms as unspecified until you add one.
