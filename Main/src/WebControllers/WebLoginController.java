package WebControllers;

import Hibernate.CompanyHibernateController;
import Hibernate.UserHibernateController;
import Objektai.Company;
import Objektai.User;
import com.google.gson.Gson;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Properties;

import static FXController.FxMain.passwordSalt;

@Controller
public class WebLoginController {
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHC = new UserHibernateController(factory);
    CompanyHibernateController companyHC = new CompanyHibernateController(factory);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public String loginPost(@RequestBody String req) {
        Gson parser = new Gson();
        Properties data = parser.fromJson(req, Properties.class);
        String login = data.getProperty("login");
        String pass = data.getProperty("pass");
        User getUser = userHC.findUser(login);
        if (getUser == null) {
            return "Neteisingai ivesti duomenys";
        }

        if (login.equals(getUser.getLogin())) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            if (bCryptPasswordEncoder.matches(pass + passwordSalt, getUser.getPass())) {
                Token token = new Token();
                return token.createToken(getUser.getId());
            }
        }

        return "Neteisingai ivesti duomenys";
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
                Token token = new Token();
                return token.createToken(getCompany.getId());
            }
        }

        return "Neteisingai ivesti duomenys";
    }

}
