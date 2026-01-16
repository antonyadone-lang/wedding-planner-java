package com.wedding.model;

public class Fiorista extends ServiziMatrimonio implements Tracciabile {
    //Attributi Specifici;
    private final double costoFisso;
    private final double costoTrasporto;
    private StatoLavoro stato = StatoLavoro.ORDINE_ACQUISTO;
    //Costruttore
    public Fiorista(String nomeFornitore, String contatto, double costoFisso, double costoTrasporto) {
        super(nomeFornitore, contatto);
        this.costoFisso = costoFisso;
        this.costoTrasporto = costoTrasporto;
    }

    //metodi
    @Override
    public double calcoloCosto() {
        return costoFisso + costoTrasporto;
    }

    @Override
    public String getStatoTracciamento() {
        return stato.name();
    }

    @Override
    public void segnaCompletato() {
        stato = StatoLavoro.INSTALLAZIONE_COMPLETATA;
    }

    enum StatoLavoro {
        ORDINE_ACQUISTO,
        FIORI_ORDINATI,
        COMPOSIZIONI_PRONTE,
        INSTALLAZIONE_COMPLETATA
    }

}
