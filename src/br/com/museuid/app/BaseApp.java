package br.com.museuid.app;

import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.ResizeHelper;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public abstract class BaseApp extends  Application{
    private static Stage mStage;
    private Scene scene;
    private AnchorPane rootPane;
    private AnchorPane titlebarPane;
    private Screen screen = Screen.getPrimary();
    private Rectangle2D windows = screen.getVisualBounds();
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean isFullScreen = false;
    @Override
    public void start(Stage stage) {
        try {
            mStage = stage;
            FXMLLoader titleBarLayout = new FXMLLoader(getClass().getResource("/br/com/museuid/screen/titlebar/title_bar.fxml"));
            FXMLLoader mainLayout = getMainContentLayout();
            mainLayout.setResources(BundleUtils.getResourceBundle());
            rootPane = mainLayout.load();
            titlebarPane = titleBarLayout.load();

            titlebarPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                        if(mouseEvent.getClickCount() == 2){
                            if (!isFullScreen) {
                                fullScreenSize();
                                mouseEvent.consume();
                            } else {
                                defaultSize();
                                mouseEvent.consume();
                            }
                        }
                    }
                }
            });
            titlebarPane.setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });

            //move around here
            titlebarPane.setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });

            VBox vBox = new VBox();


//            vBox.setPrefSize(windows.getWidth(), windows.getHeight());
//            vBox.prefWidthProperty().bind(stage.widthProperty());
//            vBox.prefHeightProperty().bind(stage.heightProperty());

            vBox.getChildren().addAll(titlebarPane, rootPane);
            VBox.setVgrow(rootPane, Priority.ALWAYS);
            vBox.setFillWidth(true);
            scene = new Scene(vBox);

            stage.initStyle(StageStyle.UNDECORATED);
            mStage.setX(windows.getMinX());
            mStage.setY(windows.getMinY());
            defaultSize();
            mStage.setMinHeight(600);
            mStage.setMinWidth(600);

//            stage.getIcons().addAll(new Image(Login.class.getResourceAsStream("icon.png")));

            stage.setScene(scene);
            stage.setResizable(true);
            ResizeHelper.addResizeListener(stage);
            stage.show();

        } catch (Exception ex) {
            Messenger.erro(BundleUtils.getResourceBundle().getString("txt_error") + ex);
        }
    }

    abstract FXMLLoader getMainContentLayout();
    public static Stage getmStage() {
        return mStage;
    }

    public Scene getScene() {
        return scene;
    }

    public AnchorPane getRootPane() {
        return rootPane;
    }

    private void fullScreenSize(){
        isFullScreen = true;

        mStage.setWidth(windows.getWidth());
        mStage.setHeight(windows.getHeight());
    }
    private void defaultSize(){
        isFullScreen = false;
        mStage.setWidth(windows.getWidth()-200);
        mStage.setHeight(windows.getHeight()-50);
        mStage.centerOnScreen();

    }
}
