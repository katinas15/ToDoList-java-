package WebControllers;

import GSONSerializers.TaskGSONSerializer;
import GSONSerializers.UserProjectGSONSerializer;
import Hibernate.CompanyHibernateController;
import Hibernate.ProjectHibernateController;
import Hibernate.TaskHibernateController;
import Hibernate.UserHibernateController;
import Objektai.Project;
import Objektai.Task;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
public class WebProjectController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHC = new UserHibernateController(factory);
    ProjectHibernateController projectHC = new ProjectHibernateController(factory);
    TaskHibernateController taskHC = new TaskHibernateController(factory);

    private void addSubTasks(Task t, List<Task> taskList){
        List<Task> allSubTasks = t.getSubTasks();
        for(Task s:allSubTasks){
            taskList.add(s);
            addSubTasks(s,taskList);
        }

    }

    @RequestMapping(value = "/project/task", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String projectTasksGet(@RequestParam String reqToken, @RequestParam String id){
        Token token = new Token();
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
        Token token = new Token();
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
        Token token = new Token();
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
        Token token = new Token();
        int userId = token.checkToken(reqToken);
        if (userId == 0) return "Incorrect Token";

        Task currentTask = taskHC.findTask(Integer.parseInt(taskId));

        taskHC.destroy(currentTask.getId());
        return "Deleted";
    }
}
