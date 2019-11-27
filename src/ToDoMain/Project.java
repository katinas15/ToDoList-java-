package ToDoMain;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
public class Project implements Serializable {
    private static final long serialVersionUID = 4L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    private String title;
    @ManyToOne
    private User createdBy, completedBy;

    @ManyToMany (cascade=CascadeType.MERGE)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<User> users = new ArrayList();

    @OneToMany  (mappedBy = "project", cascade=CascadeType.ALL, orphanRemoval=true)
    @OrderBy("id ASC")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Task> tasks = new ArrayList();

    private Date created, completed;

    public Project() {}
    //CREATE
    public Project(String title, User createdBy) {
        this.createdBy = createdBy;
        this.title = title;
        this.created = new Date();
        addUser(createdBy);
    }

    public void addUser(User u){
        users.add(u);
    }

    public void addTask(Task t) {tasks.add(t);}

    //READ
    @Override
    public String toString(){
        if(completed != null) return "COMPLETED AT - " + completed + " | COMPLETED BY (ID) - " + completedBy.getId() + " | COMPLETED BY (NAME) - " + completedBy.getName()+ " | ID - " + id + " | TITLE - " + title + " | CREATED BY - " + createdBy.getLogin() + " | CREATOR ID - " + createdBy.getId() + " | CREATED AT - " + created;
        return "ID - " + id + " | TITLE - " + title + " | CREATED BY - " + createdBy.getLogin() + " | CREATOR ID - " + createdBy.getId() + " | CREATED AT - " + created;
    }

    public int getId() {
        return id;
    }

    public List<User> getUsers() {
        return users;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Date getCompleted() {
        return completed;
    }

    public User getCreatedBy() {
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

    //UPDATE
    public void completeProject(User u){
        this.completedBy = u;
        this.completed = new Date();
    }

    public void reopenProject(){
        this.completedBy = null;
        this.completed = null;
    }

    public void setTitle(String title) {
        this.title = title;
    }




    //DELETE
    public void removeUser(User u ){
        try{
            if(u == null) throw new ObjectNotExists("user");
            users.remove(u);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void removeTask(Task t) {

        tasks.remove(t);

    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Project)) {
            return false;
        }
        Project other = (Project) o;
        return this.toString().equals(other.toString());
    }

}
