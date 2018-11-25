package br.com.museuid.screen.app;

import java.util.List;

import br.com.museuid.Constants;
import br.com.museuid.app.App;
import br.com.museuid.app.Initialize;
import br.com.museuid.app.Login;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.util.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppController {
    private static AppController instance;
    @FXML

    public VBox boxOrderSection;
    public ToggleButton btCreateOrder;

    public ToggleButton btOrderSection;
    public ToggleButton btOrderCreated;
    public Label lbUser;

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
    void initialize() {
        instance = this;

        //TODO: add more vbox list submenu
        listMenu = new VBox[]{boxOrderSection};
        //TODO: change to default selected submenu
        openCreateOrderScreen(new ActionEvent(btCreateOrder, null));
    }

    /**
     * Obter componente para exbição das notas
     */
    public VBox boxNotas() {
        return boxNotas;
    }

    public void openCreateOrderScreen(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getCreateOrderScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) actionEvent.getSource());
    }


    public void openOrderCreatedScreen(ActionEvent actionEvent) {
        if (actionEvent.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getMyOrderCreatedScreen(boxContainer);
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

    @FXML
    void minimize(ActionEvent event) {
        App.getmStage().setIconified(true);
    }

    @FXML
    void close(ActionEvent event) {
        App.getmStage().close();
    }

    public void startInitialize(){
        try {
            new Initialize().start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        App.getmStage().close();
    }

    public void goSite(ActionEvent event) {

    }
}
