package br.com.museuid.screen.stock_importing;

import org.apache.commons.lang3.StringUtils;
import org.controlsfx.control.GridView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import br.com.museuid.app.App;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.customview.FormattedNumberTableCell;
import br.com.museuid.customview.customgridview.ItemGridCellFactory;
import br.com.museuid.dto.Product;
import br.com.museuid.dto.ProductWithImage;
import br.com.museuid.dto.UploadImageDTO;
import br.com.museuid.model.data.ProductImporting;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.StockImportingRequest;
import br.com.museuid.util.AppNoticeUtils;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.ComboUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.FileUtils;
import br.com.museuid.util.ImageUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
import br.com.museuid.util.NumberUtils;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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
  public TableColumn colProductImage;
  public GridPane nodeAdd;
  public StackPane nodeEdit;
  public ComboBox<String> cbProductType;
  public Button btAddProductType;
  private List<ProductWithImage> productList = new ArrayList<>();
  private String selectedProductId = "0";

  @FXML
  private GridPane apAdd;
  @FXML
  private Label lbLegend;
  @FXML
  private Button btExclude;
  @FXML
  private TableView<ProductWithImage> tbProduct;
  @FXML
  private TextField txtProductName;
  @FXML
  private Button btSave;
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
  private ObservableList<ProductWithImage> productObservableList = FXCollections.observableList(new ArrayList<>());
  private ObservableList<ProductImporting> productImportingObservableList = FXCollections.observableList(new ArrayList<>());

  public GridView<ProductWithImage> gridProduct;
  public ComboBox<String> cbFilter;
  public Button btCreateProduct;
  public Label lbProductImage;
  private String selectedImagePath;

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
    FieldViewUtils.setNumberFormatStyleTextField( txtPrice);

    initTable();
    tbEdit(null);
    cbFilter.valueProperty().addListener(new ChangeListener<String>() {
      @Override
      public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (newValue != null) {
          txtSearch.setText("");
          filter(newValue, null, FXCollections.observableArrayList(productList));
        }
      }
    });

    txtSearch.textProperty().addListener((obs, old, novo) -> {
      filter(cbFilter.getValue(), novo, FXCollections.observableArrayList(productList));
    });
  }

  private void getProductList() {
    if (ConstantConfig.FAKE) {
      if (productList == null) {
//        productList = FakeDataUtils.getFakeProductList();
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
          List<ProductWithImage> list = new ArrayList<>();
          for (Product product : data) {
            list.add(product.convertToProductWithImage());
          }
          productList = list;
          updateProductTable(list);
        }

      });
    }
  }

  private void updateProductTable(List<ProductWithImage> data) {
    productObservableList.clear();
    productObservableList.addAll(data);

    Set<String> productType = new HashSet<String>();
    for (ProductWithImage item : productList) {
      if (!StringUtils.isEmpty(item.getType()))
        productType.add(item.getType());
    }
    ComboUtils.popular(cbProductType, new ArrayList<String>(productType));

    productType.add("Tất cả");

    ComboUtils.data(cbFilter, new ArrayList<String>(productType), "Tất cả");
  }

  private void updateImportingProductTable(ObservableList<ProductWithImage> selected) {
    productImportingObservableList.clear();
    for (ProductWithImage product : selected) {
      ProductImporting productImporting = product.convertToProductImporting();
      productImporting.setOnContentChange(
        new ProductImporting.OnContentChange() {
          @Override
          public void onNumberChange(Integer oldNumber, Integer newNumber) {
            if (newNumber >= 0) {
              productImporting.setNumberToImport(newNumber);
            } else {
//            Messenger.erro("Số lượng hàng phải lớn hơn 0");
              productImporting.getTfNumberToImport().setText(productImporting.getNumberToImport() + "");
            }
          }
        }
      );
      productImportingObservableList.add(productImporting);
    }
    tbProductImporting.refresh();
  }

  private void initTable() {
    ItemGridCellFactory itemGridCellFactory = new ItemGridCellFactory();
    gridProduct.setCellFactory(itemGridCellFactory);
    gridProduct.setItems(productObservableList);

    colProductNameImporting.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colDescriptionImporting.setCellValueFactory(new PropertyValueFactory<>("description"));
    colPriceImporting.setCellValueFactory(new PropertyValueFactory<>("price"));
    colPriceImporting.setCellFactory(tc -> new FormattedNumberTableCell());
    colNumberAdding.setCellValueFactory(new PropertyValueFactory<ProductImporting, String>("tfNumberToImport"));

    tbProductImporting.setItems(productImportingObservableList);
  }

  /**
   * FieldViewUtils de pesquisar para filtrar dados na updateTable
   */
  private void filter(String type, String keyword, ObservableList<ProductWithImage> products) {
    FilteredList<ProductWithImage> filteredList = new FilteredList<>(products, Product -> true);
    filteredList.setPredicate(product -> {
      String nonNullTypeFilter = type != null ? type : "";
      String nonNullTypeOfProduct = product.getType() != null ? product.getType() : "";
      if (keyword == null) {
        if ("Tất cả".equals(nonNullTypeFilter) || nonNullTypeFilter.equals(nonNullTypeOfProduct)) {
          return true;
        }
      } else {
        if ("Tất cả".equals(nonNullTypeFilter) || nonNullTypeOfProduct.equals(nonNullTypeFilter)) {
          if (keyword.equals("") || product.getProductName().toLowerCase().contains(keyword.toLowerCase())) {
            return true;
          }
        }
      }

      return false;
    });

    SortedList<ProductWithImage> dadosOrdenados = new SortedList<>(filteredList);

    gridProduct.setItems(dadosOrdenados);
  }

  @FXML
  void importStock(ActionEvent event) {

  }

  @FXML
  void goToProductList(ActionEvent event) {
    getProductList();
    lbTitle.setText(bundle.getString("txt_product_list"));
    lbLegend.setText("");
    NavigationUtils.setVisibility(true, apProductList, txtSearch, btEditImportingSession);
    NavigationUtils.setVisibility(false, gridStockImport, btBackToList, btCreateImportingSession, btCreateProduct);

    FieldViewUtils.setGlobalEventHandler(this, btEditImportingSession);
  }

  @FXML
  void editImportingSession(ActionEvent event) {
    ObservableList<ProductWithImage> selectedItems = FXCollections.observableArrayList(getSelectedProducts());
    if (selectedItems == null || selectedItems.isEmpty()) {
      AppNoticeUtils.alert("Vui lòng chọn sản phẩm!");
      return;
    }
    updateImportingProductTable(selectedItems);

    lbTitle.setText(bundle.getString("txt_import_stock_info"));
    lbLegend.setText("");
    FieldViewUtils.resetField(txtStockImportingName);
    NavigationUtils.setVisibility(false, apProductList, txtSearch, btEditImportingSession);
    NavigationUtils.setVisibility(true, gridStockImport, btBackToList, btCreateImportingSession);

    FieldViewUtils.setGlobalEventHandler(this, btCreateImportingSession);
  }

  @FXML
  void createImportingSession(ActionEvent event) {
    if (FieldViewUtils.noEmpty(txtStockImportingName)) {
      Messenger.info("Vui lòng nhập vào trường được yêu cầu");
      return;
    }
    ObservableList<ProductImporting> data = tbProductImporting.getItems();
    StockImportingRequest stockImportingRequest = new StockImportingRequest();
    stockImportingRequest.setName(txtStockImportingName.getText().trim());
    List<StockImportingRequest.Item> items = new ArrayList<>();

    for (ProductImporting productImporting : data) {
      if (productImporting.getNumberToImport() <= 0) {
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

  private List<ProductWithImage> getSelectedProducts() {
    List<ProductWithImage> items = new ArrayList<>();
    if (gridProduct.getItems() != null) {
      for (ProductWithImage item : gridProduct.getItems()) {
        if (item.isSelected()) {
          items.add(item);
        }
      }
    }
    return items;
  }

  private void config(String tituloTela, String msg, int grupoMenu) {
    lbTitle.setText(tituloTela);
//        NavigationUtils.setVisibility(false, btExclude, btSave, btEdit, apAdd, apEdit, txtSearch);

    lbLegend.setText(msg);
    menu.selectToggle(menu.getToggles().get(grupoMenu));

  }

  @FXML
  void tbAdd(ActionEvent event) {
    config("Thêm mới sản phẩm", "", 0);
    NavigationUtils.setVisibility(true, nodeAdd, btCreateProduct);
    NavigationUtils.setVisibility(false, nodeEdit, btBackToList, btCreateImportingSession, btEditImportingSession);
    //reset field
    FieldViewUtils.resetField(txtProductName, txtDescription, txtPrice, txtDescription);
    cbProductType.getSelectionModel().selectFirst();
    selectedImagePath = null;
    lbProductImage.setGraphic(null);
    /////

    FieldViewUtils.setGlobalEventHandler(this, btCreateProduct);
  }

  @FXML
  void tbEdit(ActionEvent event) {
    config("Nhập kho", "", 1);
    NavigationUtils.setVisibility(false, nodeAdd);
    NavigationUtils.setVisibility(true, nodeEdit);
    goToProductList(null);
  }

  @FXML
  void createProduct(ActionEvent event) {
    boolean isValid = FieldViewUtils.noEmpty(txtProductName, txtPrice);

    String productName = txtProductName.getText();
    String price = txtPrice.getText().trim();
    String inStock = "0";
    String description = txtDescription.getText();
    if (isValid) {
      AppNoticeUtils.alert(bundle.getString("txt_please_enter_info"));
      return;
    }
    String productType = cbProductType.getValue();
    if (StringUtils.isEmpty(productType)) {
      AppNoticeUtils.alert("Vui lòng chọn loại sản phẩm");
      return;
    }
    Integer priceNumber, inStockNumber;

    priceNumber = NumberUtils.convertVNDFormattedStringToInteger(price);
    inStockNumber = NumberUtils.convertVNDFormattedStringToInteger(inStock);
    if (priceNumber == null || inStockNumber == null){
      Messenger.erro("Giá và số lượng hàng trong kho phải là số nguyên!");
      return;
    }
    if (priceNumber <= 0) {
      Messenger.erro("Giá và số lượng hàng trong kho phải lớn hơn 0!");
      return;
    }

    if (selectedImagePath != null) {
      File uploadFile = new File(selectedImagePath);
      if (!uploadFile.exists()) {
        Messenger.erro("Không thể tải ảnh!");
        return;
      }
      RequestBody requestFile =
        RequestBody.create(MediaType.parse("multipart/form-data"), uploadFile);

      // MultipartBody.Part is used to send also the actual file name
      MultipartBody.Part body =
        MultipartBody.Part.createFormData("productImage", uploadFile.getName(), requestFile);

      AppController.getInstance().showProgressDialog();
      ServiceBuilder.getApiService().uploadImage(body).enqueue(new BaseCallback<UploadImageDTO>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          AppController.getInstance().hideProgressDialog();
          Messenger.erro(errorMessage);
        }

        @Override
        public void onSuccess(UploadImageDTO data) {
          AppController.getInstance().hideProgressDialog();
          addProduct(productName, description, priceNumber, inStockNumber, productType, data.get_id());
        }
      });
    } else {
      addProduct(productName, description, priceNumber, inStockNumber, productType, null);
    }
  }

  private void addProduct(String productName, String description,
                          Integer priceNumber, Integer inStockNumber, String productType, String imageId){
    Product product = new Product(productName, description, priceNumber, inStockNumber);
    product.setImageid(imageId);
    product.setType(productType);
    if (ConstantConfig.FAKE) {
//        productList.add(product);
      AppController.getInstance().hideProgressDialog();
      Messenger.info(bundle.getString("txt_operation_successful"));
    } else {
      AppController.getInstance().showProgressDialog();
      ServiceBuilder.getApiService().addProduct(product).enqueue(new BaseCallback<Product>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          AppController.getInstance().hideProgressDialog();
          Messenger.erro(errorMessage);
        }

        @Override
        public void onSuccess(Product data) {
          AppController.getInstance().hideProgressDialog();
          Messenger.info(BundleUtils.getResourceBundle().getString("txt_operation_successful"));
          tbAdd(null);
        }
      });
    }
  }
  @FXML
  void addProductTypeAction(ActionEvent event) {
    Optional<String> newProductType = DialogUtils.showTextDialog("Nhập loại sản phẩm mối");
    if (newProductType.isPresent()) {
      cbProductType.getItems().add(newProductType.get());
      cbProductType.setValue(newProductType.get());
    }
  }

  @FXML
  void chooseImage(ActionEvent event) {
    File file = FileUtils.configureImageFileChooser(new FileChooser(), App.getmStage());
    if (file != null) {
      try {
        Image image = new Image(new FileInputStream(file), 150, 150, false, false);
        ImageView imageView = new ImageView(image);
        selectedImagePath = ImageUtils.reduceImg(file, ConstantConfig.APP_DATA_FOLDER_NAME + "\\" + ConstantConfig.APP_IMAGE_SUB_FOLDER_NAME, "JPG", 150, 150);
        lbProductImage.setGraphic(imageView);
      } catch (FileNotFoundException e) {
        Messenger.erro("Không thể mở ảnh!");
      }
    }
  }


}
