package br.com.museuid.screen.stock_importing_history;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.customview.MutipleLineTableCell;
import br.com.museuid.model.data.ProductImporting;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.StockImportingRequest;
import br.com.museuid.util.BundleUtils;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class StockImportingHistoryControler extends AnchorPane {

  public TableColumn colImportingName;
  public TextField txtDescription;
  public GridPane gridStockImport;
  public TextField txtStockImportingName;
  public TableView<ProductImporting> tbProductImporting;
  public AnchorPane apProductList;
  public Button btBackToList;
  public Button btEditImportingSession;
  public Button btUpdate;
  public TableColumn colProductName;

  public TableColumn colNumberAdding;
  public TableColumn colImportingCreatedTime;
  public TableColumn colImportingUpdatedTime;
  public TableColumn colId;
  public TableColumn colImportingContent;
  public TableColumn colImportingStatus;
  public HBox boxActionList;
  public HBox boxActionDetail;
  public Button btCancel;

  @FXML
  private GridPane apAdd;
  @FXML
  private Label lbLegend;
  @FXML
  private Button btExclude;
  @FXML
  private TableView<StockImportingRequest> tbStockImportHistory;
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
  private ObservableList<StockImportingRequest> stockImportingRequestObservableList = FXCollections.observableList(new ArrayList<>());
  private ObservableList<ProductImporting> productImportingObservableList = FXCollections.observableList(new ArrayList<>());
  private StockImportingRequest selectedRequest;
  public StockImportingHistoryControler() {
    try {
      FXMLLoader fxml = new FXMLLoader(getClass().getResource("stock_importing_history.fxml"));
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
    goToHistoryList(null);
    txtSearch.textProperty().addListener((obs, old, novo) -> {
      filter(novo, FXCollections.observableArrayList(stockImportingRequestObservableList));
    });
  }

  private void getStockImportHistory() {
    if (ConstantConfig.FAKE) {
//      if (stockImportingHistoryList == null) {
//        stockImportingHistoryList = FakeDataUtils.getFakeProductList();
//      }

    } else {
      AppController.getInstance().showProgressDialog();
      ServiceBuilder.getApiService().getImportingStockRequestList().enqueue(new BaseCallback<List<StockImportingRequest>>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          AppController.getInstance().hideProgressDialog();
          Messenger.erro(errorMessage);
        }

        @Override
        public void onSuccess(List<StockImportingRequest> data) {
          AppController.getInstance().hideProgressDialog();
          int i= 1;
          for (StockImportingRequest request: data){
            request.updateFields();
            request.setNumber(""+ i++);
          }
          updateHistoryTable(data);
        }

      });
    }
  }
  private void updateHistoryTable(List<StockImportingRequest> data) {
    stockImportingRequestObservableList.clear();
    stockImportingRequestObservableList.addAll(data);
  }

  private void updateImportingProductTable(StockImportingRequest request){
    selectedRequest = request;
    productImportingObservableList.clear();
    txtStockImportingName.setText(request.getName());
    for (StockImportingRequest.Item item : request.getItems()){
      ProductImporting productImporting = new ProductImporting(item.getItemname(), item.getItemid(), item.getQuantity());
      productImporting.setOnContentChange(new ProductImporting.OnContentChange() {
        @Override
        public void onNumberChange(Integer oldNumber, Integer newNumber) {
          if (newNumber >0){
            productImporting.setNumberToImport(newNumber);
          } else {
            Messenger.erro("Số lượng hàng phải lớn hơn 0");
            productImporting.getTfNumberToImport().setText(productImporting.getNumberToImport()+"");
          }
        }}
      );
      productImportingObservableList.add(productImporting);
    }
    tbProductImporting.refresh();
  }

  private void initTable() {
    colId.setCellValueFactory(new PropertyValueFactory<>("number"));
    colImportingContent.setCellValueFactory(new PropertyValueFactory<>("content"));
    colImportingContent.setCellFactory(tv -> new MutipleLineTableCell());
    colImportingName.setCellValueFactory(new PropertyValueFactory<>("name"));
    colImportingCreatedTime.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    colImportingUpdatedTime.setCellValueFactory(new PropertyValueFactory<>("updatedAt"));
    colImportingStatus.setCellValueFactory(new PropertyValueFactory<>("statusText"));
    tbStockImportHistory.setItems(stockImportingRequestObservableList);

    colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colNumberAdding.setCellValueFactory(new PropertyValueFactory<ProductImporting,String>("tfNumberToImport"));

    tbProductImporting.setItems(productImportingObservableList);
  }

  /**
   * FieldViewUtils de pesquisar para filtrar dados na updateHistoryTable
   */
  private void filter(String valor, ObservableList<StockImportingRequest> stockImportingRequests) {
    FilteredList<StockImportingRequest> filtedList = new FilteredList<>(stockImportingRequests, stockImportingRequest -> true);
    filtedList.setPredicate(stockImportingRequest -> {

      if (valor == null || valor.isEmpty()) {
        return true;
      } else if (stockImportingRequest.getName().toLowerCase().contains(valor.toLowerCase())) {
        return true;
      }
      return false;
    });

    SortedList<StockImportingRequest> dadosOrdenados = new SortedList<>(filtedList);
    dadosOrdenados.comparatorProperty().bind(tbStockImportHistory.comparatorProperty());
//    FilterUtils.mensage(legenda, dadosOrdenados.size(), "Quantidade de Estratigrafias encontradas");

    tbStockImportHistory.setItems(dadosOrdenados);
  }
  @FXML
  void importStock(ActionEvent event){

  }
  @FXML
  void goToHistoryList(ActionEvent event){
    getStockImportHistory();
    lbTitle.setText("Lịch sử nhập kho");
    lbLegend.setText("Bạn chỉ có thể thao tác với yêu cầu chưa được xử lý.");
    NavigationUtils.setVisibility(true, apProductList, txtSearch, boxActionList);
    NavigationUtils.setVisibility(false, gridStockImport, boxActionDetail);
  }

  @FXML
  void editImportingSession(ActionEvent event){
    StockImportingRequest selectedItem = tbStockImportHistory.getSelectionModel().getSelectedItem();
    if (selectedItem == null){
      NoticeUtils.alert("Vui lòng chọn một đối tượng!");
      return;
    }
    if (!StockImportingRequest.Status.NEW.name().equals(selectedItem.getStatus())){
      NoticeUtils.alert("Bạn chỉ có thể chỉnh sửa yêu cầu chưa xử lý");
      return;
    }
    updateImportingProductTable(selectedItem);

    lbTitle.setText(bundle.getString("txt_import_stock_info"));
    lbLegend.setText("");
    NavigationUtils.setVisibility(false, apProductList, txtSearch, boxActionList);
    NavigationUtils.setVisibility(true, gridStockImport, boxActionDetail);
  }

  @FXML
  void updateImportingSession(ActionEvent event){
    if(FieldViewUtils.noEmpty(txtStockImportingName)){
      Messenger.info("Vui lòng nhập vào trường được yêu cầu");
      return;
    }
    ObservableList<ProductImporting> data = tbProductImporting.getItems();
    StockImportingRequest stockImportingRequest = new StockImportingRequest();
    stockImportingRequest.setName(txtStockImportingName.getText().trim());
    stockImportingRequest.setPendingproductid(selectedRequest.get_id());
    List<StockImportingRequest.Item> items = new ArrayList<>();
    for (ProductImporting productImporting : data){
      if (productImporting.getNumberToImport()<=0){
        Messenger.erro("Số lượng hàng phải lớn hơn 0!");
        return;
      }
      items.add(new StockImportingRequest.Item(productImporting.getProductid(), productImporting.getProductName(), productImporting.getNumberToImport()));
    }
    stockImportingRequest.setItems(items);
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().updateImportingStockRequest(stockImportingRequest).enqueue(new BaseCallback<Object>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        AppController.getInstance().hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(Object data) {
        AppController.getInstance().hideProgressDialog();
        Messenger.info(bundle.getString("txt_operation_successful"));
        goToHistoryList(null);
      }
    });

  }
  @FXML
  void cancel(ActionEvent actionEvent){
    StockImportingRequest selectedItem = tbStockImportHistory.getSelectionModel().getSelectedItem();
    if (selectedItem == null){
      NoticeUtils.alert("Vui lòng chọn một đối tượng!");
      return;
    }
    if (!StockImportingRequest.Status.NEW.name().equals(selectedItem.getStatus())){
      NoticeUtils.alert("Bạn chỉ có thể hủy yêu cầu chưa xử lý");
      return;
    }
    AppController.getInstance().showProgressDialog();
    ServiceBuilder.getApiService().deleteImportingStockRequest(selectedItem.get_id()).enqueue(new BaseCallback<Object>() {
      @Override
      public void onError(String errorCode, String errorMessage) {
        AppController.getInstance().hideProgressDialog();
        Messenger.erro(errorMessage);
      }

      @Override
      public void onSuccess(Object data) {
        AppController.getInstance().hideProgressDialog();
        Messenger.info(bundle.getString("txt_operation_successful"));
        goToHistoryList(null);
      }
    });
  }
}
