package ToDoMain;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class TasksController implements Initializable {
    @FXML
    private MenuBar menuBar;
    @FXML
    private ListView userList;
    @FXML
    private ListView projectList;
    @FXML
    private ListView taskList;
    @FXML
    private TextField projectTextField;
    @FXML
    private TextField taskNameTextField;
    @FXML
    private CheckBox subtaskCheckBox;
    @FXML
    private TextField projectEditTextField;
    @FXML
    private TextField taskEditTextField;

    private User selectedUser;
    private Project selectedProject;
    private Task selectedTask;
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHibernateController = new UserHibernateController(factory);
    ProjectHibernateController projectHibernateController = new ProjectHibernateController(factory);
    TaskHibernateController taskHibernateController = new TaskHibernateController(factory);

    @Override
    public void initialize(URL url, ResourceBundle rb){
        showAllUsers();
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
                selectedTask = null;
                projectEditTextField.clear();
                taskEditTextField.clear();
                taskList.getItems().clear();
                showAllProjects();
            }
        });

    }

    private void showAllProjects(){
        projectList.getItems().clear();
        System.out.println("showing all projects");

        for(Project p:userHibernateController.findUser(selectedUser.getId()).getAllProjects()){
            projectList.getItems().add(p);
        }

        projectList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + projectList.getSelectionModel());
                Project p = (Project) projectList.getSelectionModel().getSelectedItem();
                selectedProject = p;
                projectEditTextField.setText(p.getTitle());
                selectedTask = null;
                taskEditTextField.clear();
                try {
                    showAllTasks();
                } catch (ObjectNotExists objectNotExists) {
                    objectNotExists.printStackTrace();
                }
            }
        });

    }

    private void addSubTasks(Task t){
        List<Task> allSubTasks = t.getSubTasks();
        for(Task s:allSubTasks){
            taskList.getItems().add(s);
            addSubTasks(s);
        }

    }

    private void showAllTasks() throws ObjectNotExists {
        taskList.getItems().clear();
        System.out.println("showing all tasks");
        if(selectedProject != null){

            for(Task t:projectHibernateController.findProject(selectedProject.getId()).getTasks()){
                if(t.getParentTask() == null){
                    taskList.getItems().add(t);
                    addSubTasks(t);
                }

            }

            taskList.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    System.out.println("clicked on " + taskList.getSelectionModel());
                    Task t = (Task) taskList.getSelectionModel().getSelectedItem();
                    selectedTask = t;
                    taskEditTextField.setText(t.getTitle());
                    showAllUsers();
                }
            });
        }
    }

    public void loadUsersPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage usersStage = (Stage) menuBar.getScene().getWindow();

        Controller dash = loader.getController();
        factory.close();

        usersStage.setTitle("ToDoList administration");
        usersStage.setScene(scene);
        usersStage.show();
    }

    public void createProject() throws Exception {
        if(!checkUser()) return;

        if(!checkTextField(projectTextField)) return;

        Project project = new Project(projectTextField.getText(), selectedUser);
        projectHibernateController.create(project);

        projectTextField.clear();
        showAllProjects();
    }

    public void createTask() throws Exception {
        if(!checkProject()) return;

        if(subtaskCheckBox.isSelected()){
            if(!checkTask()) return;

            if(!checkTextField(taskNameTextField)) return;

            Task task = new Task(taskNameTextField.getText(),selectedProject, selectedTask, selectedUser);
            taskHibernateController.create(task);

        } else {
            if(!checkTextField(taskNameTextField)) return;

            Task task = new Task(taskNameTextField.getText(),selectedProject, selectedUser);
            taskHibernateController.create(task);

        }
        taskNameTextField.clear();
        showAllTasks();
    }


    public void loadProjectUsers() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("projectUsers.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage addUsersStage = new Stage();

        ProjectUsersController dash = loader.getController();
        dash.initialize();

        addUsersStage.setTitle("ToDoList administration");
        addUsersStage.setScene(scene);
        addUsersStage.show();
    }

    public void EditProjectTitle() throws Exception {
        if(!checkProject()) return;

        if(!checkTextField(projectEditTextField)) return;

        selectedProject.setTitle(projectEditTextField.getText());
        projectHibernateController.edit(selectedProject);
        showAllProjects();
    }

    public void RemoveProject() throws Exception {
        if(!checkProject()) return;

        projectHibernateController.destroy(selectedProject.getId());
        selectedProject = null;

        showAllProjects();
        showAllTasks();
        projectEditTextField.clear();
        taskEditTextField.clear();
    }

    public void EditTaskTitle() throws Exception {
        if(!checkTask()) return;

        if(!checkTextField(taskEditTextField)) return;

        selectedTask.setTitle(taskEditTextField.getText());
        taskHibernateController.edit(selectedTask);
        showAllTasks();
    }

    public void RemoveTask() throws Exception {
        if(!checkTask()) return;

        taskHibernateController.destroy(selectedTask.getId());
        selectedTask = null;
        taskEditTextField.clear();
        showAllTasks();
    }

    public void CompleteTask() throws Exception {
        if(!checkTask()) return;

        selectedTask.completeTask(selectedUser);
        taskHibernateController.edit(selectedTask);
        showAllTasks();
    }

    public void ReopenTask() throws Exception {
        if(!checkTask()) return;
        selectedTask.reopenTask();
        taskHibernateController.edit(selectedTask);
        showAllTasks();
    }






    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean checkTask(){
        if(selectedTask == null){
            showAlert("Task not selected");
            return false;
        }
        return true;
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

    private boolean checkTextField(TextField field){
        if(field.getText().equals("")){
            showAlert("Name field is empty");
            return false;
        }
        return true;
    }

}
