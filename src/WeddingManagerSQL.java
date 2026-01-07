import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class WeddingManagerSQL implements IWeddingManager {

    private static WeddingManagerSQL instance;
    private InvitatoDAO invitatoDAO;
    private List<Invitato> listaInvitati; // Cache locale

    // Costruttore Privato
    private WeddingManagerSQL() {
        this.invitatoDAO = new InvitatoDAO();
        this.listaInvitati = new ArrayList<>();

        // Caricamento iniziale
        try {
            this.listaInvitati = invitatoDAO.caricaTutti();
            System.out.println("SQL Manager: Caricati " + listaInvitati.size() + " invitati dal DB.");
        } catch (SQLException e) {
            System.err.println("ERRORE CRITICO DB: " + e.getMessage());
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
        // 1. Aggiungi alla cache locale
        listaInvitati.add(invitato);

        // 2. Salva su DB
        try {
            invitatoDAO.salva(invitato);
            return true;
        } catch (SQLException e) {
            System.err.println("Errore salvataggio DB: " + e.getMessage());
            return false;
        }
    }

    @Override
    public void confermaInvitato(String email) {
        // TODO: Implementare update su DB
        // Per ora aggiorniamo solo in memoria
        for (Invitato inv : listaInvitati) {
            if (inv.getEmail().equalsIgnoreCase(email)) {
                inv.setStato(StatoInvitato.CONFERMATO);
                // invitatoDAO.aggiorna(inv); // Da fare in futuro
                break;
            }
        }
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

    @Override
    public int rimuoviInvitatiSenzaRSVP(LocalDate dataLimite) {
        // TODO: Implementare delete su DB
        return 0;
    }

    @Override
    public void stampaListaInvitati(OrdinamentoStrategy strategia) {
        // Usa la lista locale, quindi funziona già!
        List<Invitato> ordinata = strategia.ordina(new ArrayList<>(listaInvitati));
        ordinata.forEach(System.out::println);
    }

    @Override
    public void stampaNomi(List<? extends Invitato> lista) {
        for (Invitato inv : lista) {
            System.out.println(inv.getNome());
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
        // TODO: Sync col DB
        listaInvitati.removeIf(criterio);
    }

    @Override
    public void svuotaTutto() {
        listaInvitati.clear();
        // TODO: Truncate table su DB
    }

    // ==================== GESTIONE TAVOLI (Stub) ====================
    @Override
    public void assegnaTavolo(Invitato invitato, Tavolo tavolo) throws TavoloPienoException {
        // Logica in memoria per ora
        if (tavolo.getNumeroOspiti() >= tavolo.getCapacitaMassima()) throw new TavoloPienoException("Pieno");
        tavolo.aggiungiOspite(invitato);
    }

    @Override
    public List<Invitato> getInvitatiPerTavolo(int numeroTavolo) {
        return new ArrayList<>(); // Stub
    }

    // ==================== GESTIONE FORNITORI (Stub) ====================
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

    // ==================== TRACCIABILI (Stub) ====================
    @Override
    public void aggiungiTracciabile(Tracciabile tracciabile) {}

    @Override
    public void mostraStatoTracciabili() {}

    // ==================== PERSISTENZA FILE (Non usata qui) ====================
    @Override
    public ImpostazioniMatrimonio caricaImpostazioni(String nomeFile) throws IOException {
        return new ImpostazioniMatrimonio(0, "", "");
    }

    @Override
    public void salvaInvitatiSuFile(String nomeFile) throws IOException {
        // Potremmo esportare dal DB al CSV!
    }

    @Override
    public int caricaInvitatiDaFile(String nomeFile) {
        return 0;
    }

    @Override
    public void salvaDatiBinari(String nomeFile) throws IOException {}

    @Override
    public void caricaDatiBinari(String nomeFile) throws IOException, ClassNotFoundException {}
}