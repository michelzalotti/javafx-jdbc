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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

public class SellerFormController implements Initializable {

    @FXML
    private TextField txtId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtBaseSalary;

    @FXML
    private ComboBox<Department> cbDepartment;

    @FXML
    private Label lblIdError;

    @FXML
    private Label lblNameError;

    @FXML
    private Label lblEmailError;

    @FXML
    private Label lblBirthDateError;

    @FXML
    private Label lblBaseSalaryError;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private Seller seller;
    private SellerService sellerService;
    private DepartmentService departmentService;
    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
    private ObservableList<Department> obsList;

    @FXML
    public void onBtnSaveAction(ActionEvent event) {
        if (sellerService == null)
            throw new IllegalStateException("Error! Seller service is not set.");

        try {
            seller = getFormData();
            sellerService.saveOrUpdate(seller);
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
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldMaxLength(txtEmail, 50);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");
        initializeComboBoxDepartment();
    }

    public void setEntity(Seller seller) {
        this.seller = seller;
    }

    public void setServices(SellerService sellerService, DepartmentService departmentService) {
        this.sellerService = sellerService;
        this.departmentService = departmentService;
    }

    public void updateFormData() {
        if (seller == null)
            throw new IllegalStateException("Error! Seller entity is not set.");

        txtId.setText(String.valueOf(seller.getId()));
        txtName.setText(seller.getName());
        txtEmail.setText(seller.getEmail());

        if (seller.getBirthDate() != null) {
            dpBirthDate.setValue(Utils.convertDateToLocalDate(seller.getBirthDate(), "yyy-MM-dd"));
        }

        txtBaseSalary.setText(String.valueOf(seller.getBaseSalary()));

        if (seller.getDepartment() != null)
            cbDepartment.setValue(seller.getDepartment());
        else
            cbDepartment.getSelectionModel().selectFirst();
    }

    private Seller getFormData() {
        Seller seller = new Seller();

        validateFormData();

        seller.setId(Utils.tryParseToInt(txtId.getText()));
        seller.setName(txtName.getText());
        seller.setEmail(txtEmail.getText());

        if (dpBirthDate.getValue() != null)
            seller.setBirthDate(Utils.convertLocalDateToDate(dpBirthDate.getValue()));

        seller.setBaseSalary(Utils.tryParseToDouble(txtBaseSalary.getText()));
        seller.setDepartment(cbDepartment.getValue());

        return seller;
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

        if (txtEmail.getText() == null || txtEmail.getText().trim().equals(""))
            exception.addError("email", "Field is empty!");

        if (dpBirthDate.getValue() == null)
            exception.addError("birthDate", "Field is empty!");

        if (txtBaseSalary.getText() == null || txtBaseSalary.getText().trim().equals(""))
            exception.addError("baseSalary", "Field is empty!");

        if (exception.getErrors().size() > 0)
            throw exception;
    }

    private void setErrorMessages(Map<String, String> errors) {
        Set<String> fields = errors.keySet();

        lblNameError.setText(fields.contains("name") ? errors.get("name") : "");
        lblEmailError.setText(fields.contains("email") ? errors.get("email") : "");
        lblBirthDateError.setText(fields.contains("birthDate") ? errors.get("birthDate") : "");
        lblBaseSalaryError.setText(fields.contains("baseSalary") ? errors.get("baseSalary") : "");
    }

    public void loadAssociatedObjects() {
        if (departmentService == null)
            throw new IllegalStateException("Error! Department Service is not set.");

        List<Department> departments = departmentService.findAll();
        obsList = FXCollections.observableArrayList(departments);

        cbDepartment.setItems(obsList);
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        cbDepartment.setCellFactory(factory);
        cbDepartment.setButtonCell(factory.call(null));
    }

}
