package com.wedding.util;

import com.wedding.model.Invitato;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OrdinamentoPerCognome implements OrdinamentoStrategy {
    @Override
    public List<Invitato> ordina(List<Invitato> listaNonOrdinata){
        return listaNonOrdinata.stream()
                .sorted(Comparator.comparing(Invitato::getCognome))
                .collect(Collectors.toList());
    }
}
