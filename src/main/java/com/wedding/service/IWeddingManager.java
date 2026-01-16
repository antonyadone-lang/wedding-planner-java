package com.wedding.service;

import com.wedding.model.ImpostazioniMatrimonio;
import com.wedding.model.Invitato;
import com.wedding.model.ServiziMatrimonio;
import com.wedding.model.Tavolo;
import com.wedding.model.Tracciabile;
import com.wedding.util.BudgetSuperatoException;
import com.wedding.util.FiltroInvitato;
import com.wedding.util.OrdinamentoStrategy;
import com.wedding.util.RisultatoOperazione;
import com.wedding.util.TavoloPienoException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

/**
 * Interaccia che deficinisce il contratto per il com.wedding.service.WeddingManager.
 * Permette di avere diverse implementazioni (ez.FileSystem, Database, Cloud).
 */
public interface IWeddingManager {
    //Gestione Invitati
    boolean aggiungiInvitato(Invitato invitato);
    void confermaInvitato(String email);
    RisultatoOperazione<Invitato> cercaInvitatoPerEmail(String email);
    List<Invitato> getListaInvitati();
    int rimuoviInvitatiSenzaRSVP(LocalDate dataLimite);
    void stampaListaInvitati(OrdinamentoStrategy strategia);
    void stampaNomi(List<? extends Invitato> lista);
    List<Invitato> filtraInvitati(FiltroInvitato filtro);
    void rimuoviSe(Predicate<Invitato> criterio);
    void svuotaTutto();

    //Gestione Tavoli
    void assegnaTavolo(Invitato invitato, Tavolo tavolo) throws TavoloPienoException;
    List<Invitato> getInvitatiPerTavolo(int numeroTavolo);

    //Gestione Fornitori & Budget
    void setBudgetMassimo(double budget);
    void aggiungiFornitore(ServiziMatrimonio fornitore) throws BudgetSuperatoException;
    ServiziMatrimonio cercaFornitorePerId(int id);
    double calcolaTotaleFornitori();
    double calcolaTotaleLordo();
    double calcoloCostoServiziNonPagati();

    //Gestione Tracciabili
    void aggiungiTracciabile(Tracciabile tracciabile);
    void mostraStatoTracciabili();

    //Persistenza (Questi metodi rimangono nell'interfaccia per ora)
    ImpostazioniMatrimonio caricaImpostazioni(String nomeFile) throws IOException;
    void salvaInvitatiSuFile(String nomeFile) throws IOException;
    int caricaInvitatiDaFile(String nomeFile);
    void salvaDatiBinari(String nomeFile) throws IOException;
    void caricaDatiBinari(String nomeFile) throws IOException, ClassNotFoundException;
}
