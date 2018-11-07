package br.com.museuid.view.update_user_info;

import java.util.ResourceBundle;

import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class UpdateUserInfoController extends AnchorPane {

    public TextField txtUserName;
    public TextField txtName;
    public TextArea txtPhoneNumber;
    public TextArea txtAddress;
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

    //TODO: call update
    public void updateInfo(){
        if (FieldViewUtils.noEmpty(txtName) || FieldViewUtils.noEmpty(txtAddress, txtPhoneNumber)){
            return;
        }
    }

    private void getUserInfo(){
        //TODO: call api get info and set to userDTO in userUtil, then update view in navigation
    }
}
