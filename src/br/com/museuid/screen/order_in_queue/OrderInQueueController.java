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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class OrderInQueueController extends AnchorPane {
        @FXML
    private AnchorPane apOrderInQueue;
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
        createListDeviceView();
    }

    private void createListDeviceView() {
        ItemGridCellFactory itemGridCellFactory = new ItemGridCellFactory();
        itemGridCellFactory.setOnTouch(new ItemGridCellFactory.OnTouch() {
            @Override
            public void onClick(ItemGridView item) {
                Messenger.info(item.getDeviceName());
            }
        });
        VBox vBox = new VBox();
        AnchorPane.setLeftAnchor(vBox, 0.0);
        AnchorPane.setRightAnchor(vBox, 0.0);
        AnchorPane.setTopAnchor(vBox, 0.0);
        AnchorPane.setBottomAnchor(vBox, 0.0);
        for (int j = 0; j < 3; j++){
            GridView gridView = new GridView();
            final ObservableList<ItemGridView> list = FXCollections.observableArrayList();
            gridView.setCellFactory(itemGridCellFactory);
            gridView.cellWidthProperty().set(100);
            gridView.cellHeightProperty().set(100);
            for (int i = 0; i < 50; i++) {
                ItemGridView itemGridView = new ItemGridView();
                itemGridView.setDeviceName("Máy " + (i + 1));

//            itemGridView.setNewOrder();
                list.add(itemGridView);
            }
            gridView.setItems(list);
            vBox.getChildren().add(new Label("Section "+ j));
            vBox.getChildren().add(gridView);
        }
        apOrderInQueue.getChildren().add(vBox);
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
