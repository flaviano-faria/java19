# java19

Educational Maven project showcasing **JDK 19–era APIs and language work** that remain relevant on newer JDKs. The artifact name refers to **Java 19**; the build is pinned to **Java 21** in `pom.xml` (preview features from 19 are often **final** there—no `--enable-preview` for the demos in this repo).

## Contents at a glance

| Track | What you get |
|-------|----------------|
| **Collections (JDK 19)** | Static factories that size hash structures by **expected element or mapping count** (`HashMap.newHashMap`, `HashSet.newHashSet`, `LinkedHashMap.newLinkedHashMap`, `LinkedHashSet.newLinkedHashSet`, `WeakHashMap.newWeakHashMap`), with comparisons to `new Type<>(initialCapacity)` and notes on **load factor**. |
| **Language (JEP 427)** | **`sealed` types**, **`record`**, pattern matching for **`switch`**, **`when`** guards, exhaustiveness—under package `com.storebackoffice.patternswitch`. |

## Prerequisites

- **JDK 21** or newer (matches `maven.compiler.source` / `target` in `pom.xml`)
- **Apache Maven 3.x**

```bash
java -version
mvn -version
```

## Build

```bash
mvn -q compile
```

Compiled classes are written to `target/classes`.

## Running demos

Every demo exposes `public static void main(String[] args)`.

### Option A: Maven Exec (plugin is resolved from the default plugin repository when you invoke the goal)

```bash
mvn -q compile exec:java -Dexec.mainClass=<fully.qualified.ClassName>
```

### Option B: `java` on the classpath (no Exec plugin required)

After `mvn -q compile`:

```bash
java -cp target/classes com.storebackoffice.HashMapNewHashMapDemo
java -cp target/classes com.storebackoffice.patternswitch.PatternSwitchDemo
```

### Demo catalog

Base package **`com.storebackoffice`** (except pattern switch):

| Main class | Highlights |
|------------|------------|
| `com.storebackoffice.HashMapNewHashMapDemo` | `HashMap.newHashMap`, vs `new HashMap<>(capacity)`, load factor |
| `com.storebackoffice.HashSetNewHashSetDemo` | `HashSet.newHashSet`, load factor |
| `com.storebackoffice.LinkedHashMapNewLinkedHashMapDemo` | `LinkedHashMap.newLinkedHashMap`, insertion order, load factor |
| `com.storebackoffice.LinkedHashSetNewLinkedHashSetDemo` | `LinkedHashSet.newLinkedHashSet`, insertion order, load factor |
| `com.storebackoffice.WeakHashMapNewWeakHashMapDemo` | `WeakHashMap.newWeakHashMap`, weak keys + GC caveats, load factor |
| `com.storebackoffice.Main` | Minimal placeholder |

Language demo (separate package):

| Main class | Highlights |
|------------|------------|
| `com.storebackoffice.patternswitch.PatternSwitchDemo` | JEP 427-style pattern `switch`: `sealed` `Payment`, `record` permits, `case` / `when`, exhaustive `switch` |

### Copy-paste: Exec plugin

```bash
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.HashMapNewHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.HashSetNewHashSetDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.LinkedHashMapNewLinkedHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.LinkedHashSetNewLinkedHashSetDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.WeakHashMapNewWeakHashMapDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.patternswitch.PatternSwitchDemo
mvn -q compile exec:java -Dexec.mainClass=com.storebackoffice.Main
```

## Why the new collection factories matter

For `HashMap` and friends, `new HashMap<>(n)` means **`n` is initial bucket capacity**, not “I will store `n` pairs.” With the default **load factor** (0.75), a wrong `n` causes unnecessary **resize / rehash** work.

The JDK 19 factories take **`numMappings`** or **`numElements`**: the implementation chooses a capacity suited to holding about that many entries at the default load factor.

For a **non-default load factor**, keep using constructors such as `HashMap(int, float)`, `LinkedHashMap(int, float, boolean)`, `WeakHashMap(int, float)`, etc.

## Source layout

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
        └── PatternSwitchDemo.java   # sealed Payment + records; pattern switch
```

## References

- [JDK 19 release notes](https://www.oracle.com/java/technologies/javase/19-relnote-issues.html)
- [JEP 427: Pattern Matching for switch](https://openjdk.org/jeps/427) (preview scope in JDK 19; finalized in a later release—this project uses **JDK 21** syntax)
- Javadoc (Java 21+): `HashMap.newHashMap`, `HashSet.newHashSet`, `LinkedHashMap.newLinkedHashMap`, `LinkedHashSet.newLinkedHashSet`, `WeakHashMap.newWeakHashMap`

## Encoding

Keep **`README.md` and `*.java` as UTF-8** (without UTF-16). Mixed encodings break diffs and GitHub rendering.

## License

No `LICENSE` file is checked in; treat terms as unspecified until you add one.
