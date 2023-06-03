package com.example.ddd_es.lager.domainLoesung1;

import com.example.ddd_es.lager.domainLoesung1.Event;
import com.google.common.collect.ImmutableList;

public interface Repository {

    void storeEvent(Event event);

    public ImmutableList<Event> events();
}
