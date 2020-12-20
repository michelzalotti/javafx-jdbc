package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Program;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

public class DepartmentListController implements Initializable {

    @FXML
    private Button btnNew;

    @FXML
    private TableView<Department> tableViewDepartment;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    private DepartmentService service;
    private ObservableList<Department> obsList;

    @FXML
    public void onBtnNewAction(ActionEvent event) {
        Department department = new Department();
        createDialogForm(event, "/gui/DepartmentForm.fxml", department);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Department, Integer>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<Department, String>("name"));

        Stage stage = (Stage) Program.getMainScene().getWindow();
        tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
    }

    public void setService(DepartmentService service) {
        this.service = service;
    }

    public void updateTableViewData() {
        if (service == null)
            throw new IllegalStateException("Error! Department Service is null.");

        List<Department> departments = service.findAll();
        obsList = FXCollections.observableArrayList(departments);
        tableViewDepartment.setItems(obsList);
    }

    private void createDialogForm(ActionEvent event, String viewPath, Department department) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        try {
            Pane pane = loader.load();

            DepartmentFormController controller = loader.getController();
            controller.setEntity(department);
            controller.setService(new DepartmentService());
            controller.updateFormData();

            Stage stage = new Stage();
            stage.setTitle("Department");
            stage.setScene(new Scene(pane));
            stage.setResizable(false);
            stage.initOwner(Utils.currentStage(event));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
        }
    }

}
