package br.com.museuid.util;

import br.com.museuid.view.employee_management.EmployeeManagementControler;

import br.com.museuid.view.create_order_screen.CreateOrderScreenControler;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * Principal classe para controle e carregamento dos modulos da aplicação,
 * cada menu e alguns submenus da aplicação representa um modulo na aplicação
 */
public class Model {
    //new code
    private static EmployeeManagementControler employeeManagement;
    private static CreateOrderScreenControler createOrderScreenControler;
    private Model() {
    }

      /**
     * Configuração da tela de conteúdo para resetField painel e adicionar nova
     * tela, redimensionado seu tamanho para preencher a tela
     */
    public static void config(AnchorPane box, AnchorPane conteudo) {
        box.getChildren().clear();
        box.getChildren().add(conteudo);
        Resize.margin(conteudo, 0);
    }

    /**
     * Auxiliar na visualização de elementos da tela como: subenus, subtelas e
     * etc...
     */
    public static void setVisibility(boolean valor, Node... no) {
        for (Node elemento : no) {
            elemento.setVisible(valor);
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
    public static void config(AnchorPane box, StackPane conteudo) {
        box.getChildren().clear();
        box.getChildren().add(conteudo);
        Resize.margin(conteudo, 0);
    }
}
