package br.com.museuid.view.app;

import java.util.List;

import br.com.museuid.app.App;
import br.com.museuid.util.*;
import br.com.museuid.app.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppController {
    private static AppController instance;
    @FXML
    public ToggleButton btEmployeeManagement;
    public VBox boxEmployee;
    public ToggleButton btAppManagementSection;
    public ToggleGroup gAppManagementSection;
    public VBox boxOrderSection;
    public ToggleButton btCreateOrder;
    public ToggleButton btMyHandlingOrder;
    public VBox boxStatistic;
    public ToggleButton btOrderSection;
    public ToggleButton btOrderCreated;
    public Label lbUser;
    public ToggleButton btUserInfoSection;
    public VBox boxUserInfo;
    public ToggleButton btUpdateUserInfo;
    public ToggleButton btChangePassword;

    @FXML
    private AnchorPane boxContainer;
    @FXML
    private VBox boxNotas;
    private VBox[] listMenu;
    private ToggleButton currentScreen;
    @FXML
    private ProgressIndicator progressIndicator;
    /**
     * Obter instancia do controler
     */
    public static AppController getInstance() {
        return instance;
    }

    @FXML
    void menuSair(ActionEvent event) {
        App.palco.close();
        new Login().start(new Stage());
    }

    @FXML
    void initialize() {
        instance = this;
        //TODO: add more vbox list submenu
        listMenu = new VBox[]{boxOrderSection, boxEmployee};
        //TODO: change to default selected submenu
        openCreateOrderScreen(new ActionEvent(btCreateOrder, null));
        //TODO: show user data
        lbUser.setText("Chào Chí!");
    }

    /**
     * Obter componente para exbição das notas
     */
    public VBox boxNotas() {
        return boxNotas;
    }

    /**
     * Obter componente para exibição dos modulos da aplicação
     */
    public AnchorPane getBoxConteudo() {
        return boxContainer;
    }

    /**
     * Exibir e ocultar submenus
     */
    public void submenus(ToggleButton menu, VBox box, ToggleButton... submenus) {
//        if (box.getChildren().isEmpty()) {
//            box.getChildren().addAll(submenus);
//            AnimationUtils.fade(box);
//            estilo(menu, "menu-grupo");
//        } else {
//            desativarSubmenus(box);
//            estilo(menu, "menu-grupo-inativo");
//        }
    }

    /**
     * Desativar e esconder todos submenus
     */
    public void desativarSubmenus(VBox... boxes) {
        for (VBox box : boxes) {
            box.getChildren().clear();
        }
    }

    /**
     * Apply style to show / hide submenus
     */
    public void estilo(Node no, String estilo) {
        no.getStyleClass().remove(2);
        no.getStyleClass().add(estilo);
    }

    public void openEmployeeManagement(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getEmployeeManagementScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) actionEvent.getSource());
    }

    public void openCreateOrderScreen(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getCreateOrderScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) actionEvent.getSource());
    }

    public void openMyHandlingOrderScreen(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
//        NavigationUtils.getMyOrderCreatedScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) actionEvent.getSource());
    }

    public void openOrderCreatedScreen(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getMyOrderCreatedScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) actionEvent.getSource());
    }

    public void openStatistic(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getStatisticScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) actionEvent.getSource());
    }

    public void setCurrentSubMenuAndStyleThenHideProgressIndicator(ToggleButton currentSelectedSubMenu) {
        currentScreen = currentSelectedSubMenu;
        for (VBox boxMenu :listMenu){
            List<Node> children = boxMenu.getChildren();
            if (children!=null && !children.isEmpty()){
                for (Node child : children){
                    if (child instanceof ToggleButton){
                        if (child!= currentSelectedSubMenu){
                            ((ToggleButton) child).setSelected(false);
                        } else {
                            ((ToggleButton) child).setSelected(true);
                        }
                    }
                }
            }
        }
        hideProgressDialog();
    }

    public void showProgressDialog(){
        progressIndicator.setVisible(true);
    }

    public void hideProgressDialog(){
        progressIndicator.setVisible(false);
    }

    public void openUpdateUserInfoScreen(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getUpdateUserInfoScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) actionEvent.getSource());
    }

    public void openChangePasswordScreen(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getChangePasswordScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) actionEvent.getSource());
    }
    public void updateUserNameTextView(String userName){
        lbUser.setText(BundleUtils.getResourceBundle().getString("txt_hello"+ ", "+ userName));
    }

    @FXML
    void minimize(ActionEvent event) {
        App.palco.setIconified(true);
    }

    @FXML
    void close(ActionEvent event) {
        App.palco.close();
    }
}
