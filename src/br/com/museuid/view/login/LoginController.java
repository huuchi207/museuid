package br.com.museuid.view.login;

import br.com.museuid.app.Login;
import br.com.museuid.banco.controle.ControleDAO;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.model.Usuario;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.app.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {

    public static Usuario usuarioLogado = null;

    @FXML
    private PasswordField pfPass;
    @FXML
    private Label lbErrorLogin;
    @FXML
    private TextField tfUser;

    @FXML
    void logar(ActionEvent event) {

        String login = tfUser.getText();
        String password = pfPass.getText();
        if (ConstantConfig.FAKE){
          new App().start(new Stage());
          Login.palco.close();
          return;
        }
        if (ControleDAO.getBanco().getLoginDAO().autenticarUsername(login)) {
            if (ControleDAO.getBanco().getLoginDAO().autenticarSenha(login, password)) {
                usuarioLogado = ControleDAO.getBanco().getLoginDAO().usuarioLogado(login);
                new App().start(new Stage());
                Login.palco.close();
            } else {
                lbErrorLogin.setText(BundleUtils.getResourceBundle().getString("txt_invalid_password"));
                FieldViewUtils.erroLogin(pfPass);
            }
        } else {
            lbErrorLogin.setText(BundleUtils.getResourceBundle().getString("txt_not_existed_user"));
            FieldViewUtils.erroLogin(tfUser);
        }
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
                logar(null);
            }
        });
    }

}
