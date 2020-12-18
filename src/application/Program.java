package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Program extends Application {

    public void start(Stage mainStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Main.fxml"));
            Parent parent = loader.load();

            Scene mainScene = new Scene(parent);
            mainStage.setTitle("JavaFX Demo Application");
            mainStage.setScene(mainScene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
