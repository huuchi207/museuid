package br.com.museuid.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

/**
 * Inicialização tela de login
 *
 * @author angelicalleite
 */
public class Login extends BaseApp {
    @Override
    FXMLLoader getMainContentLayout() {
        return new FXMLLoader(getClass().getResource("/br/com/museuid/screen/login/login.fxml"));
    }
    public static void main(String[] args) {
        Application.launch(Login.class, (java.lang.String[]) null);
    }
}
