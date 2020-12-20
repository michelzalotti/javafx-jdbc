package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class DepartmentFormController implements Initializable {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private Label lblIdError;

    @FXML
    private Label lblNameError;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    public void onBtnSaveAction() {
        System.out.println("Save");
    }

    @FXML
    public void onBtnCancelAction() {
        System.out.println("Cancel");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    }

}
