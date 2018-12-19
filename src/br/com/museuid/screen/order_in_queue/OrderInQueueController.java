package br.com.museuid.screen.order_in_queue;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.Constants;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.customview.FormattedNumberTableCell;
import br.com.museuid.customview.MutipleLineTableCell;
import br.com.museuid.model.data.OrderDetail;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.PutQueueRequest;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
import br.com.museuid.util.NoticeUtils;
import br.com.museuid.util.StaticVarUtils;
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

public class OrderInQueueController extends AnchorPane {
  public AnchorPane apOrderInQueueTable;
  public TableView<OrderDetail> tbOrderInQueue;
  public TableColumn colTime;
  public TableColumn colDevice;
  public TableColumn colLocation;
  public TableColumn colOrderName;
  public TableColumn colOrderDescription;
  public TableColumn colImageInOrder;
  @FXML
  private Label lbTotalPrice;
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
  private List<OrderDetail> listOrder = new ArrayList<>();
  private ObservableList<OrderDetail> orderDetailObservableList = FXCollections.observableList(listOrder);
  private List<PutQueueRequest.ItemWithImage> listProductInOrder = new ArrayList<>();
  private ObservableList<PutQueueRequest.ItemWithImage> observableListProductInOrder = FXCollections.observableList(listProductInOrder);
  private OrderDetail selecteOrder;


  public Button btHandleOrder;
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
//        createListDeviceView();
    orderQueue();

    getNewOrderList();

  }

  private void getNewOrderList() {
    if (ConstantConfig.FAKE)
      return;
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().getOrderByStatus(OrderDetail.OrderStatus.NEW.name(), null)
      .enqueue(new BaseCallback<List<OrderDetail>>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          AppController.getInstance().hideProgressDialog();
          Messenger.erro(errorMessage);
        }

        @Override
        public void onSuccess(List<OrderDetail> data) {
          AppController.getInstance().hideProgressDialog();
          orderDetailObservableList.clear();
          if (data != null) {
            for (OrderDetail orderDetail : data) {
              orderDetail.updateFields();
              orderDetailObservableList.add(orderDetail);
            }
            tbOrderInQueue.refresh();
          }
          //then listen on changes
          listenOnChange();
        }
      });
  }

  private void createListDeviceView() {
//    ItemGridCellFactory itemGridCellFactory = new ItemGridCellFactory();
//    itemGridCellFactory.setOnTouch(new ItemGridCellFactory.OnTouch() {
//      @Override
//      public void onClick(ItemGridView item) {
//        Messenger.info(item.getDeviceName());
//      }
//    });
//    gridView.setCellFactory(itemGridCellFactory);
//    VBox vBox = new VBox();
//    AnchorPane.setLeftAnchor(vBox, 0.0);
//    AnchorPane.setRightAnchor(vBox, 0.0);
//    AnchorPane.setTopAnchor(vBox, 0.0);
//    AnchorPane.setBottomAnchor(vBox, 0.0);
//    for (int j = 0; j < 3; j++) {
//      GridView gridView = new GridView();
//      final ObservableList<ItemGridView> list = FXCollections.observableArrayList();
//      gridView.setCellFactory(itemGridCellFactory);
//      gridView.cellWidthProperty().set(100);
//      gridView.cellHeightProperty().set(100);
//      for (int i = 0; i < 50; i++) {
//        ItemGridView itemGridView = new ItemGridView();
//        itemGridView.setDeviceName("Máy " + (i + 1));
//
////            itemGridView.setNewOrder();
//        list.add(itemGridView);
//      }
//      gridView.setItems(list);
//      vBox.getChildren().add(new Label("Section " + j));
//      vBox.getChildren().add(gridView);
//    }
//    apOrderInQueue.getChildren().add(vBox);
  }

  void initTable() {
    //table detail
//        colIdInOrder.setCellValueFactory(new PropertyValueFactory<>("id"));
    colProductNameInOrder.setCellValueFactory(new PropertyValueFactory<>("itemname"));
//    colDescriptionInOrder.setCellValueFactory(new PropertyValueFactory<>("description"));
    colPriceInOrder.setCellValueFactory(new PropertyValueFactory<>("price"));
    colCountInOrder.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    colMoreRequirement.setCellValueFactory(new PropertyValueFactory<>("note"));
    colImageInOrder.setCellValueFactory(new PropertyValueFactory<>("productImage"));
    colPriceInOrder.setCellFactory(tc -> new FormattedNumberTableCell());
    tbOrderInfo.setItems(observableListProductInOrder);

    //table list order
    colTime.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    colDevice.setCellValueFactory(new PropertyValueFactory<>("customername"));
    colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
    colOrderName.setCellValueFactory(new PropertyValueFactory<>("orderName"));
    colOrderDescription.setCellValueFactory(new PropertyValueFactory<>("orderDescription"));
    colOrderDescription.setCellFactory(tv -> new MutipleLineTableCell());

    tbOrderInQueue.setItems(orderDetailObservableList);
    FieldViewUtils.setEnterKeyEvent(tbOrderInQueue, btHandleOrder);
  }

  @FXML
  private void doneOrder(ActionEvent event) {
    if (selecteOrder == null) {
      Messenger.erro("Có lỗi xảy ra!");
      orderQueue();
      return;
    }
    selecteOrder.setStatus(OrderDetail.OrderStatus.DONE.name());
    selecteOrder.setHandlerid(StaticVarUtils.getSessionUserInfo().getInfo().getId());
    selecteOrder.setHandlername(StaticVarUtils.getSessionUserInfo().getInfo().getUsername());
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().updateQueue(selecteOrder).enqueue(new BaseCallback<Object>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        AppController.getInstance().hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(Object data) {
        AppController.getInstance().hideProgressDialog();
        Messenger.info(bundle.getString("txt_operation_successful"));
        selecteOrder = null;
        orderQueue();
      }
    });
  }

  @FXML
  private void cancelOrder(ActionEvent event) {
    if (selecteOrder == null) {
      Messenger.erro("Có lỗi xảy ra!");
      orderQueue();
      return;
    }
    DialogUtils.ResponseMessage responseMessage = DialogUtils.mensageConfirmer("Thông báo", "Bạn có chắc chắn muốn hủy đơn hàng này?");
    if (responseMessage != DialogUtils.ResponseMessage.YES) {
      return;
    }
    String r = DialogUtils.textDialog("Thông báo", "Nhập lý do hủy đơn hàng (nếu có)");
    selecteOrder.setComment(r);
    selecteOrder.setStatus(OrderDetail.OrderStatus.CANCELED.name());
    selecteOrder.setHandlerid(StaticVarUtils.getSessionUserInfo().getInfo().getId());
    selecteOrder.setHandlername(StaticVarUtils.getSessionUserInfo().getInfo().getUsername());
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().updateQueue(selecteOrder).enqueue(new BaseCallback<Object>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        AppController.getInstance().hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(Object data) {
        AppController.getInstance().hideProgressDialog();
        Messenger.info(bundle.getString("txt_operation_successful"));
        selecteOrder = null;
        orderQueue();
      }
    });
  }

  @FXML
  private void handleOrder(ActionEvent event) {
    if (tbOrderInQueue.getSelectionModel().getSelectedItem() == null) {
      NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
      return;
    }
    AppController.getInstance().showProgressDialog();
    OrderDetail selected = tbOrderInQueue.getSelectionModel().getSelectedItem();
    selected.setStatus(OrderDetail.OrderStatus.PROGRESSING.name());
    selected.setQueueid(selected.getId());
    selected.setHandlerid(StaticVarUtils.getSessionUserInfo().getInfo().getId());
    selected.setHandlername(StaticVarUtils.getSessionUserInfo().getInfo().getUsername());
    ServiceBuilder.getApiService().updateQueue(selected).enqueue(new BaseCallback<Object>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        AppController.getInstance().hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(Object data) {
        AppController.getInstance().hideProgressDialog();
        goToOrderDetail(selected);
      }
    });
  }

  private void orderQueue() {
    NavigationUtils.setVisibility(true, apOrderInQueueTable);
    NavigationUtils.setVisibility(false, apOrderInfo);
  }

  private void goToOrderDetail(OrderDetail selected) {
    selecteOrder = selected;
    NavigationUtils.setVisibility(false, apOrderInQueueTable);
    NavigationUtils.setVisibility(true, apOrderInfo);
    lbTotalPrice.setText("Tổng giá trị: " + selected.getSumup());
//    for (Prod)
    List<PutQueueRequest.ItemWithImage> list = new ArrayList<>();
    for (PutQueueRequest.Item item: selected.getItems()){
      list.add(item.convertToItemWithImage());
    }
    observableListProductInOrder = FXCollections.observableList(list);
    tbOrderInfo.setItems(observableListProductInOrder);
    FieldViewUtils.setGlobalEventHandler(this, btDoneOrder);

  }

  private void listenOnChange() {
    Gson gson = new Gson();
    try {
      Socket socket = IO.socket(ServiceBuilder.getBASEURL());
      socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
          socket.emit("room", "employee");
        }
      }).on(Constants.NEW_ORDER_EVENT, new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
          for (Object object : objects) {
            JSONObject jsonObject = (JSONObject) object;
            OrderDetail order = gson.fromJson(jsonObject.toString(), OrderDetail.class);
            order.updateFields();

            orderDetailObservableList.add(order);
          }
          tbOrderInQueue.refresh();
        }
      }).on(Constants.CHANGE_ORDER_EVENT, new Emitter.Listener() {
        @Override
        public void call(Object... objects) {
          for (Object object : objects) {
            JSONObject jsonObject = (JSONObject) object;
            OrderDetail order = gson.fromJson(jsonObject.toString(), OrderDetail.class);
            for (int i = 0; i < orderDetailObservableList.size(); i++) {
              OrderDetail orderDetail = orderDetailObservableList.get(i);
              if (orderDetail.getId().equals(order.getId())) {
                orderDetailObservableList.remove(i);
                break;
              }
            }
          }
          tbOrderInQueue.refresh();
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
