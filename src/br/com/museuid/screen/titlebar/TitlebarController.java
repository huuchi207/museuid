package br.com.museuid.screen.titlebar;

import br.com.museuid.app.BaseApp;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TitlebarController {
    @FXML
    void minimize(ActionEvent event) {
        BaseApp.getmStage().setIconified(true);
    }

    @FXML
    void close(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

}
