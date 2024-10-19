package ru.mpei;

import java.util.Deque;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class TripletDeque<T> implements Deque<T>, Containerable {

    private ElementWrap<T> first = null;
    private ElementWrap<T> last = null;
    private int currentSize = 0;
    private int maxSize = 1000;
    private int maxContainerSize = 5;

    public TripletDeque(int maxSize, int maxContainerSize) {
        this.maxSize = maxSize;
        this.maxContainerSize = maxContainerSize;
    }

    @Override
    public void addFirst(T t) throws IllegalStateException {
        if (t == null) {
            throw new NullPointerException("Cannot add null element");
        }

        if (this.first == null) {
            currentSize++;
            this.first = new ElementWrap<>(maxContainerSize);
            this.first.setValueContainer(t, 0);
            this.last = this.first;
        }
        else {
            int flag = isEmptyPositionFirst(t);
            if (flag == -1 && currentSize < maxSize) {
                ElementWrap<T> newEl = new ElementWrap<>(maxContainerSize);
                newEl.setValueContainer(t, 0);
                this.first.setPrev(newEl);
                newEl.setNext(this.first);
                this.first = newEl;
                currentSize ++;
            } else if (flag != -1) {
                for (int i = flag; i > 0; i--) {
                    this.first.setValueContainer(this.first.getValueContainer(i - 1), i);
                }
                this.first.setValueContainer(t, 0);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public void addLast(T t) {
        if (t == null) {
            throw new NullPointerException("Cannot add null element");
        }

        if (this.last == null) {
            currentSize++;
            this.last = new ElementWrap<>(maxContainerSize);
            this.last.setValueContainer(t, this.last.getDefaultSize() - 1);
            this.first = this.last;
        } else {
            int flag = isEmptyPositionLast(t);
            if (flag == -1 && currentSize < maxSize) {
                currentSize++;
                ElementWrap<T> newEl = new ElementWrap<>(maxContainerSize);
                newEl.setValueContainer(t, newEl.getDefaultSize() - 1);
                this.last.setNext(newEl);
                newEl.setPrev(this.last);
                this.last = newEl;
            } else if (flag != -1) {
                for (int i = flag; i < this.last.getDefaultSize() - 1; i++) {
                    this.last.setValueContainer(this.last.getValueContainer(i + 1), i);
                }
                this.last.setValueContainer(t, this.last.getDefaultSize() - 1);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    @Override
    public boolean offerFirst(T t) {
        try {
            this.addFirst(t);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public boolean offerLast(T t) {
        try {
            this.addLast(t);
            return true;
        } catch (IllegalStateException e) {
            return false;
        }
    }

    @Override
    public T removeFirst() throws NoSuchElementException {
        if (this.first == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        ElementWrap<T> bufferFirst = this.first;

        int firstNonEmptyIndex = -1;
        for (int i = 0; i < bufferFirst.getDefaultSize(); i++) {
            if (bufferFirst.getValueContainer(i) != null) {
                firstNonEmptyIndex = i;
                break;
            }
        }

        if (firstNonEmptyIndex == -1) {
            throw new NoSuchElementException("No non-empty element found in the first container");
        }

        T removedValue = bufferFirst.getValueContainer(firstNonEmptyIndex);

        for (int i = firstNonEmptyIndex; i < bufferFirst.getDefaultSize() - 1; i++) {
            bufferFirst.setValueContainer(bufferFirst.getValueContainer(i + 1), i);
        }
        bufferFirst.setValueContainer(null, bufferFirst.getDefaultSize() - 1);

        if (isContainerEmpty(bufferFirst)) {
            this.first = this.first.getNext();
            if (this.first != null) {
                this.first.setPrev(null);
            } else {
                this.last = null;
            }
        }
        currentSize--;
        return removedValue;
    }

    @Override
    public T removeLast() {
        if (this.last == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        ElementWrap<T> bufferLast = this.last;

        int lastNonEmptyIndex = -1;
        for (int i = bufferLast.getDefaultSize() - 1; i >= 0; i--) {
            if (bufferLast.getValueContainer(i) != null) {
                lastNonEmptyIndex = i;
                break;
            }
        }

        if (lastNonEmptyIndex == -1) {
            throw new NoSuchElementException("No non-empty element found in the last container");
        }

        T removedValue = bufferLast.getValueContainer(lastNonEmptyIndex);

        for (int i = lastNonEmptyIndex; i > 0; i--) {
            bufferLast.setValueContainer(bufferLast.getValueContainer(i - 1), i);
        }
        bufferLast.setValueContainer(null, 0);

        if (isContainerEmpty(bufferLast)) {
            this.last = this.last.getPrev();
            if (this.last != null) {
                this.last.setNext(null);
            } else {
                this.first = null;
            }
        }

        currentSize--;
        return removedValue;
    }

    private boolean isContainerEmpty(ElementWrap<T> container) {
        for (int i = 0; i < container.getDefaultSize(); i++) {
            if (container.getValueContainer(i) != null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public T pollFirst() {
        try {
            return this.removeFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public T pollLast() {
        try {
            return this.removeLast();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public T getFirst() throws NoSuchElementException {
        if (this.first != null) {
            return this.first.getValueContainer(0);
        }
        throw new NoSuchElementException("This item is not exist");
    }

    @Override
    public T getLast() throws NoSuchElementException {
        if (this.last == null) {
            throw new NoSuchElementException("Deque is empty");
        }
        for (int i = this.last.getDefaultSize() - 1; i >= 0; i--) {
            if (this.last.getValueContainer(i) != null) {
                return this.last.getValueContainer(i);
            }
        }
        throw new NoSuchElementException("This item is not exist");
    }

    @Override
    public T peekFirst() {
        try {
            return this.getFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public T peekLast() {
        try {
            return this.getLast();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        ElementWrap<T> current = first;
        while (current != null) {
            for (int i = 0; i < current.getDefaultSize(); i++) {
                if (current.getValueContainer(i) != null && current.getValueContainer(i).equals(o)) {
                    // Сдвигаем элементы влево, начиная с найденного элемента
                    for (int j = i; j < current.getDefaultSize() - 1; j++) {
                        current.setValueContainer(current.getValueContainer(j + 1), j);
                    }
                    current.setValueContainer(null, current.getDefaultSize() - 1); // Очищаем последний элемент

                    // Проверяем, стал ли контейнер пустым
                    if (isContainerEmpty(current)) {
                        if (current == first) {
                            first = current.getNext();
                            if (first != null) {
                                first.setPrev(null);
                            } else {
                                last = null;
                            }
                        } else if (current == last) {
                            last = current.getPrev();
                            if (last != null) {
                                last.setNext(null);
                            } else {
                                first = null;
                            }
                        } else {
                            current.getPrev().setNext(current.getNext());
                            current.getNext().setPrev(current.getPrev());
                            current.setNext(null);
                            current.setPrev(null);
                            current.setValue(null);
                        }
                        currentSize--;
                    }
                    return true;
                }
            }
            current = current.getNext();
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        ElementWrap<T> current = last;
        while (current != null) {
            for (int j = current.getDefaultSize() - 1; j >= 0; j--) {
                if (current.getValueContainer(j) != null && current.getValueContainer(j).equals(o)) {
                    current.setValueContainer(null, j);
                    return true;
                }
            }
            current = current.getPrev();
        }
        return false;
    }

    @Override
    public boolean add(T t) {
        return offerFirst(t);
    }

    @Override
    public boolean offer(T t) {
        return this.offerFirst(t);
    }

    @Override
    public T remove() {
        return this.removeFirst();
    }

    @Override
    public T poll() {
        return this.pollFirst();
    }

    @Override
    public T element() {
        return this.getFirst();
    }

    @Override
    public T peek() {
        return this.peekFirst();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if (c.size() <= (maxSize - currentSize) * maxContainerSize) {
            for (T t : c) {
                this.offerFirst(t);
            }
            return true;
        }
        return false;
    }

    @Override
    public void push(T t) {
        this.addFirst(t);
    }

    @Override
    public T pop() {
        return this.removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        return this.removeFirstOccurrence(o);
    }

    @Override
    public boolean contains(Object o) {
        if (this.first != null) {
            ElementWrap<T> currentEl = this.last;
            for (int i = 0; i < this.currentSize; i++) {
                for (int j = 0; j < currentEl.getDefaultSize(); j++) {
                    if (currentEl.getValueContainer(j) != null) {
                        if (currentEl.getValueContainer(j).equals(o)) {
                            return true;
                        }
                    }
                }
                if (currentEl.getNext() == this.last) {
                    break;
                }
            }
        }
        return false;
    }

    @Override
    public int size() {
        return currentSize;
    }

    @Override
    public Iterator<T> iterator() {
        return new DeqIterator<>(first);
    }

    @Override
    public Iterator<T> descendingIterator() {
        throw new UnsupportedOperationException("It's not realised");
    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    @Override
    public Object[] toArray() {
        Object[] result = new Object[currentSize];
        int index = 0;
        ElementWrap<T> current = first;
        while (current != null) {
            for (int i = 0; i < current.getDefaultSize(); i++) {
                if (current.getValueContainer(i) != null) {
                    result[index++] = current.getValueContainer(i);
                }
            }
            current = current.getNext();
        }
        return result;
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return null;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        first = null;
        last = null;
        currentSize = 0;
    }

    private int isEmptyPositionFirst(T t) {
        for (int i = 0; i < maxContainerSize; i++) {
            if (first.getValueContainer(i) == null) {
                return i;
            }
        }
        return -1;
    }

    private int isEmptyPositionLast(T t) {
        for (int i = (maxContainerSize - 1); i >= 0; i--) {
            if (last.getValueContainer(i) == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getContainerByIndex(int cIndex) {
        ElementWrap<T> current = first;
        for (int i = 0; i < cIndex; i++) {
            if (current == null) {
                return new Object[0];
            }
            current = current.getNext();
        }
        if (current == null) {
            return null;
        }

        return current.getValueContainer();
    }

}