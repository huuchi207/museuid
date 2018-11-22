package br.com.museuid.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

public class App extends BaseApp {

    public static void main(String[] args) {
        Application.launch(App.class, (java.lang.String[]) null);
    }

    @Override
    FXMLLoader getMainContentLayout() {
        return new FXMLLoader(App.class.getResource("/br/com/museuid/screen/app/app.fxml"));
    }
}
