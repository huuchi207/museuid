package br.com.museuid.screen.login;

import br.com.museuid.app.App;
import br.com.museuid.app.Login;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.StringBody;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController {

    public AnchorPane apSecureQuestion;
    public AnchorPane apLogin;
    public ComboBox<String> cbQuestions;
    public TextField tfAnswer;
    public Button btOK;
    public Button btCancel;
    public Label lbSecureQuestion;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private PasswordField pfPass;
    @FXML
    private Label lbErrorLogin;
    @FXML
    private TextField tfKey;
    private boolean isLoginPressed = false;


    @FXML
    void active(ActionEvent event) {
        if (FieldViewUtils.noEmpty(tfKey)){
            return;
        }
        if (ConstantConfig.FAKE){
            Messenger.info("Kích hoạt thành công!");
            Platform.exit();
            System.exit(0);
        } else {
            showProgressDialog();
            ServiceBuilder.getApiService().activeServer(new StringBody(tfKey.getText().trim())).enqueue(new BaseCallback<Object>() {
                @Override
                public void onError(String errorCode, String errorMessage) {
                    hideProgressDialog();
                    Messenger.erro(errorMessage);
                }

                @Override
                public void onSuccess(Object data) {
                    hideProgressDialog();
                    Messenger.info("Kích hoạt thành công!");
                    Platform.exit();
                    System.exit(0);
                }
            });
        }
    }

    private void startMain() {
        new App().start(new Stage());
        Login.palco.close();
    }

    @FXML
    void initialize() {

    }

    public void forgotPassword(ActionEvent event) {

    }

    public void ok(ActionEvent event) {

    }

    public void cancel(ActionEvent event) {

    }

    public void showProgressDialog() {
        progressIndicator.setVisible(true);
    }

    public void hideProgressDialog() {
        progressIndicator.setVisible(false);
    }
}
