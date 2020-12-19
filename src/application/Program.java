package application;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Program extends Application {

    public void start(Stage mainStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Main.fxml"));
            ScrollPane sPane = loader.load();

            sPane.setFitToWidth(true);
            sPane.setFitToHeight(true);

            Scene mainScene = new Scene(sPane);
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
