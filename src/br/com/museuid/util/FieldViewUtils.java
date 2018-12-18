package br.com.museuid.util;

import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/**
 * Utilitario para formatação e verificação de campos de textos, labels e textareas
 */
public class FieldViewUtils {

    private FieldViewUtils() {
    }

    /**
     * Não permitir que campos de textos com valores nulos
     */
    public static boolean noEmpty(TextField... field) {

        boolean vazio = false;

        for (TextField campo : field) {
            if (campo.getText() == null || campo.getText().trim().isEmpty()) {
                erro(campo, BundleUtils.getResourceBundle().getString("txt_check_empty_txt"));
                vazio = true;
            }
        }

        return vazio;
    }

    public static boolean noEmpty(TextArea... field) {

        boolean vazio = false;

        for (TextArea campo : field) {
            if (campo.getText() == null || campo.getText().trim().isEmpty()) {
                erro(campo, BundleUtils.getResourceBundle().getString("txt_check_empty_txt"));
                vazio = true;
            }
        }

        return vazio;
    }

    public static boolean noEmpty(PasswordField... field) {

        boolean vazio = false;

        for (PasswordField campo : field) {
            if (campo.getText() == null || campo.getText().trim().isEmpty()) {
                erro(campo, BundleUtils.getResourceBundle().getString("txt_check_empty_txt"));
                vazio = true;
            }
        }

        return vazio;
    }
    /**
     * Limpar textos dos campos informados
     */
    public static void resetField(TextField... no) {
        for (TextField campo : no) {
            campo.setText("");
        }
    }

    /**
     * Limpar textos dos labels informados
     */
    public static void resetField(Label... no) {
        for (Label campo : no) {
            campo.setText("");
        }
    }

    /**
     * Limpar textos dos TextArea informado
     */
    public static void resetField(TextArea... no) {
        for (TextArea campo : no) {
            campo.setText("");
        }
    }

    /**
     * Indicador erro no campo informado com borda vermelha
     */
    public static void erro(Node no, String mensagem) {
        try {
            if (no != null) {
                no.setStyle("-fx-border-color: #ff7575;");
                origem(no);
            }
        } catch (Exception ex) {
            Messenger.erro(BundleUtils.getResourceBundle().getString("txt_error") + ex);
        }
    }

    /**
     * Ao clicar no campo voltar ao estilo padrão do campo
     */
    private static void origem(Node no) {
        no.setOnMouseClicked((MouseEvent me) -> {
            no.setStyle("-fx-border-color: #eaeaea;");
        });
    }

    /**
     * Exibir erro campo no login caso deixe espaço vazio ou incorreto
     */
    public static void erroLogin(Node no) {
      no.setStyle("-fx-border-color: #ff8b8b;");
      no.setOnMouseClicked((MouseEvent me) -> {
        no.setStyle("-fx-border-color: transparent transparent #e8e8e8 transparent;");
      });
    }

    public static void disableViews(boolean b, Node... nodes){
      for (Node node : nodes){
        node.setDisable(b);
      }
    }
    public static void disableViews(boolean b, List<Node> nodes){
      for (Node node : nodes){
        node.setDisable(b);
      }
    }
    private static EventHandler<KeyEvent> eventHandler;
    public static void setGlobalEventHandler(Node root, Button fireButton) {
      if (eventHandler!= null || fireButton== null){
        root.removeEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
      }
      if (fireButton != null){
        eventHandler = (ev -> {
          if (ev.getCode() == KeyCode.ENTER) {
            if (fireButton!= null)
              fireButton.fire();
            ev.consume();
          }
        });
        root.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
      }
    }

    public static void setEnterKeyEvent(Node node, Button fireButton){
      node.setOnKeyReleased(evt -> {
        if (evt.getCode() == KeyCode.ENTER) {
          fireButton.fire();
        }
        evt.consume();
      });
    }
}
