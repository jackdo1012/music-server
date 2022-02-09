package com.music.server.utils;

import java.util.ArrayList;
import java.util.List;

public class QueueImpl<T> implements Queue<T> {
    List<T> arr = new ArrayList<T>();

    public QueueImpl() {
    }

    @Override
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        return arr.get(0);
    }

    @Override
    public void enqueue(T data) {
        arr.add(data);
    }

    @Override
    public void dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Queue is empty");
        }
        arr.remove(0);
    }

    @Override
    public boolean isEmpty() {
        return arr.isEmpty();
    }

    @Override
    public int size() {
        return arr.size();
    }

    @Override
    public void removeAt(int index) {
        arr.remove(index);
    }

    @Override
    public void clear() {
        arr.clear();
    }

    @Override
    public void set(Queue<T> datas) {
        arr = datas.toArray();
    }

    @Override
    public List<T> toArray() {
        return arr;
    }

    @Override
    public String toString() {
        return "QueueImpl{" +
                arr +
                '}';
    }
}
