package ToDoMain;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ProjectUsersController {
    @FXML
    private ListView userList;
    @FXML
    private ListView projectList;
    @FXML
    private ListView projectUserList;

    private User selectedUser;
    private Project selectedProject;
    private User selectedProjectUser;

    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHibernateController = new UserHibernateController(factory);
    ProjectHibernateController projectHibernateController = new ProjectHibernateController(factory);


    private String DBurl = "jdbc:mysql://localhost/ToDoList";
    private String DBusername = "root";
    private String DBpass = "";
    private Connection conn = null;

    public void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(DBurl, DBusername, DBpass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectFromDatabase() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void initialize(){
        showAllUsers();
        showAllProjects();
    }


    private void showAllUsers(){
        userList.getItems().clear();
        System.out.println("showing all users");

        for(User u:userHibernateController.findUserEntities()){
            userList.getItems().add(u);
        }

        userList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + userList.getSelectionModel());
                User u = (User) userList.getSelectionModel().getSelectedItem();
                selectedUser = u;
                selectedProject = null;
                showAllProjects();
            }
        });

    }

    private void showAllProjects(){
        projectList.getItems().clear();
        System.out.println("showing all projects");

        for(Project p:projectHibernateController.findProjectEntities()){
            projectList.getItems().add(p);
        }

        projectList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + projectList.getSelectionModel());
                Project p = (Project) projectList.getSelectionModel().getSelectedItem();
                selectedProject = p;
                showSelectedProjectUsers();
            }
        });

    }

    private void showSelectedProjectUsers(){
        projectUserList.getItems().clear();
        System.out.println("showing all project users");

        for(User u:projectHibernateController.findProject(selectedProject.getId()).getUsers()){
            projectUserList.getItems().add(u);
        }

        projectUserList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + projectUserList.getSelectionModel());
                User u = (User) projectUserList.getSelectionModel().getSelectedItem();
                selectedProjectUser = u;
            }
        });

    }

    public void AddUserToProject() throws Exception {
        if(!checkProject()) return;
        if(!checkUser()) return;
        selectedUser = userHibernateController.findUser(selectedUser.getId());
        for(Project p:selectedUser.getAllProjects()){
            if(p.getId() == selectedProject.getId()) return;
        }

        connectToDatabase();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO PROJECT_USER"
                    + "(projects_id, users_id) VALUES"
                    + "(?, ?)");
            ps.setString(1, Integer.toString(selectedProject.getId()));
            ps.setString(2, Integer.toString(selectedUser.getId()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();
        showSelectedProjectUsers();
    }

    public void RemoveUserFromProject() throws Exception {
        if(!checkProject()) return;
        if(!checkProjectUser()) return;

        connectToDatabase();
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM PROJECT_USER "
                    + "WHERE projects_id = " + selectedProject.getId() + " AND users_id = " + selectedProjectUser.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();

        showSelectedProjectUsers();
        selectedProjectUser = null;
    }



    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean checkProject(){
        if(selectedProject == null){
            showAlert("Project not selected");
            return false;
        }
        return true;
    }

    private boolean checkUser(){
        if(selectedUser == null){
            showAlert("User not selected");
            return false;
        }
        return true;
    }

    private boolean checkProjectUser(){
        if(selectedProjectUser == null){
            showAlert("User not selected");
            return false;
        }
        return true;
    }





}
