package com.wedding.dao;

import com.wedding.model.Invitato;
import com.wedding.model.StatoInvitato;
import com.wedding.model.Tavolo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per la gestione della tabella 'invitati'.
 * Fornisce metodi CRUD per interagire con i dati degli invitati nel database.
 */
public class InvitatoDAO {

    /**
     * Salva un nuovo invitato nel database.
     * @param invitato L'oggetto com.wedding.model.Invitato da salvare.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void salva(Invitato invitato) throws SQLException {
        String sql = "INSERT INTO invitati (email, nome, cognome, stato, numero_tavolo) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, invitato.getEmail());
            pstmt.setString(2, invitato.getNome());
            pstmt.setString(3, invitato.getCognome());
            pstmt.setString(4, invitato.getStato().name());

            // Gestione del tavolo assegnato
            if (invitato.getTavoloAssegnato() != null) {
                pstmt.setInt(5, invitato.getTavoloAssegnato().getNumeroTavolo());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }

            pstmt.executeUpdate();
            System.out.println("💾 com.wedding.model.Invitato salvato sul DB: " + invitato.getEmail());
        }
    }

    /**
     * Carica tutti gli invitati dal database.
     * @return Una lista di oggetti com.wedding.model.Invitato.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Invitato> caricaTutti() throws SQLException {
        List<Invitato> lista = new ArrayList<>();
        String sql = "SELECT * FROM invitati";

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String email = rs.getString("email");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String statoStr = rs.getString("stato");
                int numeroTavolo = rs.getInt("numero_tavolo"); // Leggiamo il numero del tavolo

                Invitato inv = new Invitato(nome, cognome, email);

                try {
                    inv.setStato(StatoInvitato.valueOf(statoStr));
                } catch (IllegalArgumentException e) {
                    inv.setStato(StatoInvitato.IN_ATTESA);
                }

                // Se il numero del tavolo non è 0 (o null), lo impostiamo
                if (numeroTavolo > 0) {
                    // Creiamo un oggetto com.wedding.model.Tavolo "segnaposto" con solo il numero.
                    inv.setTavoloAssegnato(new Tavolo(numeroTavolo, 0));
                }

                lista.add(inv);
            }
        }
        return lista;
    }

    /**
     * Elimina un invitato dal database usando la sua email.
     * @param email L'email dell'invitato da eliminare.
     * @return true se l'invitato è stato eliminato, false altrimenti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public boolean elimina(String email) throws SQLException {
        String sql = "DELETE FROM invitati WHERE email = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            int righeCancellate = pstmt.executeUpdate();

            return righeCancellate > 0; // true se ha trovato e cancellato qualcuno
        }
    }

    /**
     * Aggiorna lo stato di un invitato nel database.
     * @param email L'email dell'invitato da aggiornare.
     * @param nuovoStato Il nuovo stato da impostare.
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public boolean aggiornaStato(String email, StatoInvitato nuovoStato) throws SQLException {
        String sql = "UPDATE invitati SET stato = ? WHERE email = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuovoStato.name());
            pstmt.setString(2, email);

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;
        }
    }

    /**
     * Aggiorna il tavolo assegnato a un invitato nel database.
     * @param email L'email dell'invitato.
     * @param numeroTavolo Il numero del nuovo tavolo (può essere null per rimuovere l'assegnazione).
     * @return true se l'aggiornamento è avvenuto con successo, false altrimenti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public boolean aggiornaTavolo(String email, Integer numeroTavolo) throws SQLException {
        String sql = "UPDATE invitati SET numero_tavolo = ? WHERE email = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (numeroTavolo != null) {
                pstmt.setInt(1, numeroTavolo);
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setString(2, email);

            int righeAggiornate = pstmt.executeUpdate();
            return righeAggiornate > 0;
        }
    }

    // ========== METODI PER TRANSAZIONI (Overloading) ==========

    /**
     * Versione transazionale di aggiornaTavolo.
     * Usa una connessione esistente e NON la chiude (la gestisce il chiamante).
     */
    public boolean aggiornaTavolo(Connection conn, String email, Integer numeroTavolo) throws SQLException {
        String sql = "UPDATE invitati SET numero_tavolo = ? WHERE email = ?";


        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {

            if (numeroTavolo != null) {
                pstmt.setInt(1, numeroTavolo);
            } else {
                pstmt.setNull(1, java.sql.Types.INTEGER);
            }
            pstmt.setString(2, email);

            return pstmt.executeUpdate() > 0;
        }
    }

    /**
     * Versione transazionale di aggiornaStato.
     */
    public boolean aggiornaStato(Connection conn, String email, StatoInvitato nuovoStato) throws SQLException {
        String sql = "UPDATE invitati SET stato = ? WHERE email = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuovoStato.name());
            pstmt.setString(2, email);

            return pstmt.executeUpdate() > 0;
        }
    }
}