package com.example.ddd_es.lager.domainLoesung2;

import com.example.ddd_es.lager.domainLoesung1.Repository;

public class CommandHandler {

    private final Repository repository;

    public CommandHandler(Repository repository){
        this.repository = repository;
    }

    public void handleCommand(Command command){
        if(command instanceof VerkaufeArtikel verkaufe){

        }
    }
}
