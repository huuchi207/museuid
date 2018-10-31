package br.com.museuid.util;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
            if (campo.getText().trim().isEmpty()) {
                erro(campo, "Verificar valor vazio!");
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
            NoticeUtils.erro("Erro erro campo");
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
}
