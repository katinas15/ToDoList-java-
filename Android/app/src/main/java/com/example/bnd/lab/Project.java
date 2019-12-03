package com.example.bnd.lab;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Project implements Serializable {
    private static final long serialVersionUID = 4L;

    private int id;
    private String title;

    private String createdBy, completedBy;
    private String createdByName, completedByName;


    private List<com.example.bnd.lab.Company> companies = new ArrayList();


    private List<com.example.bnd.lab.User> users = new ArrayList();


    private List<com.example.bnd.lab.Task> tasks = new ArrayList();

    private String createdAt, completedAt = null;

    public Project() {}
    //CREATE
    public Project(String title, String createdBy) {
        this.createdBy = createdBy;
        this.title = title;
    }

    public void addUser(com.example.bnd.lab.User u){
        users.add(u);
    }

    public void addTask(com.example.bnd.lab.Task t) {tasks.add(t);}

    //READ
    @Override
    public String toString(){
        if(completedAt != null) return "COMPLETED AT - " + completedAt + " | COMPLETED BY (ID) - " + completedBy+ " | COMPLETED BY (NAME) - " + completedByName+ " | ID - " + id + " | TITLE - " + title + " | CREATED BY - " + createdByName + " | CREATOR ID - " + createdBy + " | CREATED AT - " + createdAt;
        return "ID - " + id + " | TITLE - " + title + " | CREATED BY - " + createdByName + " | CREATOR ID - " + createdBy + " | CREATED AT - " + createdAt;
    }

    public int getId() {
        return id;
    }

    public List<com.example.bnd.lab.User> getUsers() {
        return users;
    }

    public List<com.example.bnd.lab.Task> getTasks() {
        return tasks;
    }


    public String getCreatedBy() {
        return createdBy;
    }

    public void removeTasks(){
        tasks.clear();
    }

    public void removeUsers(){
        users.clear();
    }

    public String getTitle() {
        return title;
    }

    public List<com.example.bnd.lab.Company> getCompanies() {
        return companies;
    }

    //UPDATE
    public void completeProject(String u){
        this.completedBy = u;
    }


    public void setTitle(String title) {
        this.title = title;
    }




    //DELETE
    public void removeUser(com.example.bnd.lab.User u ){
        try{
            if(u == null) throw new com.example.bnd.lab.ObjectNotExists("user");
            users.remove(u);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void removeTask(com.example.bnd.lab.Task t) {

        tasks.remove(t);

    }

    public String getCompletedBy() {
        return completedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Project)) {
            return false;
        }
        Project other = (Project) o;
        return this.toString().equals(other.toString());
    }


    public void addCompany(com.example.bnd.lab.Company c) {
        companies.add(c);
    }

    public void removeCompany(com.example.bnd.lab.Company c){
        companies.remove(c);
    }
}
