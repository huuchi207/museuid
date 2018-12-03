package br.com.museuid.screen.stock_importing;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.Product;
import br.com.museuid.model.data.ProductImporting;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.StockImportingRequest;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FakeDataUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
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
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class StockImportingControler extends AnchorPane {

  public TableColumn colProductName;
  public TextField txtDescription;
  public GridPane gridStockImport;
  public TextField txtStockImportingName;
  public TableView<ProductImporting> tbProductImporting;
  public AnchorPane apProductList;
  public Button btBackToList;
  public Button btEditImportingSession;
  public Button btCreateImportingSession;
  public TableColumn colProductNameImporting;
  public TableColumn colDescriptionImporting;
  public TableColumn colPriceImporting;
  public TableColumn colNumberAdding;
  private List<Product> productList = new ArrayList<>();
  private String selectedProductId = "0";

  @FXML
  private GridPane apAdd;
  @FXML
  private Label lbLegend;
  @FXML
  private Button btExclude;
  @FXML
  private TableView<Product> tbProduct;
  @FXML
  private TextField txtProductName;
  @FXML
  private Button btSave;
  @FXML
  private TextField txtInStock;
  @FXML
  private ToggleGroup menu;
  @FXML
  private TableColumn colPrice;
  @FXML
  private TextField txtSearch;
  @FXML
  private TableColumn colInStock;
  @FXML
  private Label lbTitle;
  @FXML
  private TableColumn colDescription;
  @FXML
  private Button btEdit;
  //    @FXML
//    private TableColumn colId;
  @FXML
  private TextField txtPrice;
  @FXML
  private AnchorPane apEdit;
  @FXML
  private HBox boxEdit;
  private ResourceBundle bundle;
  private ObservableList<Product> productObservableList = FXCollections.observableList(new ArrayList<>());
  private ObservableList<ProductImporting> productImportingObservableList = FXCollections.observableList(new ArrayList<>());
  public StockImportingControler() {
    try {
      FXMLLoader fxml = new FXMLLoader(getClass().getResource("stock_importing.fxml"));
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

  private void getProductList() {
    if (ConstantConfig.FAKE) {
      if (productList == null) {
        productList = FakeDataUtils.getFakeProductList();
      }

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
          productList = data;
          updateProductTable(data);
        }

      });
    }
  }
  private void updateProductTable(List<Product> data) {
    productObservableList.clear();
    productObservableList.addAll(data);
  }

  private void updateImportingProductTable(ObservableList<Product> selected){
    productImportingObservableList.clear();
    for (Product product : selected){
      ProductImporting productImporting = new ProductImporting(product.getId(), product.getProductName(), product.getDescription(), product.getPrice(), product.getInStock());
      productImporting.setOnContentChange(new ProductImporting.OnContentChange() {
        @Override
        public void onNumberChange(Integer oldNumber, Integer newNumber) {
          if (newNumber >=0){
            productImporting.setNumberToImport(newNumber);
          } else {
//            Messenger.erro("Số lượng hàng phải lớn hơn 0");
            productImporting.getTfNumberToImport().setText(productImporting.getNumberToImport()+"");
          }
        }}
      );
      productImportingObservableList.add(productImporting);
    }
    tbProductImporting.refresh();
  }

  private void initTable() {
    tbProduct.getSelectionModel().setSelectionMode(
      SelectionMode.MULTIPLE
    );
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
//    colInStock.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

    tbProduct.setItems(productObservableList);

    colProductNameImporting.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colDescriptionImporting.setCellValueFactory(new PropertyValueFactory<>("description"));
    colPriceImporting.setCellValueFactory(new PropertyValueFactory<>("price"));
    colNumberAdding.setCellValueFactory(new PropertyValueFactory<ProductImporting,String>("tfNumberToImport"));

    tbProductImporting.setItems(productImportingObservableList);
  }

  /**
   * FieldViewUtils de pesquisar para filtrar dados na updateProductTable
   */
  private void filter(String valor, ObservableList<Product> products) {

    FilteredList<Product> filtedList = new FilteredList<>(products, product -> true);
    filtedList.setPredicate(product -> {

      if (valor == null || valor.isEmpty()) {
        return true;
      } else if (product.getProductName().toLowerCase().contains(valor.toLowerCase())) {
        return true;
      }
      return false;
    });

    SortedList<Product> dadosOrdenados = new SortedList<>(filtedList);
    dadosOrdenados.comparatorProperty().bind(tbProduct.comparatorProperty());
//    FilterUtils.mensage(legenda, dadosOrdenados.size(), "Quantidade de Estratigrafias encontradas");

    tbProduct.setItems(dadosOrdenados);
  }
  @FXML
  void importStock(ActionEvent event){

  }
  @FXML
  void goToProductList(ActionEvent event){
    getProductList();
    lbTitle.setText(bundle.getString("txt_product_list"));
    lbLegend.setText(bundle.getString("txt_hold_ctrl_to_choose_items"));
    NavigationUtils.setVisibility(true, apProductList, txtSearch, btEditImportingSession);
    NavigationUtils.setVisibility(false, gridStockImport, btBackToList, btCreateImportingSession);
  }

  @FXML
  void editImportingSession(ActionEvent event){
    ObservableList<Product> selectedItems = tbProduct.getSelectionModel().getSelectedItems();
    if (selectedItems == null || selectedItems.isEmpty()){
      NoticeUtils.alert("Vui lòng chọn sản phẩm!");
      return;
    }
    updateImportingProductTable(selectedItems);

    lbTitle.setText(bundle.getString("txt_import_stock_info"));
    lbLegend.setText("");
    FieldViewUtils.resetField(txtStockImportingName);
    NavigationUtils.setVisibility(false, apProductList, txtSearch, btEditImportingSession);
    NavigationUtils.setVisibility(true, gridStockImport, btBackToList, btCreateImportingSession);
  }

  @FXML
  void createImportingSession(ActionEvent event){
    if(FieldViewUtils.noEmpty(txtStockImportingName)){
      Messenger.info("Vui lòng nhập vào trường được yêu cầu");
      return;
    }
    ObservableList<ProductImporting> data = tbProductImporting.getItems();
    StockImportingRequest stockImportingRequest = new StockImportingRequest();
    stockImportingRequest.setName(txtStockImportingName.getText().trim());
    List<StockImportingRequest.Item> items = new ArrayList<>();

    for (ProductImporting productImporting : data){
      if (productImporting.getNumberToImport()<=0){
        Messenger.erro("Số lượng hàng phải lớn hơn 0!");
        return;
      }
      items.add(new StockImportingRequest.Item(productImporting.getId(), productImporting.getProductName(), productImporting.getNumberToImport()));
    }
    stockImportingRequest.setItems(items);
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().createImportingStockRequest(stockImportingRequest).enqueue(new BaseCallback<Object>() {
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
}
