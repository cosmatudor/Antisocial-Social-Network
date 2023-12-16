package com.example.socialnetwork.java.ir.map.utils.observer;

import com.example.socialnetwork.java.ir.map.utils.events.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);
    void removeObserver(Observer<E> e);
    void notifyObservers(E t);
}
