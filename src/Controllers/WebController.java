package Controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ToDoMain.*;

import com.google.gson.reflect.TypeToken;
import org.springframework.http.HttpStatus;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Properties;

@Controller
@SessionAttributes("user")
public class WebController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHC = new UserHibernateController(factory);
    ProjectHibernateController projectHC = new ProjectHibernateController(factory);
    TaskHibernateController taskHC = new TaskHibernateController(factory);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String loginPost(@RequestBody String user){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gsonBuilder.create();

        User paduotas  = parser.fromJson(user, User.class);
        User getUser = userHC.findUser(paduotas.getLogin());
        if(getUser != null){
            if(paduotas.getLogin().equals(getUser.getLogin()) && paduotas.getPass().equals(getUser.getPass())){
                return parser.toJson(getUser);
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
    public String userProjectsGet(@RequestBody String user){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gsonBuilder.create();

        User obj  = parser.fromJson(user, User.class);

        User currentUser = userHC.findUser(obj.getId());
        List<Project> projects = currentUser.getAllProjects();


        gsonBuilder.registerTypeAdapter(Project.class, new ProjectGSONSerializer());
        parser = gsonBuilder.create();
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
        String userId = data.getProperty("userId");

        User currentUser = userHC.findUser(Integer.parseInt(userId));

        Project proj = new Project(title, currentUser);
        projectHC.create(proj);

        if(proj.getId() != 0) return "Created";
        return "Error creating";
    }


    @RequestMapping(value = "/project/task", method = RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String projectTasksGet(@RequestBody String project){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Project.class, new ProjectGSONSerializer());
        Gson parser = gsonBuilder.create();

        Project obj  = parser.fromJson(project, Project.class);

        Project currentProject = projectHC.findProject(obj.getId());
        List<Task> tasks = currentProject.getTasks();

        gsonBuilder.registerTypeAdapter(Task.class, new TaskGSONSerializer());
        parser = gsonBuilder.create();
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
        String userId = data.getProperty("userId");

        User currentUser = userHC.findUser(Integer.parseInt(userId));
        Project currentProject = projectHC.findProject(Integer.parseInt(projectId));

        Task tsk = new Task(title, currentProject, currentUser);
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
        String completedBy = data.getProperty("completedBy");
        String reopen = data.getProperty("reopen");

        Task currentTask = taskHC.findTask(Integer.parseInt(taskId));
        if(title != null){
            currentTask.setTitle(title);
        }

        if(completedBy != null){
            User u = userHC.findUser(Integer.parseInt(completedBy));
            currentTask.completeTask(u);
        }

        if(reopen != null){
            if(Boolean.parseBoolean(reopen)){
                currentTask.reopenTask();
            }
        }

        taskHC.edit(currentTask);
        return "Edited";
    }




















    @RequestMapping(value = "/loginPage", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    public String loginPageGet(){
        return "login";
    }

    @RequestMapping(value = "/loginPage", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public String loginPagePost(@RequestParam(value="login") String login,
                            @RequestParam(value="pass") String pass,
                            ModelMap model){

        User getUser = userHC.findUser(login);
        if(getUser != null){
            if(login.equals(getUser.getLogin()) && pass.equals(getUser.getPass())){
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
                Gson parser = gsonBuilder.create();

                model.put("user", parser.toJson(getUser));
                return "redirect:main";
            }
        }
        model.addAttribute("error", true);
        return "login";
    }


    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String main(@RequestParam(value = "user") String user, ModelMap model){
        if(user.isEmpty()){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
            Gson parser = gsonBuilder.create();
            model.addAttribute("kintamasis", user);
        }
        else model.addAttribute("kintamasis", "Welcome");
        return "main";
    }



    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index(@ModelAttribute("user") User user, ModelMap model){
        if(user != null){
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
            Gson parser = gsonBuilder.create();
            model.addAttribute("kintamasis", parser.toJson(user));
        }
        else model.addAttribute("kintamasis", "Welcome");
        return "main";
    }





}
