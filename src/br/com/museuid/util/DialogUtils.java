package br.com.museuid.util;

import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
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
    static Dialog dialog;
    private static ResponseMessage responseMessage = ResponseMessage.CANCEL;

    private DialogUtils() {
    }

    /**
     * Conforme o tipo da mensagem showDialog seu respectivo icone
     */
    public static Label icone(String tipo) {

        Label label = new Label();

        switch (tipo) {
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
     * Formata texto (titulo e descrição) da mensagem
     */
    public static VBox texto(String title, String msg) {
        VBox box = new VBox();

        Label titulo = new Label(title);
        titulo.getStyleClass().add("titulo-dialogs");

        Label mensagem = new Label(msg);
        mensagem.getStyleClass().add("mensagem-dialogs");

        box.getChildren().addAll(titulo, mensagem);
        box.getStyleClass().add("caixa-mensagem");

        return box;
    }

    /**
     * Adiciona ações como Sim, Não, Cancelar, Ok
     */
    public static HBox acoes() {
        HBox box = new HBox();
        box.getStyleClass().add("box-acao-dialog");

        Button ok = new Button(BundleUtils.getResourceBundle().getString("txt_ok"));
        ok.setOnAction((ActionEvent e) -> {
            dialog.close();
        });

        ok.getStyleClass().add("bt-ok");
        box.getChildren().addAll(ok);

        return box;
    }

    /**
     * Adiciona titulo e descriçao do alert ao box de mensagem
     */
    public static void message(String tipo, String title, String message) {
        box(icone(tipo), texto(title, message), acoes());
    }

    /**
     * Permite que o usuário retorne uma responseMessage da mensagem de acordo com o
     * tipo da mensagem como: OK, SIM, NÃO e CANCELAR
     */
    public static ResponseMessage mensageConfirmar(String titulo, String mensagem) {
        HBox box = new HBox();
        box.getStyleClass().add("box-acao-dialog");

        Button yes = new Button(BundleUtils.getResourceBundle().getString("txt_ok"));
        yes.setOnAction((ActionEvent e) -> {
            dialog.close();
            responseMessage = ResponseMessage.YES;
        });
        yes.getStyleClass().add("bt-sim");

        Button no = new Button(BundleUtils.getResourceBundle().getString("txt_cancel"));
        no.setOnAction((ActionEvent e) -> {
            dialog.close();
            responseMessage = ResponseMessage.NO;
        });
        no.getStyleClass().add("bt-nao");
        box.getChildren().addAll(yes, no);

        box(icone("CONFIRMAR"), texto(titulo, mensagem), box);

        return responseMessage;
    }

    /**
     * Box principal que adiciona e formata o icone, mensagem e ação ao box de
     * mensagem
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
        Resize.margin(boxCentral, 0);

        AnchorPane boxPrincipal = new AnchorPane(boxCentral);
        boxPrincipal.setStyle("-fx-background-color: rgba(0, 0, 0, 0.0);");
        Resize.margin(boxPrincipal, 0);

        boxDialogo(boxPrincipal);
    }

    /**
     * Box principal da mensagem e adicionado a tela principal
     */
    public static void boxDialogo(AnchorPane pane) {
        Scene scene = new Scene(pane);
        scene.getStylesheets().add("br/com/museuid/css/dialog.css");
        scene.setFill(Color.TRANSPARENT);

        dialog = new Dialog(new Stage(), scene);
        dialog.showDialog();
    }

    public enum ResponseMessage {
        NO, YES, OK, CANCEL
    }

    /**
     * Cria e formata a stage principal que sera exibido a mensagem de dialog
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
        if (dialog.isShowing()){
            dialog.close();
        }
    }
}
