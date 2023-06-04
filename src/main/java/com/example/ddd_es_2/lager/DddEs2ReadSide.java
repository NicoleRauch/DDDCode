package com.example.ddd_es_2.lager;

import com.example.ddd_es_1.lager.domainLoesung1.Event;
import com.example.ddd_es_1.lager.domainLoesung1.LagerplatzProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Base64;

public class DddEs2ReadSide extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(DddEs2ReadSide.class);

    public void run() {
        LOGGER.info("Read-Side running");

        LagerplatzProjection lagerplatzProjection = new LagerplatzProjection();

        try (ZContext context = new ZContext()) {
            //  Socket to talk to server
            ZMQ.Socket subscriber = context.createSocket(SocketType.SUB);
            subscriber.connect("tcp://localhost:5556");

            subscriber.subscribe("".getBytes(ZMQ.CHARSET));

            while (true) {
                String eventString = subscriber.recvStr(0).trim();
                try {
                    ObjectInputStream ois = new ObjectInputStream(
                            new ByteArrayInputStream(Base64.getDecoder().decode(eventString)));
                    Event event = (Event) ois.readObject();
                    ois.close();
                    lagerplatzProjection.project(event);
                    LOGGER.info("Lagerplatz-Bestand:" + lagerplatzProjection.toString());

                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
