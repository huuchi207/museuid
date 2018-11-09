package br.com.museuid.screen.order_in_queue;

import org.controlsfx.control.GridView;

import java.util.ResourceBundle;

import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Messenger;
import customview.sectiongridview.ItemGridCellFactory;
import customview.sectiongridview.ItemGridView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class OrderInQueueController extends AnchorPane {
    //    @FXML
//    private AnchorPane apGridDevice;
    @FXML
    private AnchorPane apOrderInfo;
    @FXML
    private TableView tbOrderInfo;
    @FXML
    private TableColumn colIdInOrder;
    @FXML
    private TableColumn colProductNameInOrder;
    @FXML
    private TableColumn colDescriptionInOrder;
    @FXML
    private TableColumn colPriceInOrder;
    @FXML
    private TableColumn colCountInOrder;
    @FXML
    private TableColumn colMoreRequirement;
    @FXML
    private Button btDoneOrder, btCancelOrder;
    @FXML
    private GridView gridViewDevice;
    private ResourceBundle bundle;


    public OrderInQueueController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("order_in_queue.fxml"));

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
        initDeviceView();
    }

    private void initDeviceView() {
        final ObservableList<ItemGridView> list = FXCollections.observableArrayList();
        ItemGridCellFactory itemGridCellFactory = new ItemGridCellFactory();
        itemGridCellFactory.setOnTouch(new ItemGridCellFactory.OnTouch() {
            @Override
            public void onClick(ItemGridView item) {
                Messenger.info(item.getDeviceName());
            }
        });
        gridViewDevice.setCellFactory(itemGridCellFactory);
        gridViewDevice.cellWidthProperty().set(100);
        gridViewDevice.cellHeightProperty().set(100);
        for (int i = 0; i < 50; i++) {
            ItemGridView itemGridView = new ItemGridView();
            itemGridView.setDeviceName("Máy " + (i + 1));

//            itemGridView.setNewOrder();
            list.add(itemGridView);
        }
        gridViewDevice.setItems(list);
    }

    void initTable() {
//        colIdInOrder.setCellValueFactory(new PropertyValueFactory<>("id"));
//        colProductNameInOrder.setCellValueFactory(new PropertyValueFactory<>("productName"));
//        colDescriptionInOrder.setCellValueFactory(new PropertyValueFactory<>("description"));
//        colPriceInOrder.setCellValueFactory(new PropertyValueFactory<>("price"));
//        colCountInOrder.setCellValueFactory(new PropertyValueFactory<>("countString"));
//        colMoreRequirement.setCellValueFactory(new PropertyValueFactory<>("moreRequirement"));
    }

    @FXML
    private void doneOrder(ActionEvent event) {
        //TODO: update state of order
    }

    @FXML
    private void cancelOrder(ActionEvent event) {
        //TODO: update state of order
    }
}
