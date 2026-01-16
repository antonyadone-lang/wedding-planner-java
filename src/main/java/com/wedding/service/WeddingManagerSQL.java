package com.wedding.service;

import com.wedding.dao.ConnessioneDB;
import com.wedding.dao.InvitatoDAO;
import com.wedding.dao.TavoloDAO;
import com.wedding.model.ImpostazioniMatrimonio;
import com.wedding.model.Invitato;
import com.wedding.model.ServiziMatrimonio;
import com.wedding.model.StatoInvitato;
import com.wedding.model.Tavolo;
import com.wedding.model.Tracciabile;
import com.wedding.util.BudgetSuperatoException;
import com.wedding.util.FiltroInvitato;
import com.wedding.util.InvitatoNonTrovatoException;
import com.wedding.util.OrdinamentoStrategy;
import com.wedding.util.RisultatoOperazione;
import com.wedding.util.TavoloPienoException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class WeddingManagerSQL implements IWeddingManager {

    private static WeddingManagerSQL instance;
    private final InvitatoDAO invitatoDAO;
    private final TavoloDAO tavoloDAO;
    private List<Invitato> listaInvitati;
    private List<Tavolo> listaTavoli;

    // Costruttore Privato: Carica tutto dal DB all'avvio.
    private WeddingManagerSQL() {
        this.invitatoDAO = new InvitatoDAO();
        this.tavoloDAO = new TavoloDAO();
        this.listaInvitati = new ArrayList<>();
        this.listaTavoli = new ArrayList<>();

        System.out.println("--- Inizio Caricamento Dati dal Database ---");
        try {
            // 1. Carica prima le entità indipendenti (Tavoli)
            this.listaTavoli = tavoloDAO.caricaTutti();
            System.out.println("SQL Manager: Caricati " + listaTavoli.size() + " tavoli dal DB.");

            // 2. Carica le entità dipendenti (Invitati)
            this.listaInvitati = invitatoDAO.caricaTutti();
            System.out.println("SQL Manager: Caricati " + listaInvitati.size() + " invitati dal DB.");

            // 3. Collega gli oggetti in memoria
            collegaInvitatiATavoli();
            System.out.println("--- Fine Caricamento Dati ---");

        } catch (SQLException e) {
            System.err.println("ERRORE CRITICO DB: Impossibile inizializzare il com.wedding.service.WeddingManagerSQL. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized WeddingManagerSQL getInstance() {
        if (instance == null) {
            instance = new WeddingManagerSQL();
        }
        return instance;
    }

    // ==================== GESTIONE INVITATI ====================

    @Override
    public boolean aggiungiInvitato(Invitato invitato) {
        try {
            invitatoDAO.salva(invitato);
            // Aggiungiamo alla cache locale solo se il salvataggio su DB ha successo
            listaInvitati.add(invitato);
            return true;
        } catch (SQLException e) {
            System.err.println("Errore salvataggio DB: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void confermaInvitato(String email) {
        // 1. Aggiorna in memoria
        Invitato invitatoDaAggiornare = null;
        for (Invitato inv : listaInvitati) {
            if (inv.getEmail().equalsIgnoreCase(email)) {
                inv.setStato(StatoInvitato.CONFERMATO);
                invitatoDaAggiornare = inv;
                break;
            }
        }

        if (invitatoDaAggiornare == null) {
            throw new InvitatoNonTrovatoException(email);
        }

        // 2. Aggiorna sul Database
        try {
            invitatoDAO.aggiornaStato(email, StatoInvitato.CONFERMATO);
        } catch (SQLException e) {
            System.err.println("Errore DB in conferma: " + e.getMessage());
            // In un'app reale, qui dovremmo annullare la modifica in memoria (rollback)
        }
    }

    @Override
    public int rimuoviInvitatiSenzaRSVP(LocalDate dataLimite) {
        int rimossi = 0;
        Iterator<Invitato> iterator = listaInvitati.iterator();
        while (iterator.hasNext()) {
            Invitato inv = iterator.next();

            // Rimuoviamo chi è ancora IN_ATTESA o DA_INVITARE
            if (inv.getStato() == StatoInvitato.IN_ATTESA || inv.getStato() == StatoInvitato.DA_INVITARE) {
                try {
                    if (invitatoDAO.elimina(inv.getEmail())) {
                        iterator.remove(); // Rimuovi dalla lista solo se cancellato dal DB
                        rimossi++;
                    }
                } catch (SQLException e) {
                    System.err.println("Errore eliminazione " + inv.getEmail() + ": " + e.getMessage());
                }
            }
        }
        return rimossi;
    }

    @Override
    public RisultatoOperazione<Invitato> cercaInvitatoPerEmail(String email) {
        for (Invitato inv : listaInvitati) {
            if (inv.getEmail().equalsIgnoreCase(email)) {
                return new RisultatoOperazione<>(inv);
            }
        }
        return new RisultatoOperazione<>("Non trovato");
    }

    @Override
    public List<Invitato> getListaInvitati() {
        return listaInvitati;
    }

    // ==================== GESTIONE TAVOLI ====================

    @Override
    public void assegnaTavolo(Invitato invitato, Tavolo tavolo) throws TavoloPienoException {

        if (tavolo.getNumeroOspiti() >= tavolo.getCapacitaMassima()) {
            throw new TavoloPienoException("Il com.wedding.model.Tavolo " + tavolo.getNumeroTavolo() + " è pieno!");
        }

        Connection conn = null;
        try {

            conn = ConnessioneDB.getConnection();

            conn.setAutoCommit(false);

            boolean dbSuccess = invitatoDAO.aggiornaTavolo(conn, invitato.getEmail(), tavolo.getNumeroTavolo());

            if (!dbSuccess) {
                throw new SQLException("Nessuna riga aggiornata per l'invitato " + invitato.getEmail());
            }

            conn.commit();
            System.out.println(" TRANSAZIONE COMMITTATA: Assegnazione salvata su DB.");


            tavolo.aggiungiOspite(invitato);
            invitato.setTavoloAssegnato(tavolo);

        } catch (SQLException e) {

            System.err.println(" ERRORE TRANSAZIONE: " + e.getMessage());
            if (conn != null) {
                try {
                    System.err.println(" Eseguo ROLLBACK...");
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } finally {

            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public List<Invitato> getInvitatiPerTavolo(int numeroTavolo) {
        List<Invitato> risultato = new ArrayList<>();
        for (Invitato inv : listaInvitati) {
            if (inv.getTavoloAssegnato() != null && inv.getTavoloAssegnato().getNumeroTavolo() == numeroTavolo) {
                risultato.add(inv);
            }
        }
        return risultato;
    }

    // ==================== METODI DI UTILITÀ E STUB ====================

    public void ricaricaDalDB() {
        // Logica spostata nel costruttore per l'inizializzazione, ma il metodo può servire per refresh manuali
        try {
            this.listaTavoli = tavoloDAO.caricaTutti();
            this.listaInvitati = invitatoDAO.caricaTutti();
            collegaInvitatiATavoli();
            System.out.println("Dati ricaricati dal DB.");
        } catch (SQLException e) {
            System.err.println("Errore ricaricamento DB: " + e.getMessage());
        }
    }

    private void collegaInvitatiATavoli() {
        for (Invitato inv : listaInvitati) {
            if (inv.getTavoloAssegnato() != null) {
                int numeroTavoloCercato = inv.getTavoloAssegnato().getNumeroTavolo();
                for (Tavolo t : listaTavoli) {
                    if (t.getNumeroTavolo() == numeroTavoloCercato) {
                        inv.setTavoloAssegnato(t);
                        t.aggiungiOspite(inv);
                        break;
                    }
                }
            }
        }
    }

    // --- Metodi non ancora implementati con SQL ---

    @Override
    public void stampaListaInvitati(OrdinamentoStrategy strategia) {
        List<Invitato> ordinata = strategia.ordina(new ArrayList<>(listaInvitati));
        ordinata.forEach(System.out::println);
    }

    @Override
    public void stampaNomi(List<? extends Invitato> lista) {
        for (Invitato inv : lista) {
            System.out.println(inv.getNome() + " " + inv.getCognome());
        }
    }

    @Override
    public List<Invitato> filtraInvitati(FiltroInvitato filtro) {
        List<Invitato> ris = new ArrayList<>();
        for (Invitato inv : listaInvitati) {
            if (filtro.soddisfaCondizione(inv)) ris.add(inv);
        }
        return ris;
    }

    @Override
    public void rimuoviSe(Predicate<Invitato> criterio) {
        listaInvitati.removeIf(criterio);
    }

    @Override
    public void svuotaTutto() {
        listaInvitati.clear();
    }

    @Override
    public void setBudgetMassimo(double budget) {}

    @Override
    public void aggiungiFornitore(ServiziMatrimonio fornitore) throws BudgetSuperatoException {}

    @Override
    public ServiziMatrimonio cercaFornitorePerId(int id) { return null; }

    @Override
    public double calcolaTotaleFornitori() { return 0; }

    @Override
    public double calcolaTotaleLordo() { return 0; }

    @Override
    public double calcoloCostoServiziNonPagati() { return 0; }

    @Override
    public void aggiungiTracciabile(Tracciabile tracciabile) {}

    @Override
    public void mostraStatoTracciabili() {}

    @Override
    public ImpostazioniMatrimonio caricaImpostazioni(String nomeFile) throws IOException { return null; }

    @Override
    public void salvaInvitatiSuFile(String nomeFile) throws IOException {}

    @Override
    public int caricaInvitatiDaFile(String nomeFile) { return 0; }

    @Override
    public void salvaDatiBinari(String nomeFile) throws IOException {}

    @Override
    public void caricaDatiBinari(String nomeFile) throws IOException, ClassNotFoundException {}
}
