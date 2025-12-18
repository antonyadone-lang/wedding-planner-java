import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Gestore centrale per l'organizzazione del matrimonio.
 * Coordina invitati, tavoli e fornitori di servizi.
 */
public class WeddingManager {
    // ========== ATTRIBUTI ==========
    private ArrayList<Invitato> listaInvitati;
    private ArrayList<Tavolo> listaTavoli;
    private ArrayList<ServiziMatrimonio> elencoFornitori;
    private final ArrayList<Tracciabile> listaTracciabili;
    private final HashSet<String> emailRegistrate;
    private final HashMap<Integer, ServiziMatrimonio> mappaFornitori;
    private static WeddingManager instance;


    // ========== COSTRUTTORE ==========
    private WeddingManager() {
        this.listaInvitati = new ArrayList<>();
        this.listaTavoli = new ArrayList<>();
        this.elencoFornitori = new ArrayList<>();
        this.listaTracciabili = new ArrayList<>();
        this.emailRegistrate = new HashSet<>();
        this.mappaFornitori = new HashMap<>();
    }

    // ========== SINGLETON INSTANCE ==========
    /**
     * Restituisce l'unica istanza di WeddingManager (Singleton Pattern).
     * Thread-safe grazie alla sincronizzazione.
     *
     * @return L'istanza singleton di WeddingManager.
     */
    public static synchronized WeddingManager getInstance(){
        if(instance == null){
            instance = new WeddingManager();
        }
        return instance;
    }

    // ========== METODI ==========

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
            throw new TavoloPienoException("Il Tavolo " + tavolo.getNumeroTavolo() + " è pieno! Capacità Massima: " + tavolo.getCapacitaMassima());
        }
        boolean aggiunto = tavolo.aggiungiOspite(invitato);
        if (aggiunto) {
            invitato.setTavoloAssegnato(tavolo);
            System.out.println(invitato.getNome() + " assegnato al tavolo " + tavolo.getNumeroTavolo());
        } else {
            System.out.println("Impossibile assegnare " + invitato.getNome() + " - Tavolo pieno!");
        }
    }

    public void aggiungiFornitore(ServiziMatrimonio fornitore) {
        elencoFornitori.add(fornitore);
        mappaFornitori.put(fornitore.getIdServizio(), fornitore);
    }

    public void aggiungiTracciabile(Tracciabile tracciabile) {
        listaTracciabili.add(tracciabile);
    }

    /**
     * Aggiunge un invitato alla lista, verificando l'unicità dell'email.
     *
     * @param invitato L'invitato da aggiungere.
     * @return true se l'invitato è stato aggiunto, false se l'email è già registrata.
     */
    public boolean aggiungiInvitato(Invitato invitato) {
        if (emailRegistrate.contains(invitato.getEmail())) {
            System.err.println("ERRORE: email già registrata - " + invitato.getEmail());
            return false;
        }
        listaInvitati.add(invitato);
        emailRegistrate.add(invitato.getEmail());
        return true;
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

    public void mostraStatoTracciabili() {
        for (Tracciabile tracciabile : listaTracciabili) {
            System.out.println("Stato: " + tracciabile.getStatoTracciamento());
        }
    }

    /**
     * cerca un invitato tramite indirizzo email.
     *
     * @param email L'indirizzo email da cercare.
     * @return RisultatoOperazione contenente l'invitato se trovato, altrimenti un messaggio di errore.
     */
    public RisultatoOperazione<Invitato> cercaInvitatoPerEmail(String email) {
        for (Invitato inv : listaInvitati) {
            if (inv.getEmail().equals(email)) {
                return new RisultatoOperazione<>(inv);
            }
        }
        return new RisultatoOperazione<>("Invitato con email " + email + " non trovato");
    }

    public void stampaNomi(List<? extends Invitato> lista) {
        for (Invitato inv : lista) {
            System.out.println(inv.getNome() + " " + inv.getCognome());
        }
    }

    /**
     * Rimuove gli invitati che non hanno confermato entro la data limite.
     * Utilizza un Iterator per rimozione sicura durante l'iterazione.
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
                iterator.remove();
                rimossi++;
            }
        }
        return rimossi;
    }

    public List<Invitato> getListaInvitati() {
        return listaInvitati;
    }

    public ServiziMatrimonio cercaFornitorePerId(int id) {
        return mappaFornitori.get(id);
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
        listaInvitati.removeIf(criterio);
    }

    public List<Invitato> getInvitatiPerTavolo(int numeroTavolo) {
        return listaInvitati.stream()
                .filter(inv -> inv.getTavoloAssegnato() != null)
                .filter(inv -> inv.getTavoloAssegnato().getNumeroTavolo() == numeroTavolo)
                .collect(Collectors.toList());
    }

    /**
     * Carica le impostazioni del matrimonio da file di testo.
     * In caso di errore, restituisce valori predefiniti.
     *
     * @param nomeFile Il percorso del file da leggere.
     * @return Oggetto ImpostazioniMatrimonio con i dati caricati o valori predefiniti.
     * @throws IOException Se si verifica un errore durante la lettura del file.
     */
    public ImpostazioniMatrimonio caricaImpostazioni(String nomeFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomeFile))) {
            String rigaBudget = reader.readLine();
            String rigaData = reader.readLine();
            String rigaLocation = reader.readLine();
            double budget = Double.parseDouble(rigaBudget);
            return new ImpostazioniMatrimonio(budget, rigaData, rigaLocation);
        } catch (FileNotFoundException e) {
            System.err.println("ERRORE: File non trovato -" + nomeFile);
            System.err.println("Causa: " + e.getMessage());
            return new ImpostazioniMatrimonio(50000.0, "Non Impostata", "Non Impostata");
        } catch (IOException e) {
            System.err.println("ERRORE: Impossibile leggere il file -" + nomeFile);
            System.err.println("Causa: " + e.getMessage());
            return new ImpostazioniMatrimonio(50000.0, "Non Impostata", "Non Impostata");
        }
    }

    public void salvaInvitatiSuFile(String nomeFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFile))) {
            for (Invitato inv : listaInvitati) {
                String rigaCSV = inv.getNome() + "," + inv.getCognome() + "," + inv.getEmail() + "," + inv.isConfermato();
                writer.write(rigaCSV);
                writer.newLine();
            }
        }
        System.out.println("Invitati salvati su: " + nomeFile);
    }

    public void caricaInvitatiDaFile(String nomeFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomeFile))) {
            String riga;
            while ((riga = reader.readLine()) != null) {
                String[] dati = riga.split(",");
                String nome = dati[0];
                String cognome = dati[1];
                String email = dati[2];
                boolean confermato = Boolean.parseBoolean(dati[3]);
                Invitato invitato = new Invitato(nome, cognome, email);
                invitato.setConfermato(confermato);
                aggiungiInvitato(invitato);
            }
        }
        System.out.println("Invitati caricati da: " + nomeFile);
    }

    public void svuotaTutto() {
        listaInvitati.clear();
        emailRegistrate.clear();
    }

    /**
     * Salva lo stato completo del WeddingManager su file binario.
     * Utilizza la serializzazione Java per salvare invitati, tavoli e fornitori.
     *
     * @param nomeFile Il percorso del file di destinazione (es. "backup.dat").
     * @throws IOException Se si verifica un errore di scrittura su disco.
     */
    public void salvaDatiBinari(String nomeFile) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeFile))) {
            oos.writeObject(listaInvitati);
            oos.writeObject(listaTavoli);
            oos.writeObject(elencoFornitori);
        }
        System.out.println("Dati salvati in: " + nomeFile);
    }

    /**
     * Carica lo stato completo del WeddingManager da file binario.
     * Ricostruisce automaticamente le strutture dati derivate (HashSet, Hashmap).
     *
     * @param nomeFile Il percorso del file da caricare.
     * @throws IOException            Se si verifica un errore di lettura del file.
     * @throws ClassNotFoundException Se le classi serializzate non sono disponibili.
     */
    public void caricaDatiBinari(String nomeFile) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeFile))) {
            listaInvitati = (ArrayList<Invitato>) ois.readObject();
            listaTavoli = (ArrayList<Tavolo>) ois.readObject();
            elencoFornitori = (ArrayList<ServiziMatrimonio>) ois.readObject();
            emailRegistrate.clear();
            for (Invitato inv : listaInvitati) {
                emailRegistrate.add(inv.getEmail());
            }
            mappaFornitori.clear();
            for (ServiziMatrimonio serv : elencoFornitori) {
                mappaFornitori.put(serv.getIdServizio(), serv);
            }
        }
        System.out.println("Dati caricati da: " + nomeFile);
    }

    public void stampaListaInvitati(OrdinamentoStrategy strategia){
        List<Invitato> listaDaOrdinare = new ArrayList<>(this.listaInvitati);
        List<Invitato> listaOrdinata = strategia.ordina(listaDaOrdinare);
        System.out.println("--- Lista invitati ordinata con strategia: " + strategia.getClass().getSimpleName() + "---");
        listaOrdinata.forEach(System.out::println);
    }
}
