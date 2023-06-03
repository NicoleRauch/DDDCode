package com.example.ddd_es.lager.applicationLoesung1;

import com.example.ddd_es.lager.domainLoesung1.Event;
import com.example.ddd_es.lager.domainLoesung1.Repository;
import com.google.common.collect.ImmutableList;

public class RepositoryImpl implements Repository {

    private ImmutableList<Event> events = ImmutableList.<Event>builder().build();

    public void storeEvent(Event event){
        events = ImmutableList.<Event>builder().addAll(events).add(event).build();
    }

    public ImmutableList<Event> events(){
        return events;
    }
}
