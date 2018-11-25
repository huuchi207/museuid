package br.com.museuid.app;

import java.util.Optional;

import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Messenger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

public class Initialize extends Application {
    public static Stage palco;
    private static Scene cena;
    private static AnchorPane page;

    private Screen screen = Screen.getPrimary();
    private Rectangle2D windows = screen.getVisualBounds();

    @Override
    public void start(Stage stage) throws Exception {
        try {
            palco = stage;
            FXMLLoader fxml = new FXMLLoader(App.class.getResource("/br/com/museuid/screen/initialize/initialize.fxml"));
            fxml.setResources(BundleUtils.getResourceBundle());
            page = fxml.load();
            cena = new Scene(page);

            //            stage.setX(windows.getMinX());
//            stage.setY(windows.getMinY());
//            stage.setWidth(619);
//            stage.setHeight(445);
            stage.resizableProperty().setValue(Boolean.FALSE);
//            stage.setMinWidth(windows.getWidth()-200);
//            stage.setMinHeight(windows.getHeight()-100);
            stage.centerOnScreen();
//            stage.getIcons().addAll(new Image(App.class.getResourceAsStream("icon.png")));

            stage.setScene(cena);
            stage.show();

        } catch (Exception ex) {
            Messenger.erro(BundleUtils.getResourceBundle().getString("txt_error_when_launch_app") + ex);
        }
    }
}
