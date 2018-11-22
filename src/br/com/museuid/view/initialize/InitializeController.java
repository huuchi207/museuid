package br.com.museuid.view.initialize;

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
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Optional;

public class InitializeController {
    private static InitializeController instance;
    public VBox vboxProgress;
    public ProgressIndicator progressIndicator;
    public VBox vBoxRegisterDevice;
    public Label lbLegend;
    public TextField tfDeviceName;
    public TextField tfDeviceLocation;
    public Button btRegister;

    public static InitializeController getInstance() {
        return instance;
    }

    private String macAddress;

    @FXML
    void initialize() {
        instance = this;

        macAddress = StaticVarUtils.getMacAddress();
        ServiceBuilder.getApiService().checkDevice(macAddress).enqueue(new BaseCallback<SessionDeviceInfo>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
                if ("Can not find information.".equalsIgnoreCase(errorMessage)){
                    openFormRegisterDevice();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle(BundleUtils.getResourceBundle().getString("txt_error"));
                    alert.setHeaderText(null);
                    alert.setContentText("Xảy ra lỗi. Vui lòng thử lại sau!");

                    Optional<ButtonType> r = alert.showAndWait();
                    if (r.get() == ButtonType.OK){
                        Initialize.stage.close();
                    }
                }
            }

            @Override
            public void onSuccess(SessionDeviceInfo data) {
                startMain();
                StaticVarUtils.setSessionDeviceInfo(data);
            }
        });
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
        if (FieldViewUtils.noEmpty(tfDeviceLocation, tfDeviceName)) {
            return;
        }
        showProgressView();
        if (ConstantConfig.FAKE) {
            startMain();
        }
        String deviceName = tfDeviceName.getText().trim();
        String deviceLocation = tfDeviceLocation.getText().trim();
        ServiceBuilder.getApiService().addDevice(new AddDeviceRequest(macAddress,deviceName, deviceLocation)).
            enqueue(new BaseCallback<DeviceInfo>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
                Messenger.erro(errorMessage);
                FieldViewUtils.resetField(tfDeviceName, tfDeviceLocation);
            }

            @Override
            public void onSuccess(DeviceInfo data) {
                startMain();
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
        Initialize.stage.close();
    }
}
