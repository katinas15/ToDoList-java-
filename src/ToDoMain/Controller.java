package ToDoMain;

import ToDoMain.CompanyHibernateController;
import ToDoMain.TasksController;
import ToDoMain.User;
import ToDoMain.UserHibernateController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private PieChart userC;
    @FXML
    private ListView userList;
    @FXML
    private TextField loginTextField;
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField surnameTextField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField editLoginTextField;
    @FXML
    private TextField editNameTextField;
    @FXML
    private TextField editSurnameTextField;
    @FXML
    private PasswordField editPasswordField;
    @FXML
    private Text idEditText;
    @FXML
    private Text activityEditText;
    @FXML
    private Button registerUserButton;
    @FXML
    private MenuBar menuBar;




    private User selectedUser;
    EntityManagerFactory factory = Persistence.createEntityManagerFactory("Lab4PU");
    UserHibernateController userHibernateController = new UserHibernateController(factory);
    CompanyHibernateController companyHibernateController = new CompanyHibernateController(factory);

    private void restartPage(){
        showUserStatistics();
        showAllUsers();
    }

    private void showUserStatistics(){
        System.out.println("showing user statistics");

        int userCount = userHibernateController.getUserCount();
        int companyCount = companyHibernateController.getCompanyCount();
            ObservableList<PieChart.Data> pieChartData =
                    FXCollections.observableArrayList(
                            new PieChart.Data("Persons" + "(" + userCount + ")", userCount),
                            new PieChart.Data("Companies"+ "(" + companyCount + ")", companyCount));
            userC.setData(pieChartData);
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
                updateEditText();
            }
        });

    }

    public void updateEditText(){
        idEditText.setText(Integer.toString(selectedUser.getId()));
        editLoginTextField.setText(selectedUser.getLogin());
        editPasswordField.setText(selectedUser.getPass());
        editNameTextField.setText(selectedUser.getName());
        editSurnameTextField.setText(selectedUser.getSurname());
        activityEditText.setText(Boolean.toString(selectedUser.isActive()));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        showUserStatistics();
        showAllUsers();
    }

    public void vaizduoti(javafx.event.ActionEvent actionEvent) {
        restartPage();
    }

    private void showAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void registerUser() throws Exception {
        String login = loginTextField.getText();
        String pass = passwordField.getText();
        String name = nameTextField.getText();
        String surname = surnameTextField.getText();

        if(login.isEmpty() || pass.isEmpty() || name.isEmpty() || surname.isEmpty()){
            showAlert("Some of the fields are empty");
            return;
        }

        User user = new User(login, pass,name,surname);
        userHibernateController.create(user);
        restartPage();


        loginTextField.clear();
        passwordField.clear();
        nameTextField.clear();
        surnameTextField.clear();
    }

    public void loadTasksPage() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("tasks.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage TasksStage = (Stage) menuBar.getScene().getWindow();

        TasksController dash = loader.getController();

        TasksStage.setTitle("ToDoList administration");
        TasksStage.setScene(scene);
        TasksStage.show();
        factory.close();
    }

    public void editUser() throws Exception {

        if(selectedUser != null){
            String login = editLoginTextField.getText();
            String pass = editPasswordField.getText();
            String name = editNameTextField.getText();
            String surname = editSurnameTextField.getText();

            if(login.isEmpty() || pass.isEmpty() || name.isEmpty() || surname.isEmpty()){
                showAlert("Some of the fields are empty");
                return;
            }

            selectedUser.setLogin(login);
            selectedUser.setPass(pass);
            selectedUser.setName(name);
            selectedUser.setSurname(surname);
            userHibernateController.edit(selectedUser);

            restartPage();


            loginTextField.clear();
            passwordField.clear();
            nameTextField.clear();
            surnameTextField.clear();
        } else {
            showAlert("User not selected");

        }

    }

    public void changeActivity() throws Exception {
        if(selectedUser != null) {
            int id = Integer.parseInt(idEditText.getText());
            selectedUser.changeActivity();
            userHibernateController.edit(selectedUser);
            showAllUsers();
            updateEditText();
        }else {
            showAlert("User not selected");

        }
    }
}
