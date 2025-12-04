import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.HashSet;
import java.util.HashMap;
import java.time.LocalDate;
import java.util.function.Predicate;

public class WeddingManager {
    // ========== ATTRIBUTI ==========
    private ArrayList<Invitato> listaInvitati;
    private ArrayList<Tavolo> listaTavoli;
    private ArrayList<ServiziMatrimonio> elencoFornitori;
    private ArrayList<Tracciabile> listaTracciabili;
    private HashSet<String> emailRegistrate;
    private HashMap<Integer, ServiziMatrimonio> mappaFornitori;


    // ========== COSTRUTTORE ==========
    public WeddingManager(){
        this.listaInvitati = new ArrayList<>();
        this.listaTavoli = new ArrayList<>();
        this.elencoFornitori = new ArrayList<>();
        this.listaTracciabili = new ArrayList<>();
        this.emailRegistrate = new HashSet<>();
        this.mappaFornitori = new HashMap<>();
    }

    // ========== METODI ==========
    public void assegnaTavolo(Invitato invitato, Tavolo tavolo){
        boolean aggiunto = tavolo.aggiungiOspite(invitato);
        if(aggiunto){
            System.out.println(invitato.getNome() + " assegnato al tavolo "+ tavolo.getNumeroTavolo());
        }else{
            System.out.println("Impossibile assegnare " + invitato.getNome() + " - Tavolo pieno!");
        }
    }

    public void aggiungiFornitore(ServiziMatrimonio fornitore){
        elencoFornitori.add(fornitore);
        mappaFornitori.put(fornitore.getIdServizio(), fornitore);
    }

    public void aggiungiTracciabile(Tracciabile tracciabile){listaTracciabili.add(tracciabile);}

    public boolean aggiungiInvitato(Invitato invitato){
        if (emailRegistrate.contains(invitato.getEmail())) {
            System.out.println("ERRORE: email già registrata - " + invitato.getEmail());
            return false;
        }
        listaInvitati.add(invitato);
        emailRegistrate.add(invitato.getEmail());
        return true;
    }

    public double calcolaTotaleFornitori(){
        double totale = 0;
        for(ServiziMatrimonio fornitore : elencoFornitori){
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

    public void mostraStatoTracciabili(){
        for(Tracciabile tracciabile : listaTracciabili){
            System.out.println("Stato: "+ tracciabile.getStatoTracciamento());
        }
    }

    public RisultatoOperazione<Invitato> cercaInvitatoPerEmail(String email){
        for(Invitato inv : listaInvitati){
            if(inv.getEmail().equals(email)) {
                return new RisultatoOperazione<>(inv);
            }
        }
        return new RisultatoOperazione<>("Invitato con email " + email + " non trovato");
    }

    public void stampaNomi(List<? extends Invitato> lista){
        for(Invitato inv : lista){
            System.out.println(inv.getNome() + " " + inv.getCognome());
        }
    }

    public int rimuoviInvitatiSenzaRSVP (LocalDate dataLimite){
        int rimossi = 0;
        Iterator<Invitato> iterator = listaInvitati.iterator();
        while(iterator.hasNext()){
            Invitato inv = iterator.next();
            if(inv.getDataRisposta() == null || inv.getDataRisposta().isAfter(dataLimite) || !inv.isConfermato()){
                iterator.remove();
                rimossi++;
            }
        }
        return rimossi;
    }

    public List<Invitato> getListaInvitati(){
        return listaInvitati;
    }

    public ServiziMatrimonio cercaFornitorePerId(int id){
        return mappaFornitori.get(id);
    }

    public List<Invitato> filtraInvitati(FiltroInvitato filtro){
        List<Invitato> risultato = new ArrayList<>();
        for(Invitato inv : listaInvitati){
            if (filtro.soddisfaCondizione(inv))
                risultato.add(inv);
        }
        return risultato;
    }

    public void rimuoviSe(Predicate<Invitato> criterio){
        listaInvitati.removeIf(criterio);
    }
}
