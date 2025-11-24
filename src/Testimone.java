public class Testimone extends Invitato{
    private boolean ruoloChiesa;
    public Testimone(String nome,String cognome,String email,boolean ruoloChiesa){
        super(nome, cognome, email);
        this.ruoloChiesa = ruoloChiesa;
    }
    @Override
    public void presentati(){
        System.out.println("Ciao, Sono " + getNome() + " " + getCognome() + ", e sono il Testimone!");
    }
    public boolean isRuoloChiesa() { return ruoloChiesa;}
}
