package ru.mpei;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class DeqIterator<T> implements Iterator<T> {
    private ElementWrap<T> currentWrap;
    private int index;

    public DeqIterator(ElementWrap<T> currentWrap) {
        this.currentWrap = currentWrap;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return currentWrap != null && (index < currentWrap.getDefaultSize() || currentWrap.getNext() != null);
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("There has no elements or deque has ended");
        }
        if (index >= currentWrap.getDefaultSize()) {
            currentWrap = currentWrap.getNext();
            index = 0;
        }
        return currentWrap.getValueContainer(index++);
    }
}