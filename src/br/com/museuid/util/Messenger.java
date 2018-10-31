package br.com.museuid.util;

import br.com.museuid.util.DialogUtils.Resposta;

/**
 * Criação de mensagem apartir do classe de dialogo
 */
public class Messenger {

    private Messenger() {
    }

    public static void info(String mensagem) {
        DialogUtils.message("INFO", "Informação", mensagem);
    }

    public static void info(String mensagem, String titulo) {
        DialogUtils.message("INFO", titulo, mensagem);
    }

    public static void erro(String mensagem) {
        DialogUtils.message("ERRO", "Erro", mensagem);
    }

    public static void erro(String mensagem, String titulo) {
        DialogUtils.message("ERRO", titulo, mensagem);
    }

    public static void alert(String mensagem) {
        DialogUtils.message("ALERTA", "Alerta", mensagem);
    }

    public static void alert(String mensagem, String titulo) {
        DialogUtils.message("ALERTA", titulo, mensagem);
    }

    public static Resposta confirm(String mensagem) {
        return DialogUtils.mensageConfirmar("Xác nhận", mensagem);
    }

    public static Resposta confirm(String titulo, String mensagem) {
        return DialogUtils.mensageConfirmar(titulo, mensagem);
    }
}
