/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ToDoMain;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author katinas
 */


@Entity
public class Task  implements Serializable {
    private static final long serialVersionUID = 4L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String title;
    private Date created, completed;
    private boolean done;
    @ManyToOne
    private User createdBy, completedBy;
    @OneToMany (mappedBy = "parentTask", cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Task> subTasks = new ArrayList();
    @ManyToOne
    private Task parentTask = null;
    @ManyToOne
    private Project project;
    private static int idCounter = 1;

    public Task(){}

    //CREATE
    public Task(String title, Project project, User createdBy) {
        this.createdBy = createdBy;
        this.title = title;
        this.project = project;
        project.getTasks().add(this);
        this.created = new Date();
    }

    public Task(String title, Project project, Task parentTask, User createdBy) {
        this.createdBy = createdBy;
        this.title = title;
        this.project = project;
        this.parentTask = parentTask;
        this.created = new Date();
    }

    public void addSubTask(String title, User createdBy) {
        Task subTask = new Task(title, this.project, this, createdBy);
        subTasks.add(subTask);
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

    public void completeTask(User u){
        this.completedBy = u;
        this.completed = new Date();
        for(Task s:subTasks){
            s.completeTask(u);
        }
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString(){
        String indent = "TASK - \t";
        Task searchParent = parentTask;
        if(searchParent != null) indent = "SUBTASK - \t";
        while(searchParent != null){
            searchParent = searchParent.parentTask;
            indent += "\t";
        }

        if(completed != null) return indent + "COMPLETED AT - " + completed + " | COMPLETED BY (ID) - " + completedBy.getId() + " | COMPLETED BY (NAME) - " + completedBy.getName() + " | ID - " + id + " | TITLE - " + title + " | PROJECT ID - " + project.getId() + " | CREATED BY - " + createdBy.getLogin() + " | CREATOR ID - " + createdBy.getId() + " | CREATED AT - " + created;

        return indent + "ID - " + id + " | TITLE - " + title + " | PROJECT ID - " + project.getId() + " | CREATED BY - " + createdBy.getLogin() + " | CREATOR ID - " + createdBy.getId() + " | CREATED AT - " + created;
    }

    public Task getParentTask(){
        return parentTask;
    }




    //UPDATE
    public void reopenTask(){
        this.completedBy = null;
        this.completed = null;
        for(Task s:subTasks){
            s.reopenTask();
        }
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
}
