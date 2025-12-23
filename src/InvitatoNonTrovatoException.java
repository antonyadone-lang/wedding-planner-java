public class InvitatoNonTrovatoException extends RuntimeException {
    public InvitatoNonTrovatoException(String email){
        super("Attenzione: Nessun invitato trovato con l'email: " + email);
    }
}
