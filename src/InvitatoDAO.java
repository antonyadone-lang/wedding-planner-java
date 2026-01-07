import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvitatoDAO {
    private String url = "jdbc:mysql://localhost:3306/wedding_db";
    private String user = "root";
    private String password = "password";

    public void salva(Invitato invitato) throws SQLException {
        String sql = "INSERT INTO invitati (email, nome, cognome, stato) VALUES (?, ?, ? , ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password);
        PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, invitato.getEmail());
            pstmt.setString(2, invitato.getNome());
            pstmt.setString(3, invitato.getCognome());
            pstmt.setString(4, invitato.getStato().name());

            pstmt.executeUpdate();
            System.out.println("Invitato salvato sul DB");

        }
    }

    public List<Invitato> caricaTutti() throws SQLException {
        List<Invitato> lista = new ArrayList<>();
        String sql = "SELECT * FROM invitati";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String email = rs.getString("email");
                String nome = rs.getString("nome");
                String cognome = rs.getString("cognome");
                String statoStr = rs.getString("stato");

                Invitato inv = new Invitato(nome, cognome, email);

                try {
                    inv.setStato(StatoInvitato.valueOf(statoStr));
                }catch (IllegalArgumentException e) {
                    inv.setStato(StatoInvitato.IN_ATTESA);
                }
                lista.add(inv);
            }
        }
        return lista;
    }
}
