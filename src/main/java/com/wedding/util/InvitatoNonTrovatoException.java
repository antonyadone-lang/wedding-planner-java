package com.wedding.util;

public class InvitatoNonTrovatoException extends WeddingException {
    public InvitatoNonTrovatoException(String email){
        super("Attenzione: Nessun invitato trovato con l'email: " + email);
    }
}
