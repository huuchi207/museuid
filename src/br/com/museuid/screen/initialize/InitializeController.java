package br.com.museuid.screen.initialize;

import java.util.Optional;

import br.com.museuid.app.App;
import br.com.museuid.app.Initialize;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.DeviceInfo;
import br.com.museuid.dto.SessionDeviceInfo;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.AddDeviceRequest;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.StaticVarUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class InitializeController {
    private static InitializeController instance;
    public VBox vboxProgress;
    public ProgressIndicator progressIndicator;
    public VBox vBoxRegisterDevice;
    public Label lbLegend;
    public TextField tfDeviceName;
    public TextField tfDeviceLocation;
    public Button btRegister;
    public TextField tfUsername;
    public PasswordField pfPass;

    public static InitializeController getInstance() {
        return instance;
    }

    private String macAddress;

    @FXML
    void initialize() {
        instance = this;

        macAddress = StaticVarUtils.getMacAddress();
        callAutoClientAPI();
        if (ConstantConfig.FAKE) {
//            PauseTransition pause = new PauseTransition(Duration.seconds(5));
//            pause.setOnFinished(new EventHandler<ActionEvent>() {
//                @Override
//                public void handle(ActionEvent event) {
//                   openFormRegisterDevice();
//                }
//            });
//            pause.play();
        }
    }

    @FXML
    private void signIn(ActionEvent event) {
        if (FieldViewUtils.noEmpty(tfDeviceLocation, tfDeviceName,tfUsername, pfPass)) {
            return;
        }
        showProgressView();
        if (ConstantConfig.FAKE) {
            startMain();
        }
        String deviceName = tfDeviceName.getText().trim();
        String deviceLocation = tfDeviceLocation.getText().trim();
        String username = tfUsername.getText().trim();
        String pass = pfPass.getText();
        ServiceBuilder.getApiService().addDevice(new
            AddDeviceRequest(macAddress,deviceName, deviceLocation, username, pass)).
            enqueue(new BaseCallback<DeviceInfo>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
                Messenger.erro(errorMessage);
                openFormRegisterDevice();
//                FieldViewUtils.resetField(tfDeviceName, tfDeviceLocation);
            }

            @Override
            public void onSuccess(DeviceInfo data) {
                callAutoClientAPI();
            }
        });
    }
    void openFormRegisterDevice(){
        vBoxRegisterDevice.setVisible(true);
        vboxProgress.setVisible(false);
    }

    void showProgressView(){
        vboxProgress.setVisible(true);
        vBoxRegisterDevice.setVisible(false);
    }
    void startMain(){
        new App().start(new Stage());
        Initialize.palco.close();
    }

    void callAutoClientAPI(){
        ServiceBuilder.getApiService().checkDevice(macAddress).enqueue(new BaseCallback<SessionDeviceInfo>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
                if ("Không thể tìm thấy thông tin.".equalsIgnoreCase(errorMessage)){
                    openFormRegisterDevice();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(BundleUtils.getResourceBundle().getString("txt_error"));
                    alert.setHeaderText(null);
                    alert.setContentText("Xảy ra lỗi. Vui lòng thử lại sau!");

                    Optional<ButtonType> r = alert.showAndWait();
                    if (r.get() == ButtonType.OK){
                        Initialize.palco.close();
                    }
                }
            }

            @Override
            public void onSuccess(SessionDeviceInfo data) {
                StaticVarUtils.setSessionDeviceInfo(data);
                startMain();
            }
        });
    }
}