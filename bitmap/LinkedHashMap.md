# LinkedHashMap 分析

LinkedHashMap继承自HashMap，同时使用了双向链表将节点链接起来，双向链表的排序有两种方式，
分别是按照插入顺序排序以及按照访问顺序排序。最后被插入的节点或者最后被访问的节点放在链表的尾部。

```java
public class LinkedHashMap<K,V>
    extends HashMap<K,V>
    implements Map<K,V>
{ 
    public LinkedHashMap(int initialCapacity,
                      float loadFactor,
                      boolean accessOrder) {
     super(initialCapacity, loadFactor);
     // 记录当前的元素是以访问顺序排序，还是以插入顺序排序
     this.accessOrder = accessOrder;
    }   

    // HashMap.put()会回调该方法插入一个节点
    Node<K,V> newNode(int hash, K key, V value, Node<K,V> e) {
        LinkedHashMapEntry<K,V> p =
            new LinkedHashMapEntry<K,V>(hash, key, value, e);
        // 将新建的节点插入到尾部
        linkNodeLast(p);
        return p;
    }

    // link at the end of list
    // 将节点链接到尾部
    private void linkNodeLast(LinkedHashMapEntry<K,V> p) {
        LinkedHashMapEntry<K,V> last = tail;
        tail = p;
        if (last == null)
            head = p;
        else {
            p.before = last;
            last.after = p;
        }
    }

    public V get(Object key) {
        Node<K,V> e;
        // 得到key对应的Node
        if ((e = getNode(hash(key), key)) == null)
            return null;
        // 维护访问顺序
        if (accessOrder)
            // 如果以访问顺序排序， 继续处理
            afterNodeAccess(e);
        return e.value;
    }

    // 将e移动到最后也就是最新的
    void afterNodeAccess(Node<K,V> e) { // move node to last
        LinkedHashMapEntry<K,V> last;
        // e已经在最后，则不操作
        if (accessOrder && (last = tail) != e) {
            LinkedHashMapEntry<K,V> p =
                (LinkedHashMapEntry<K,V>)e, b = p.before, a = p.after;
            p.after = null;
            // 先删除e
            // 调整前一个节点
            if (b == null)
                head = a;
            else
                b.after = a;
            // 调整后一个节点
            // 这里a一定不为null
            if (a != null)
                a.before = b;
            else
                last = b;
            // 将e插入到尾部
            if (last == null)
                head = p;
            else {
                p.before = last;
                last.after = p;
            }
            tail = p;
            ++modCount;
        }
    }
}
```