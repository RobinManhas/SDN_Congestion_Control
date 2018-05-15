package net.floodlightcontroller.project;

import java.util.NoSuchElementException;

public class CircularBuffer<T> {
    private T[] data;
    // indices for inserting and removing from queue
    private int front = 0;
    private int insertLocation = 0;
    private int size = 0; // number of elements in queue

    public CircularBuffer(int bufferSize) {
        data = (T[]) new Object[bufferSize];
    }

    public synchronized void insert(T item) {
        data[insertLocation] = item;
        insertLocation = (insertLocation + 1) % data.length;
        // increment front on overwrite
        if (size == data.length) {
            front = (front + 1) % data.length;
        } else {
            size++;
        }
    }

    public synchronized int size() {
        return size;
    }

    public synchronized T removeFront() {
        if (size == 0) {
            throw new NoSuchElementException();
        }
        T retValue = data[front];
        front = (front + 1) % data.length;
        size--;
        return retValue;
    }

    public synchronized T peekFront() {
        if (size == 0) {
            return null;
        } else {
            return data[front];
        }
    }

    public synchronized T peekLast() {
        if (size == 0) {
            return null;
        } else {
            int lastElement = insertLocation - 1;
            if (lastElement < 0) {
                lastElement = data.length - 1;
            }
            return data[lastElement];
        }
    }
}
