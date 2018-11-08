package br.com.museuid.view.initialize;

import br.com.museuid.app.App;
import br.com.museuid.app.Initialize;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.NetworkUtils;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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

         macAddress = NetworkUtils.getAddress("mac");
         //TODO: call api check macAddress existed in server or not, if not, show register form
         if (ConstantConfig.FAKE){
             PauseTransition pause = new PauseTransition(Duration.seconds(5));
             pause.setOnFinished(new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {
                     vBoxRegisterDevice.setVisible(true);
                     vboxProgress.setVisible(false);
                 }});

             pause.play();

         }
    }
    @FXML
    private void signIn(ActionEvent event){
        if (FieldViewUtils.noEmpty(tfDeviceLocation, tfDeviceName)){
            return;
        }
        //TODO: call api register device, then go to main
        if (ConstantConfig.FAKE){
            new App().start(new Stage());
            Initialize.palco.close();
        }
    }
}
