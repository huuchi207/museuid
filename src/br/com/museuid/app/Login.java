package br.com.museuid.app;

import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Messenger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Inicialização tela de login
 *
 * @author angelicalleite
 */
public class Login extends Application {

  public static Stage palco;
  private Scene cena;
  private AnchorPane page;

  private Screen screen = Screen.getPrimary();
  private Rectangle2D windows = screen.getVisualBounds();

  @Override
  public void start(final Stage stage) {
    try {
      palco = stage;

      FXMLLoader fxml = new FXMLLoader(App.class.getResource("/br/com/museuid/screen/login/login.fxml"));
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
      stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
        @Override
        public void handle(WindowEvent event) {
          Platform.exit();
          System.exit(0);
        }
      });
      stage.setScene(cena);
      stage.show();

    } catch (Exception ex) {
      Messenger.erro(BundleUtils.getResourceBundle().getString("txt_error") + ex);
    }
  }

  public static void main(String[] args) {
    Application.launch(Login.class, (java.lang.String[]) null);
  }
}
