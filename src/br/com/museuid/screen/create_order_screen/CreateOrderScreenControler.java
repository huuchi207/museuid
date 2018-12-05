package br.com.museuid.screen.create_order_screen;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.Product;
import br.com.museuid.dto.ProductWithImage;
import br.com.museuid.model.data.ProductInOrder;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.PutQueueRequest;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FakeDataUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
import br.com.museuid.util.StaticVarUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class CreateOrderScreenControler extends AnchorPane {
  //header
  public Label lbTitle;
  public TextField txtSearch;
  //product list
  public AnchorPane apProductList;
  public TableView<ProductWithImage> tbProduct;
  //    public TableColumn colId;
  public TableColumn colProductName;
  public TableColumn colDescription;
  public TableColumn colPrice;
  public TableColumn colImage;
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
    goToProductList(null);

    txtSearch.textProperty().addListener((obs, old, novo) -> {
      filter(novo, FXCollections.observableArrayList(productList));
    });
  }

  private void initTable() {
    tbProduct.getSelectionModel().setSelectionMode(
      SelectionMode.MULTIPLE
    );
//        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    colImage.setCellValueFactory(new PropertyValueFactory<>("productImage"));


//        colIdInOrder.setCellValueFactory(new PropertyValueFactory<>("id"));
    colProductNameInOrder.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colDescriptionInOrder.setCellValueFactory(new PropertyValueFactory<>("description"));
    colPriceInOrder.setCellValueFactory(new PropertyValueFactory<>("price"));
    colCountInOrder.setCellValueFactory(new PropertyValueFactory<>("tfNumber"));
    colMoreRequirement.setCellValueFactory(new PropertyValueFactory<>("tfNote"));
    colImageInOrder.setCellValueFactory(new PropertyValueFactory<>("productImage"));

  }

  /**
   * Mapear dados objetos para inserção dos dados na updateTable
   */
  private void updateProductTable() {
    productObservableList = FXCollections.observableArrayList(productList);
    tbProduct.setItems(productObservableList);
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

      if (valor == null || valor.isEmpty()) {
        return true;
      } else if (product.getProductName().toLowerCase().contains(valor.toLowerCase())) {
        return true;
      }

      return false;
    });

    SortedList<ProductWithImage> dadosOrdenados = new SortedList<>(filteredList);
    dadosOrdenados.comparatorProperty().bind(tbProduct.comparatorProperty());
//    FilterUtils.mensage(legenda, dadosOrdenados.size(), "Quantidade de Estratigrafias encontradas");

    tbProduct.setItems(dadosOrdenados);
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
    lbTitle.setText(bundle.getString("txt_edit_order"));
    NavigationUtils.setVisibility(false, btEditOrder, apProductList, txtSearch);
    NavigationUtils.setVisibility(true, btBackToList, btCreateOrder, apEditOrderList);

  }

  @FXML
  void createOrder(ActionEvent event) {
    if (totalPrice <= 0) {
      Messenger.info(bundle.getString("txt_please_choose_number_of_product_correctly"));
      return;
    }
    if (ConstantConfig.FAKE) {
      Messenger.info(bundle.getString("msg_create_order_successfully") + "\"" + bundle.getString("txt_order_created") + "\"");
    } else {
      PutQueueRequest queueRequest = createPutOrderRequest();
      if (queueRequest == null) {
        return;
      }
      AppController.getInstance().showProgressDialog();
      ServiceBuilder.getApiService().putOrder(queueRequest).enqueue(new BaseCallback<Object>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          AppController.getInstance().hideProgressDialog();
          Messenger.erro(errorMessage);
        }

        @Override
        public void onSuccess(Object data) {
          AppController.getInstance().hideProgressDialog();
          Messenger.info(bundle.getString("txt_operation_successful"));
          goToProductList(event);
        }
      });
    }
  }

  @FXML
  void goToProductList(ActionEvent event) {
    getProductList();
    lbTitle.setText(bundle.getString("txt_product_list"));
    lbLegend.setText(bundle.getString("txt_hold_ctrl_to_choose_items"));
    tbProduct.getSelectionModel().clearSelection();

    NavigationUtils.setVisibility(true, btEditOrder, apProductList, txtSearch);
    NavigationUtils.setVisibility(false, btBackToList, btCreateOrder, apEditOrderList);
  }

  private List<ProductInOrder> createProductInOrderListFromSelectedProductList() {
    if (productList == null || productList.isEmpty()) {
      return new ArrayList<>();
    }

    ObservableList<ProductWithImage> selectedItems = tbProduct.getSelectionModel().getSelectedItems();

    ListIterator<ProductWithImage> iterator = selectedItems.listIterator();
    List<ProductInOrder> selectedProducts = new ArrayList<>();
    while (iterator.hasNext()) {
      Product item = iterator.next();
      for (ProductWithImage product : productList) {
        if (product.getId().equals(item.getId())) {
          ProductInOrder productInOrder = product.convertToProductInOrder();
          productInOrder.setOnContentChange(new ProductInOrder.OnContentChange() {
            @Override
            public void onNumberChange(Integer oldNumber, Integer newNumber) {
              if (newNumber >=0){
                totalPrice += (newNumber-productInOrder.getCount())*productInOrder.getPrice();
                productInOrder.setCount(newNumber);
                lbLegend.setText(bundle.getString("txt_total_price") + ": " + totalPrice + " " + bundle.getString("txt_vnd"));
              } else {
//                Messenger.erro("Số lượng hàng phải lớn hơn 0");
                productInOrder.getTfNumber().setText(productInOrder.getCount()+"");
              }
            }}
          );
          selectedProducts.add(productInOrder);
        }
      }
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

  private PutQueueRequest createPutOrderRequest() {
    if (StaticVarUtils.getSessionDeviceInfo() == null || StaticVarUtils.getSessionDeviceInfo().getInfo() == null) {
      AppController.getInstance().startInitialize();
    }
    ObservableList<ProductInOrder> orderObservableList = tbProductInOrder.getItems();

    List<PutQueueRequest.Item> items = new ArrayList<>();
    ListIterator<ProductInOrder> iterator = orderObservableList.listIterator();
    int totalPrice = 0;
    while (iterator.hasNext()) {
      ProductInOrder productInOrder = iterator.next();
      if (productInOrder.getCount()<=0){
        Messenger.erro("Số lượng hàng phải lớn hơn 0!");
        return  null;
      }
      int priceOfProduct = productInOrder.getPrice() * productInOrder.getCount();
      items.add(new PutQueueRequest.Item(productInOrder.getProductName(),
        productInOrder.getId(),
        productInOrder.getCount(),
        productInOrder.getPrice(),
        priceOfProduct,
        productInOrder.getTfNote().getText().trim(),
        priceOfProduct,
        productInOrder.getImageid()));
      totalPrice += priceOfProduct;
    }

    PutQueueRequest putQueueRequest = new PutQueueRequest(totalPrice, items,
      StaticVarUtils.getSessionDeviceInfo().getInfo().getName(),
      StaticVarUtils.getSessionDeviceInfo().getInfo().getId(),
      StaticVarUtils.getSessionDeviceInfo().getSessionid(),
      null);
    putQueueRequest.setLocation(StaticVarUtils.getSessionDeviceInfo().getInfo().getLocation());
    return putQueueRequest;
  }
}
