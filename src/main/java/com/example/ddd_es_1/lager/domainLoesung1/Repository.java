package com.example.ddd_es_1.lager.domainLoesung1;

import com.google.common.collect.ImmutableList;

public interface Repository {

    void storeEvent(Event event);

    public ImmutableList<Event> events();
}
