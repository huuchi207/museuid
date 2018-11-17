package br.com.museuid.controller;

import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.com.museuid.app.App;
import br.com.museuid.app.Login;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
import br.com.museuid.util.StaticVarUtils;
import javafx.application.Platform;
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
    public VBox boxManagement;
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
    public ToggleButton btOrderInQueue;
    public ToggleButton btProductManagement;
    public ToggleButton btDeviceManagement;
    public ToggleButton btStatistic;
    public ToggleButton btStockImporting;
    public VBox boxMenu;

    @FXML
    private AnchorPane boxContainer;
    @FXML
    private VBox boxNotas;
    private ToggleButton currentScreen;
    @FXML
    private ProgressIndicator progressIndicator;

    private List<Node> userListBoxMenu;
    private List<Node> userListFeatureView;

    public static AppController getInstance() {
        return instance;
    }

    @FXML
    void logOut(ActionEvent event) {
        showProgressDialog();
        ServiceBuilder.getApiService().logOut().enqueue(new BaseCallback<Object>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
                hideProgressDialog();
                Messenger.erro(errorMessage);
            }

            @Override
            public void onSuccess(Object data) {
                App.palco.close();
                new Login().start(new Stage());
            }
        });
    }

    @FXML
    void initialize() {
        instance = this;

        if (!EnumUtils.isValidEnum(UserDTO.UserRole.class, StaticVarUtils.getSessionUserInfo().getInfo().getRole()) && !ConstantConfig.FAKE){
            startLogin();
        }
        UserDTO.UserRole role;
        if (ConstantConfig.FAKE){
            role = UserDTO.UserRole.ADMIN;
        } else{
            role = UserDTO.UserRole.valueOf(StaticVarUtils.getSessionUserInfo().getInfo().getRole());
        }
        switch (role){
            case ADMIN:
                userListBoxMenu = new ArrayList<Node>(
                    Arrays.asList(btOrderSection, boxOrderSection, btAppManagementSection, boxManagement, btUserInfoSection, boxUserInfo));
                userListFeatureView = new ArrayList<Node>(
                Arrays.asList(btOrderInQueue, btEmployeeManagement, btProductManagement, btDeviceManagement, btStatistic, btStockImporting,
                btUpdateUserInfo, btChangePassword));
                openOrderInQueueScreen(new ActionEvent(btOrderInQueue, null));
                break;
            case MANAGER:
                userListBoxMenu = new ArrayList<Node>(
                    Arrays.asList(btAppManagementSection, boxManagement, btUserInfoSection,  boxUserInfo));
                userListFeatureView = new ArrayList<Node>(
                    Arrays.asList(btStatistic, btUpdateUserInfo, btChangePassword));
                openStatistic(new ActionEvent(btStatistic, null));
                break;
            case STOCKER:
                userListBoxMenu = new ArrayList<Node>(
                    Arrays.asList(btAppManagementSection, boxManagement, btUserInfoSection, boxUserInfo));
                userListFeatureView = new ArrayList<Node>(
                    Arrays.asList(btStockImporting, btUpdateUserInfo, btChangePassword));
                openStockImporting(new ActionEvent(btStockImporting, null));
                break;
            case EMPLOYEE:
                userListBoxMenu = new ArrayList<Node>(
                    Arrays.asList(btOrderSection, boxOrderSection, btUserInfoSection, boxUserInfo));
                userListFeatureView = new ArrayList<Node>(
                    Arrays.asList(btOrderInQueue, btUpdateUserInfo, btChangePassword));
                openOrderInQueueScreen(new ActionEvent(btOrderInQueue, null));
                break;
        }
        //remove views
        List<Node> removingParentViews= new ArrayList<>();
        List<Node> removingChildViews= new ArrayList<>();
        for (Node child: boxMenu.getChildren()){
            if (userListBoxMenu.contains(child)){
                //check children of this child
                removingChildViews.clear();
                if (child instanceof VBox){
                    for (Node childOfChild: ((VBox) child).getChildren()){
                        if (!userListFeatureView.contains(childOfChild)){
                            removingChildViews.add(childOfChild);
                        }
                    }
                    ((VBox) child).getChildren().removeAll(removingChildViews);
                }
            } else {
                removingParentViews.add(child);
            }
        }
        boxMenu.getChildren().removeAll(removingParentViews);
        if (!ConstantConfig.FAKE)
            lbUser.setText("Xin chào, "+ StaticVarUtils.getSessionUserInfo().getInfo().getUsername());
    }

    /**
     * Obter componente para exbição das notas
     */
    public VBox boxNotas() {
        return boxNotas;
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
        for (Node boxMenu : userListBoxMenu){
            if (boxMenu instanceof VBox){
                List<Node> children = ((VBox) boxMenu).getChildren();
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
        Platform.exit();
        System.exit(0);
    }

    public void openOrderInQueueScreen(ActionEvent event) {
        if (event.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getOrderInQueueScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) event.getSource());
    }

    public void startLogin(){
        new Login().start(new Stage());
        App.palco.close();
    }

    public void openProductManagement(ActionEvent event) {
        if (event.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getProductManagementScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) event.getSource());
    }

    public void openDeviceManagement(ActionEvent event) {
        if (event.getSource() == currentScreen){
            return;
        }
        NavigationUtils.getDeviceManagementScreen(boxContainer);
        setCurrentSubMenuAndStyleThenHideProgressIndicator((ToggleButton) event.getSource());
    }

    public void openStockImporting(ActionEvent event) {

    }
}
