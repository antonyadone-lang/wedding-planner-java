public class Dj extends ServiziMatrimonio {
    //Attributi specifici;
    private final double costoPacchetto;

    //Costruttore
    public Dj(String nomeFornitore, String contatto, double costoPacchetto) {
        super(nomeFornitore, contatto);
        this.costoPacchetto = costoPacchetto;
    }

    //metodi
    @Override
    public double calcoloCosto() {
        return costoPacchetto;
    }
}
