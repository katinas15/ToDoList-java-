package com.example.bnd.Objektai;

import com.example.bnd.Exceptions.ObjectNotExists;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class Project implements Serializable {
    private static final long serialVersionUID = 4L;

    private int id;
    private String title;

    private String createdBy, completedBy;
    private String createdByName, completedByName;


    private List<com.example.bnd.Objektai.Company> companies = new ArrayList();


    private List<com.example.bnd.Objektai.User> users = new ArrayList();


    private List<com.example.bnd.Objektai.Task> tasks = new ArrayList();

    private String createdAt, completedAt = null;

    public Project() {}
    //CREATE
    public Project(String title, String createdBy) {
        this.createdBy = createdBy;
        this.title = title;
    }

    public void addUser(com.example.bnd.Objektai.User u){
        users.add(u);
    }

    public void addTask(com.example.bnd.Objektai.Task t) {tasks.add(t);}

    //READ
    @Override
    public String toString(){
        if(completedAt != null) return "COMPLETED AT - " + completedAt + " | COMPLETED BY (ID) - " + completedBy+ " | COMPLETED BY (NAME) - " + completedByName+ " | ID - " + id + " | TITLE - " + title + " | CREATED BY - " + createdByName + " | CREATOR ID - " + createdBy + " | CREATED AT - " + createdAt;
        return "ID - " + id + " | TITLE - " + title + " | CREATED BY - " + createdByName + " | CREATOR ID - " + createdBy + " | CREATED AT - " + createdAt;
    }

    public int getId() {
        return id;
    }

    public List<com.example.bnd.Objektai.User> getUsers() {
        return users;
    }

    public List<com.example.bnd.Objektai.Task> getTasks() {
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

    public List<com.example.bnd.Objektai.Company> getCompanies() {
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
    public void removeUser(com.example.bnd.Objektai.User u ){
        try{
            if(u == null) throw new ObjectNotExists("user");
            users.remove(u);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void removeTask(com.example.bnd.Objektai.Task t) {

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


    public void addCompany(com.example.bnd.Objektai.Company c) {
        companies.add(c);
    }

    public void removeCompany(com.example.bnd.Objektai.Company c){
        companies.remove(c);
    }
}
