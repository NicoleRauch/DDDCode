package com.example.ddd_es.lager.domainLoesung1;

import com.example.ddd.lager.domain.ArtikelId;
import com.example.ddd.lager.domain.LagerplatzId;

import com.google.common.collect.ImmutableMap;

import java.util.HashMap;

public class LagerplatzProjection {

    private ImmutableMap<LagerplatzId, LagerplatzBestand> bestand = ImmutableMap.<LagerplatzId, LagerplatzBestand>builder().build();

    public void project(Event event) {
        if (event instanceof ArtikelEingelagert eingelagert) {
            aktualisiereBestand(eingelagert.lagerplatz(), eingelagert.anzahl(), eingelagert.artikel());
        }
        if (event instanceof ArtikelUmgelagert umgelagert) {
            aktualisiereBestand(umgelagert.quellLagerplatz(), -umgelagert.anzahl(), umgelagert.artikel());
            aktualisiereBestand(umgelagert.zielLagerplatz(), umgelagert.anzahl(), umgelagert.artikel());
        }
        if (event instanceof ArtikelVerkauft verkauft) {
            aktualisiereBestand(verkauft.lagerplatz(), -verkauft.anzahl(), verkauft.artikel());
        }
    }

    private void aktualisiereBestand(LagerplatzId lagerplatz, int anzahl, ArtikelId artikel) {
        LagerplatzBestand lagerplatzBestand = bestand.get(lagerplatz);
        LagerplatzBestand neuerBestand = lagerplatzBestand == null ? new LagerplatzBestand() : lagerplatzBestand;
        neuerBestand.aktualisiereBestand(artikel, anzahl);
        bestand = ImmutableMap.<LagerplatzId, LagerplatzBestand>builder()
                .putAll(new HashMap<>() {{
                    putAll(bestand);
                    put(lagerplatz, neuerBestand);
                }})
                .build();
    }

    public String toString() {
        return bestand.toString();
    }
}

