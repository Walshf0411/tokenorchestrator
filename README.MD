### Data structure for the Key
```text
id -> UUID
value -> String
createdAt -> DateTime
blockedAt -> DateTime
```

### Data structures in use
```java
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

//  Holds all keys
Map<UUID, Key> keys = new HashMap<>();

// Available keys
Stack<UUID> availableKeys = new Stack<UUID>();

// Blocked keys
Set<UUID> blockedKeys = new HashSet<>();

```
