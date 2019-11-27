package ToDoMain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.List;

public class test {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
        UserHibernateController userHC = new UserHibernateController(factory);
        String user = "{\"id\":1}";

        JsonParser jsonParser = new JsonParser();
        JsonObject element = (JsonObject)jsonParser.parse(user);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gsonBuilder.create();

        User obj  = parser.fromJson(user, User.class);

        User currentUser = userHC.findUser(obj.getId());
        List<Project> projects = currentUser.getAllProjects();


//        gsonBuilder.registerTypeAdapter(Project.class, new ProjectGSONSerializer());
//        parser = gsonBuilder.create();
//        System.out.println(parser.toJson(projects.get(0)));

        Type projectList = new TypeToken<List<Project>>() {}.getType();
        gsonBuilder.registerTypeAdapter(projectList, new UserProjectGSONSerializer());
        parser = gsonBuilder.create();
        System.out.println(parser.toJson(projects));



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
