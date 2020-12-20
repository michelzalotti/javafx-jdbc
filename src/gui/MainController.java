package gui;

import gui.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import model.services.DepartmentService;
import model.services.SellerService;

public class MainController {

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemDepartmentAction() {
        Utils.loadView(this, "/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
            controller.setService(new DepartmentService());
            controller.updateTableViewData();
        });
    }

    @FXML
    public void onMenuItemSellerAction() {
        Utils.loadView(this, "/gui/SellerList.fxml", (SellerListController controller) -> {
            controller.setService(new SellerService());
            controller.updateTableViewData();
        });
    }

    @FXML
    public void onMenuItemAboutAction() {
        Utils.loadView(this, "/gui/About.fxml", x -> {
        });
    }

}
