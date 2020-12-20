package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

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

    private Department department;

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
        initializeNodes();
    }

    private void initializeNodes() {
        Constraints.setTextFieldInteger(txtId);
        Constraints.setTextFieldMaxLength(txtName, 30);
    }

    public void setEntity(Department department) {
        this.department = department;
    }

    public void updateFormData() {
        if (department == null)
            throw new IllegalStateException("Error! Department entity is not set.");

        txtId.setText(String.valueOf(department.getId()));
        txtName.setText(department.getName());
    }

}
