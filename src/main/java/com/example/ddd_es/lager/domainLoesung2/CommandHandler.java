package com.example.ddd_es.lager.domainLoesung2;

import com.example.ddd.lager.domain.LagerplatzId;
import com.example.ddd_es.lager.domainLoesung1.ArtikelVerkauft;
import com.example.ddd_es.lager.domainLoesung1.Event;
import com.example.ddd_es.lager.domainLoesung1.Repository;

public class CommandHandler {

    private final Repository repository;
    private final ArtikelbestandWriteModel artikelbestandWriteModel = new ArtikelbestandWriteModel();

    public CommandHandler(Repository repository){
        this.repository = repository;
        repository.events().forEach(artikelbestandWriteModel::project);
    }

    public void handleCommand(Command command){
        if(command instanceof VerkaufeArtikel verkaufe){
            ArtikelbestandWriteModel.ArtikelBestand bestand = artikelbestandWriteModel.getBestandFuer(verkaufe.artikel());
            LagerplatzId lagerplatz = bestand.kleinsterLagerplatzMitMindestens(verkaufe.anzahl());
            Event event = new ArtikelVerkauft(verkaufe.artikel(), verkaufe.anzahl(), lagerplatz, verkaufe.kunde());
            repository.storeEvent(event); // zuerst das Speichern sicherstellen!
            artikelbestandWriteModel.project(event);
        }
    }
}
