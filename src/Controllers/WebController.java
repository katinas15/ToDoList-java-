package Controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ToDoMain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

@Controller
public class WebController {

    User currentUser = null;
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHC = new UserHibernateController(factory);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(value= HttpStatus.OK)
    @ResponseBody
    public String login(@RequestBody String user){
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
    public String index(ModelMap model){
        model.addAttribute("kintamasis", 15);
        return "index";
    }



}
