package br.com.museuid.screen.update_user_info;

import java.util.Optional;
import java.util.ResourceBundle;

import br.com.museuid.customview.PasswordDialog;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.StaticVarUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class UpdateUserInfoController extends AnchorPane {
    @FXML
    public TextField txtUserName;
    @FXML
    public TextField txtName;
    @FXML
    public TextField txtPhoneNumber;
    @FXML
    public TextField txtEmail;
    @FXML
    public TextField txtAddress;
    @FXML
    public Button btSave;
    private ResourceBundle bundle;

    public UpdateUserInfoController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("update_user_info.fxml"));

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
        getUserInfo();
    }

    //call update
    public void updateInfo(){
        if (FieldViewUtils.noEmpty(txtName)){
//            Messenger.erro("Họ và tên không được để trống!");
            return;
        }
        if (FieldViewUtils.noEmpty(txtEmail) && UserDTO.UserRole.ADMIN.name().equalsIgnoreCase(StaticVarUtils.getSessionUserInfo().getInfo().getRole())){
            Messenger.erro("Email cần thiết để sử dụng tính năng Quên mật khẩu. Vui lòng nhập vào!");
            return;
        }
        PasswordDialog pd = new PasswordDialog();
        Optional<String> result = pd.showAndWait();
        result.ifPresent(password -> {
            AppController.getInstance().showProgressDialog();
            String name = txtName.getText();
            String address = txtAddress.getText();
            String phoneNumber = txtPhoneNumber.getText();
            String mail = txtEmail.getText();

            UserDTO userDTO = StaticVarUtils.getSessionUserInfo().getInfo().copy();
            userDTO.setName(name);
            userDTO.setAddress(address);
            userDTO.setPhone(phoneNumber);
            userDTO.setEmail(mail);
            userDTO.setPassword(password);
            userDTO.setUserid(userDTO.getId());

            ServiceBuilder.getApiService().changeUserInfo(userDTO).enqueue(new BaseCallback<UserDTO>() {
                @Override
                public void onError(String errorCode, String errorMessage) {
                    AppController.getInstance().hideProgressDialog();
                    Messenger.erro(errorMessage);
                }

                @Override
                public void onSuccess(UserDTO data) {
                    AppController.getInstance().hideProgressDialog();
                    Messenger.info(bundle.getString("txt_operation_successful"));
                    StaticVarUtils.getSessionUserInfo().setInfo(data);
                }
            });
        });
    }

    private void getUserInfo(){
        //call api get info and set to userDTO in userUtil, then update screen in navigation
        AppController.getInstance().showProgressDialog();
        ServiceBuilder.getApiService().getCurrentUserInfo(StaticVarUtils.getSessionUserInfo().getInfo().getId()).enqueue(new BaseCallback<UserDTO>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
                AppController.getInstance().hideProgressDialog();
                Messenger.erro(errorMessage);
            }

            @Override
            public void onSuccess(UserDTO data) {
                updateViews(data);
                AppController.getInstance().hideProgressDialog();
                StaticVarUtils.getSessionUserInfo().setInfo(data);
            }
        });
    }
    private void updateViews(UserDTO data){
        txtName.setText(data.getName());
        txtUserName.setText(data.getUsername());
        txtAddress.setText(data.getAddress());
        txtEmail.setText(data.getEmail());
        txtPhoneNumber.setText(data.getPhone());
    }
}
