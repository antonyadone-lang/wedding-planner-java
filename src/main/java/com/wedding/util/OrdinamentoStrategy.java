package com.wedding.util;

import com.wedding.model.Invitato;

import java.util.List;
public interface OrdinamentoStrategy {
    List<Invitato> ordina(List<Invitato> listaNonOrdinata);
}
