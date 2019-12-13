package FXController;

import Objektai.Company;
import Objektai.Project;
import Objektai.User;
import Hibernate.CompanyHibernateController;
import Hibernate.ProjectHibernateController;
import Hibernate.UserHibernateController;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class CompanyFxController {
    @FXML
    private MenuBar menuBar;
    @FXML
    private ListView userList;
    @FXML
    private ListView projectList;
    @FXML
    private ListView companyList;
    @FXML
    private ListView companyUserList;
    @FXML
    private ListView companyProjectList;
    @FXML
    private TextField companyLogin;
    @FXML
    private TextField companyPass;
    @FXML
    private TextField companyTextField;
    @FXML
    private TextField companyEditTextField;

    private User selectedUser;
    private User selectedCompanyUser;
    private Project selectedProject;
    private Project selectedCompanyProject;
    private Company selectedCompany;
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHibernateController = new UserHibernateController(factory);
    ProjectHibernateController projectHibernateController = new ProjectHibernateController(factory);
    CompanyHibernateController companyHibernateController = new CompanyHibernateController(factory);

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
        showAllCompanies();
    }

    private void showAllCompanies(){
        companyList.getItems().clear();
        System.out.println("showing all companies");

        for(Company c:companyHibernateController.findCompanyEntities()){
            companyList.getItems().add(c);
        }

        companyList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + companyList.getSelectionModel());
                Company c = (Company) companyList.getSelectionModel().getSelectedItem();
                selectedCompany = c;
                showCompanyUsers();
                showCompanyProjects();
            }
        });

    }

    private void showCompanyUsers(){
        selectedCompany = companyHibernateController.findCompany(selectedCompany.getId());
        companyUserList.getItems().clear();
        System.out.println("showing all company users");

        for(User u:selectedCompany.getUsers()){
            companyUserList.getItems().add(u);
        }

        companyUserList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + companyUserList.getSelectionModel());
                User u = (User) companyUserList.getSelectionModel().getSelectedItem();
                selectedCompanyUser = u;
            }
        });
    }

    private void showCompanyProjects(){
        selectedCompany = companyHibernateController.findCompany(selectedCompany.getId());
        companyProjectList.getItems().clear();
        System.out.println("showing all company projects");

        for(Project p:selectedCompany.getProjects()){
            companyProjectList.getItems().add(p);
        }

        companyProjectList.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + companyProjectList.getSelectionModel());
                Project p = (Project) companyProjectList.getSelectionModel().getSelectedItem();
                selectedCompanyProject = p;
            }
        });
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
                companyEditTextField.setText(p.getTitle());
            }
        });

    }





    public void loadUsersPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage usersStage = (Stage) menuBar.getScene().getWindow();

        Controller dash = loader.getController();
        factory.close();

        usersStage.setTitle("ToDoList administration");
        usersStage.setScene(scene);
        usersStage.show();
    }

    public void createCompany() throws Exception {
        if(!checkUser()) return;

        if(!checkTextField(companyTextField)) return;

        Company company = new Company(companyLogin.getText(), companyPass.getText(), companyTextField.getText(), selectedUser);
        companyHibernateController.create(company);

        companyLogin.clear();
        companyPass.clear();
        companyTextField.clear();
        showAllCompanies();
        showCompanyUsers();
    }


    public void EditCompanyTitle() throws Exception {
        if(!checkCompany()) return;

        if(!checkTextField(companyEditTextField)) return;

        selectedCompany.setCompanyName(companyEditTextField.getText());
        companyHibernateController.edit(selectedCompany);
        showAllCompanies();
    }

    public void RemoveCompany() throws Exception {
        if(!checkCompany()) return;

        companyHibernateController.destroy(selectedCompany.getId());
        selectedProject = null;

        showAllCompanies();
        showCompanyProjects();
        showCompanyUsers();
        companyEditTextField.clear();
    }

    public void loadTasksPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/tasks.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage TasksStage = (Stage) menuBar.getScene().getWindow();

        TasksController dash = loader.getController();

        TasksStage.setTitle("ToDoList administration");
        TasksStage.setScene(scene);
        TasksStage.show();
        factory.close();
    }

    public void loadCompaniesPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/company.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage CompanyStage = (Stage) menuBar.getScene().getWindow();

        CompanyFxController dash = loader.getController();
        dash.initialize();

        CompanyStage.setTitle("ToDoList administration");
        CompanyStage.setScene(scene);
        CompanyStage.show();
        factory.close();
    }



    public void AddUserToCompany() throws Exception {
        if(!checkUser()) return;
        if(!checkCompany()) return;
        selectedUser = userHibernateController.findUser(selectedUser.getId());
        for(Company c:selectedUser.getCompanies()){
            if(c.getId() == selectedCompany.getId()) return;
        }

        connectToDatabase();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO COMPANY_USER"
                    + "(companies_id, users_id) VALUES"
                    + "(?, ?)");
            ps.setString(1, Integer.toString(selectedCompany.getId()));
            ps.setString(2, Integer.toString(selectedUser.getId()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();


        showCompanyUsers();
    }

    public void RemoveUserFromCompany() throws Exception {
        if(!checkCompany()) return;
        if(!checkCompanyUser()) return;

        connectToDatabase();
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM COMPANY_USER "
                    + "WHERE companies_id = " + selectedCompany.getId() + " AND users_id = " + selectedCompanyUser.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();


        showCompanyUsers();
        selectedCompanyUser = null;
    }

    public void AddProjectToCompany() throws Exception {
        if(!checkCompany()) return;
        if(!checkProject()) return;

        selectedProject = projectHibernateController.findProject(selectedProject.getId());
        for(Project p:selectedCompany.getProjects()){
            if(p.getId() == selectedProject.getId()) return;
        }

        connectToDatabase();
        try {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO COMPANY_PROJECT"
                    + "(companies_id, projects_id) VALUES"
                    + "(?, ?)");
            ps.setString(1, Integer.toString(selectedCompany.getId()));
            ps.setString(2, Integer.toString(selectedProject.getId()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();


        showCompanyProjects();
    }

    public void RemoveProjectFromCompany() throws Exception {
        if(!checkCompany()) return;
        if(!checkCompanyProject()) return;

        connectToDatabase();
        try {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM COMPANY_PROJECT "
                    + "WHERE companies_id = " + selectedCompany.getId() + " AND projects_id = " + selectedCompanyProject.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        disconnectFromDatabase();


        showCompanyProjects();
        selectedCompanyProject = null;
    }








    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean checkCompany(){
        if(selectedCompany == null){
            showAlert("Company not selected");
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

    private boolean checkCompanyUser(){
        if(selectedCompanyUser == null){
            showAlert("User not selected");
            return false;
        }
        return true;
    }

    private boolean checkCompanyProject(){
        if(selectedCompanyProject == null){
            showAlert("Project not selected");
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
