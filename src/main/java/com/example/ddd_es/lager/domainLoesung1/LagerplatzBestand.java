package com.example.ddd_es.lager.domainLoesung1;

import com.example.ddd.lager.domain.ArtikelId;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;

public class LagerplatzBestand {
    private ImmutableMap<ArtikelId, Integer> lagerplatzBestand = ImmutableMap.<ArtikelId, Integer>builder().build();

    public void aktualisiereBestand(ArtikelId artikel, Integer bestandsAenderung) {
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
