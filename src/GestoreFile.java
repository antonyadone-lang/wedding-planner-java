import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe di utilità per la gestione dell'Input/Output su file.
 * Si occupa esclusivamente di leggere e scrivere dati, senza contenere logica di business.
 */
public class GestoreFile {

    // =================================================================================
    // GESTIONE FILE DI TESTO (Impostazioni)
    // =================================================================================

    public static ImpostazioniMatrimonio caricaImpostazioni(String nomeFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(nomeFile))) {
            String rigaBudget = reader.readLine();
            String rigaData = reader.readLine();
            String rigaLocation = reader.readLine();

            // Gestione robusta: se il file è vuoto o mancano righe
            double budget = (rigaBudget != null) ? Double.parseDouble(rigaBudget) : 0.0;
            String data = (rigaData != null) ? rigaData : "Non Impostata";
            String location = (rigaLocation != null) ? rigaLocation : "Non Impostata";

            return new ImpostazioniMatrimonio(budget, data, location);

        } catch (FileNotFoundException e) {
            System.err.println("ERRORE: File impostazioni non trovato -" + nomeFile);
            return new ImpostazioniMatrimonio(50000.0, "Non Impostata", "Non Impostata"); // Default
        } catch (IOException | NumberFormatException e) {
            System.err.println("ERRORE: Impossibile leggere il file impostazioni -" + nomeFile);
            return new ImpostazioniMatrimonio(50000.0, "Non Impostata", "Non Impostata"); // Default
        }
    }

    // =================================================================================
    // GESTIONE FILE CSV (Invitati)
    // =================================================================================

    /**
     * Scrive la lista di invitati su un file CSV.
     * @param listaInvitati La lista da salvare.
     * @param nomeFile Il percorso del file.
     * @throws IOException Rilancia l'eccezione al chiamante (WeddingManager).
     */
    public static void salvaInvitatiSuFile(List<Invitato> listaInvitati, String nomeFile) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFile))) {
            for (Invitato inv : listaInvitati) {
                String rigaCSV = String.format("%s,%s,%s,%s",
                        inv.getNome(),
                        inv.getCognome(),
                        inv.getEmail(),
                        inv.getStato());
                writer.write(rigaCSV);
                writer.newLine();
            }
        }
    }

    /**
     * Legge un file CSV e restituisce una lista di oggetti Invitato.
     * @param nomeFile Il percorso del file da leggere.
     * @return Una lista di invitati caricati.
     */
    public static List<Invitato> caricaInvitatiDaFile(String nomeFile) {
        List<Invitato> invitatiCaricati = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(nomeFile))) {
            String riga;
            while ((riga = reader.readLine()) != null) {
                try {
                    String[] dati = riga.split(",");

                    if (dati.length < 4) {
                        throw new DatiInvitatoException("Riga corrotta o incompleta: " + riga);
                    }

                    String nome = dati[0];
                    String cognome = dati[1];
                    String email = dati[2];
                    String statoStr = dati[3];

                    Invitato invitato = new Invitato(nome, cognome, email);
                    try {
                        StatoInvitato stato = StatoInvitato.valueOf(statoStr);
                        invitato.setStato(stato);
                    }catch (IllegalArgumentException e) {
                        if (statoStr.equalsIgnoreCase("true")) {
                            invitato.setStato(StatoInvitato.CONFERMATO);
                        }else if (statoStr.equalsIgnoreCase("false")) {
                            invitato.setStato(StatoInvitato.IN_ATTESA);
                        }else {
                            invitato.setStato(StatoInvitato.DA_RICONTATTARE);
                        }
                    }

                    invitatiCaricati.add(invitato);

                } catch (DatiInvitatoException e) {
                    System.err.println("[LOG ERRORE] Salto riga non valida: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Errore lettura file invitati: " + e.getMessage());
        }

        return invitatiCaricati;
    }
}