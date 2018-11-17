package br.com.museuid.util;

import br.com.museuid.controller.ChangePasswordController;
import br.com.museuid.controller.CreateOrderScreenControler;
import br.com.museuid.controller.DeviceManagementControler;
import br.com.museuid.controller.MyOrderCreatedController;
import br.com.museuid.controller.OrderInQueueController;
import br.com.museuid.controller.ProductManagementControler;
import br.com.museuid.controller.StatisticController;
import br.com.museuid.controller.UpdateUserInfoController;
import br.com.museuid.controller.UserManagementControler;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * Main class for controlling and loading the application modules,
 * each menu and some submenus of the application represents a module in the application
 */
public class NavigationUtils {
    //new code
    private static UserManagementControler employeeManagement;
    private static CreateOrderScreenControler createOrderScreenControler;
    private static UpdateUserInfoController updateUserInfoController;
    private static MyOrderCreatedController myOrderCreatedController;
    private static StatisticController statisticController;
    private static ChangePasswordController changePasswordController;
    private static OrderInQueueController orderInQueueController;
    private static ProductManagementControler productManagementControler;
    private static DeviceManagementControler deviceManagementControler;
    private NavigationUtils() {
    }

    /**
     * Content controller setup for panel resetField and
     * add new controller, resized its size to fill the controller
     */
    public static void config(AnchorPane box, AnchorPane conteudo) {
        box.getChildren().clear();
        box.getChildren().add(conteudo);
        ResizeUtils.margin(conteudo, 0);
    }

    /**
     * Assist in the visualization of controller elements like: subenus, subtelas and etc ...
     */
    public static void setVisibility(boolean valor, Node... no) {
        for (Node element : no) {
            element.setVisible(valor);
        }
    }

    public static void getEmployeeManagementScreen(AnchorPane box) {
        employeeManagement = new UserManagementControler();
        config(box, employeeManagement);
    }

    public static void getCreateOrderScreen(AnchorPane box){
        createOrderScreenControler = new CreateOrderScreenControler();
        config(box, createOrderScreenControler);
    }

    public static void getUpdateUserInfoScreen(AnchorPane box){
        updateUserInfoController = new UpdateUserInfoController();
        config(box, updateUserInfoController);
    }

    public static void getMyOrderCreatedScreen(AnchorPane box){
        myOrderCreatedController = myOrderCreatedController!= null ? myOrderCreatedController : new MyOrderCreatedController();
        config(box, myOrderCreatedController);
    }

    public static void getStatisticScreen(AnchorPane box){
        statisticController = new StatisticController();
        config(box, statisticController);
    }

    public static void getChangePasswordScreen(AnchorPane box){
        changePasswordController = new ChangePasswordController();
        config(box, changePasswordController);
    }

    public static void getOrderInQueueScreen(AnchorPane box){
        orderInQueueController = new OrderInQueueController();
        config(box, orderInQueueController);
    }

    public static void getProductManagementScreen(AnchorPane box){
        productManagementControler = new ProductManagementControler();
        config(box, productManagementControler);
    }

    public static void getDeviceManagementScreen(AnchorPane box){
        deviceManagementControler = new DeviceManagementControler();
        config(box, deviceManagementControler);
    }
    public static void config(AnchorPane box, StackPane conteudo) {
        box.getChildren().clear();
        box.getChildren().add(conteudo);
        ResizeUtils.margin(conteudo, 0);
    }
}
