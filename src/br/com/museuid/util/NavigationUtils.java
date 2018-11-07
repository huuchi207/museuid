package br.com.museuid.util;

import br.com.museuid.view.change_password.ChangePasswordController;
import br.com.museuid.view.employee_management.EmployeeManagementControler;

import br.com.museuid.view.create_order_screen.CreateOrderScreenControler;
import br.com.museuid.view.order_created.MyOrderCreatedController;
import br.com.museuid.view.statistic.StatisticController;
import br.com.museuid.view.update_user_info.UpdateUserInfoController;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * Main class for controlling and loading the application modules,
 * each menu and some submenus of the application represents a module in the application
 */
public class NavigationUtils {
    //new code
    private static EmployeeManagementControler employeeManagement;
    private static CreateOrderScreenControler createOrderScreenControler;
    private static UpdateUserInfoController updateUserInfoController;
    private static MyOrderCreatedController myOrderCreatedController;
    private static StatisticController statisticController;
    private static ChangePasswordController changePasswordController;
    private NavigationUtils() {
    }

    /**
     * Content screen setup for panel resetField and
     * add new screen, resized its size to fill the screen
     */
    public static void config(AnchorPane box, AnchorPane conteudo) {
        box.getChildren().clear();
        box.getChildren().add(conteudo);
        ResizeUtils.margin(conteudo, 0);
    }

    /**
     * Assist in the visualization of screen elements like: subenus, subtelas and etc ...
     */
    public static void setVisibility(boolean valor, Node... no) {
        for (Node element : no) {
            element.setVisible(valor);
        }
    }

    public static void getEmployeeManagementScreen(AnchorPane box) {
        employeeManagement = new EmployeeManagementControler();
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
        myOrderCreatedController = new MyOrderCreatedController();
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
    public static void config(AnchorPane box, StackPane conteudo) {
        box.getChildren().clear();
        box.getChildren().add(conteudo);
        ResizeUtils.margin(conteudo, 0);
    }
}
