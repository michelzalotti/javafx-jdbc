package gui.util;

import java.io.IOException;
import java.util.function.Consumer;

import application.Program;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Utils {

    public static Stage currentStage(ActionEvent event) {
        return (Stage) ((Node) event.getSource()).getScene().getWindow();
    }

    public static synchronized <T> void loadView(Object mainClass, String viewPath, Consumer<T> action) {
        FXMLLoader loader = new FXMLLoader(mainClass.getClass().getResource(viewPath));

        try {
            VBox vBox = loader.load();

            VBox mainVBox = (VBox) ((ScrollPane) Program.getMainScene().getRoot()).getContent();
            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(vBox.getChildren());

            T controller = loader.getController();
            action.accept(controller);

        } catch (IOException e) {
            Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
        }
    }

}
