package com.example.ddd_es.lager.applicationLoesung1;

import com.example.ddd_es.lager.domainLoesung1.Event;

import java.util.ArrayList;
import java.util.List;

public class RepositoryImpl {

    private List<Event> events = new ArrayList<>();

    public void storeEvent(Event event){
        events.add(event);
    }

    public List<Event> events(){
        return events;
    }
}
