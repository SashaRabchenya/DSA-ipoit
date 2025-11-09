package by.it.group410971.rabchenya.lesson10;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Queue;

public class MyPriorityQueue<E> implements Queue<E> {

    private static final int DEFAULT_CAPACITY = 10;
    private Object[] heap;
    private int size;
    private final Comparator<? super E> comparator;

    @SuppressWarnings("unchecked")
    public MyPriorityQueue() {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
        comparator = null;
    }

    @SuppressWarnings("unchecked")
    public MyPriorityQueue(Comparator<? super E> comparator) {
        heap = new Object[DEFAULT_CAPACITY];
        size = 0;
        this.comparator = comparator;
    }

    /////////////////////////////////////////////////////////////////////////
    //////               Обязательные к реализации методы             ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < size; i++) {
            sb.append(heap[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            heap[i] = null;
        }
        size = 0;
    }

    @Override
    public boolean add(E element) {
        return offer(element);
    }

    @Override
    public boolean remove(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) ||
                    (o != null && o.equals(heap[i]))) {
                removeAt(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public E remove() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        return poll();
    }

    @Override
    public boolean contains(Object o) {
        for (int i = 0; i < size; i++) {
            if ((o == null && heap[i] == null) ||
                    (o != null && o.equals(heap[i]))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean offer(E element) {
        if (element == null) {
            throw new NullPointerException();
        }

        ensureCapacity();
        heap[size] = element;
        siftUp(size);
        size++;
        return true;
    }

    @Override
    public E poll() {
        if (size == 0) {
            return null;
        }

        @SuppressWarnings("unchecked")
        E result = (E) heap[0];

        size--;
        heap[0] = heap[size];
        heap[size] = null;

        if (size > 0) {
            siftDown(0);
        }

        return result;
    }

    @Override
    public E peek() {
        if (size == 0) {
            return null;
        }
        @SuppressWarnings("unchecked")
        E result = (E) heap[0];
        return result;
    }

    @Override
    public E element() {
        if (size == 0) {
            throw new java.util.NoSuchElementException();
        }
        @SuppressWarnings("unchecked")
        E result = (E) heap[0];
        return result;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object element : c) {
            if (!contains(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c.isEmpty()) {
            return false;
        }

        for (E element : c) {
            offer(element);
        }
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (c.contains(heap[i])) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        for (int i = size - 1; i >= 0; i--) {
            if (!c.contains(heap[i])) {
                removeAt(i);
                modified = true;
            }
        }
        return modified;
    }

    // Вспомогательные методы для работы с кучей
    @SuppressWarnings("unchecked")
    private void siftUp(int index) {
        E element = (E) heap[index];
        while (index > 0) {
            int parentIndex = (index - 1) >>> 1;
            E parent = (E) heap[parentIndex];

            if (compare(element, parent) >= 0) {
                break;
            }

            heap[index] = parent;
            index = parentIndex;
        }
        heap[index] = element;
    }

    @SuppressWarnings("unchecked")
    private void siftDown(int index) {
        E element = (E) heap[index];
        int half = size >>> 1;

        while (index < half) {
            int childIndex = (index << 1) + 1;
            E child = (E) heap[childIndex];
            int rightIndex = childIndex + 1;

            if (rightIndex < size && compare(child, (E) heap[rightIndex]) > 0) {
                childIndex = rightIndex;
                child = (E) heap[childIndex];
            }

            if (compare(element, child) <= 0) {
                break;
            }

            heap[index] = child;
            index = childIndex;
        }
        heap[index] = element;
    }

    @SuppressWarnings("unchecked")
    private int compare(E a, E b) {
        if (comparator != null) {
            return comparator.compare(a, b);
        } else {
            Comparable<? super E> comparable = (Comparable<? super E>) a;
            return comparable.compareTo(b);
        }
    }

    private void ensureCapacity() {
        if (size == heap.length) {
            int newCapacity = heap.length * 2;
            Object[] newHeap = new Object[newCapacity];
            System.arraycopy(heap, 0, newHeap, 0, size);
            heap = newHeap;
        }
    }

    private void removeAt(int index) {
        size--;
        if (index == size) {
            heap[index] = null;
        } else {
            heap[index] = heap[size];
            heap[size] = null;
            siftDown(index);
            if (heap[index] == heap[size]) {
                siftUp(index);
            }
        }
    }

    /////////////////////////////////////////////////////////////////////////
    //////        Остальные методы - необязательные к реализации     ///////
    /////////////////////////////////////////////////////////////////////////

    @Override
    public Iterator<E> iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[size];
        System.arraycopy(heap, 0, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            return (T[]) toArray();
        }
        System.arraycopy(heap, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    // Эти методы не реализованы, так как не требуются по заданию
    // но они должны быть объявлены для реализации интерфейса Queue

    // Остальные методы интерфейса Collection и Queue
    // (не требуются по заданию, но должны быть объявлены)
}