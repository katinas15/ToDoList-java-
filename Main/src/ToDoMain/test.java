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
        CompanyHibernateController companyHC = new CompanyHibernateController(factory);
        String company = "{\"login\":\"t\",\"pass\":\"tt\"}";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Company.class, new CompanyGSONSerializer());
        Gson parser = gsonBuilder.create();
        Company paduotas  = parser.fromJson(company, Company.class);
        Company getCompany = companyHC.findCompany(paduotas.getLogin());
        if(getCompany != null){
            if(paduotas.getLogin().equals(getCompany.getLogin()) && paduotas.getPass().equals(getCompany.getPass())){
                System.out.println(parser.toJson(getCompany));
            }
        }
    }

}
