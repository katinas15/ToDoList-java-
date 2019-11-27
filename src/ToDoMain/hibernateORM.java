package ToDoMain;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class hibernateORM {

    public static void main(String[] args) throws Exception {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
        UserHibernateController ktr = new UserHibernateController(factory);
        CompanyHibernateController ckrt = new CompanyHibernateController(factory);
        ProjectHibernateController pr = new ProjectHibernateController(factory);



        Project pro = new Project();
        pr.create(pro);

        List<Project> visiP = pr.findProjectEntities();
        for (Project u: visiP){
            System.out.println(u);
        }
        factory.close();
    }

}
