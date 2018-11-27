package br.com.museuid.screen.order_created;


import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.customview.MutipleLineTableCell;
import br.com.museuid.model.data.OrderDetail;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Messenger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    public TableView<OrderDetail> tbOrderCreated;
    public TableColumn colId;
    public TableColumn colOrderName;
    public TableColumn colOrderDescription;
    public TableColumn colStatus;
    public Button btCancelOrder;
    private ResourceBundle bundle;
    private List<OrderDetail> listOrder = new ArrayList<>();
    private ObservableList<OrderDetail> observableList = FXCollections.observableList(listOrder);
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
//        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrderDescription.setCellValueFactory( new PropertyValueFactory<>("orderDescription"));
        colOrderDescription.setCellFactory(tv -> new MutipleLineTableCell());
        colOrderName.setCellValueFactory(new PropertyValueFactory<>("orderName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusText"));
        tbOrderCreated.setItems(observableList);
    }

    @FXML
    public void cancelOrder(ActionEvent action){
        OrderDetail selected = tbOrderCreated.getSelectionModel().getSelectedItem();
        if (selected == null){
            Messenger.info(bundle.getString("txt_please_choose_target"));
            return;
        }
        if (selected.getStatus().equalsIgnoreCase(OrderDetail.OrderStatus.NEW.name())){
            Messenger.info(bundle.getString("txt_operation_successful"));
        }else {
            Messenger.info(bundle.getString("txt_please_choose_target"));
        }
    }
}
