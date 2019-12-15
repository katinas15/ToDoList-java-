package WebControllers;

import GSONSerializers.ProjectGSONSerializer;
import GSONSerializers.UserGSONSerializer;
import GSONSerializers.UserProjectGSONSerializer;
import Hibernate.CompanyHibernateController;
import Objektai.Company;
import Objektai.Project;
import Objektai.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Type;
import java.util.List;

@Controller
public class WebCompanyController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    CompanyHibernateController companyHC = new CompanyHibernateController(factory);

    @RequestMapping(value = "/company/user", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String getCompanyUsers(@RequestParam String reqToken){
        Token token = new Token();
        int companyId = token.checkToken(reqToken);
        if(companyId  == 0) return "Incorrect Token";

        Company currentCompany  = companyHC.findCompany(companyId);
        List<User> users = currentCompany.getUsers();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gsonBuilder.create();
        parser.toJson(users.get(0)); //kazkodel reikia bent karta parsint ir tada veikia su list

        return parser.toJson(users);

    }

    @RequestMapping(value = "/company/project", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String getCompanyProjects(@RequestParam String reqToken){
        Token token = new Token();
        int companyId = token.checkToken(reqToken);
        if(companyId  == 0) return "Incorrect Token";

        Company currentCompany  = companyHC.findCompany(companyId);
        List<Project> projects = currentCompany.getProjects();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Project.class, new ProjectGSONSerializer());
        Gson parser = gsonBuilder.create();
        parser.toJson(projects.get(0)); //kazkodel reikia bent karta parsint ir tada veikia su list

        Type projectList = new TypeToken<List<Project>>() {}.getType();
        gsonBuilder.registerTypeAdapter(projectList, new UserProjectGSONSerializer());
        parser = gsonBuilder.create();
        return parser.toJson(projects);

    }
}
