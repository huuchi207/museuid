package br.com.museuid.screen.login;

import br.com.museuid.app.App;
import br.com.museuid.app.Login;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.SecureQuestionDTO;
import br.com.museuid.dto.SessionUserInfo;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.LoginRequest;
import br.com.museuid.service.remote.requestbody.SecureQuestionRequest;
import br.com.museuid.util.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.List;

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
  private TextField tfUser;
  private boolean isLoginPressed = false;

  private enum SecureMode {
    CREATE_SECURE_QUESTION, FORGOT_PASS
  }
  private int numberOfForgotPassClick = 0;
  private SecureMode currentSecureMode;

  @FXML
  void login(ActionEvent event) {
    if (FieldViewUtils.noEmpty(tfUser, pfPass)) {
      return;
    }
    if (isLoginPressed) {
      return;
    }
    isLoginPressed = true;
    String login = tfUser.getText();
    String password = pfPass.getText();
    if (ConstantConfig.FAKE) {
      new App().start(new Stage());
      Login.getmStage().close();
      return;
    }
    showProgressDialog();
    ServiceBuilder.getApiService().login(new LoginRequest(login, password)).
      enqueue(new BaseCallback<SessionUserInfo>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          hideProgressDialog();
          Messenger.erro(errorMessage);
          isLoginPressed = false;
        }

        @Override
        public void onSuccess(SessionUserInfo data) {
          StaticVarUtils.setSessionUserInfo(data);
          if (UserDTO.UserRole.valueOf(data.getInfo().getRole()) == UserDTO.UserRole.ADMIN
            && !data.getHaveSecureQuestion()) {
            getSecureQuestionList(SecureMode.CREATE_SECURE_QUESTION);
          } else {
            startMain();
          }
          isLoginPressed = false;
        }
      });
  }

  private void startMain() {
    new App().start(new Stage());
    Login.getmStage().close();
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
    numberOfForgotPassClick++;
    if (numberOfForgotPassClick< 2){
      DialogUtils.ResponseMessage responseMessage = DialogUtils.mensageConfirmer(BundleUtils.getResourceBundle().getString("txt_notice"),
        "Tính năng này chỉ dành cho chủ quán? Bạn có muốn sử dụng?");
      if (responseMessage == DialogUtils.ResponseMessage.YES) {
        if (FieldViewUtils.noEmpty(tfUser)){
          Messenger.info("Vui lòng nhập tên đăng nhập!");
        } else{
          getSecureQuestionList(SecureMode.FORGOT_PASS);
        }
      }
    } else {
      if (FieldViewUtils.noEmpty(tfUser)){
        Messenger.info("Vui lòng nhập tên đăng nhập!");
      } else{
        getSecureQuestionList(SecureMode.FORGOT_PASS);
      }
    }

  }

  private void getSecureQuestionList(SecureMode secureMode) {
    currentSecureMode = secureMode;
    showProgressDialog();
    ServiceBuilder.getApiService().getListSecureQuestion().enqueue(new BaseCallback<SecureQuestionDTO>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(SecureQuestionDTO data) {
        hideProgressDialog();
        showSecureQuestionPane(data.getQuestions());
      }
    });
  }
//  private void showSecureQuestionDialog(List<String> question) {
//    VBox vBox = new VBox();
//    Label lbQuestion = new Label("Chọn câu hỏi");
//    ComboBox<String> cbQuestions = new ComboBox<>();
//    ComboUtils.popular(cbQuestions, question);
//    Label lbAnswer = new Label("Chọn câu trả lời");
//    TextField tfAnswer = new TextField();
//
//    vBox.getChildren().addAll(lbQuestion, cbQuestions, lbAnswer, cbQuestions);
//    VBox.setMargin(lbQuestion, new Insets(0, 0, 15, 0));
//    VBox.setMargin(cbQuestions, new Insets(0, 0, 15, 0));
//    VBox.setMargin(lbAnswer, new Insets(0, 0, 15, 0));
//    VBox.setMargin(tfAnswer, new Insets(0, 0, 0, 0));
//
//    DialogUtils.ResponseMessage responseMessage = DialogUtils.customMessageConfirm(vBox, "Thông báo",
//      "Hãy tạo câu hỏi bảo mật trước khi sử dụng phần mềm.", "OK", "Hủy");
//    FieldViewUtils.noEmpty(tfAnswer);
//    if (responseMessage == DialogUtils.ResponseMessage.OK) {
//      SecureQuestionRequest secureQuestionRequest = new SecureQuestionRequest(
//        StaticVarUtils.getSessionUserInfo().getInfo().getUsername(),
//        cbQuestions.getValue(), tfAnswer.getText());
//      ServiceBuilder.getApiService().updateSecureQuestion(secureQuestionRequest).enqueue(new BaseCallback<Object>() {
//        @Override
//        public void onError(String errorCode, String errorMessage) {
//
//        }
//
//        @Override
//        public void onSuccess(Object data) {
//
//        }
//      });
//    } else {
//      Login.getmStage().close();
//    }
//  }

  private void showLoginPane() {
    apLogin.setVisible(true);
    apSecureQuestion.setVisible(false);
    FieldViewUtils.resetField(tfUser, pfPass);
  }

  private void showSecureQuestionPane(List<String> question) {
    apLogin.setVisible(false);
    apSecureQuestion.setVisible(true);
    FieldViewUtils.resetField(tfAnswer);
    if (currentSecureMode == SecureMode.CREATE_SECURE_QUESTION) {
      lbSecureQuestion.setText("Hãy tạo câu hỏi bảo mật trước khi sử dụng phần mềm.");
    } else {
      lbSecureQuestion.setText("Hãy chọn thông tin bảo mật cho người dùng: "+ tfUser.getText().trim());
    }
    ComboUtils.popular(cbQuestions, question);
  }

  public void showProgressDialog() {
    progressIndicator.setVisible(true);
  }

  public void hideProgressDialog() {
    progressIndicator.setVisible(false);
  }

  public void cancel(ActionEvent actionEvent) {
    Platform.exit();
  }

  public void ok(ActionEvent actionEvent) {
    if (FieldViewUtils.noEmpty(tfAnswer)) {
      return;
    }
    showProgressDialog();
    if (currentSecureMode == SecureMode.CREATE_SECURE_QUESTION) {
      SecureQuestionRequest secureQuestionRequest = new SecureQuestionRequest(
        StaticVarUtils.getSessionUserInfo().getInfo().getUsername(),
        cbQuestions.getValue(), tfAnswer.getText());
      ServiceBuilder.getApiService().updateSecureQuestion(secureQuestionRequest).enqueue(new BaseCallback<Object>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          hideProgressDialog();
          Messenger.erro(errorMessage);
        }

        @Override
        public void onSuccess(Object data) {
          hideProgressDialog();
          Messenger.info("Tạo câu hỏi bảo mật thành công");
          startMain();
        }
      });
    } else {
      SecureQuestionRequest secureQuestionRequest = new SecureQuestionRequest(
        tfUser.getText().trim(),
        cbQuestions.getValue(), tfAnswer.getText());
      ServiceBuilder.getApiService().forgotPass(secureQuestionRequest).enqueue(new BaseCallback<UserDTO>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          hideProgressDialog();
          Messenger.erro(errorMessage);
          showLoginPane();
        }

        @Override
        public void onSuccess(UserDTO data) {
          hideProgressDialog();
          Messenger.info("Mật khẩu đã được reset về " + data.getPassword());
          showLoginPane();
        }
      });
    }

  }
}
