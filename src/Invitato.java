import java.time.LocalDate;

public class Invitato {
    // ========== ATTRIBUTI ==========
    private String nome;
    private String cognome;
    private String email;
    private boolean confermato;
    private static int contatoreInvitati = 0;
    private final int idInvitato;
    private LocalDate dataRisposta;

    // ========== COSTRUTTORE ==========
    public Invitato(String nome, String cognome, String email){
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.confermato = false;
        this.idInvitato = ++contatoreInvitati;
    }

    // ========== METODI==========
    public void presentati(){
        System.out.println("Ciao sono " + nome + " " + cognome);
    }
    public String getNome() {
        return nome;
    }

    public String getCognome() {
        return cognome;
    }

    public String getEmail() {
        return email;
    }

    public int getIdInvitato(){
        return idInvitato;
    }

    public boolean isConfermato() {
        return confermato;
    }

    public void setConfermato(boolean confermato) {
        this.confermato = confermato;
    }

    public static int getTotaleInvitati() {
        return contatoreInvitati;
    }

    public LocalDate getDataRisposta() {
        return dataRisposta;
    }

    public void setDataRisposta(LocalDate dataRisposta){
        this.dataRisposta = dataRisposta;
    }

    public boolean haRisposto(){
        return dataRisposta != null;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Invitato altro = (Invitato) obj;
        return email.equals(altro.email);
    }

    @Override
    public int hashCode(){
        return email.hashCode();
    }
}
