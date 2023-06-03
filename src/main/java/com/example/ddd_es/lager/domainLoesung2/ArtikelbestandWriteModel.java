package com.example.ddd_es.lager.domainLoesung2;

import com.example.ddd.lager.domain.ArtikelId;
import com.example.ddd.lager.domain.LagerplatzId;
import com.example.ddd_es.lager.domainLoesung1.*;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;

public class ArtikelbestandWriteModel {

    private ImmutableMap<ArtikelId, ArtikelBestand> bestand = ImmutableMap.<ArtikelId, ArtikelBestand>builder().build();

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
        ArtikelBestand artikelBestand = bestand.get(artikel);
        ArtikelBestand neuerBestand = artikelBestand == null ? new ArtikelBestand() : artikelBestand;
        neuerBestand.aktualisiereBestand(lagerplatz, anzahl);
        bestand = ImmutableMap.<ArtikelId, ArtikelBestand>builder()
                .putAll(new HashMap<>() {{
                    putAll(bestand);
                    put(artikel, neuerBestand);
                }})
                .build();
    }

    private static class ArtikelBestand {
        private ImmutableMap<LagerplatzId, Integer> artikelBestand = ImmutableMap.<LagerplatzId, Integer>builder().build();

        public void aktualisiereBestand(LagerplatzId lagerplatz, Integer bestandsAenderung) {
            if (bestandsAenderung == null || bestandsAenderung == 0) {
                return;
            }
            Integer aktuellerBestand = artikelBestand.get(lagerplatz);
            int neuerBestand = (aktuellerBestand != null ? aktuellerBestand : 0) + bestandsAenderung;
            artikelBestand = ImmutableMap.<LagerplatzId, Integer>builder()
                    .putAll(new HashMap<>() {{
                        putAll(artikelBestand);
                        put(lagerplatz, neuerBestand);
                    }})
                    .build();
        }

        public String toString() {
            return artikelBestand.toString();
        }

    }

}
