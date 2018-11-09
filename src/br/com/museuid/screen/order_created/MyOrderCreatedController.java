package br.com.museuid.screen.order_created;

import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Messenger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class MyOrderCreatedController extends AnchorPane {

    public Label lbTitle;
    public AnchorPane apOrderCreatedTable;
    public TableView tbOrderCreated;
    public TableColumn colId;
    public TableColumn colOrderName;
    public TableColumn colOrderDescription;
    public TableColumn colStatus;
    public Button btCancelOrder;
    private ResourceBundle bundle;

    public MyOrderCreatedController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("my_order_created.fxml"));

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
        initTable();
    }

    void initTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrderDescription.setCellValueFactory(new PropertyValueFactory<>("orderDescription"));
        colOrderName.setCellValueFactory(new PropertyValueFactory<>("orderName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatusName"));
    }
    //TODO: call
    void updateOrderCreatedTableData(){}

    @FXML
    private void cancelOrder(ActionEvent event) {
        if (ConstantConfig.FAKE){
            Messenger.info(BundleUtils.getResourceBundle().getString("txt_operation_successful"));
            return;
        }
    }
}
