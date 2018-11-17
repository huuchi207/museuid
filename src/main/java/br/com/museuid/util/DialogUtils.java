package br.com.museuid.util;

import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Criar janelas de dialogos
 */
public class DialogUtils {

    private static final Screen screen = Screen.getPrimary();
    private static final Rectangle2D windows = screen.getVisualBounds();
    static Dialog stageDialog;
    private static ResponseMessage responseMessage = ResponseMessage.CANCEL;

    private DialogUtils() {
    }

    /**
     * According to the message type showDialog its respective icon
     */
    public static Label icon(String type) {

        Label label = new Label();

        switch (type) {
            case "INFO":
                label.getStyleClass().add("img-dialog");
                break;
            case "ERRO":
                label.getStyleClass().add("img-dialog-erro");
                break;
            case "ALERTA":
                label.getStyleClass().add("img-dialog-alert");
                break;
            case "CONFIRMAR":
                label.getStyleClass().add("img-dialog-confirm");
                break;
            default:
                label.getStyleClass().add("img-dialog");
                break;
        }
        return label;
    }

    /**
     * Formats text (title and description) of the message
     */
    public static VBox text(String title, String msg) {
        VBox box = new VBox();

        Label lbTitle = new Label(title);
        lbTitle.getStyleClass().add("titulo-dialogs");

        Label lbMsg = new Label(msg);
        lbMsg.getStyleClass().add("mensagem-dialogs");

        box.getChildren().addAll(lbTitle, lbMsg);
        box.getStyleClass().add("caixa-mensagem");

        return box;
    }

    /**
     * Add actions like Yes, No, Cancel, Ok
     */
    public static HBox action() {
        HBox box = new HBox();
        box.getStyleClass().add("box-acao-dialog");

        Button ok = new Button(BundleUtils.getResourceBundle().getString("txt_ok"));
        ok.setOnAction((ActionEvent e) -> {
            stageDialog.close();
        });

        ok.getStyleClass().add("bt-ok");
        box.getChildren().addAll(ok);

        return box;
    }

    /**
     * Add alert title and description to the message box
     */
    public static void message(String tipo, String title, String message) {
        box(icon(tipo), text(title, message), action());
    }

    /**
     * Allows the user to return a responseMessage of the message according to the type of the message as: OK, YES, NO and CANCEL
     */
    public static ResponseMessage mensageConfirmer(String titulo, String mensagem) {
        HBox box = new HBox();
        box.getStyleClass().add("box-acao-dialog");

        Button yes = new Button(BundleUtils.getResourceBundle().getString("txt_ok"));
        yes.setOnAction((ActionEvent e) -> {
            stageDialog.close();
            responseMessage = ResponseMessage.YES;
        });
        yes.getStyleClass().add("bt-sim");

        Button no = new Button(BundleUtils.getResourceBundle().getString("txt_cancel"));
        no.setOnAction((ActionEvent e) -> {
            stageDialog.close();
            responseMessage = ResponseMessage.NO;
        });
        no.getStyleClass().add("bt-nao");
        box.getChildren().addAll(yes, no);

        box(icon("CONFIRMAR"), text(titulo, mensagem), box);

        return responseMessage;
    }

    /**
     * Main Box that adds and formats the icon, message and action to the message box
     */
    public static void box(Label icon, VBox mensagem, HBox acoes) {
        GridPane grid = new GridPane();
        grid.add(icon, 0, 0);
        grid.add(mensagem, 1, 0);
        grid.add(acoes, 1, 1);
        grid.getStyleClass().add("box-grid");
        grid.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        HBox boxCentral = new HBox(grid);
        boxCentral.getStyleClass().add("box-msg");
        ResizeUtils.margin(boxCentral, 0);

        AnchorPane boxPrincipal = new AnchorPane(boxCentral);
        boxPrincipal.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");
        ResizeUtils.margin(boxPrincipal, 0);

        boxDialog(boxPrincipal);
    }

    /**
     * Main message box and added the main controller
     */
    public static void boxDialog(AnchorPane pane) {
        closeDialog();
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("br/com/museuid/css/dialog.css");
        scene.setFill(Color.TRANSPARENT);

        stageDialog = new Dialog(new Stage(), scene);
        stageDialog.showDialog();
    }

    public enum ResponseMessage {
        NO, YES, OK, CANCEL
    }

    /**
     * Creates and formats the main stage that will display the stageDialog message
     */
    static class Dialog extends Stage {
        public Dialog(Stage stage, Scene scene) {
            initStyle(StageStyle.TRANSPARENT);
            initModality(Modality.APPLICATION_MODAL);
            initOwner(stage);
            setX(windows.getMinX());
            setY(windows.getMinY());
            setWidth(windows.getWidth());
            setHeight(windows.getHeight());
            setScene(scene);
        }

        public void showDialog() {
            centerOnScreen();
            showAndWait();
        }
    }


    public static void closeDialog(){
        if (stageDialog != null && stageDialog.isShowing()){
            stageDialog.close();
        }
    }

    public static Optional<String> showPasswordRequireDialog(){
        TextInputDialog dialog = new TextInputDialog("walter");
        dialog.setTitle(BundleUtils.getResourceBundle().getString("txt_password_required"));
        dialog.setContentText(BundleUtils.getResourceBundle().getString("txt.password"));

        // Traditional way to get the response value.
        Optional<String> result = dialog.showAndWait();
        return result;

        // The Java 8 way to get the response value (with lambda expression).
        //        result.ifPresent(name -> System.out.println("Your name: " + name));
    }
    public static void showProgressDialog(){
        closeDialog();
        VBox boxCentral = new VBox();
        boxCentral.getChildren().add(new ProgressIndicator());
        Label lb = new Label(BundleUtils.getResourceBundle().getString("txt_progressing"));
        lb.getStyleClass().add("progress-dialogs-msg");
        boxCentral.getChildren().add(lb);
        boxCentral.getStyleClass().add("box-msg");
        ResizeUtils.margin(boxCentral, 0);

        AnchorPane apProgress = new AnchorPane(boxCentral);
        apProgress.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");
        ResizeUtils.margin(apProgress, 0);

        boxDialog(apProgress);
    }
}
