package ru.mpei;

import ru.mpei.ElementWrap;

import java.util.Iterator;
import java.util.NoSuchElementException;

class DeqIterator<T> implements Iterator<T> {
    private ElementWrap<T> currentWrap;
    private int index;

    public DeqIterator(ElementWrap<T> first) {
        this.currentWrap = first;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return currentWrap != null && (index < currentWrap.getDefaultSize() || currentWrap.getNext() != null);
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        if (index >= currentWrap.getDefaultSize()) {
            currentWrap = currentWrap.getNext();
            index = 0;
        }
        return currentWrap.getValueContainer(index++);
    }
}