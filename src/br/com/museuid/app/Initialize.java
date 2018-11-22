package br.com.museuid.app;

import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Messenger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.util.Optional;

public class Initialize extends Application {
    public static Stage stage;
    private static Scene scene;
    private static AnchorPane rootPane;

    private Screen screen = Screen.getPrimary();
    private Rectangle2D windows = screen.getVisualBounds();

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Initialize.stage = stage;
            FXMLLoader fxml = new FXMLLoader(App.class.getResource("/br/com/museuid/view/initialize/initialize.fxml"));
            fxml.setResources(BundleUtils.getResourceBundle());
            rootPane = fxml.load();
            scene = new Scene(rootPane);

            stage.initStyle(StageStyle.UNDECORATED);

            stage.setX(windows.getMinX());
            stage.setY(windows.getMinY());
            stage.setWidth(windows.getWidth());
            stage.setHeight(windows.getHeight());

//            mStage.getIcons().addAll(new Image(App.class.getResourceAsStream("icon.png")));

            stage.setScene(scene);
            stage.show();

        } catch (Exception ex) {
            Messenger.erro(BundleUtils.getResourceBundle().getString("txt_error_when_launch_app") + ex);
        }
    }
    private void showInitDialog(){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Đăng ký máy!");
        dialog.setHeaderText("Để sử dụng phần mềm, vui lòng nhập các thông tin sau");

        // Set the icon (must be included in the project).
//        dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType(BundleUtils.getResourceBundle().getString("txt.sign.in"), ButtonBar.ButtonData.OK_DONE);
//        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField name = new TextField();
        name.setPromptText("VD: Máy 1");
        TextField location = new TextField();
        location.setPromptText("VD: Tầng 1");

        grid.add(new Label("Tên máy:"), 0, 0);
        grid.add(name, 1, 0);
        grid.add(new Label("Vị trí:"), 0, 1);
        grid.add(location, 1, 1);

        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        name.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> name.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(name.getText(), location.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(data -> {

        });
    }
}
