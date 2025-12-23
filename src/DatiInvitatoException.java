public class DatiInvitatoException extends Exception {
    public DatiInvitatoException(String messaggio) {
        super(messaggio);
    }

    public DatiInvitatoException(String messaggio, Throwable causa) {
        super(messaggio, causa);
    }
}
