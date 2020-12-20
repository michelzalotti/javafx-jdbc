package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

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
    private DepartmentService departmentService;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    @FXML
    public void onBtnSaveAction(ActionEvent event) {
        if (departmentService == null)
            throw new IllegalStateException("Error! Department service is not set.");

        try {
            department = getFormData();
            departmentService.saveOrUpdate(department);
            notifyListeners();
            closeWindow(event);
        } catch (DbException e) {
            Alerts.showAlert("Error!", null, e.getMessage(), AlertType.ERROR);
        } catch (ValidationException e) {
            setErrorMessages(e.getErrors());
        }
    }

    @FXML
    public void onBtnCancelAction(ActionEvent event) {
        closeWindow(event);
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

    public void setService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    public void updateFormData() {
        if (department == null)
            throw new IllegalStateException("Error! Department entity is not set.");

        txtId.setText(String.valueOf(department.getId()));
        txtName.setText(department.getName());
    }

    private Department getFormData() {
        Department department = new Department();

        validateFormData();

        department.setId(Utils.tryParseToInt(txtId.getText()));
        department.setName(txtName.getText());

        return department;
    }

    private void closeWindow(ActionEvent event) {
        Utils.currentStage(event).close();
    }

    public void subscribeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    private void notifyListeners() {
        for (DataChangeListener l : dataChangeListeners)
            l.onChangedData();
    }

    private void validateFormData() {
        ValidationException exception = new ValidationException("Validation error!");

        if (txtName.getText() == null || txtName.getText().trim().equals(""))
            exception.addError("name", "Field is empty!");

        if (exception.getErrors().size() > 0)
            throw exception;
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        lblNameError.setText(fields.contains("name") ? errors.get("name") : "");

    }

}
