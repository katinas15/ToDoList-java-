package WebControllers;

import GSONSerializers.ProjectGSONSerializer;
import GSONSerializers.UserProjectGSONSerializer;
import Hibernate.ProjectHibernateController;
import Hibernate.UserHibernateController;
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
import java.util.Properties;

@Controller
public class WebUserController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHC = new UserHibernateController(factory);
    ProjectHibernateController projectHC = new ProjectHibernateController(factory);

    @RequestMapping(value = "/user/project", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String userProjectsGet(@RequestParam String reqToken){
        Token token = new Token();
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        User currentUser = userHC.findUser(userId);
        List<Project> projects = currentUser.getAllProjects();

        for(Company c:userHC.findUser(currentUser.getId()).getCompanies()){
            for(Project p:c.getProjects()){
                boolean same = false;
                for(Project pu:userHC.findUser(currentUser.getId()).getAllProjects()){
                    if(pu.equals(p)) same = true;
                }
                if(!same) {
                    projects.add(p);
                }
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Project.class, new ProjectGSONSerializer());
        Gson parser = gsonBuilder.create();
        parser.toJson(projects.get(0)); //kazkodel reikia bent karta parsint ir tada veikia su list

        Type projectList = new TypeToken<List<Project>>() {}.getType();
        gsonBuilder.registerTypeAdapter(projectList, new UserProjectGSONSerializer());
        parser = gsonBuilder.create();
        return parser.toJson(projects);
    }


    @RequestMapping(value = "/user/project", method = RequestMethod.POST)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String userProjectsPost(@RequestBody String project) throws Exception {
        Gson parser = new Gson();
        Properties data = parser.fromJson(project, Properties.class);
        String title = data.getProperty("title");

        String reqToken = data.getProperty("reqToken");
        Token token = new Token();
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        User currentUser = userHC.findUser(userId);

        Project proj = new Project(title, currentUser);
        projectHC.create(proj);

        if(proj.getId() != 0) return "Created";
        return "Error creating";
    }


    @RequestMapping(value = "/user/project", method = RequestMethod.PUT)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String userProjectsPut(@RequestBody String project) throws Exception {
        Gson parser = new Gson();
        Properties data = parser.fromJson(project, Properties.class);
        String title = data.getProperty("title");
        String projectId = data.getProperty("projectId");
        String completed = data.getProperty("completed");
        String reopen = data.getProperty("reopen");

        String reqToken = data.getProperty("reqToken");
        Token token = new Token();
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        Project currentProject = projectHC.findProject(Integer.parseInt(projectId));
        if(currentProject == null) return "bad project id";

        if(title != null){
            currentProject.setTitle(title);
        }

        if(completed != null){
            if(Boolean.parseBoolean(completed)){
                currentProject.completeProject(userHC.findUser(userId));
            }
        }

        if(reopen != null){
            if(Boolean.parseBoolean(reopen)){
                currentProject.reopenProject();
            }
        }

        projectHC.edit(currentProject);
        return "Edited";
    }


    @RequestMapping(value = "/user/project", method = RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String userProjectsDelete(@RequestBody String project) throws Exception {
        Gson parser = new Gson();
        Properties data = parser.fromJson(project, Properties.class);
        String projectId = data.getProperty("projectId");

        String reqToken = data.getProperty("reqToken");
        Token token = new Token();
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        Project currentProject = projectHC.findProject(Integer.parseInt(projectId));

        projectHC.destroy(currentProject.getId());
        return "Deleted";
    }
}
