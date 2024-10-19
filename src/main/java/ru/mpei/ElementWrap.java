package ru.mpei;

public class ElementWrap<E> {
    private ElementWrap<E> next;
    private ElementWrap<E> prev;
    private int defaultSize = 5;
    private E[] valueContainer = (E[]) new Object[defaultSize];

    public void setValue(E[] value) {
        this.valueContainer = value;
    }

    public E[] getValueContainer() {
        return valueContainer;
    }

    public ElementWrap(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public int getDefaultSize() {
        return defaultSize;
    }

    public E getValueContainer(int i) {
        return valueContainer[i];
    }

    public void setValueContainer(E value, int i) {
        this.valueContainer[i] = value;
    }

    public ElementWrap<E> getNext() {
        return next;
    }

    public ElementWrap<E> getPrev() {
        return prev;
    }

    public void setNext(ElementWrap<E> next) {
        this.next = next;
    }

    public void setPrev(ElementWrap<E> prev) {
        this.prev = prev;
    }
}