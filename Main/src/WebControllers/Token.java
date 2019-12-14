package WebControllers;

import java.security.SecureRandom;
import java.sql.*;
import java.util.Base64;

public class Token {

    private static final SecureRandom secureRandom = new SecureRandom(); //threadsafe
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder(); //threadsafe

    public Token() {
        createTable();
    }

    private static String generateNewToken() {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    private String DBurl = "jdbc:mysql://localhost/ToDoList";
    private String DBusername = "root";
    private String DBpass = "";
    private Connection conn = null;

    public void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DBurl, DBusername, DBpass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromDatabase() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createTable() {
        connectToDatabase();
        try {
            PreparedStatement ps = conn.prepareStatement("CREATE TABLE IF NOT EXISTS TOKENS"
                    + "(token  VARCHAR(255), id INTEGER, date TIMESTAMP DEFAULT CURRENT_TIMESTAMP )" );
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();
    }

    public String createToken(int id){
        connectToDatabase();
        String token = null;
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO tokens"
                    + "(token, id) VALUES"
                    + "(?, ?)");
            token = generateNewToken();
            ps.setString(1, token);
            ps.setString(2, Integer.toString(id));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            disconnectFromDatabase();
            return token;
        }
    }

    public int checkToken(String token) {
        connectToDatabase();
        int id = 0;
        try {

            String query = "select * from tokens where DATE_ADD(date, interval 30 minute) > sysdate() ORDER BY date DESC LIMIT 1 ";
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery(query);
            while (rs.next()) {
                String sqlToken = rs.getString("token");
                if(sqlToken.equals(token)){
                    id = rs.getInt("id");
                    break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            disconnectFromDatabase();
            return id;
        }
    }
}
