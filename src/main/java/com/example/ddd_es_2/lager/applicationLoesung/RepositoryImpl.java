package com.example.ddd_es_2.lager.applicationLoesung;

import com.example.ddd_es_1.lager.domainLoesung1.Event;
import com.example.ddd_es_1.lager.domainLoesung1.Repository;
import com.google.common.collect.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zeromq.ZMQ;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class RepositoryImpl implements Repository {

    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryImpl.class);

    private final ZMQ.Socket socket;
    private ImmutableList<Event> events = ImmutableList.<Event>builder().build();

    public RepositoryImpl(ZMQ.Socket socket) {
        this.socket = socket;
    }

    public void storeEvent(Event event){
        events = ImmutableList.<Event>builder().addAll(events).add(event).build();

        byte[] reply = socket.recv(0);
        LOGGER.info(
                "Received " + ": [" + new String(reply, ZMQ.CHARSET) + "]"
        );

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject( event );
            oos.close();
            LOGGER.info("Sending event");
            socket.send(Base64.getEncoder()
                            .encodeToString(baos.toByteArray())
                            .getBytes(ZMQ.CHARSET),
                    0);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ImmutableList<Event> events(){
        return events;
    }
}
