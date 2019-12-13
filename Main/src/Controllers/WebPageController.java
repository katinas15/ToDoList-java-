package Controllers;

import GSONSerializers.UserGSONSerializer;
import Hibernate.ProjectHibernateController;
import Hibernate.TaskHibernateController;
import Hibernate.UserHibernateController;
import Objektai.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class WebPageController {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHC = new UserHibernateController(factory);
    ProjectHibernateController projectHC = new ProjectHibernateController(factory);
    TaskHibernateController taskHC = new TaskHibernateController(factory);

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
