package com.example.ddd_es_2.lager.applicationLoesung;

import com.example.ddd_es_1.lager.domainLoesung1.Event;
import com.example.ddd_es_1.lager.domainLoesung1.Repository;
import com.google.common.collect.ImmutableList;
import org.zeromq.ZMQ;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class RepositoryImpl implements Repository {

    private final ZMQ.Socket publisher;
    private ImmutableList<Event> events = ImmutableList.<Event>builder().build();

    public RepositoryImpl(ZMQ.Socket publisher) {
        this.publisher = publisher;
    }

    public void storeEvent(Event event){
        events = ImmutableList.<Event>builder().addAll(events).add(event).build();

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject( event );
            oos.close();
            publisher.send(Base64.getEncoder().encodeToString(baos.toByteArray()).getBytes(ZMQ.CHARSET), 0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImmutableList<Event> events(){
        return events;
    }
}
