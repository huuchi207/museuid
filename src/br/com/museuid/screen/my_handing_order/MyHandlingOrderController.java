package br.com.museuid.screen.my_handing_order;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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

public class MyHandlingOrderController extends AnchorPane {
  public AnchorPane apOrderInQueueTable;
  public TableView<OrderDetail> tbOrderList;
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
  public MyHandlingOrderController() {
    try {
      FXMLLoader fxml = new FXMLLoader(getClass().getResource("my_handling_order.fxml"));

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

    getHandlingOrderList();

  }

  private void getHandlingOrderList() {
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().getOrderByStatus(OrderDetail.OrderStatus.PROGRESSING.name(),
      StaticVarUtils.getSessionUserInfo().getInfo().getId())
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
            tbOrderList.refresh();
          }

        }
      });
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

    tbOrderList.setItems(orderDetailObservableList);
    FieldViewUtils.setEnterKeyEvent(tbOrderList, btHandleOrder);
  }

  @FXML
  private void doneOrder(ActionEvent event) {
    if (selecteOrder == null) {
      Messenger.erro("Có lỗi xảy ra!");
      orderQueue();
      return;
    }
    selecteOrder.setStatus(OrderDetail.OrderStatus.DONE.name());
    selecteOrder.setQueueid(selecteOrder.getId());
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
        removeOrderFromOrderObservableList(selecteOrder);
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
    selecteOrder.setQueueid(selecteOrder.getId());
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
        removeOrderFromOrderObservableList(selecteOrder);
        selecteOrder = null;
        orderQueue();
      }
    });
  }

  @FXML
  private void handleOrder(ActionEvent event) {
    if (tbOrderList.getSelectionModel().getSelectedItem() == null) {
      NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
      return;
    }

    goToOrderDetail(tbOrderList.getSelectionModel().getSelectedItem());
    FieldViewUtils.setGlobalEventHandler(this, btDoneOrder);

  }

  private void orderQueue() {
    NavigationUtils.setVisibility(true, apOrderInQueueTable);
    NavigationUtils.setVisibility(false, apOrderInfo);

    FieldViewUtils.setGlobalEventHandler(this, btHandleOrder);

  }

  private void goToOrderDetail(OrderDetail selected) {
    selecteOrder = selected;
    NavigationUtils.setVisibility(false, apOrderInQueueTable);
    NavigationUtils.setVisibility(true, apOrderInfo);
    lbTotalPrice.setText("Tổng giá trị: " + selected.getSumup() + " VND");
    List<PutQueueRequest.ItemWithImage> list = new ArrayList<>();
    for (PutQueueRequest.Item item: selected.getItems()){
      list.add(item.convertToItemWithImage());
    }
    observableListProductInOrder = FXCollections.observableList(list);
    tbOrderInfo.setItems(observableListProductInOrder);
  }

  private void removeOrderFromOrderObservableList(OrderDetail orderDetail) {
    for (int i = 0; i < orderDetailObservableList.size(); i++) {
      if (orderDetail.getId().equals(orderDetailObservableList.get(i).getId())) {
        orderDetailObservableList.remove(i);
        break;
      }
    }
    tbOrderList.refresh();
  }
}
