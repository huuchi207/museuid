package br.com.museuid.screen.stock_importing;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.Product;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FakeDataUtils;
import br.com.museuid.util.Messenger;
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

public class StockImportingControler extends AnchorPane {

  public TableColumn colProductName;
  public TextField txtDescription;
  public GridPane gridStockImport;
  public TextField txtStockImportingName;
  public TableView tbProductImporting;
  private List<Product> productList = new ArrayList<>();
  private String selectedProductId = "0";

  @FXML
  private GridPane apAdd;
  @FXML
  private Label legenda;
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
//        Grupo.notEmpty(menu);
    getProductList();

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
          updateTable(data);
        }

      });
    }
  }
  private void updateTable(List<Product> data) {
    productObservableList.clear();
    productObservableList.addAll(data);

  }
  private void initTable() {
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colInStock.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

    tbProductImporting.setItems(productObservableList);
  }

  /**
   * FieldViewUtils de pesquisar para filtrar dados na updateTable
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

}
