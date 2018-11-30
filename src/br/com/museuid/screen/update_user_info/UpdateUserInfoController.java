package br.com.museuid.screen.update_user_info;

import java.util.Optional;
import java.util.ResourceBundle;

import br.com.museuid.customview.PasswordDialog;
import br.com.museuid.dto.SecureQuestionDTO;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.SecureQuestionRequest;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.ComboUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.StaticVarUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

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
  public ToggleGroup toggleGroup;
  public AnchorPane apSecureQuestion;
  public GridPane gridGeneralInfo;
  public ComboBox<String> cbQuestions;
  public TextField tfAnswer;
  public RadioButton rbGeneralInfo;
  public RadioButton rbSecureQuestion;
  private ResourceBundle bundle;
  @FXML
  private ComboBox<String> cbUserRole;

  public VBox vboxContent;
  private enum UpdateMode{
    GENERAL_INFO, SECURE_QUESTION
  }
  private UpdateMode currentMode = UpdateMode.GENERAL_INFO;
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
    FieldViewUtils.disableViews(true, apSecureQuestion.getChildren());
    toggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      @Override
      public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {
        if (toggleGroup.getSelectedToggle() == null) {
          return;
        }
        if (toggleGroup.getSelectedToggle() == rbGeneralInfo){
          FieldViewUtils.disableViews(false, gridGeneralInfo.getChildren());
          FieldViewUtils.disableViews(true, apSecureQuestion.getChildren());
          currentMode = UpdateMode.GENERAL_INFO;
        } else {
          FieldViewUtils.disableViews(true, gridGeneralInfo.getChildren());
          FieldViewUtils.disableViews(false, apSecureQuestion.getChildren());
          currentMode = UpdateMode.SECURE_QUESTION;
        }
      }
    });
    if (UserDTO.UserRole.valueOf(StaticVarUtils.getSessionUserInfo().getInfo().getRole()) == UserDTO.UserRole.ADMIN){
      getSecureQuestion();
    }
    else {
      vboxContent.getChildren().removeAll(apSecureQuestion, rbGeneralInfo, rbSecureQuestion);
    }

  }

  //call update
  public void updateInfo() {
//    if (FieldViewUtils.noEmpty(txtName)) {
////            Messenger.erro("Họ và tên không được để trống!");
//      return;
//    }
//        if (FieldViewUtils.noEmpty(txtEmail) && UserDTO.UserRole.ADMIN.name().equalsIgnoreCase(StaticVarUtils.getSessionUserInfo().getInfo().getRole())){
//            Messenger.erro("Email cần thiết để sử dụng tính năng Quên mật khẩu. Vui lòng nhập vào!");
//            return;
//        }
    if (currentMode == UpdateMode.SECURE_QUESTION){
      if (FieldViewUtils.noEmpty(tfAnswer)){
        return;
      }
    }
    PasswordDialog pd = new PasswordDialog();
    Optional<String> result = pd.showAndWait();
    result.ifPresent(password -> {
      AppController.getInstance().showProgressDialog();
      if (currentMode == UpdateMode.GENERAL_INFO){
        String name = txtName.getText();
        String address = txtAddress.getText();
        String phoneNumber = txtPhoneNumber.getText();
        String mail = txtEmail.getText();

        UserDTO userDTO = StaticVarUtils.getSessionUserInfo().getInfo().copy();
        userDTO.setFullname(name);
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
      } else {
        SecureQuestionRequest secureQuestionRequest = new SecureQuestionRequest(
          StaticVarUtils.getSessionUserInfo().getInfo().getUsername(),
          cbQuestions.getValue(), tfAnswer.getText());
        ServiceBuilder.getApiService().updateSecureQuestion(secureQuestionRequest).enqueue(new BaseCallback<Object>() {
          @Override
          public void onError(String errorCode, String errorMessage) {
            AppController.getInstance().hideProgressDialog();
            Messenger.erro(errorMessage);
          }

          @Override
          public void onSuccess(Object data) {
            AppController.getInstance().hideProgressDialog();
            Messenger.info("Cập nhật câu hỏi bảo mật thành công");
          }
        });
      }


    });
  }

  private void getUserInfo() {
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

  private void getSecureQuestion(){
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().getListSecureQuestion().enqueue(new BaseCallback<SecureQuestionDTO>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        AppController.getInstance().hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(SecureQuestionDTO data) {
        AppController.getInstance().hideProgressDialog();
        ComboUtils.popular(cbQuestions, data.getQuestions());
      }
    });
  }

  private void updateViews(UserDTO data) {
    txtName.setText(data.getFullname());
    txtUserName.setText(data.getUsername());
    txtAddress.setText(data.getAddress());
    txtEmail.setText(data.getEmail());
    txtPhoneNumber.setText(data.getPhone());
    ObservableList<String> options =
      FXCollections.observableArrayList(
        UserDTO.UserRole.valueOf(data.getRole()).toString()
      );
    cbUserRole.setItems(options);
    cbUserRole.setValue(UserDTO.UserRole.valueOf(data.getRole()).toString());
  }
}
