package gui.util;

import java.io.IOException;

import application.Program;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class Utils {

    public static void loadView(Object mainClass, String viewPath) {
        FXMLLoader loader = new FXMLLoader(mainClass.getClass().getResource(viewPath));

        try {
            VBox vBox = loader.load();

            VBox mainVBox = (VBox) ((ScrollPane) Program.getMainScene().getRoot()).getContent();
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(vBox.getChildren());

        } catch (IOException e) {
            Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
        }
    }

}
