import java.io.Serializable;
import java.time.LocalDate;

public class Invitato implements Serializable {
    private static int contatoreInvitati = 0;
    private final int idInvitato;
    // ========== ATTRIBUTI ==========
    private final String nome;
    private final String cognome;
    private final String email;
    private boolean confermato;
    private LocalDate dataRisposta;
    private Tavolo tavoloAssegnato;
    private String dietaSpeciale;
    private String allergie;
    private String numeroTelefono;

    // ========== COSTRUTTORE ==========
    public Invitato(String nome, String cognome, String email) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.confermato = false;
        this.idInvitato = ++contatoreInvitati;
    }

    private Invitato (Builder builder){
        this.nome = builder.nome;
        this.cognome = builder.cognome;
        this.email = builder.email;
        this.confermato = builder.confermato;
        this.dietaSpeciale = builder.dietaSpeciale;
        this.allergie = builder.allergie;
        this.numeroTelefono = builder.numeroTelefono;
        this.idInvitato = ++contatoreInvitati;
    }

    public static int getTotaleInvitati() {
        return contatoreInvitati;
    }

    // ========== METODI==========
    public void presentati() {
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

    public int getIdInvitato() {
        return idInvitato;
    }

    public boolean isConfermato() {
        return confermato;
    }

    public void setConfermato(boolean confermato) {
        this.confermato = confermato;
    }

    public LocalDate getDataRisposta() {
        return dataRisposta;
    }

    public void setDataRisposta(LocalDate dataRisposta) {
        this.dataRisposta = dataRisposta;
    }

    public boolean haRisposto() {
        return dataRisposta != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;

        Invitato altro = (Invitato) obj;
        return email.equals(altro.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    //Getter
    public Tavolo getTavoloAssegnato() {
        return tavoloAssegnato;
    }

    //Setter
    public void setTavoloAssegnato(Tavolo tavolo) {
        this.tavoloAssegnato = tavolo;
    }

    public String getDietaSpeciale() {
        return dietaSpeciale;
    }

    public String getAllergie(){
        return allergie;
    }

    public String getNumeroTelefono(){
        return numeroTelefono;
    }

    public static class Builder {
        private final String nome;
        private final String cognome;
        private final String email;
        private boolean confermato = false;
        private String dietaSpeciale;
        private String allergie;
        private String numeroTelefono;

        public Builder(String nome, String cognome, String email){
            this.nome = nome;
            this.cognome = cognome;
            this.email = email;
        }

        public Builder confermato(boolean confermato){
            this.confermato = confermato;
            return this;
        }

        public Builder dietaSpeciale(String dietaSpeciale){
            this.dietaSpeciale = dietaSpeciale;
            return this;
        }

        public Builder allergie(String allergie){
            this.allergie = allergie;
            return this;
        }

        public Builder numeroTelefono(String numeroTelefono){
            this.numeroTelefono = numeroTelefono;
            return this;
        }

        public Invitato build(){
            return new Invitato(this);
        }
    }

    @Override
    public String toString() {
        return "Invitato{" +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", email='" + email + '\'' +
                ", statoConferma=" + confermato +
                '}';
    }

}
