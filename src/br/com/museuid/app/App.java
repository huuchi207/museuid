package br.com.museuid.app;

import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.ResizeHelper;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Inicialização da aplicação princial
 *
 * @author angelicalleite
 */
public class App extends Application {

    private static Stage mStage;
    private static Scene mScene;
    private static AnchorPane rootPane;

    private Screen screen = Screen.getPrimary();
    private Rectangle2D windows = screen.getVisualBounds();

    @Override
    public void start(final Stage stage) {
        try {
            mStage = stage;
            FXMLLoader fxml = new FXMLLoader(App.class.getResource("/br/com/museuid/screen/app/app.fxml"));
            fxml.setResources(BundleUtils.getResourceBundle());
            rootPane = fxml.load();
            mScene = new Scene(rootPane);

            stage.setX(windows.getMinX());
            stage.setY(windows.getMinY());
            stage.setWidth(windows.getWidth());
            stage.setHeight(windows.getHeight());
            stage.setMinWidth(windows.getWidth()-200);
            stage.setMinHeight(windows.getHeight()-100);


//            stage.getIcons().addAll(new Image(App.class.getResourceAsStream("icon.png")));

            stage.setScene(mScene);
            ResizeHelper.addResizeListener(stage);
            stage.show();

        } catch (Exception ex) {
            System.out.println(BundleUtils.getResourceBundle().getString("txt_error_when_launch_app") + ex);
        }
    }

    public static void main(String[] args) {
        Application.launch(App.class, (java.lang.String[]) null);
    }

    public static Stage getmStage(){
        return mStage;
    }
}
