# LruCache 分析

LruCache使用了LinkedHashMap来实现，元素的访问顺序由LinkedHashMap来维护，
LruCache自身负责缓存的插入和删除。

使用LruCache需要设置缓存容量，以及重写sizeOf()方法来计算每个数据项的大小。

```java
public class LruCache<K, V> {
    // 内部基于LinkedHashMap
    private final LinkedHashMap<K, V> map;
    // 缓存大小
    private int size;
    // 缓存大小上限
    private int maxSize;

   public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }


    public final V get(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }

        V mapValue;
        // LRUCache是加锁的
        synchronized (this) {
            // key对应的value已经缓存过就直接返回
            mapValue = map.get(key);
            if (mapValue != null) {
                hitCount++;
                return mapValue;
            }
            missCount++;
        }

        /*
         * Attempt to create a value. This may take a long time, and the map
         * may be different when create() returns. If a conflicting value was
         * added to the map while create() was working, we leave that value in
         * the map and release the created value.
         */

        // 创建一个值
        V createdValue = create(key);
        if (createdValue == null) {
            // 值不存在仍然返回空
            return null;
        }

        synchronized (this) {
            createCount++;
            // 将新值放入缓存
            mapValue = map.put(key, createdValue);

            if (mapValue != null) {
                // There was a conflict so undo that last put
                // 如果旧值存在，将旧值放入缓存
                map.put(key, mapValue);
            } else {
                size += safeSizeOf(key, createdValue);
            }
        }

        if (mapValue != null) {
            // 如果旧值存在，通知释放新值
            entryRemoved(false, key, createdValue, mapValue);
            return mapValue;
        } else {
            // 插入新值后，根据lru算法移除旧值
            trimToSize(maxSize);
            return createdValue;
        }
    }

    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }

        V previous;
        synchronized (this) {
            putCount++;
            // 累加新值对应的内存大小
            size += safeSizeOf(key, value);
            // 获取到旧值
            previous = map.put(key, value);
            if (previous != null) {
                // 减去旧值对应的内存大小
                size -= safeSizeOf(key, previous);
            }
        }

        if (previous != null) {
            // 旧值存在，则通知旧值已经从缓存中移除了
            entryRemoved(false, key, previous, value);
        }

        // 瘦身缓存，根据lru算法移除一些数据
        trimToSize(maxSize);
        return previous;
    }

    // 调整缓存大小
    private void trimToSize(int maxSize) {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (size < 0 || (map.isEmpty() && size != 0)) {
                    throw new IllegalStateException(getClass().getName()
                            + ".sizeOf() is reporting inconsistent results!");
                }

                // 缓存大小小于上限，则推出调整
                if (size <= maxSize) {
                    break;
                }

                // BEGIN LAYOUTLIB CHANGE
                // get the last item in the linked list.
                // This is not efficient, the goal here is to minimize the changes
                // compared to the platform version.
                // 获取到链表中的最后一个数据，待删除的数据
                Map.Entry<K, V> toEvict = null;
                for (Map.Entry<K, V> entry : map.entrySet()) {
                    toEvict = entry;
                }
                // END LAYOUTLIB CHANGE

                if (toEvict == null) {
                    break;
                }
                // 删除该数据
                key = toEvict.getKey();
                value = toEvict.getValue();
                map.remove(key);
                size -= safeSizeOf(key, value);
                evictionCount++;
            }
            // 通知数据已删除
            entryRemoved(true, key, value, null);
        }
    }

}

```