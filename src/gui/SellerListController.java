package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Program;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

    @FXML
    private Button btnNew;

    @FXML
    private TableView<Seller> tableViewSeller;

    @FXML
    private TableColumn<Seller, Integer> tableColumnId;

    @FXML
    private TableColumn<Seller, String> tableColumnName;

    @FXML
    private TableColumn<Seller, String> tableColumnEmail;

    @FXML
    private TableColumn<Seller, Date> tableColumnBirthDate;

    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEdit;

    @FXML
    private TableColumn<Seller, Seller> tableColumnDelete;

    private SellerService service;
    private ObservableList<Seller> obsList;

    @FXML
    public void onBtnNewAction(ActionEvent event) {
        Seller seller = new Seller();
        createDialogForm(event, "/gui/SellerForm.fxml", seller);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        initializeNodes();
    }

    private void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<Seller, Integer>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<Seller, String>("name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<Seller, String>("email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<Seller, Date>("birthDate"));
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<Seller, Double>("baseSalary"));

        Utils.formatDateTableColumn(tableColumnBirthDate, "dd/MM/yyyy");
        Utils.formatDoubleTableColumn(tableColumnBaseSalary);

        Stage stage = (Stage) Program.getMainScene().getWindow();
        tableViewSeller.prefHeightProperty().bind(stage.heightProperty());
    }

    public void setService(SellerService service) {
        this.service = service;
    }

    public void updateTableViewData() {
        if (service == null)
            throw new IllegalStateException("Error! Seller Service is null.");

        List<Seller> sellers = service.findAll();
        obsList = FXCollections.observableArrayList(sellers);
        tableViewSeller.setItems(obsList);
        initEditButtons();
        initDeleteButtons();
    }

    private void createDialogForm(ActionEvent event, String viewPath, Seller seller) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(viewPath));
        try {
            Pane pane = loader.load();

            SellerFormController controller = loader.getController();
            controller.setEntity(seller);
            controller.setService(new SellerService());
            controller.subscribeListener(this);
            controller.updateFormData();

            Stage stage = new Stage();
            stage.setTitle("Seller");
            stage.setScene(new Scene(pane));
            stage.setResizable(false);
            stage.initOwner(Utils.currentStage(event));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            Alerts.showAlert("Error", null, e.getMessage(), AlertType.ERROR);
        }
    }

    @Override
    public void onChangedData() {
        updateTableViewData();
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param -> new TableCell<Seller, Seller>() {

            private final Button btnEdit = new Button("Edit");

            @Override
            protected void updateItem(Seller seller, boolean empty) {
                if (empty) {
                    setText(null);
                    return;
                }
                setGraphic(btnEdit);
                btnEdit.setOnAction(event -> editSeller(seller, event));
            }

        });
    }

    private void editSeller(Seller seller, ActionEvent event) {
        createDialogForm(event, "/gui/SellerForm.fxml", seller);
    }

    private void initDeleteButtons() {
        tableColumnDelete.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnDelete.setCellFactory(param -> new TableCell<Seller, Seller>() {

            private final Button btnDelete = new Button("Delete");

            @Override
            protected void updateItem(Seller seller, boolean empty) {
                if (empty) {
                    setText(null);
                    return;
                }

                setGraphic(btnDelete);
                btnDelete.setOnAction(event -> deleteSeller(seller));
            }

        });
    }

    private void deleteSeller(Seller seller) {
        Optional<ButtonType> result = Alerts.showConfirmation("Delete?", "Confirm delete?");

        if (result.get() == ButtonType.OK) {
            if (service == null)
                throw new IllegalStateException("Error! Seller Service is not set.");

            try {
                service.remove(seller);
                updateTableViewData();
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Error!", null, e.getMessage(), AlertType.ERROR);
            }
        }
    }

}
