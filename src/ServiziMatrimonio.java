public abstract class ServiziMatrimonio {
    //Attributi
    protected String nomeFornitore;
    protected String contatto;
    protected static int contatoreServizi = 0;
    protected final int idServizio;

    //Costruttore
    public ServiziMatrimonio(String nomeFornitore, String contatto){
        this.nomeFornitore = nomeFornitore;
        this.contatto = contatto;
        this.idServizio = ++contatoreServizi;
    }

    //Metodi
    public abstract double calcoloCosto();
    public void scheda(){
        System.out.println("=== FORNITORE ===");
        System.out.println("Nome: "+ nomeFornitore);
        System.out.println("Contatto: "+ contatto);
        System.out.println("Costo:€ "+ calcoloCosto());
    }
    //getter
    public String getNomeFornitore() {
        return nomeFornitore;}

    public String getContatto() {
        return contatto;}

    public int getIdServizio() {
        return idServizio;
    }

}
