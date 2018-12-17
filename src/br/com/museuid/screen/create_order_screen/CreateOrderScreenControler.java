package br.com.museuid.screen.create_order_screen;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.customview.AutocompletionlTextField;
import br.com.museuid.customview.customgridview.ItemGridCellFactory;
import br.com.museuid.dto.DeviceInfo;
import br.com.museuid.dto.Product;
import br.com.museuid.dto.ProductWithImage;
import br.com.museuid.model.data.OrderDetail;
import br.com.museuid.model.data.ProductInOrder;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.PutQueueRequest;
import br.com.museuid.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.GridView;

import java.util.*;

public class CreateOrderScreenControler extends AnchorPane {
  //header
  public Label lbTitle;
//  public TextField txtSearch;
  //product list
  public AnchorPane apProductList;
//  public TableView<ProductWithImage> tbProduct;
  //    public TableColumn colId;
//  public TableColumn colProductName;
//  public TableColumn colDescription;
//  public TableColumn colPrice;
//  public TableColumn colImage;

  public GridView<ProductWithImage> gridProduct;
  public ComboBox<String> cbFilter;
  //order
  public AnchorPane apEditOrderList;
  public TableView<ProductInOrder> tbProductInOrder;
  //    public TableColumn colIdInOrder;
  public TableColumn colProductNameInOrder;
  public TableColumn colDescriptionInOrder;
  public TableColumn colPriceInOrder;
  public TableColumn colImageInOrder;
  public TableColumn<ProductInOrder, String> colCountInOrder;
  public TableColumn colStatus;
  public TableColumn<ProductInOrder, String> colMoreRequirement;
  public GridPane gridEditOrderList;
  public AutocompletionlTextField<DeviceInfo> txtChooseDevice;
  //bottom button
  public Button btEditOrder;
  public Button btCreateOrder;
  public Button btBackToList;
  public Label lbLegend;

  private ResourceBundle bundle;

  private List<ProductWithImage> productList;
  private List<ProductInOrder> productInOrders;

  private ObservableList<ProductWithImage> productObservableList;
  private ObservableList<ProductInOrder> productInOrderObservableList;
  private int totalPrice;
  private ObservableList<DeviceInfo> deviceInfoObservableList = FXCollections.observableList(new ArrayList<>());

  public CreateOrderScreenControler() {
    try {
      FXMLLoader fxml = new FXMLLoader(getClass().getResource("create_order_screen.fxml"));

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

    cbFilter.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue!= null){
          filter(newValue, FXCollections.observableArrayList(productList));
        }
      }
    });
    goToProductList(null);


  }

  private void initTable() {
//    tbProduct.getSelectionModel().setSelectionMode(
//      SelectionMode.MULTIPLE
//    );
////        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
//    colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
//    colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
//    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
//    colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
//    colImage.setCellValueFactory(new PropertyValueFactory<>("productImage"));


//        colIdInOrder.setCellValueFactory(new PropertyValueFactory<>("id"));
    colProductNameInOrder.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colDescriptionInOrder.setCellValueFactory(new PropertyValueFactory<>("description"));
    colPriceInOrder.setCellValueFactory(new PropertyValueFactory<>("price"));
    colCountInOrder.setCellValueFactory(new PropertyValueFactory<>("tfNumber"));
    colMoreRequirement.setCellValueFactory(new PropertyValueFactory<>("tfNote"));
    colImageInOrder.setCellValueFactory(new PropertyValueFactory<>("productImage"));


    ItemGridCellFactory itemGridCellFactory = new ItemGridCellFactory();
    itemGridCellFactory.setOnTouch(new ItemGridCellFactory.OnTouch() {
      @Override
      public void onClick(ProductWithImage item) {
//        Messenger.info(item.getProductName());
      }
    });
    gridProduct.setCellFactory(itemGridCellFactory);
  }

  /**
   * Mapear dados objetos para inserção dos dados na updateTable
   */
  private void updateProductTable() {
    productObservableList = FXCollections.observableArrayList(productList);
    gridProduct.setItems(productObservableList);

    Set<String> productType = new HashSet<String>();
    for (ProductWithImage item: productList){
      productType.add(item.getType());
    }
    productType.add("Tất cả");

    ComboUtils.data(cbFilter, new ArrayList<String>(productType), "Tất cả");
  }

  private void updateProductInOrderTable() {
    productInOrderObservableList = FXCollections.observableArrayList(productInOrders);

    tbProductInOrder.setItems(productInOrderObservableList);
  }

  /**
   * FieldViewUtils de pesquisar para filtrar dados na updateTable
   */
  private void filter(String valor, ObservableList<ProductWithImage> products) {
    FilteredList<ProductWithImage> filteredList = new FilteredList<>(products, Product -> true);
    filteredList.setPredicate(product -> {
      if ("Tất cả".equals(valor)){
        return true;
      } else if (valor == null || valor.isEmpty()) {
        return true;
      } else if (product.getType().toLowerCase().contains(valor.toLowerCase())) {
        return true;
      }

      return false;
    });

    SortedList<ProductWithImage> dadosOrdenados = new SortedList<>(filteredList);

    gridProduct.setItems(dadosOrdenados);
  }

  private void getProductList() {
    if (ConstantConfig.FAKE) {
      updateProductList(FakeDataUtils.getFakeProductList());
      updateProductTable();
    } else {
      AppController.getInstance().showProgressDialog();
      ServiceBuilder.getApiService().getProductList().enqueue(new BaseCallback<List<Product>>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          AppController.getInstance().hideProgressDialog();
          Messenger.erro(errorMessage);
        }

        @Override
        public void onSuccess(List<Product> data) {
          AppController.getInstance().hideProgressDialog();
          updateProductList(data);
          updateProductTable();
        }
      });
    }
  }

  @FXML
  void editOrder(ActionEvent event) {
    productInOrders = createProductInOrderListFromSelectedProductList();
    if (productInOrders.isEmpty()) {
      Messenger.info(bundle.getString("txt_please_choose_product"));
      return;
    } else {
      for (ProductInOrder productInOrder : productInOrders) {
        if (productInOrder.getInStock() == null || productInOrder.getInStock() == 0) {
          Messenger.erro("Sản phẩm " + productInOrder.getProductName() + " hiện đang hết hàng. Vui lòng chọn sản phẩm khác!");
          return;
        }
      }
    }
    updateProductInOrderTable();
    updateProductInOrderScreenData();
    getDeviceList();
    FieldViewUtils.resetField(txtChooseDevice);
    lbTitle.setText(bundle.getString("txt_edit_order"));
    NavigationUtils.setVisibility(false, btEditOrder, apProductList);
    NavigationUtils.setVisibility(true, btBackToList, btCreateOrder, gridEditOrderList);

  }

  @FXML
  void createOrder(ActionEvent event) {
    if (totalPrice <= 0) {
      Messenger.info(bundle.getString("txt_please_choose_number_of_product_correctly"));
      return;
    }
    if (txtChooseDevice.getSelectedItem() == null) {
      Messenger.info("Vui lòng chọn máy!");
      return;
    }
    if (ConstantConfig.FAKE) {
      Messenger.info(bundle.getString("msg_create_order_successfully") + "\"" + bundle.getString("txt_order_created") + "\"");
    } else {
      OrderDetail queueRequest = createPutOrderRequest();
      if (queueRequest == null) {
        return;
      }
      DialogUtils.ResponseMessage responseMessage =
        DialogUtils.mensageConfirmer("Xác nhận", "Bạn có muốn xử lý đơn hàng luôn?", "Có", "Không, đưa vào hàng đợi");
      AppController.getInstance().showProgressDialog();
      if (responseMessage == DialogUtils.ResponseMessage.YES) {
        queueRequest.setHandlerid(StaticVarUtils.getSessionUserInfo().getInfo().getId());
        queueRequest.setHandlername(StaticVarUtils.getSessionUserInfo().getInfo().getUsername());
        queueRequest.setStatus(OrderDetail.OrderStatus.DONE.name());
        AppController.getInstance().showProgressDialog();
        putOrder(queueRequest);
      } else {
        AppController.getInstance().showProgressDialog();
        putOrder(queueRequest);
      }


    }
  }

  private void putOrder(OrderDetail orderDetail) {
    ServiceBuilder.getApiService().putOrder(orderDetail).enqueue(new BaseCallback<Object>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        AppController.getInstance().hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(Object data) {
        AppController.getInstance().hideProgressDialog();
        Messenger.info(bundle.getString("txt_operation_successful"));
        goToProductList(null);
      }
    });
  }

  @FXML
  void goToProductList(ActionEvent event) {
    getProductList();
    lbTitle.setText(bundle.getString("txt_product_list"));
//    lbLegend.setText(bundle.getString("txt_hold_ctrl_to_choose_items"));
    lbLegend.setText("");
//    tbProduct.getSelectionModel().clearSelection();

    NavigationUtils.setVisibility(true, btEditOrder, apProductList);
    NavigationUtils.setVisibility(false, btBackToList, btCreateOrder, gridEditOrderList);

  }

  private List<ProductInOrder> createProductInOrderListFromSelectedProductList() {
    if (productList == null || productList.isEmpty()) {
      return new ArrayList<>();
    }

    ObservableList<ProductWithImage> selectedItems = FXCollections.observableArrayList(getSelectedProducts());

//    ListIterator<ProductWithImage> iterator = selectedItems.listIterator();
    List<ProductInOrder> selectedProducts = new ArrayList<>();
//    while (iterator.hasNext()) {
//      Product item = iterator.next();
      for (ProductWithImage product : selectedItems) {
//        if (product.getId().equals(item.getId())) {
          ProductInOrder productInOrder = product.convertToProductInOrder();
          productInOrder.setOnContentChange(
            new ProductInOrder.OnContentChange() {
              @Override
              public void onNumberChange(Integer oldNumber, Integer newNumber) {
                if (newNumber >= 0) {
                  totalPrice += (newNumber - productInOrder.getCount()) * productInOrder.getPrice();
                  productInOrder.setCount(newNumber);
                  lbLegend.setText(bundle.getString("txt_total_price") + ": " + totalPrice + " " + bundle.getString("txt_vnd"));
                } else {
//                        Messenger.erro("Số lượng hàng phải lớn hơn 0");
                  productInOrder.getTfNumber().setText(productInOrder.getCount() + "");
                }
              }
            }
          );
          selectedProducts.add(productInOrder);
//        }
//      }
    }
    return selectedProducts;
  }

  private void updateProductInOrderScreenData() {
    //reset total for counting
    totalPrice = 0;
    ObservableList<ProductInOrder> orderObservableList = tbProductInOrder.getItems();

    ListIterator<ProductInOrder> iterator = orderObservableList.listIterator();
    while (iterator.hasNext()) {
      ProductInOrder item = iterator.next();
      totalPrice += item.getPrice() * item.getCount();
    }
    //update list
//        iterator.forEachRemaining(productInOrders::add);
    //update text
    lbLegend.setText(bundle.getString("txt_total_price") + ": " + totalPrice + " " + bundle.getString("txt_vnd"));
  }

  private void updateProductList(List<Product> products) {
    List<ProductWithImage> list = new ArrayList<>();
    for (Product product : products) {
      ProductWithImage productWithImage = product.convertToProductWithImage();
      productWithImage.updateStatus();
      list.add(productWithImage);
    }
    productList = list;
  }

  private OrderDetail createPutOrderRequest() {
    ObservableList<ProductInOrder> orderObservableList = tbProductInOrder.getItems();
    int totalPrice = 0;
    List<PutQueueRequest.Item> items = new ArrayList<>();
    ListIterator<ProductInOrder> iterator = orderObservableList.listIterator();
    while (iterator.hasNext()) {
      ProductInOrder productInOrder = iterator.next();
      if (productInOrder.getCount() <= 0) {
        Messenger.erro("Số lượng hàng phải lớn hơn 0!");
        return null;
      }
      int priceOfProduct = productInOrder.getPrice() * productInOrder.getCount();
      items.add(new PutQueueRequest.Item(productInOrder.getProductName(),
        productInOrder.getId(),
        productInOrder.getCount(),
        productInOrder.getPrice(),
        priceOfProduct,
        productInOrder.getTfNote().getText().trim(),
        priceOfProduct, productInOrder.getImageid()));
      totalPrice += priceOfProduct;
    }
    DeviceInfo deviceInfo = txtChooseDevice.getSelectedItem();
    OrderDetail putQueueRequest = new OrderDetail(totalPrice, items,
      deviceInfo.getName(),
      deviceInfo.getId(),
      StaticVarUtils.getSessionUserInfo().getSessionid(), null);
    putQueueRequest.setLocation(deviceInfo.getLocation());
    return putQueueRequest;
  }

  private void getDeviceList() {
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().getListDevice().enqueue(new BaseCallback<List<DeviceInfo>>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        AppController.getInstance().hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(List<DeviceInfo> data) {
        AppController.getInstance().hideProgressDialog();
        for (DeviceInfo deviceInfo : data) {
          deviceInfo.buildDisplayName();
        }
//                deviceInfoObservableList.clear();
//                deviceInfoObservableList.addAll(data);
//
        txtChooseDevice.getEntries().clear();
        txtChooseDevice.getEntries().addAll(data);
      }
    });
  }
  private void clearSelectionOfGridProduct(){
    if (gridProduct.getItems()!= null){
      for (ProductWithImage item : gridProduct.getItems()){
        item.setSelected(false);
      }
    }
  }
  private List<ProductWithImage> getSelectedProducts(){
    List<ProductWithImage> items = new ArrayList<>();
    if (gridProduct.getItems()!= null){
      for (ProductWithImage item : gridProduct.getItems()){
        if (item.isSelected()){
          items.add(item);
        }
      }
    }
    return items;
  }
}
