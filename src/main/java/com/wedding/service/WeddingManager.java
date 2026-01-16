package com.wedding.service;

import com.wedding.model.ConfigurazioneMatrimonio;
import com.wedding.model.ImpostazioniMatrimonio;
import com.wedding.model.Invitato;
import com.wedding.model.ServiziMatrimonio;
import com.wedding.model.StatoInvitato;
import com.wedding.model.Tavolo;
import com.wedding.model.Tracciabile;
import com.wedding.util.BudgetSuperatoException;
import com.wedding.util.FiltroInvitato;
import com.wedding.util.GestoreFile;
import com.wedding.util.InvitatoNonTrovatoException;
import com.wedding.util.OrdinamentoStrategy;
import com.wedding.util.RisultatoOperazione;
import com.wedding.util.TavoloPienoException;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Gestore centrale per l'organizzazione del matrimonio.
 * Coordina invitati, tavoli e fornitori di servizi.
 */
public class WeddingManager implements IWeddingManager {

    // =================================================================================
    // ATTRIBUTI
    // =================================================================================
    
    // Singleton Instance
    private static WeddingManager instance;

    // Strutture Dati Invitati (Sincronizzate per scopo didattico)
    private ArrayList<Invitato> listaInvitati;      // Accesso sequenziale/ordinato
    private HashSet<String> emailRegistrate;        // Controllo duplicati
    private Map<String, Invitato> mappaInvitati;    // Accesso diretto O(1)

    // Altre Strutture Dati
    private ArrayList<Tavolo> listaTavoli;
    private ArrayList<ServiziMatrimonio> elencoFornitori;
    private final HashMap<Integer, ServiziMatrimonio> mappaFornitori;
    private final ArrayList<Tracciabile> listaTracciabili;

    // Impostazioni
    private double budgetMassimo;


    // =================================================================================
    // COSTRUTTORE & SINGLETON
    // =================================================================================

    private WeddingManager() {
        // Inizializzazione Invitati
        this.listaInvitati = new ArrayList<>();
        this.emailRegistrate = new HashSet<>();
        this.mappaInvitati = new HashMap<>();
        
        // Inizializzazione Altri
        this.listaTavoli = new ArrayList<>();
        this.elencoFornitori = new ArrayList<>();
        this.mappaFornitori = new HashMap<>();
        this.listaTracciabili = new ArrayList<>();
    }

    /**
     * Restituisce l'unica istanza di com.wedding.service.WeddingManager (Singleton Pattern).
     * Thread-safe grazie alla sincronizzazione.
     *
     * @return L'istanza singleton di com.wedding.service.WeddingManager.
     */
    public static synchronized WeddingManager getInstance(){
        if(instance == null){
            instance = new WeddingManager();
        }
        return instance;
    }


    // =================================================================================
    // GESTIONE INVITATI
    // =================================================================================

    /**
     * Aggiunge un invitato mantenendo sincronizzate tutte le strutture dati (List, Set, Map).
     *
     * @param invitato L'invitato da aggiungere.
     * @return true se l'invitato è stato aggiunto, false se l'email è già registrata.
     */
    public synchronized boolean aggiungiInvitato(Invitato invitato) {
        String emailKey = invitato.getEmail().toLowerCase();
        
        // Controllo duplicati usando la Mappa
        if (mappaInvitati.containsKey(emailKey)) {
            return false;
        }
        
        // Sincronizzazione: Aggiungo a tutte le strutture
        listaInvitati.add(invitato);            // Aggiungo alla lista (mantiene ordine inserimento)
        emailRegistrate.add(invitato.getEmail()); // Aggiungo al set (traccia email usate)
        mappaInvitati.put(emailKey, invitato);    // Aggiungo alla mappa (per accesso rapido)
        
        return true;
    }

    /**
     * Cerca un invitato tramite indirizzo email.
     * Utilizza il metodo generico di ricerca su Mappa.
     *
     * @param email L'indirizzo email da cercare.
     * @return com.wedding.util.RisultatoOperazione contenente l'invitato se trovato.
     */
    public RisultatoOperazione<Invitato> cercaInvitatoPerEmail(String email) {
        Invitato inv = mappaInvitati.get(email.toLowerCase());
        if (inv != null) {
            return new RisultatoOperazione<>(inv);
        }
        return new RisultatoOperazione<>("com.wedding.model.Invitato con email " + email + " non trovato");
    }

    public synchronized void confermaInvitato(String email){
        Invitato inv = mappaInvitati.get(email.toLowerCase());
        
        if (inv == null) {
            throw new InvitatoNonTrovatoException(email);
        }
        
        inv.setStato(StatoInvitato.CONFERMATO);
    }

    /**
     * Rimuove gli invitati che non hanno confermato entro la data limite.
     * Aggiorna tutte le strutture dati per mantenerle sincronizzate.
     *
     * @param dataLimite Data limite per la conferma RSVP.
     * @return Numero di invitati rimossi.
     */
    public int rimuoviInvitatiSenzaRSVP(LocalDate dataLimite) {
        int rimossi = 0;
        Iterator<Invitato> iterator = listaInvitati.iterator();
        while (iterator.hasNext()) {
            Invitato inv = iterator.next();
            if (inv.getDataRisposta() == null || inv.getDataRisposta().isAfter(dataLimite) || !inv.isConfermato()) {
                iterator.remove(); // Rimuove dalla Lista
                
                // Sincronizzazione
                emailRegistrate.remove(inv.getEmail());
                mappaInvitati.remove(inv.getEmail().toLowerCase());
                
                rimossi++;
            }
        }
        return rimossi;
    }

    public List<Invitato> filtraInvitati(FiltroInvitato filtro) {
        List<Invitato> risultato = new ArrayList<>();
        for (Invitato inv : listaInvitati) {
            if (filtro.soddisfaCondizione(inv))
                risultato.add(inv);
        }
        return risultato;
    }

    public void rimuoviSe(Predicate<Invitato> criterio) {
        List<Invitato> daRimuovere = new ArrayList<>();
        for (Invitato inv : listaInvitati) {
            if (criterio.test(inv)) {
                daRimuovere.add(inv);
            }
        }
        
        for (Invitato inv : daRimuovere) {
            listaInvitati.remove(inv);
            emailRegistrate.remove(inv.getEmail());
            mappaInvitati.remove(inv.getEmail().toLowerCase());
        }
    }

    public void stampaListaInvitati(OrdinamentoStrategy strategia){
        List<Invitato> listaDaOrdinare = new ArrayList<>(this.listaInvitati);
        List<Invitato> listaOrdinata = strategia.ordina(listaDaOrdinare);
        System.out.println("--- Lista invitati ordinata con strategia: " + strategia.getClass().getSimpleName() + "---");
        listaOrdinata.forEach(System.out::println);
    }

    public void stampaNomi(List<? extends Invitato> lista) {
        for (Invitato inv : lista) {
            System.out.println(inv.getNome() + " " + inv.getCognome());
        }
    }

    public List<Invitato> getListaInvitati() {
        return listaInvitati;
    }


    // =================================================================================
    // GESTIONE TAVOLI
    // =================================================================================

    /**
     * Assegna un invitato a un tavolo specifico.
     * Verifica la capacità del tavolo prima dell'assegnazione.
     *
     * @param invitato L'invitato da assegnare.
     * @param tavolo   Il tavolo di destinazione.
     * @throws TavoloPienoException Se il tavolo ha raggiunto la capacità massima.
     */
    public void assegnaTavolo(Invitato invitato, Tavolo tavolo) throws TavoloPienoException {
        if (tavolo.getNumeroOspiti() >= tavolo.getCapacitaMassima()) {
            throw new TavoloPienoException("Il com.wedding.model.Tavolo " + tavolo.getNumeroTavolo() + " è pieno! Capacità Massima: " + tavolo.getCapacitaMassima());
        }
        boolean aggiunto = tavolo.aggiungiOspite(invitato);
        if (aggiunto) {
            invitato.setTavoloAssegnato(tavolo);
        } else {
            throw new TavoloPienoException("Impossibile assegnare al tavolo (errore generico)");
        }
    }

    public List<Invitato> getInvitatiPerTavolo(int numeroTavolo) {
        return listaInvitati.stream()
                .filter(inv -> inv.getTavoloAssegnato() != null)
                .filter(inv -> inv.getTavoloAssegnato().getNumeroTavolo() == numeroTavolo)
                .collect(Collectors.toList());
    }


    // =================================================================================
    // GESTIONE FORNITORI & BUDGET
    // =================================================================================

    public synchronized void aggiungiFornitore(ServiziMatrimonio fornitore) throws BudgetSuperatoException {
        double spesaFutura = calcolaTotaleLordo() + fornitore.calcoloCosto();
        if (budgetMassimo > 0 && spesaFutura > budgetMassimo) {
            throw new BudgetSuperatoException("Attenzione! Budget superato. Spesa prevista: €" + spesaFutura + " (Budget: €" + budgetMassimo + ")");
        }
        elencoFornitori.add(fornitore);
        mappaFornitori.put(fornitore.getIdServizio(), fornitore);
    }

    public ServiziMatrimonio cercaFornitorePerId(int id) {
        return mappaFornitori.get(id);
    }

    public double calcolaTotaleFornitori() {
        double totale = 0;
        for (ServiziMatrimonio fornitore : elencoFornitori) {
            totale += fornitore.calcoloCosto();
        }
        double iva = totale * ConfigurazioneMatrimonio.ALIQUOTA_IVA;
        totale += iva;
        return totale;
    }

    public double calcolaTotaleLordo() {
        double totale = 0;
        for (ServiziMatrimonio fornitore : elencoFornitori) {
            totale += fornitore.calcoloCosto();
        }
        return totale;
    }

    public double calcoloCostoServiziNonPagati() {
        return elencoFornitori.stream()
                .filter(servizio -> !servizio.isPagato())
                .mapToDouble(servizio -> servizio.calcoloCosto())
                .sum();
    }

    public void setBudgetMassimo(double budgetMassimo) {
        this.budgetMassimo = budgetMassimo;
    }


    // =================================================================================
    // GESTIONE TRACCIABILI
    // =================================================================================

    public void aggiungiTracciabile(Tracciabile tracciabile) {
        listaTracciabili.add(tracciabile);
    }

    public void mostraStatoTracciabili() {
        for (Tracciabile tracciabile : listaTracciabili) {
            System.out.println("Stato: " + tracciabile.getStatoTracciamento());
        }
    }


    // =================================================================================
    // PERSISTENZA (FILE I/O)
    // =================================================================================

    // =================================================================================
    // PERSISTENZA (FILE I/O) - DELEGATA A GESTOREFILE
    // =================================================================================

    /**
     * Carica le impostazioni del matrimonio da file di testo.
     * Delega la lettura a com.wedding.util.GestoreFile.
     */
    public ImpostazioniMatrimonio caricaImpostazioni(String nomeFile) throws IOException {
        return GestoreFile.caricaImpostazioni(nomeFile);
    }

    /**
     * Salva gli invitati su file CSV.
     * Delega la scrittura a com.wedding.util.GestoreFile passando la lista corrente.
     */
    public synchronized void salvaInvitatiSuFile(String nomeFile) throws IOException {
        GestoreFile.salvaInvitatiSuFile(this.listaInvitati, nomeFile);
    }

    /**
     * Carica gli invitati da file CSV e li aggiunge al sistema.
     * Usa com.wedding.util.GestoreFile per leggere, poi aggiunge uno a uno per mantenere la sincronizzazione.
     */
    public int caricaInvitatiDaFile(String nomeFile) {
        List<Invitato> nuoviInvitati = GestoreFile.caricaInvitatiDaFile(nomeFile);

        int contatore = 0;
        for (Invitato inv : nuoviInvitati) {
            if (aggiungiInvitato(inv)) {
                contatore++;
            }
        }
        return contatore;
    }


    /**
     * Salva lo stato completo del com.wedding.service.WeddingManager su file binario.
     */
    public void salvaDatiBinari(String nomeFile) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeFile))) {
            oos.writeObject(listaInvitati);
            oos.writeObject(listaTavoli);
            oos.writeObject(elencoFornitori);
        }
    }

    /**
     * Carica lo stato completo del com.wedding.service.WeddingManager da file binario.
     */
    public void caricaDatiBinari(String nomeFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeFile))) {
            listaInvitati = (ArrayList<Invitato>) ois.readObject();
            
            // Ricostruzione strutture derivate
            emailRegistrate.clear();
            mappaInvitati.clear();
            for (Invitato inv : listaInvitati) {
                emailRegistrate.add(inv.getEmail());
                mappaInvitati.put(inv.getEmail().toLowerCase(), inv);
            }

            listaTavoli = (ArrayList<Tavolo>) ois.readObject();
            elencoFornitori = (ArrayList<ServiziMatrimonio>) ois.readObject();
            
            mappaFornitori.clear();
            for (ServiziMatrimonio serv : elencoFornitori) {
                mappaFornitori.put(serv.getIdServizio(), serv);
            }
        }
    }


    // =================================================================================
    // METODI DI UTILITÀ (PRIVATI & GENERICS)
    // =================================================================================

    public synchronized void svuotaTutto() {
        listaInvitati.clear();
        emailRegistrate.clear();
        mappaInvitati.clear();
    }

}
