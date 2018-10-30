package br.com.museuid.util;

import br.com.museuid.util.Dialogo.Resposta;

/**
 * Criação de mensagem apartir do classe de dialogo
 */
public class Messenger {

    private Messenger() {
    }

    public static void info(String mensagem) {
        Dialogo.message("INFO", "Informação", mensagem);
    }

    public static void info(String mensagem, String titulo) {
        Dialogo.message("INFO", titulo, mensagem);
    }

    public static void erro(String mensagem) {
        Dialogo.message("ERRO", "Erro", mensagem);
    }

    public static void erro(String mensagem, String titulo) {
        Dialogo.message("ERRO", titulo, mensagem);
    }

    public static void alerta(String mensagem) {
        Dialogo.message("ALERTA", "Alerta", mensagem);
    }

    public static void alerta(String mensagem, String titulo) {
        Dialogo.message("ALERTA", titulo, mensagem);
    }

    public static Resposta confirmar(String mensagem) {
        return Dialogo.mensageConfirmar("Confirmar", mensagem);
    }

    public static Resposta confirmar(String titulo, String mensagem) {
        return Dialogo.mensageConfirmar(titulo, mensagem);
    }
}
