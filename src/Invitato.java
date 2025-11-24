public class Invitato {
    private String nome;
    private String cognome;
    private String email;
    private boolean confermato;

    public Invitato(String nome, String cognome, String email){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.confermato = false;
    }
    public void presentati(){
        System.out.println("Ciao sono " + nome + " " + cognome);
    }
    public String getNome() { return nome; }
    public String getCognome() { return cognome; }
    public String getEmail() { return email; }
    public boolean isConfermato() { return confermato; }
    public void setConfermato(boolean confermato) { this.confermato = confermato; }
}
