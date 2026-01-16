package com.wedding.model;

import java.io.Serializable;

public abstract class ServiziMatrimonio implements Serializable {
    protected static int contatoreServizi = 0;
    protected final int idServizio;
    //Attributi
    protected String nomeFornitore;
    protected String contatto;
    protected boolean pagato;

    //Costruttore
    public ServiziMatrimonio(String nomeFornitore, String contatto) {
        this.nomeFornitore = nomeFornitore;
        this.contatto = contatto;
        this.idServizio = ++contatoreServizi;
        this.pagato = false;
    }

    //Metodi
    public abstract double calcoloCosto();

    public void scheda() {
        System.out.println("=== FORNITORE ===");
        System.out.println("Nome: " + nomeFornitore);
        System.out.println("Contatto: " + contatto);
        System.out.println("Costo:€ " + calcoloCosto());
    }

    //getter
    public String getNomeFornitore() {
        return nomeFornitore;
    }

    public String getContatto() {
        return contatto;
    }

    public int getIdServizio() {
        return idServizio;
    }

    public boolean isPagato() {
        return pagato;
    }

    //setter
    public void setPagato(boolean pagato) {
        this.pagato = pagato;
    }

}
