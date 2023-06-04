package com.example.ddd_es_1.lager.domainLoesung2;

import com.example.ddd.lager.domain.ArtikelId;
import com.example.ddd.lager.domain.LagerplatzId;
import com.example.ddd_es_1.lager.domainLoesung1.*;
import com.google.common.collect.ImmutableMap;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

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

    public ArtikelBestand getBestandFuer(ArtikelId artikel) {
        return bestand.get(artikel);
    }

    public static class ArtikelBestand {
        private ImmutableMap<LagerplatzId, Integer> artikelBestand = ImmutableMap.<LagerplatzId, Integer>builder().build();

        private void aktualisiereBestand(LagerplatzId lagerplatz, Integer bestandsAenderung) {
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

        public LagerplatzId kleinsterLagerplatzMitMindestens(int anzahl){
            Comparator<Map.Entry<LagerplatzId, Integer>> comparator = new Comparator<>() {
                @Override
                public int compare(Map.Entry<LagerplatzId, Integer> o1, Map.Entry<LagerplatzId, Integer> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            };

            return artikelBestand.entrySet().stream().toList().stream()
                    .filter((Map.Entry<LagerplatzId, Integer> entry) -> entry.getValue() >= anzahl)
                    .min(comparator).orElseThrow()
                    .getKey();
        }

        public String toString() {
            return artikelBestand.toString();
        }

    }

}
