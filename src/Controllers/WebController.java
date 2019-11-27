package Controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ToDoMain.*;

import org.springframework.http.HttpStatus;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@Controller
@SessionAttributes("user")
public class WebController {

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHC = new UserHibernateController(factory);

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    @ResponseStatus(value= HttpStatus.OK)
    public String loginGet(){
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    public String loginPost(@RequestParam(value="login") String login,
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

    @RequestMapping(value = "/full", method = RequestMethod.GET)
    @ResponseStatus(value=HttpStatus.OK)
    @ResponseBody
    public String full(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(User.class, new UserGSONSerializer());
        Gson parser = gsonBuilder.create();

        List<User> allUsers = userHC.findUserEntities();
        return parser.toJson(allUsers);

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
