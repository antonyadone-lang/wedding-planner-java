/**
 * Eccezione base per tutto il progetto WeddingPlanner.
 * Tutte le eccezioni specifiche (es. TavoloPieno, BudgetSuperato) devono estendere questa classe.
 */
public class WeddingException extends RuntimeException {
   public WeddingException(String message) {
       super(message);
   }
   // Costruttore con cause (utile per il chaining delle eccezioni)
    public WeddingException(String message,Throwable cause) {
       super(message, cause);
    }
}
