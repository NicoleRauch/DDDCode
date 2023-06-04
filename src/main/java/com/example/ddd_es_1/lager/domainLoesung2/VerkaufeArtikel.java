package com.example.ddd_es_1.lager.domainLoesung2;

import com.example.ddd.lager.domain.ArtikelId;
import com.example.ddd.lager.domain.KundenId;

public record VerkaufeArtikel(ArtikelId artikel, int anzahl, KundenId kunde) implements Command {
}
