package br.com.museuid.view.order_created;

import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.EmployeeDTO;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.sample.SampleCallback;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.FileUtils;
import br.com.museuid.util.Grupo;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.Model;
import br.com.museuid.util.NoticeUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class MyOrderCreated extends AnchorPane {

    public Label lbTitle;
    public AnchorPane apOrderCreatedTable;
    public TableView tbOrderCreated;
    public TableColumn colId;
    public TableColumn colOrderName;
    public TableColumn colOrderDescription;
    public TableColumn colStatus;
    public Button btCancelOrder;
    private ResourceBundle bundle;

    public MyOrderCreated() {
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
}
