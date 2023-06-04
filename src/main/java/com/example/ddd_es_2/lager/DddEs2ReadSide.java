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
            ZMQ.Socket socket = context.createSocket(SocketType.REQ);
            socket.connect("tcp://localhost:5556");

            for (int requestNbr = 0; requestNbr != 10; requestNbr++) {
                String request = "Hello";
                LOGGER.info("Sending request " + requestNbr);
                socket.send(request.getBytes(ZMQ.CHARSET), 0);

                byte[] reply = socket.recv(0);
                LOGGER.info("Received " + requestNbr);

                try {
                    ObjectInputStream ois = new ObjectInputStream(
                            new ByteArrayInputStream(Base64.getDecoder()
                                    .decode(new String(reply, ZMQ.CHARSET))));

                    Event event = (Event) ois.readObject();
                    ois.close();
                    lagerplatzProjection.project(event);
                    LOGGER.info("Lagerplatz-Bestand:" + lagerplatzProjection.toString());

                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } // end of ZeroMQ context
    }
}

