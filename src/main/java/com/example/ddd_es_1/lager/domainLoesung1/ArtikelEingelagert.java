package com.example.ddd_es_1.lager.domainLoesung1;


import com.example.ddd.lager.domain.ArtikelId;
import com.example.ddd.lager.domain.LagerplatzId;

public record ArtikelEingelagert(ArtikelId artikel, int anzahl, LagerplatzId lagerplatz) implements Event {
}
