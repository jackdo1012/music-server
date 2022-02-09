package com.music.server.utils;

import java.util.List;

public interface Queue<T> {
    T peek();

    void enqueue(T data);

    void dequeue();

    boolean isEmpty();

    int size();

    void removeAt(int index);

    void clear();

    void set(Queue<T> datas);

    List<T> toArray();
}
