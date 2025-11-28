public class Invitato {
    private String nome;
    private String cognome;
    private String email;
    private boolean confermato;
    private static int contatoreInvitati = 0;
    private final int idInvitato;

    public Invitato(String nome, String cognome, String email){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.confermato = false;
        this.idInvitato = ++contatoreInvitati;
    }
    public void presentati(){
        System.out.println("Ciao sono " + nome + " " + cognome);
    }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
    public int getIdInvitato(){ return idInvitato;}
    public boolean isConfermato() { return confermato; }
    public void setConfermato(boolean confermato) { this.confermato = confermato; }
    public static int getTotaleInvitati() {return contatoreInvitati;}
}
