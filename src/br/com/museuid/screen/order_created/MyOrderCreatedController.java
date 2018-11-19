package br.com.museuid.screen.order_created;


import br.com.museuid.customview.MutipleLineTableCell;
import br.com.museuid.model.data.OrderDetail;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.StaticVarUtils;
import com.google.gson.Gson;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import javafx.application.Platform;
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
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
       getListOrder();

    }

    void initTable() {
//        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colOrderDescription.setCellValueFactory(new PropertyValueFactory<>("orderDescription"));
        colOrderDescription.setCellFactory(tv -> new MutipleLineTableCell());
        colOrderName.setCellValueFactory(new PropertyValueFactory<>("orderName"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("statusText"));
        tbOrderCreated.setItems(observableList);
    }

    @FXML
    public void cancelOrder(ActionEvent action) {
        OrderDetail selected = tbOrderCreated.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Messenger.info(bundle.getString("txt_please_choose_target"));
            return;
        }

        if (selected.getStatus().equalsIgnoreCase(OrderDetail.OrderStatus.NEW.name())) {
            //TODO: call api cancel order
            DialogUtils.ResponseMessage responseMessage = Messenger.confirm("Bạn có chắc chắn muốn hủy đơn hàng này?");

            if (responseMessage == DialogUtils.ResponseMessage.YES) {
                selected.setStatus(OrderDetail.OrderStatus.CANCELED.name());
                selected.setQueueid(selected.getId());
                AppController.getInstance().showProgressDialog();
                ServiceBuilder.getApiService().updateOrder(selected).enqueue(new BaseCallback<Object>() {
                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        AppController.getInstance().hideProgressDialog();
                        Messenger.erro(errorMessage);
                    }

                    @Override
                    public void onSuccess(Object data) {
                        AppController.getInstance().hideProgressDialog();
                        Messenger.info(bundle.getString("txt_operation_successful"));
                    }
                });
            }
        } else {
            Messenger.erro(bundle.getString("txt_can_only_cancel_unhandle_order"));
        }
    }

    private void getListOrder(){
        AppController.getInstance().showProgressDialog();
        ServiceBuilder.getApiService().getMyOrderList().enqueue(new BaseCallback<List<OrderDetail>>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
                AppController.getInstance().hideProgressDialog();
                Messenger.erro(errorMessage);
            }

            @Override
            public void onSuccess(List<OrderDetail> data) {
                AppController.getInstance().hideProgressDialog();
                if (data != null){
                    for (OrderDetail orderDetail: data){
                        orderDetail.updateFields();
                    }
                    observableList.addAll(data);
                }

                listenOnChange();
            }
        });
    }
    private void listenOnChange(){
        Gson gson = new Gson();
        try {
            Socket socket = IO.socket(ServiceBuilder.getBASEURL());
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {

                }
            }).on(StaticVarUtils.getSessionDeviceInfo().getSessionid(), new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    for (Object object : objects) {
                        JSONObject jsonObject = (JSONObject) object;
                        OrderDetail order = gson.fromJson(jsonObject.toString(), OrderDetail.class);
                        for (int i = 0; i < observableList.size(); i++){
                            OrderDetail orderDetail = observableList.get(i);
                            if (orderDetail.getId().equals(order.getId())){
                                order.updateFields();
                                observableList.set(i, order);
                                tbOrderCreated.refresh();
                                break;
                            }
                        }
                    }
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
//                            Messenger.info("Disconnected");
                        }
                    });
                }

            });
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
