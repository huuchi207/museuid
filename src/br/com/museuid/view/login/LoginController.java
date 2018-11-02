package br.com.museuid.view.login;

import br.com.museuid.app.Login;
import br.com.museuid.banco.controle.ControleDAO;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.model.Usuario;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Link;
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
    private PasswordField psSenha;
    @FXML
    private Label lbErroLogin;
    @FXML
    private TextField txtUsuario;

    @FXML
    void logar(ActionEvent event) {

        String login = txtUsuario.getText();
        String senha = psSenha.getText();
        if (ConstantConfig.FAKE){
          new App().start(new Stage());
          Login.palco.close();
        }
        if (ControleDAO.getBanco().getLoginDAO().autenticarUsername(login)) {
            if (ControleDAO.getBanco().getLoginDAO().autenticarSenha(login, senha)) {
                usuarioLogado = ControleDAO.getBanco().getLoginDAO().usuarioLogado(login);
                new App().start(new Stage());
                Login.palco.close();
            } else {
                lbErroLogin.setText(BundleUtils.getResourceBundle().getString("txt_invalid_password"));
                FieldViewUtils.erroLogin(psSenha);
            }
        } else {
            lbErroLogin.setText(BundleUtils.getResourceBundle().getString("txt_not_existed_user"));
            FieldViewUtils.erroLogin(txtUsuario);
        }
    }

    @FXML
    void linkMuseuID(ActionEvent event) {
        Link.endereco("http://www.museuid.com.br");
    }

    @FXML
    void linkGeoPark(ActionEvent event) {
        Link.endereco("http://geoparkararipe.org.br");
    }

    @FXML
    void minimizar(ActionEvent event) {
        Login.palco.setIconified(true);
    }

    @FXML
    void fechar(ActionEvent event) {
        Login.palco.close();
    }

    @FXML
    void initialize() {
        acessar(psSenha);
    }

    /**
     * Função para chamar procedimento de login ao clicar ENTER no campo de
     * senha
     */
    private void acessar(PasswordField senha) {
        senha.setOnKeyReleased((KeyEvent key) -> {
            if (key.getCode() == KeyCode.ENTER) {
                logar(null);
            }
        });
    }
}
