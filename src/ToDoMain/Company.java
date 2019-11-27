package ToDoMain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Company implements Serializable {
    private static final long serialVersionUID = 4L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String login, pass, companyName;
    private static int idCounter = 1;
    private boolean active = true;
    @ManyToOne
    private User owner;
    @OneToMany
    private List<Project> projects = new ArrayList();
    @OneToMany
    private List<User> users = new ArrayList();

    public Company(){}
    //CREATE
    public Company(String login, String pass, String companyName, User owner) {
        this.login = login;
        this.pass = pass;
        this.companyName = companyName;
        this.owner = owner;
    }

    public void addProject(Project p) {
        this.projects.add(p);
    }

    public void addUser(User u) {
        this.users.add(u);
    }



    //READ
    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return "ID - " + id + " | LOGIN - " + login + " | NAME - " + companyName + " | IS ACTIVE? - " + active + " | OWNER ID - " + owner.getId();
    }
    public List<Project> getProjects(){
        return projects;
    }

    public List<User> getUsers(){
        return users;
    }

    public boolean isActive() {
        return active;
    }

    public User getOwner(){
        return owner;
    }


    //UPDATE
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void changeActivity(){
        this.active = !this.active;
    }

    public void setOwner(User u){
        this.owner = u;
    }

    public static void changeIdCounter(int counter){
        idCounter = counter;
    }



    //DELETE
    public void removeUser(User u) {
        this.users.remove(u);
    }

    public void removeProject(Project p){ this.projects.remove(p); }
}
