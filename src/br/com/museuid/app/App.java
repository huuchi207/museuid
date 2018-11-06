package br.com.museuid.app;

import br.com.museuid.util.BundleUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Inicialização da aplicação princial
 *
 * @author angelicalleite
 */
public class App extends Application {

    public static Stage palco;
    private static Scene cena;
    private static AnchorPane page;

    private Screen screen = Screen.getPrimary();
    private Rectangle2D windows = screen.getVisualBounds();

    @Override
    public void start(final Stage stage) {
        try {
            palco = stage;
            FXMLLoader fxml = new FXMLLoader(App.class.getResource("/br/com/museuid/view/app/app.fxml"));
            fxml.setResources(BundleUtils.getResourceBundle());
            page = fxml.load();
            cena = new Scene(page);

            stage.initStyle(StageStyle.UNDECORATED);

            stage.setX(windows.getMinX());
            stage.setY(windows.getMinY());
            stage.setWidth(windows.getWidth());
            stage.setHeight(windows.getHeight());

//            stage.getIcons().addAll(new Image(App.class.getResourceAsStream("icon.png")));

            stage.setScene(cena);
            stage.show();

        } catch (Exception ex) {
            System.out.println(BundleUtils.getResourceBundle().getString("txt_error_when_launch_app") + ex);
        }
    }

    public static void main(String[] args) {
        Application.launch(App.class, (java.lang.String[]) null);
    }
}
