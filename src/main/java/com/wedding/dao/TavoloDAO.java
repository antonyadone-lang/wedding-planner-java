package com.wedding.dao;

import com.wedding.model.Tavolo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO per la gestione della tabella 'tavoli'.
 * Fornisce metodi CRUD per interagire con i dati dei tavoli nel database.
 */
public class TavoloDAO {

    /**
     * Salva un nuovo tavolo nel database.
     * @param tavolo L'oggetto com.wedding.model.Tavolo da salvare.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public void salva(Tavolo tavolo) throws SQLException {
        String sql = "INSERT INTO tavoli (numero_tavolo, capacita) VALUES (?, ?)";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tavolo.getNumeroTavolo());
            pstmt.setInt(2, tavolo.getCapacitaMassima());

            pstmt.executeUpdate();
        }
    }

    /**
     * Carica tutti i tavoli dal database.
     * @return Una lista di oggetti com.wedding.model.Tavolo.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public List<Tavolo> caricaTutti() throws SQLException {
        List<Tavolo> tavoli = new ArrayList<>();
        String sql = "SELECT numero_tavolo, capacita FROM tavoli";

        try (Connection conn = ConnessioneDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int numeroTavolo = rs.getInt("numero_tavolo");
                int capacita = rs.getInt("capacita");
                Tavolo tavolo = new Tavolo(numeroTavolo, capacita);
                tavoli.add(tavolo);
            }
        }
        return tavoli;
    }

    /**
     * Cerca un tavolo specifico per il suo numero.
     * @param numeroTavolo Il numero del tavolo da cercare.
     * @return L'oggetto com.wedding.model.Tavolo se trovato, altrimenti null.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public Tavolo cercaByNumero(int numeroTavolo) throws SQLException {
        String sql = "SELECT numero_tavolo, capacita FROM tavoli WHERE numero_tavolo = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numeroTavolo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int numTavolo = rs.getInt("numero_tavolo");
                    int capacita = rs.getInt("capacita");
                    return new Tavolo(numTavolo, capacita);
                }
            }
        }
        return null; // com.wedding.model.Tavolo non trovato
    }

    /**
     * Elimina un tavolo dal database usando il suo numero.
     * @param numeroTavolo Il numero del tavolo da eliminare.
     * @return true se il tavolo è stato eliminato, false altrimenti.
     * @throws SQLException Se si verifica un errore SQL.
     */
    public boolean elimina(int numeroTavolo) throws SQLException {
        String sql = "DELETE FROM tavoli WHERE numero_tavolo = ?";

        try (Connection conn = ConnessioneDB.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, numeroTavolo);
            int righeCancellate = pstmt.executeUpdate();

            return righeCancellate > 0;
        }
    }
}