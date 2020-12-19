package gui;

import gui.util.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class MainController {

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemDepartmentAction() {
        Utils.loadView(this, "/gui/DepartmentList.fxml");
    }

    @FXML
    public void onMenuItemSellerAction() {
        System.out.println("Seller");
    }

    @FXML
    public void onMenuItemAboutAction() {
        Utils.loadView(this, "/gui/About.fxml");
    }

}
