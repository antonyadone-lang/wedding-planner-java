public class Fiorista extends ServiziMatrimonio{
    //Attributi Specifici;
    private double costoFisso;
    private double costoTrasporto;

    //Costruttore
    public Fiorista(String nomeFornitore, String contatto,double costoFisso, double costoTrasporto){
        super(nomeFornitore, contatto);
        this.costoFisso = costoFisso;
        this.costoTrasporto = costoTrasporto;
    }

    //metodi
    @Override
    public double calcoloCosto(){
        return costoFisso + costoTrasporto;
    }
}
