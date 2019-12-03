package com.example.bnd.lab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Company implements Serializable {
    private static final long serialVersionUID = 4L;

    private int id;
    private String login, pass, companyName;
    private static int idCounter = 1;
    private boolean active = true;

    private String owner;

    private List<com.example.bnd.lab.Project> projects = new ArrayList();

    private List<User> users = new ArrayList();

    public Company(){}
    //CREATE
    public Company(String login, String pass, String companyName, String owner) {
        this.companyName = companyName;
        this.owner = owner;
        this.login = login;
        this.pass = pass;

    }

    public boolean addProject(com.example.bnd.lab.Project p) {
        for(com.example.bnd.lab.Project proj:projects){
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



    //READ
    public int getId() {
        return id;
    }

    @Override
    public String toString(){
        return "ID - " + id + " | NAME - " + companyName + " | IS ACTIVE? - " + active + " | OWNER ID - " + owner;
    }
    public List<com.example.bnd.lab.Project> getProjects(){
        return projects;
    }

    public List<User> getUsers(){
        return users;
    }

    public boolean isActive() {
        return active;
    }


    //UPDATE
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void changeActivity(){
        this.active = !this.active;
    }

    public static void changeIdCounter(int counter){
        idCounter = counter;
    }



    //DELETE
    public void removeUser(User u) {
        this.users.remove(u);
    }

    public void removeProject(com.example.bnd.lab.Project p){ this.projects.remove(p); }
}
