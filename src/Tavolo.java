import java.util.ArrayList;

public class Tavolo {
    private int numeroTavolo;
    private int capacitaMassima;
    private ArrayList<Invitato> listaOspiti;
    public Tavolo(int numeroTavolo, int capacitaMassima){
        this.numeroTavolo = numeroTavolo;
        this.capacitaMassima = capacitaMassima;
        this.listaOspiti = new ArrayList<>();
    }
    public boolean aggiungiOspite(Invitato invitato){
        if (listaOspiti.size() < capacitaMassima){
            listaOspiti.add(invitato);
            System.out.println(invitato.getNome() + " aggiunto!");
            return true;
        }else{
            System.out.println("Il Tavolo è pieno!");
            return false;
        }
    }
    public int getNumeroTavolo() {
        return numeroTavolo;}
    public int getCapacitaMassima(){
        return capacitaMassima;
    }
    public int getNumeroOspiti(){
        return listaOspiti.size();
    }
}
