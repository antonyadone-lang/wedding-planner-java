import java.util.ArrayList;
import java.util.List;
public class WeddingManager {
    // ========== ATTRIBUTI ==========
    private ArrayList<Invitato> listaInvitati;
    private ArrayList<Tavolo> listaTavoli;
    private ArrayList<ServiziMatrimonio> elencoFornitori;
    private ArrayList<Tracciabile> listaTracciabili;

    // ========== COSTRUTTORE ==========
    public WeddingManager(){
        this.listaInvitati = new ArrayList<>();
        this.listaTavoli = new ArrayList<>();
        this.elencoFornitori = new ArrayList<>();
        this.listaTracciabili = new ArrayList<>();
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
    }
    public void aggiungiTracciabile(Tracciabile tracciabile){listaTracciabili.add(tracciabile);}
    public void aggiungiInvitato(Invitato invitato){
        listaInvitati.add(invitato);
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
}
