package by.it.group410971.rabchenya.lesson12;

import java.util.Comparator;
import java.util.NavigableMap;
import java.util.NavigableSet;

public class MySplayMap implements NavigableMap<Integer, String> {

    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int size;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.size = 1;
        }
    }

    private Node root;

    public MySplayMap() {
        root = null;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (root == null) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) {
            sb.setLength(sb.length() - 2);
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String put(Integer key, String value) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            root = new Node(key, value);
            return null;
        }

        String[] oldValue = new String[1];
        root = splay(root, key);

        int cmp = key.compareTo(root.key);
        if (cmp == 0) {
            oldValue[0] = root.value;
            root.value = value;
        } else if (cmp < 0) {
            Node newNode = new Node(key, value);
            newNode.left = root.left;
            newNode.right = root;
            root.left = null;
            updateSize(root);
            root = newNode;
        } else {
            Node newNode = new Node(key, value);
            newNode.right = root.right;
            newNode.left = root;
            root.right = null;
            updateSize(root);
            root = newNode;
        }

        updateSize(root);
        return oldValue[0];
    }

    @Override
    public String remove(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (!(key instanceof Integer)) {
            return null;
        }

        if (root == null) {
            return null;
        }

        root = splay(root, (Integer) key);

        if (((Integer) key).compareTo(root.key) != 0) {
            return null;
        }

        String removedValue = root.value;

        if (root.left == null) {
            root = root.right;
        } else {
            Node newRoot = root.right;
            if (newRoot != null) {
                newRoot = splay(newRoot, (Integer) key);
                newRoot.left = root.left;
                root = newRoot;
            } else {
                root = root.left;
            }
        }

        if (root != null) {
            updateSize(root);
        }

        return removedValue;
    }

    @Override
    public String get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (!(key instanceof Integer)) {
            return null;
        }

        if (root == null) {
            return null;
        }

        root = splay(root, (Integer) key);
        return (root.key.equals(key)) ? root.value : null;
    }

    @Override
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (!(key instanceof Integer)) {
            return false;
        }

        if (root == null) {
            return false;
        }

        root = splay(root, (Integer) key);
        return root.key.equals(key);
    }

    @Override
    public boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }

        if (!(value instanceof String)) {
            return false;
        }

        return containsValue(root, (String) value);
    }

    @Override
    public int size() {
        return size(root);
    }

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public Integer firstKey() {
        if (root == null) {
            throw new java.util.NoSuchElementException();
        }
        return min(root).key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) {
            throw new java.util.NoSuchElementException();
        }
        return max(root).key;
    }

    @Override
    public Integer lowerKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            return null;
        }

        root = splay(root, key);
        if (root.key.compareTo(key) < 0) {
            return root.key;
        }

        if (root.left != null) {
            Node node = max(root.left);
            return node.key;
        }

        return null;
    }

    @Override
    public Integer floorKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            return null;
        }

        root = splay(root, key);
        if (root.key.compareTo(key) <= 0) {
            return root.key;
        }

        if (root.left != null) {
            Node node = max(root.left);
            return node.key;
        }

        return null;
    }

    @Override
    public Integer ceilingKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            return null;
        }

        root = splay(root, key);
        if (root.key.compareTo(key) >= 0) {
            return root.key;
        }

        if (root.right != null) {
            Node node = min(root.right);
            return node.key;
        }

        return null;
    }

    @Override
    public Integer higherKey(Integer key) {
        if (key == null) {
            throw new NullPointerException();
        }

        if (root == null) {
            return null;
        }

        root = splay(root, key);
        if (root.key.compareTo(key) > 0) {
            return root.key;
        }

        if (root.right != null) {
            Node node = min(root.right);
            return node.key;
        }

        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) {
            throw new NullPointerException();
        }

        MySplayMap result = new MySplayMap();
        headMap(root, toKey, result);
        return result;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) {
            throw new NullPointerException();
        }

        MySplayMap result = new MySplayMap();
        tailMap(root, fromKey, result);
        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Вспомогательные методы Splay-дерева       ///////
    /////////////////////////////////////////////////////////////////////////

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node != null) {
            inOrderToString(node.left, sb);
            sb.append(node.key).append("=").append(node.value).append(", ");
            inOrderToString(node.right, sb);
        }
    }

    private int size(Node node) {
        return node == null ? 0 : node.size;
    }

    private void updateSize(Node node) {
        if (node != null) {
            node.size = size(node.left) + size(node.right) + 1;
        }
    }

    private Node splay(Node node, Integer key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            if (node.left == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.left.key);
            if (cmp2 < 0) {
                node.left.left = splay(node.left.left, key);
                node = rotateRight(node);
            } else if (cmp2 > 0) {
                node.left.right = splay(node.left.right, key);
                if (node.left.right != null) {
                    node.left = rotateLeft(node.left);
                }
            }
            return (node.left == null) ? node : rotateRight(node);
        } else if (cmp > 0) {
            if (node.right == null) {
                return node;
            }
            int cmp2 = key.compareTo(node.right.key);
            if (cmp2 < 0) {
                node.right.left = splay(node.right.left, key);
                if (node.right.left != null) {
                    node.right = rotateRight(node.right);
                }
            } else if (cmp2 > 0) {
                node.right.right = splay(node.right.right, key);
                node = rotateLeft(node);
            }
            return (node.right == null) ? node : rotateLeft(node);
        } else {
            return node;
        }
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        updateSize(h);
        updateSize(x);
        return x;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        updateSize(h);
        updateSize(x);
        return x;
    }

    private boolean containsValue(Node node, String value) {
        if (node == null) {
            return false;
        }
        if (value.equals(node.value)) {
            return true;
        }
        return containsValue(node.left, value) || containsValue(node.right, value);
    }

    private Node min(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    private Node max(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void headMap(Node node, Integer toKey, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(toKey) < 0) {
            result.put(node.key, node.value);
            headMap(node.right, toKey, result);
        }
        headMap(node.left, toKey, result);
    }

    private void tailMap(Node node, Integer fromKey, MySplayMap result) {
        if (node == null) {
            return;
        }
        if (node.key.compareTo(fromKey) >= 0) {
            result.put(node.key, node.value);
            tailMap(node.left, fromKey, result);
        }
        tailMap(node.right, fromKey, result);
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Остальные методы - заглушки                ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Entry<Integer, String> lowerEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> floorEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> ceilingEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> higherEntry(Integer key) {
        return null;
    }

    @Override
    public Entry<Integer, String> firstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> lastEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollFirstEntry() {
        return null;
    }

    @Override
    public Entry<Integer, String> pollLastEntry() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> descendingMap() {
        return null;
    }

    @Override
    public NavigableSet<Integer> navigableKeySet() {
        return null;
    }

    @Override
    public NavigableSet<Integer> descendingKeySet() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) {
        return null;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        return null;
    }

    @Override
    public NavigableMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        return null;
    }

    @Override
    public void putAll(java.util.Map<? extends Integer, ? extends String> m) {
    }

    @Override
    public java.util.Set<Integer> keySet() {
        return null;
    }

    @Override
    public java.util.Collection<String> values() {
        return null;
    }

    @Override
    public java.util.Set<Entry<Integer, String>> entrySet() {
        return null;
    }
}