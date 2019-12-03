package com.example.bnd.lab;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class User  implements Serializable {
    private static final long serialVersionUID = 4L;

    private int id;
    private String login, pass, name, surname;
    private boolean active = true;



    private List<com.example.bnd.lab.Project> projects;

    private List<com.example.bnd.lab.Company> companies = new ArrayList();

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public User(){}
            
    public User(String login, String pass, String name, String surname){
        this.login = login;
        this.pass = pass;
        this.name = name;
        this.surname = surname;
    }

    public void removeProject(com.example.bnd.lab.Project p){
        try{
            if(p == null) throw new com.example.bnd.lab.ObjectNotExists("project");
            projects.remove(p);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void addProject(com.example.bnd.lab.Project p){
        projects.add(p);
    }

    public void addCompany(com.example.bnd.lab.Company c){
        companies.add(c);
    }

    public void removeCompany(com.example.bnd.lab.Company c) { companies.remove(c); }

    public List<com.example.bnd.lab.Project> getAllProjects(){
        return projects;
    }

    public List<com.example.bnd.lab.Company> getCompanies() {
        return companies;
    }

    public int compareTo(User o){
        int loginC = this.getLogin().compareTo(o.getLogin());
        
        if(loginC == 0){
            int passC = this.getPass().compareTo(o.getPass());
            if(passC == 0){
                return Integer.compare(o.id, this.id);
            } else {
                return -1*passC;
            }
        } else {
            return loginC;
        }
    }

    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public boolean isActive() {
        return active;
    }
    
    public void changeActivity(){
        this.active = !this.active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    @Override
    public String toString(){
        return "ID - " + id + " | LOGIN - " + login + " | NAME - " + name + " | SURNAME - " + surname + " | IS ACTIVE? - " + active;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }
        User other = (User) o;
        return this.toString().equals(other.toString());
    }



}
