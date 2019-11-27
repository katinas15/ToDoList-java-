package ToDoMain;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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
        selectedProject.addUser(selectedUser);
        projectHibernateController.edit(selectedProject);
        showSelectedProjectUsers();
    }

    public void RemoveUserFromProject() throws Exception {
        if(!checkProject()) return;
        if(!checkProjectUser()) return;
        projectHibernateController.removeUserFromProject(selectedProjectUser, selectedProject);
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
