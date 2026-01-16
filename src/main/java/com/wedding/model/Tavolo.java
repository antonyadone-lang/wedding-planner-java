package com.wedding.model;

import java.util.ArrayList;

public class Tavolo {
    private final int numeroTavolo;
    private final int capacitaMassima;
    private final ArrayList<Invitato> listaOspiti;

    public Tavolo(int numeroTavolo, int capacitaMassima) {
        this.numeroTavolo = numeroTavolo;
        this.capacitaMassima = capacitaMassima;
        this.listaOspiti = new ArrayList<>();
    }

    public boolean aggiungiOspite(Invitato invitato) {
        if (listaOspiti.size() < capacitaMassima) {
            listaOspiti.add(invitato);
            System.out.println(invitato.getNome() + " aggiunto!");
            return true;
        } else {
            System.out.println("Il com.wedding.model.Tavolo è pieno!");
            return false;
        }
    }

    public int getNumeroTavolo() {
        return numeroTavolo;
    }

    public int getCapacitaMassima() {
        return capacitaMassima;
    }

    public int getNumeroOspiti() {
        return listaOspiti.size();
    }
}
