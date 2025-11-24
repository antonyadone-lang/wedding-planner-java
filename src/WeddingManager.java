import java.util.ArrayList;
public class WeddingManager {
    private ArrayList<Invitato> listaInvitati;
    private ArrayList<Tavolo> listaTavoli;

    public WeddingManager(){
        this.listaInvitati = new ArrayList<>();
        this.listaTavoli = new ArrayList<>();
    }
    public void assegnaTavolo(Invitato invitato, Tavolo tavolo){
        boolean aggiunto = tavolo.aggiungiOspite(invitato);
        if(aggiunto){
            System.out.println(invitato.getNome() + " assegnato al tavolo "+ tavolo.getNumeroTavolo());
        }else{
            System.out.println("Impossibile assegnare " + invitato.getNome() + " - Tavolo pieno!");
        }
    }
}
