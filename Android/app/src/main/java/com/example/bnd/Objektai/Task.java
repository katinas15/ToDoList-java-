package com.example.bnd.Objektai;/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author katinas
 */



public class Task  implements Serializable {
    private static final long serialVersionUID = 4L;

    private int id;
    private String title;
    private String createdAt, completedAt = null;
    private boolean done;

    private String createdBy, completedBy;
    private String createdByName, completedByName;

    private List<Task> subTasks = new ArrayList();

    private String parentTask = null;

    private Project project;
    private static int idCounter = 1;

    private int indent = 0;

    public Task(){}

    //CREATE
    public Task(String title, Project project, String createdBy) {
        this.createdBy = createdBy;
        this.title = title;
        this.project = project;
        project.getTasks().add(this);
    }

    public Task(String title, Project project, String parentTask, String createdBy) {
        this.createdBy = createdBy;
        this.title = title;
        this.project = project;
        this.parentTask = parentTask;
    }


    public void addSubTask(Task t){
        subTasks.add(t);

    }




    //READ
    public int getId() {
        return id;
    }

    public List<Task> getSubTasks() {
        return subTasks;
    }

    public Project getProject() {
        return project;
    }

    public void completeTask(String u){
        this.completedBy = u;
        for(Task s:subTasks){
            s.completeTask(u);
        }
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString(){
        String indentation = "TASK - \t";
        String searchParent = parentTask;
        if(searchParent != null) indentation = "SUBTASK - \t";
        for(int i = 0;i<indent;i++){
            indentation += "\t";
        }

        if(completedAt != null) return indentation + "COMPLETED AT - " + completedAt + " | COMPLETED BY (ID) - " + completedBy + " | COMPLETED BY (NAME) - " + completedByName + " | ID - " + id + " | TITLE - " + title + " | CREATED BY - " + createdByName + " | CREATOR ID - " + createdBy + " | CREATED AT - " + createdAt;

        return indentation + "ID - " + id + " | TITLE - " + title  + " | CREATED BY - " + createdByName + " | CREATOR ID - " + createdBy + " | CREATED AT - " + createdAt;
    }

    public String getParentTask(){
        return parentTask;
    }






    public void setTitle(String title) {
        this.title = title;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public static void changeIdCounter(int counter){
        idCounter = counter;
    }

    //DELETE
    public void removeSubTask(Task t){
        this.subTasks.remove(t);
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCompletedBy() {
        return completedBy;
    }

}
