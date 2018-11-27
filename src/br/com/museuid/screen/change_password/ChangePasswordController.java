package br.com.museuid.screen.change_password;

import org.apache.commons.lang3.StringUtils;

import java.util.ResourceBundle;

import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.ChangePasswordRequest;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.StaticVarUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;

public class ChangePasswordController extends AnchorPane {

    public Label lbTitle;
    public PasswordField pfOldPassword;
    public PasswordField pfNewPassword;
    public PasswordField pfConfirmNewPassword;
    public Button btChangePassword;
    private ResourceBundle bundle;

    public ChangePasswordController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("change_password.fxml"));

            fxml.setRoot(this);
            fxml.setController(this);
            bundle = BundleUtils.getResourceBundle();
            fxml.setResources(bundle);
            fxml.load();

        } catch (Exception ex) {
            Messenger.erro(bundle.getString("txt_loading_screen_error") + ex);
        }
    }

    @FXML
    public void initialize() {
        AppController.getInstance().showProgressDialog();
    }

    public void changePassword(){
        if (FieldViewUtils.noEmpty(pfConfirmNewPassword, pfNewPassword, pfOldPassword)){
            return;
        }
        if (!StringUtils.equals(pfNewPassword.getText(), pfConfirmNewPassword.getText())){
            Messenger.erro(bundle.getString("txt_new_password_mismatch"));
            return;
        }
        AppController.getInstance().showProgressDialog();
        ServiceBuilder.getApiService().changePassword(new ChangePasswordRequest(StaticVarUtils.getSessionUserInfo().getInfo().getId(),
            pfOldPassword.getText(), pfNewPassword.getText())).enqueue(new BaseCallback<Object>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
                AppController.getInstance().hideProgressDialog();
                Messenger.erro(errorMessage);
            }

            @Override
            public void onSuccess(Object data) {
                AppController.getInstance().hideProgressDialog();
                Messenger.info(bundle.getString("txt_operation_successful"));
            }
        });
    }

}
