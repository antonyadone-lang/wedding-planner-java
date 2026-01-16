package com.wedding.model;

public class Fotografo extends ServiziMatrimonio {
    //Attributi specififici;
    private final double costoPacchetto;

    //Costruttore
    public Fotografo(String nomeFornitore, String contatto, double costoPacchetto) {
        super(nomeFornitore, contatto);
        this.costoPacchetto = costoPacchetto;
    }

    //implementazione metodo astratto;
    @Override
    public double calcoloCosto() {
        return costoPacchetto;
    }
}
