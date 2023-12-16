package com.example.socialnetwork.java.ir.map.utils.observer;

import com.example.socialnetwork.java.ir.map.utils.events.Event;

public interface Observer<E extends Event> {
    void update(E e);
}
