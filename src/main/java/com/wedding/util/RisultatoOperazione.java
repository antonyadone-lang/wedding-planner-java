package com.wedding.util;

public class RisultatoOperazione<T> {
    private final T dato;
    private final boolean successo;
    private final String messaggioErrore;

    //Costruttore per Successo
    public RisultatoOperazione(T dato) {
        this.dato = dato;
        this.successo = true;
        this.messaggioErrore = null;
    }

    //Costruttore per Fallimento
    public RisultatoOperazione(String messaggioErrore) {
        this.dato = null;
        this.successo = false;
        this.messaggioErrore = messaggioErrore;
    }

    //Getter
    public T getDato() {
        return dato;
    }

    public boolean isSuccesso() {
        return successo;
    }

    public String getMessaggioErrore() {
        return messaggioErrore;
    }
}
