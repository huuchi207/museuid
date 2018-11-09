package br.com.museuid.screen.login;

import java.util.List;

import br.com.museuid.app.App;
import br.com.museuid.app.Login;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.model.Usuario;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.sample.SampleCallback;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {

    public static Usuario usuarioLogado = null;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private PasswordField pfPass;
    @FXML
    private Label lbErrorLogin;
    @FXML
    private TextField tfUser;

    @FXML
    void login(ActionEvent event) {
        if (FieldViewUtils.noEmpty(tfUser, pfPass)){
            return;
        }
        String login = tfUser.getText();
        String password = pfPass.getText();
        if (ConstantConfig.FAKE){
          new App().start(new Stage());
          Login.palco.close();
          return;
        }
//        if (ControleDAO.getBanco().getLoginDAO().autenticarUsername(login)) {
//            if (ControleDAO.getBanco().getLoginDAO().autenticarSenha(login, password)) {
//                usuarioLogado = ControleDAO.getBanco().getLoginDAO().usuarioLogado(login);
//                new App().start(new Stage());
//                Login.palco.close();
//            } else {
//                lbErrorLogin.setText(BundleUtils.getResourceBundle().getString("txt_invalid_password"));
//                FieldViewUtils.erroLogin(pfPass);
//            }
//        } else {
//            lbErrorLogin.setText(BundleUtils.getResourceBundle().getString("txt_not_existed_user"));
//            FieldViewUtils.erroLogin(tfUser);
//        }
    }

    @FXML
    void minimize(ActionEvent event) {
        Login.palco.setIconified(true);
    }

    @FXML
    void close(ActionEvent event) {
        Login.palco.close();
    }

    @FXML
    void initialize() {
        pfPass.setOnKeyReleased((KeyEvent key) -> {
            if (key.getCode() == KeyCode.ENTER) {
                login(null);
            }
        });
    }

    public void forgotPassword(ActionEvent event) {
        DialogUtils.ResponseMessage responseMessage = DialogUtils.mensageConfirmer(BundleUtils.getResourceBundle().getString("txt_notice"),
            "Tính năng này chỉ dành cho chủ quán? Bạn có muốn sử dụng?");
        if (responseMessage == DialogUtils.ResponseMessage.YES) {
            //TODO: call api reset pass
           if (ConstantConfig.FAKE){
               showProgressDialog();
               ServiceBuilder.getApiService().getSample("12,32,15,37,10", "b1412ab2fc899acb1e7612034bfdf412").enqueue(new SampleCallback<Item>() {
                   @Override
                   public void onError(String errorCode, String errorMessage) {
                   }

                   @Override
                   public void onSuccess(List<Item> data) {
                       hideProgressDialog();
                       Messenger.info("Một tin nhắn được gửi tới email của bạn. Vui lòng kiểm tra và làm theo hướng dẫn!");
                   }
               });
           }
        }
    }
    public void showProgressDialog(){
        progressIndicator.setVisible(true);
    }

    public void hideProgressDialog(){
        progressIndicator.setVisible(false);
    }
}
