package br.com.museuid.util;

import br.com.museuid.screen.app.AppController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * Criar Notas informativas, simplicação sistema de mensage
 */
public class AppNoticeUtils {

  private static int qtNotas = 0;
  private static AppNoticeUtils instance;
  public static AppNoticeUtils getInstance(){
    if (instance == null){
      instance = new AppNoticeUtils();
    }
    return instance;
  }
  private static VBox vBox;
  private AppNoticeUtils() {
    Timeline fiveSecondsWonder = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {

      @Override
      public void handle(ActionEvent event) {
        if (vBox!= null && vBox.getChildren()!=null && vBox.getChildren().size()>0){
          vBox.getChildren().remove(vBox.getChildren().size() - 1);
        }
      }
    }));
    fiveSecondsWonder.setCycleCount(Timeline.INDEFINITE);
    fiveSecondsWonder.play();
  }

  /**
   * Cria a nota e adicionar ao box passado
   */
  private static void nota(VBox box, String mensagem, String tipo) {
    vBox = box;
    HBox nota = new HBox(label(mensagem, tipo));
    nota.getStylesheets().add("br/com/museuid/css/dialog.css");

    nota.getChildren().add(close(box, nota));
    nota.getStyleClass().add("box-nota");
    AnimationUtils.fade(nota, 0.5, 1, 1);
    ++qtNotas;

    if (qtNotas >= 5) {
      box.getChildren().remove(box.getChildren().size() - 1);
      --qtNotas;
    }

    box.getChildren().add(nota);
  }

  /**
   * Adicionar ação fechar nota
   */
  private static Button close(VBox box, HBox nota) {

    Button acao = new Button();
    acao.getStyleClass().add("bt-close-nota");

    acao.setOnAction((ActionEvent e) -> {
      box.getChildren().remove(nota);
      --qtNotas;
    });

    return acao;
  }

  /**
   * Formatar mensage da nota
   */
  private static Label label(String texto, String tipo) {

    Label mensagem = new Label(texto);
    mensagem.getStyleClass().add("nota-texto");
    HBox.setHgrow(mensagem, Priority.ALWAYS);
    mensagem.setMaxWidth(Double.MAX_VALUE);
    icone(tipo, mensagem);

    return mensagem;
  }

  /**
   * Conforme o tipo da nota showDialog seu respectivo icon
   */
  private static void icone(String tipo, Label mensagem) {
    switch (tipo) {
      case "INFO":
        mensagem.getStyleClass().add("nota-info");
        break;
      case "ERRO":
        mensagem.getStyleClass().add("nota-erro");
        break;
      case "ALERTA":
        mensagem.getStyleClass().add("nota-alert");
        break;
      case "CONFIRMAR":
        mensagem.getStyleClass().add("nota-confirma");
        break;
      default:
        mensagem.getStyleClass().add("nota-info");
        break;
    }
  }

  public static void alert(String mensagem) {
    getInstance().nota(AppController.getInstance().boxNotas(), mensagem, "ALERTA");
  }

  public static void info(String mensagem) {
    getInstance().nota(AppController.getInstance().boxNotas(), mensagem, "INFO");
  }

  public static void erro(String mensagem) {
    getInstance(). nota(AppController.getInstance().boxNotas(), mensagem, "ERRO");
  }

  public static void confirma(String mensagem) {
    getInstance().nota(AppController.getInstance().boxNotas(), mensagem, "CONFIRMAR");
  }
}
