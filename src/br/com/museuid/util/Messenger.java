package br.com.museuid.util;

/**
 * Criação de mensagem apartir do classe de dialog
 */
public class Messenger {

    private Messenger() {
    }

    public static void info(String mensagem) {
        DialogUtils.message("INFO", BundleUtils.getResourceBundle().getString("txt_announcement"), mensagem);
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

    public static DialogUtils.ResponseMessage confirm(String mensagem) {
        return DialogUtils.mensageConfirmar("Xác nhận", mensagem);
    }

    public static DialogUtils.ResponseMessage confirm(String titulo, String mensagem) {
        return DialogUtils.mensageConfirmar(titulo, mensagem);
    }
}
