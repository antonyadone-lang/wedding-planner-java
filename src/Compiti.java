public class Compiti implements Tracciabile {
    //Attributi;
    private final String descrizione;
    private boolean completato;

    //Costruttore;
    public Compiti(String descrizione) {
        this.descrizione = descrizione;
        this.completato = false;
    }

    //metodi
    @Override
    public String getStatoTracciamento() {
        return completato ? "COMPLETATO" : "IN LAVORAZIONE";
    }

    @Override
    public void segnaCompletato() {
        completato = true;
    }

    //getter
    public String getDescrizione() {
        return descrizione;
    }
}
