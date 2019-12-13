package Objektai;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    @ManyToMany (cascade=CascadeType.MERGE)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Project> projects = new ArrayList();

    @ManyToMany (cascade=CascadeType.MERGE)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList();

    public Company(){}

    public String getPass() {
        return pass;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getLogin() {
        return login;
    }

    //CREATE
    public Company(String login, String pass, String companyName, User owner) {
        this.companyName = companyName;
        this.owner = owner;
        this.login = login;
        this.pass = pass;
        addUser(owner);
    }

    public boolean addProject(Project p) {
        for(Project proj:projects){
            if(proj == p) return false;
        }
        this.projects.add(p);
        return true;
    }

    public boolean addUser(User u) {
        for(User user:users){
            if(user.getLogin().equals(u.getLogin())) return false;
        }
        this.users.add(u);
        return true;
    }


    public void setPass(String pass) {
        this.pass = pass;
    }

    //READ
    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return "ID - " + id + " | NAME - " + companyName + " | IS ACTIVE? - " + active + " | OWNER ID - " + owner.getId();
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Company)) {
            return false;
        }
        Company other = (Company) o;
        return this.toString().equals(other.toString());
    }
}
