package com.example.ddd_es_1.lager.domainLoesung1;


import com.example.ddd.lager.domain.ArtikelId;
import com.example.ddd.lager.domain.KundenId;
import com.example.ddd.lager.domain.LagerplatzId;

public record ArtikelVerkauft(ArtikelId artikel, int anzahl, LagerplatzId lagerplatz, KundenId kunde) implements Event {
}
