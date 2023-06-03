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

    private static class LagerplatzBestand {
        private ImmutableMap<ArtikelId, Integer> lagerplatzBestand = ImmutableMap.<ArtikelId, Integer>builder().build();

        private void aktualisiereBestand(ArtikelId artikel, Integer bestandsAenderung) {
            if (bestandsAenderung == null || bestandsAenderung == 0) {
                return;
            }
            Integer aktuellerBestand = lagerplatzBestand.get(artikel);
            int neuerBestand = (aktuellerBestand != null ? aktuellerBestand : 0) + bestandsAenderung;
            lagerplatzBestand = ImmutableMap.<ArtikelId, Integer>builder()
                    .putAll(new HashMap<>() {{
                        putAll(lagerplatzBestand);
                        put(artikel, neuerBestand);
                    }})
                    .build();
        }

        public String toString() {
            return lagerplatzBestand.toString();
        }
    }

}

