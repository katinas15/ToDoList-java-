package FXController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxMain extends Application {
    public static String passwordSalt = "This is Salt";
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../FXML/sample.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Controller dash = loader.getController();

        primaryStage.setTitle("ToDoList administration");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
