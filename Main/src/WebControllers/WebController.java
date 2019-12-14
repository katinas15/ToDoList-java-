package WebControllers;


import GSONSerializers.*;
import Hibernate.CompanyHibernateController;
import Hibernate.ProjectHibernateController;
import Hibernate.TaskHibernateController;
import Hibernate.UserHibernateController;
import Objektai.Company;
import Objektai.Project;
import Objektai.Task;
import Objektai.User;
import WebControllers.Token;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Type;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import static FXController.FxMain.passwordSalt;

@Controller
@SessionAttributes("user")
public class WebController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHC = new UserHibernateController(factory);
    ProjectHibernateController projectHC = new ProjectHibernateController(factory);
    TaskHibernateController taskHC = new TaskHibernateController(factory);
    CompanyHibernateController companyHC = new CompanyHibernateController(factory);
    Token token = new Token();

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String loginPost(@RequestBody String req){
        Gson parser = new Gson();
        Properties data = parser.fromJson(req, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        User getUser = userHC.findUser(login);
        if(getUser == null) {
            return "Neteisingai ivesti duomenys";
        }

        if(login.equals(getUser.getLogin())){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if(bCryptPasswordEncoder.matches(pass + passwordSalt, getUser.getPass())) {
                return token.createToken(getUser.getId());
            }
        }

        return "Neteisingai ivesti duomenys";
    }

    @RequestMapping(value = "/user/all", method = RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String full(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gsonBuilder.create();

        List<User> allUsers = userHC.findUserEntities();
        return parser.toJson(allUsers);
    }





    @RequestMapping(value = "/user/project", method = RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String userProjectsGet(@RequestParam String reqToken){
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
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        Project currentProject = projectHC.findProject(Integer.parseInt(projectId));

        projectHC.destroy(currentProject.getId());
        return "Deleted";
    }





    private void addSubTasks(Task t, List<Task> taskList){
        List<Task> allSubTasks = t.getSubTasks();
        for(Task s:allSubTasks){
            taskList.add(s);
            addSubTasks(s,taskList);
        }

    }

    @RequestMapping(value = "/project/task", method = RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String projectTasksGet(@RequestParam String reqToken, @RequestParam String id){
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        Project currentProject = projectHC.findProject(Integer.parseInt(id));

        List<Task> tasks = new ArrayList<>();

        for(Task t:currentProject.getTasks()){
            if(t.getParentTask() == null){
                tasks.add(t);
                addSubTasks(t,tasks);
            }
        }

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Task.class, new TaskGSONSerializer());
        Gson parser = gsonBuilder.create();
        parser.toJson(tasks.get(0)); //kazkodel reikia bent karta parsint ir tada veikia su list

        Type taskList = new TypeToken<List<Task>>() {}.getType();
        gsonBuilder.registerTypeAdapter(taskList, new UserProjectGSONSerializer());
        parser = gsonBuilder.create();
        return parser.toJson(tasks);
    }


    @RequestMapping(value = "/project/task", method = RequestMethod.POST)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String projectTasksPost(@RequestBody String task) throws Exception {
        Gson parser = new Gson();
        Properties data = parser.fromJson(task, Properties.class);
        String title = data.getProperty("title");
        String projectId = data.getProperty("projectId");
        String taskId = data.getProperty("taskId");

        String reqToken = data.getProperty("reqToken");
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        User currentUser = userHC.findUser(userId);
        Project currentProject = projectHC.findProject(Integer.parseInt(projectId));

        Task tsk = null;
        if(taskId != null){
            Task parentTask = taskHC.findTask(Integer.parseInt(taskId));
            tsk = new Task(title, currentProject, parentTask, currentUser);
        } else {
            tsk = new Task(title, currentProject, currentUser);
        }

        taskHC.create(tsk);

        if(tsk.getId() != 0) return "Created";
        return "Error creating";
    }


    @RequestMapping(value = "/project/task", method = RequestMethod.PUT)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String projectTasksPut(@RequestBody String task) throws Exception {
        Gson parser = new Gson();
        Properties data = parser.fromJson(task, Properties.class);
        String title = data.getProperty("title");
        String taskId = data.getProperty("taskId");
        String completed = data.getProperty("completed");
        String reopen = data.getProperty("reopen");

        String reqToken = data.getProperty("reqToken");
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        Task currentTask = taskHC.findTask(Integer.parseInt(taskId));
        if(currentTask == null) return "bad task id";

        if(title != null){
            currentTask.setTitle(title);
        }

        if(completed != null){
            if(Boolean.parseBoolean(completed)){
                User u = userHC.findUser(userId);
                currentTask.completeTask(u);
            }
        }

        if(reopen != null){
            if(Boolean.parseBoolean(reopen)){
                currentTask.reopenTask();
            }
        }

        taskHC.edit(currentTask);
        return "Edited";
    }

    @RequestMapping(value = "/project/task", method = RequestMethod.DELETE)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String projectTasksDelete(@RequestBody String task) throws Exception {
        Gson parser = new Gson();
        Properties data = parser.fromJson(task, Properties.class);
        String taskId = data.getProperty("taskId");

        String reqToken = data.getProperty("reqToken");
        int userId = token.checkToken(reqToken);
        if(userId  == 0) return "Incorrect Token";

        Task currentTask = taskHC.findTask(Integer.parseInt(taskId));

        taskHC.destroy(currentTask.getId());
        return "Deleted";
    }










    @RequestMapping(value = "/login/company", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String loginPostCompany(@RequestBody String req){
        Gson parser = new Gson();
        Properties data = parser.fromJson(req, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");

        Company getCompany = companyHC.findCompany(login);
        if(getCompany == null) {
            return "Neteisingai ivesti duomenys";
        }

        if(login.equals(getCompany.getLogin())){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if(bCryptPasswordEncoder.matches(pass + passwordSalt, getCompany.getPass())) {
                return token.createToken(getCompany.getId());
            }
        }

        return "Neteisingai ivesti duomenys";
    }


    @RequestMapping(value = "/company/user", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String getCompanyUsers(@RequestParam String reqToken){
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
