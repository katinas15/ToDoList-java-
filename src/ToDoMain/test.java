package ToDoMain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.*;

public class test {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        User currentUser = null;
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
        UserHibernateController userHC = new UserHibernateController(factory);
        String user = "{\"login\":\"w\", \"pass\":\"w\"}";

        JsonParser jsonParser = new JsonParser();
        JsonObject element = (JsonObject)jsonParser.parse(user);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gsonBuilder.create();

            User paduotas  = parser.fromJson(user, User.class);
            User getUser = userHC.findUser(paduotas.getLogin());
            if(getUser != null){
                if(paduotas.getLogin().equals(getUser.getLogin()) && paduotas.getPass().equals(getUser.getPass())){

                    String a = parser.toJson(getUser);
                    System.out.println(a);
                }
            }



//        Class.forName("com.mysql.cj.jdbc.Driver");
//        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cms", "root", "");
//        String query = "SELECT * FROM USERS";
//        Statement stat = conn.createStatement();
//        ResultSet rs = stat.executeQuery(query);
//        while(rs.next()){
//            int id = rs.getInt("user_id");
//            String firstName = rs.getString("user_firstname");
//            System.out.println(id + " " + firstName);
//        }
//        conn.close();
    }

}
