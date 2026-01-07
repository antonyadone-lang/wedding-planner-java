import java.sql.Connection;
import java.sql.DriverManager;

public class TestConnessione {
    public static void main(String[] args) {
        String url= "jdbc:mysql://localhost:3306/wedding_db";
        String user= "root";
        String password = "password";

        try {
            Connection con = DriverManager.getConnection(url, user, password);
            System.out.println("Connessione riuscita, Java e MySQL si parlano.");
            con.close();
        }catch (Exception e) {
            System.out.println("Errore di connessione:");
            e.printStackTrace();
        }
    }
}
