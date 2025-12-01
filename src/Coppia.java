public class Coppia <K,V>{
    //Attributi
    private K chiave;
    private V valore;
//Costruttore
    public Coppia(K chiave, V valore){
        this.chiave = chiave;
        this.valore = valore;
    }
//Getter
    public K getChiave() {return chiave;}
    public V getValore() {return valore;}
//To string per utilità
    @Override
    public String toString() {
        return "(" + chiave.toString() + ", " + valore.toString() + ")";
    }
}
