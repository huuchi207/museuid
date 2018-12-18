package br.com.museuid.screen.stock_importing_management;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.customview.CustomListCellComboBox;
import br.com.museuid.customview.MutipleLineTableCell;
import br.com.museuid.model.data.ProductImporting;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.StockImportingRequest;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.ComboUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
import br.com.museuid.util.NoticeUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

public class StockImportingManagementControler extends AnchorPane {

  public TableColumn colImportingName;
  public TextField txtDescription;
//  public GridPane gridStockImport;
//  public TextField txtStockImportingName;
  public TableView<ProductImporting> tbProductImporting;
  public AnchorPane apProductList;
  public Button btBackToList;
  public Button btEditImportingSession;
  public Button btAccept;
  public TableColumn colProductName;

  public TableColumn colNumberAdding;
  public TableColumn colImportingCreatedTime;
  public TableColumn colImporterName;
  public TableColumn colId;
  public TableColumn colImportingContent;
  public TableColumn colImportingStatus;
  public HBox boxActionList;
  public HBox boxActionDetail;
  public ComboBox<StockImportingRequest.Status> cbFilter;
  public Button btReject;
  public TableColumn colIdInDetail;

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
  public StockImportingManagementControler() {
    try {
      FXMLLoader fxml = new FXMLLoader(getClass().getResource("stock_importing_management.fxml"));
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
    initComboboxFilter();
    initTable();
    goToHistoryList(null);
//    txtSearch.textProperty().addListener((obs, old, novo) -> {
//      filter(novo, FXCollections.observableArrayList(stockImportingRequestObservableList));
//    });
  }

  private void initComboboxFilter(){
    ObservableList<StockImportingRequest.Status> listRole = FXCollections.observableArrayList(StockImportingRequest.Status.getStockImportingStatusList());
    Callback<ListView<StockImportingRequest.Status>, ListCell<StockImportingRequest.Status>> cellFactory = new Callback<ListView<StockImportingRequest.Status>, ListCell<StockImportingRequest.Status>>() {
      @Override
      public ListCell<StockImportingRequest.Status> call(ListView<StockImportingRequest.Status> param) {
        return new CustomListCellComboBox<>();
      }
    };
    cbFilter.setCellFactory(cellFactory);
    cbFilter.setButtonCell(cellFactory.call(null));
    cbFilter.valueProperty().addListener(new ChangeListener<StockImportingRequest.Status>() {
      @Override
      public void changed(ObservableValue<? extends StockImportingRequest.Status> observable,StockImportingRequest.Status oldValue, StockImportingRequest.Status newValue) {
        selectedRequest = null;
        filter(newValue, FXCollections.observableArrayList(stockImportingRequestObservableList));
      }
    });

    ComboUtils.popular(cbFilter, listRole);
    cbFilter.setValue(StockImportingRequest.Status.ALL);
  }
  private void getStockImportHistory() {
    if (ConstantConfig.FAKE) {
//      if (stockImportingHistoryList == null) {
//        stockImportingHistoryList = FakeDataUtils.getFakeProductList();
//      }

    } else {
      AppController.getInstance().showProgressDialog();
      ServiceBuilder.getApiService().getImportingStockRequestList(null, null).
        enqueue(new BaseCallback<List<StockImportingRequest>>() {
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
    cbFilter.setValue(StockImportingRequest.Status.ALL);
  }

  private void updateImportingProductTable(StockImportingRequest request){
    selectedRequest = request;
    productImportingObservableList.clear();
//    txtStockImportingName.setText(request.getName());
    int i = 1;
    for (StockImportingRequest.Item item : request.getItems()){
      ProductImporting productImporting = new ProductImporting(item.getItemname(), item.getItemid(), item.getQuantity());
      productImporting.setNumber(i++);
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
    colImporterName.setCellValueFactory(new PropertyValueFactory<>("requestername"));
    colImportingStatus.setCellValueFactory(new PropertyValueFactory<>("statusText"));
    tbStockImportHistory.setItems(stockImportingRequestObservableList);

    colIdInDetail.setCellValueFactory(new PropertyValueFactory<>("number"));
    colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colNumberAdding.setCellValueFactory(new PropertyValueFactory<>("numberToImport"));

    tbProductImporting.setItems(productImportingObservableList);

    FieldViewUtils.setEnterKeyEvent(tbStockImportHistory, btEditImportingSession);
  }

  /**
   * FieldViewUtils de pesquisar para filtrar dados na updateHistoryTable
   */
  private void filter(StockImportingRequest.Status valor, ObservableList<StockImportingRequest> stockImportingRequests) {
    if (valor == null){
      return;
    }
    FilteredList<StockImportingRequest> filtedList = new FilteredList<>(stockImportingRequests, stockImportingRequest -> true);

    filtedList.setPredicate(stockImportingRequest -> {
      if (StockImportingRequest.Status.ALL == valor){
        return true;
      } else if (StockImportingRequest.Status.valueOf(stockImportingRequest.getStatus()) == valor) {
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
  void goToHistoryList(ActionEvent event){
    getStockImportHistory();
    lbTitle.setText("Lịch sử nhập kho");
    lbLegend.setText("Bạn chỉ có thể thao tác với yêu cầu chưa được xử lý.");
    NavigationUtils.setVisibility(true, apProductList, cbFilter, boxActionList);
    NavigationUtils.setVisibility(false, tbProductImporting, boxActionDetail);

    FieldViewUtils.setGlobalEventHandler(this, btEditImportingSession);

  }

  @FXML
  void handleImportingSession(ActionEvent event){
    StockImportingRequest selectedItem = tbStockImportHistory.getSelectionModel().getSelectedItem();
    if (selectedItem == null){
      NoticeUtils.alert("Vui lòng chọn một đối tượng!");
      return;
    }
    if (!StockImportingRequest.Status.NEW.name().equals(selectedItem.getStatus())){
      NoticeUtils.alert("Yêu cầu này đã được xử lý!");
      return;
    }
    updateImportingProductTable(selectedItem);

    lbTitle.setText(bundle.getString("txt_import_stock_info")+ ": "+ selectedItem.getName());
    lbLegend.setText("");
    NavigationUtils.setVisibility(false, apProductList, cbFilter, boxActionList);
    NavigationUtils.setVisibility(true, tbProductImporting, boxActionDetail);

    FieldViewUtils.setGlobalEventHandler(this, null);

  }

  @FXML
  void accept(ActionEvent event){
    handleRequest(selectedRequest, StockImportingRequest.Status.APPROVED);
  }
  @FXML
  void reject(ActionEvent actionEvent){
    handleRequest(selectedRequest, StockImportingRequest.Status.REJECTED);
  }
  private void handleRequest(StockImportingRequest request, StockImportingRequest.Status status){
    StockImportingRequest stockImportingRequest = new StockImportingRequest();
    stockImportingRequest.setPendingproductid(request.get_id());
    stockImportingRequest.setDecision(status.name());
    AppController.getInstance().showProgressDialog();

    ServiceBuilder.getApiService().handleImportingRequest(stockImportingRequest).enqueue(new BaseCallback<Object>() {
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
