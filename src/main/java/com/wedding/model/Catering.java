package com.wedding.model;

public class Catering extends ServiziMatrimonio {
    //Attributi;
    private final double costoPerPersona;
    private final int numeroPersone;

    //Costruttore
    public Catering(String nomeFornitore, String contatto, double costoPerPersona, int numeroPersone) {
        super(nomeFornitore, contatto);
        this.costoPerPersona = costoPerPersona;
        this.numeroPersone = numeroPersone;
    }

    //metodi
    @Override
    public double calcoloCosto() {
        return costoPerPersona * numeroPersone;
    }
}
