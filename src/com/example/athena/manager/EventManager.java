package com.example.athena.manager;

import com.example.athena.manager.events.Event;
import com.example.athena.manager.events.EventListener;
import com.example.athena.manager.events.EventListenerManager;
import com.example.athena.manager.events.EventType;

public class EventManager {

    private final EventListenerManager listenerManager = new EventListenerManager();

    public void register(EventType type, EventListener listener) {
        this.listenerManager.register(type, listener);
    }

    public void unregister(EventType type, EventListener listener) {
        this.listenerManager.unregister(type, listener);
    }

    public void submit(Event event) {
        if (event == null) {
            throw new NullPointerException("When submitting an event, the given event must not be null.");
        }
        listenerManager.getEventListeners(event.getType()).forEach(l -> l.executeEvent(event));
    }

}