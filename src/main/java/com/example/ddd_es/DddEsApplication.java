package com.example.ddd_es;

import com.example.ddd.lager.domain.ArtikelId;
import com.example.ddd.lager.domain.KundenId;
import com.example.ddd.lager.domain.LagerplatzId;
import com.example.ddd_es.lager.domainLoesung1.LagerplatzProjection;
import com.example.ddd_es.lager.applicationLoesung1.RepositoryImpl;
import com.example.ddd_es.lager.domainLoesung1.*;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DddEsApplication {

	private static final Logger LOGGER=LoggerFactory.getLogger(DddEsApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DddEsApplication.class, args);
	}

	private final RepositoryImpl repository = new RepositoryImpl();

	@PostConstruct
	public void init(){
		ArtikelId artikel1 = new ArtikelId("Artikel1");
		ArtikelId artikel2 = new ArtikelId("Artikel2");

		LagerplatzId lagerplatz1 = new LagerplatzId("Lagerplatz1");
		LagerplatzId lagerplatz2 = new LagerplatzId("Lagerplatz2");
		LagerplatzId lagerplatz3 = new LagerplatzId("Lagerplatz3");

		KundenId kundenId = new KundenId("Kunde");

		repository.storeEvent(new ArtikelEingelagert(artikel1, 7, lagerplatz1)); // L1: A1: 7
		repository.storeEvent(new ArtikelEingelagert(artikel2, 5, lagerplatz2)); // L1: A1: 7; L2: A2: 5
		repository.storeEvent(new ArtikelUmgelagert(artikel1, 4, lagerplatz1, lagerplatz3)); // L1: A1: 3; L2: A2: 5; L3: A1: 4
		repository.storeEvent(new ArtikelVerkauft(artikel2, 2, lagerplatz2, kundenId)); // L1: A1: 3; L2: A2: 3; L3: A1: 4

		LagerplatzProjection lagerplatzProjection = new LagerplatzProjection();
		repository.events().forEach(lagerplatzProjection::project);

		LOGGER.info("Lagerplatz-Bestand:" + lagerplatzProjection.toString());
	}
}
