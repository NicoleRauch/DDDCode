package com.example.ddd_es_2.lager;

import com.example.ddd.lager.domain.ArtikelId;
import com.example.ddd.lager.domain.KundenId;
import com.example.ddd.lager.domain.LagerplatzId;
import com.example.ddd_es_1.lager.domainLoesung1.*;
import com.example.ddd_es_1.lager.domainLoesung2.CommandHandler;
import com.example.ddd_es_1.lager.domainLoesung2.VerkaufeArtikel;
import com.example.ddd_es_2.lager.applicationLoesung.RepositoryImpl;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

@SpringBootApplication
public class DddEs2Application {

	private static final Logger LOGGER=LoggerFactory.getLogger(DddEs2Application.class);

	public static void main(String[] args) {
		SpringApplication.run(DddEs2Application.class, args);
	}

	private Repository repository = null;

	@PostConstruct
	public void init() throws InterruptedException {

		//  Prepare our context and publisher
		try (ZContext context = new ZContext()) {
			ZMQ.Socket publisher = context.createSocket(SocketType.PUB);
			publisher.bind("tcp://*:5556");
			repository = new RepositoryImpl(publisher);
		}

		Thread readSide = new DddEs2ReadSide();
		readSide.start();
		Thread.sleep(1000); // nur zu Demo-Zwecken!!

		ArtikelId artikel1 = new ArtikelId("Artikel1");
		ArtikelId artikel2 = new ArtikelId("Artikel2");

		LagerplatzId lagerplatz1 = new LagerplatzId("Lagerplatz1");
		LagerplatzId lagerplatz2 = new LagerplatzId("Lagerplatz2");
		LagerplatzId lagerplatz3 = new LagerplatzId("Lagerplatz3");

		KundenId kundenId = new KundenId("Kunde");

		repository.storeEvent(new ArtikelEingelagert(artikel1, 7, lagerplatz1)); // L1: A1: 7
		/*
		repository.storeEvent(new ArtikelEingelagert(artikel2, 5, lagerplatz2)); // L1: A1: 7; L2: A2: 5
		repository.storeEvent(new ArtikelUmgelagert(artikel1, 4, lagerplatz1, lagerplatz3)); // L1: A1: 3; L2: A2: 5; L3: A1: 4
		repository.storeEvent(new ArtikelVerkauft(artikel2, 2, lagerplatz2, kundenId)); // L1: A1: 3; L2: A2: 3; L3: A1: 4

		CommandHandler handler = new CommandHandler(repository);
		handler.handleCommand(new VerkaufeArtikel(artikel1, 4, kundenId));

		try {
			handler.handleCommand(new VerkaufeArtikel(artikel1, 4, kundenId));
		} catch(Exception e){
			LOGGER.info("Exception: " + e.getMessage());
		}
		*/
	}
}
