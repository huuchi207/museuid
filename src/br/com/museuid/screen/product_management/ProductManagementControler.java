package br.com.museuid.screen.product_management;

import org.apache.commons.lang3.StringUtils;

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
import br.com.museuid.dto.Product;
import br.com.museuid.dto.ProductWithImage;
import br.com.museuid.dto.UploadImageDTO;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.ComboUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.FileUtils;
import br.com.museuid.util.ImageUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
import br.com.museuid.util.NoticeUtils;
import br.com.museuid.util.NumberUtils;
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
import javafx.stage.FileChooser;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProductManagementControler extends AnchorPane {

  public TableColumn colProductName;
  public TableColumn colImage;
  public TextField txtDescription;

  public TableColumn colProductType;
  private List<ProductWithImage> productList = new ArrayList<>();
  private Product selectedProduct = null;
  public ComboBox<String> cbProductType;
  @FXML
  private GridPane apAdd;
  @FXML
  private Label legenda;
  @FXML
  private Button btExclude;
  @FXML
  private TableView<ProductWithImage> tbProduct;
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
  public Label lbProductImage;
  public Button btUploadImage;
  private String selectedImagePath;

  public ProductManagementControler() {
    try {
      FXMLLoader fxml = new FXMLLoader(getClass().getResource("product_management.fxml"));
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
  void tbAdd(ActionEvent event) {
    config(bundle.getString("txt_create_product"), bundle.getString("txt_required_fields"), 0);
    NavigationUtils.setVisibility(true, apAdd, btSave);
    NavigationUtils.setVisibility(false, boxEdit, apEdit, txtSearch);
    resetField();
    txtProductName.setDisable(false);

    FieldViewUtils.setGlobalEventHandler(this, btSave);

  }

  @FXML
  void tbEdit(ActionEvent event) {
    config(bundle.getString("txt.edit.product.info"), "", 1);
    NavigationUtils.setVisibility(true, apEdit, boxEdit, txtSearch);
    NavigationUtils.setVisibility(false, btSave, apAdd);

    getProductList();
    FieldViewUtils.setGlobalEventHandler(this, null);

  }


  @FXML
  void save(ActionEvent event) {
    boolean isValid = FieldViewUtils.noEmpty(txtProductName, txtPrice, txtInStock);

    String productName = txtProductName.getText();
    String price = txtPrice.getText().trim();
    String inStock = txtInStock.getText();
    String description = txtDescription.getText();
    if (isValid) {
      NoticeUtils.alert(bundle.getString("txt_please_enter_info"));
      return;
    }
    String productType = cbProductType.getValue();
    if (StringUtils.isEmpty(productType)) {
      NoticeUtils.alert("Vui lòng chọn loại sản phẩm");
      return;
    }
    Integer priceNumber, inStockNumber;

    priceNumber = NumberUtils.convertVNDFormattedStringToInteger(price);
    inStockNumber = NumberUtils.convertVNDFormattedStringToInteger(inStock);
    if (priceNumber == null || inStockNumber == null){
      Messenger.erro("Giá và số lượng hàng trong kho phải là số nguyên!");
      return;
    }
    if (priceNumber <= 0 || inStockNumber <= 0) {
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
          updateDataToServer(productName, description, priceNumber, inStockNumber, productType, data.get_id());
        }
      });
    } else {
      updateDataToServer(productName, description, priceNumber, inStockNumber, productType, null);
    }
  }

  private void updateDataToServer(String productName, String description,
                                  Integer priceNumber, Integer inStockNumber, String productType, String imageId) {
    if (selectedProduct == null) {
      Product product = new Product(productName, description, priceNumber, inStockNumber);
      product.setImageid(imageId);
      product.setType(productType);
      if (ConstantConfig.FAKE) {
//        productList.add(product);
        updateTable();
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
            getProductList();
            AppController.getInstance().hideProgressDialog();
            Messenger.info(BundleUtils.getResourceBundle().getString("txt_operation_successful"));
            tbAdd(null);
          }
        });
      }
    } else {
      Product product = selectedProduct;
      product.setProductName(productName);
      product.setType(productType);
      product.setDescription(description);
      product.setPrice(priceNumber);
      product.setInStock(inStockNumber);
      if (imageId != null) {
        product.setImageid(imageId);
      }
      if (ConstantConfig.FAKE) {
        productList.remove(tbProduct.getSelectionModel().getSelectedItem());
//        productList.add(product);
        updateTable();
        Messenger.info(bundle.getString("txt_operation_successful"));
      } else {
        AppController.getInstance().showProgressDialog();
        ServiceBuilder.getApiService().updateProduct(product).enqueue(new BaseCallback<Product>() {
          @Override
          public void onError(String errorCode, String errorMessage) {
            AppController.getInstance().hideProgressDialog();
            Messenger.erro(errorMessage);
          }

          @Override
          public void onSuccess(Product data) {
            getProductList();
            AppController.getInstance().hideProgressDialog();
            Messenger.info(BundleUtils.getResourceBundle().getString("txt_operation_successful"));
            tbAdd(null);
          }
        });
      }
    }
  }

  @FXML
  void edit(ActionEvent event) {
    try {
      ProductWithImage selectedProduct = tbProduct.getSelectionModel().getSelectedItem();

      if (selectedProduct == null) {
        NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
        return;
      }

      tbAdd(null);

      txtProductName.setText(selectedProduct.getProductName());
      txtPrice.setText(selectedProduct.getPrice() + "");
      txtInStock.setText(selectedProduct.getInStock() + "");
      txtDescription.setText(selectedProduct.getDescription());
      lbProductImage.setGraphic(selectedProduct.getProductImage());
      txtProductName.setDisable(true);
      cbProductType.setValue(selectedProduct.getType());

      lbTitle.setText(bundle.getString("txt.edit.product.info"));
      menu.selectToggle(menu.getToggles().get(1));

      this.selectedProduct = selectedProduct;

    } catch (NullPointerException ex) {
      NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
    }
  }

  @FXML
  void exclude(ActionEvent event) {
    try {
      Product selectedProduct = tbProduct.getSelectionModel().getSelectedItem();

      if (selectedProduct == null) {
        NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
        return;
      }

      DialogUtils.ResponseMessage responseMessage = Messenger.confirm("Loại trừ " + selectedProduct.getProductName() + " ?");

      if (responseMessage == DialogUtils.ResponseMessage.YES) {
        if (ConstantConfig.FAKE) {
          Messenger.info(bundle.getString("txt_delete_successfully"));
          productList.remove(selectedProduct);
          updateTable();
          tbProduct.getSelectionModel().clearSelection();
          return;
        } else {
          AppController.getInstance().showProgressDialog();
          ServiceBuilder.getApiService().deleteProduct(selectedProduct.getId()).enqueue(new BaseCallback<Object>() {
            @Override
            public void onError(String errorCode, String errorMessage) {
              AppController.getInstance().hideProgressDialog();
              Messenger.erro(errorMessage);
            }

            @Override
            public void onSuccess(Object data) {
              AppController.getInstance().hideProgressDialog();
              Messenger.info(bundle.getString("txt_operation_successful"));
              getProductList();
              tbProduct.getSelectionModel().clearSelection();
            }
          });
        }


      }
    } catch (NullPointerException ex) {
      Messenger.alert(bundle.getString("txt_please_choose_target"));
    }
  }

  @FXML
  public void initialize() {
    FieldViewUtils.setNumberFormatStyleTextField(txtInStock, txtPrice);
    tbAdd(null);

//        Grupo.notEmpty(menu);
    getProductList();

    txtSearch.textProperty().addListener((obs, old, novo) -> {
      filter(novo, FXCollections.observableArrayList(productList));
    });
  }

  /**
   * Configurações de tela, titulos e exibição de telas e menus
   */
  private void config(String tituloTela, String msg, int grupoMenu) {
    lbTitle.setText(tituloTela);
//        NavigationUtils.setVisibility(false, btExclude, btSave, btEdit, apAdd, apEdit, txtSearch);

    legenda.setText(msg);
    tbProduct.getSelectionModel().clearSelection();
    menu.selectToggle(menu.getToggles().get(grupoMenu));

    selectedProduct = null;
  }

  /**
   * Sincronizar dados com banco de dados
   */
  private void getProductList() {
    if (ConstantConfig.FAKE) {
      if (productList == null) {
//        productList = FakeDataUtils.getFakeProductList();
      }
      updateTable();
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
          productList.clear();
          for (Product product : data) {
            productList.add(product.convertToProductWithImage());
          }
          updateTable();
          updateComboboxProductType();
        }
      });
    }
  }

  private void updateComboboxProductType() {
    Set<String> listProductType = new HashSet<>();
    for (ProductWithImage item : productList) {
      listProductType.add(item.getType());
    }
    ComboUtils.popular(cbProductType, new ArrayList<String>(listProductType));
  }

  /**
   * Mapear dados objetos para inserção dos dados na updateTable
   */
  private void updateTable() {
    ObservableList data = FXCollections.observableArrayList(productList);

//        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
    colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    colInStock.setCellValueFactory(new PropertyValueFactory<>("inStock"));
    colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
    colImage.setCellValueFactory(new PropertyValueFactory<>("productImage"));
    colProductType.setCellValueFactory(new PropertyValueFactory<>("type"));

    colInStock.setCellFactory(tc -> new FormattedNumberTableCell());
    colPrice.setCellFactory(tc -> new FormattedNumberTableCell());
    tbProduct.setItems(data);
  }

  /**
   * FieldViewUtils de pesquisar para filtrar dados na updateTable
   */
  private void filter(String valor, ObservableList<ProductWithImage> products) {

    FilteredList<ProductWithImage> filtedList = new FilteredList<>(products, product -> true);
    filtedList.setPredicate(product -> {

      if (valor == null || valor.isEmpty()) {
        return true;
      } else if (product.getProductName().toLowerCase().contains(valor.toLowerCase())) {
        return true;
      }
      return false;
    });

    SortedList<ProductWithImage> dadosOrdenados = new SortedList<>(filtedList);
    dadosOrdenados.comparatorProperty().bind(tbProduct.comparatorProperty());
//    FilterUtils.mensage(legenda, dadosOrdenados.size(), "Quantidade de Estratigrafias encontradas");

    tbProduct.setItems(dadosOrdenados);
  }

  /**
   * Limpar campos textfield cadastro de Estratigrafias
   */
  private void resetField() {
    FieldViewUtils.resetField(txtProductName, txtPrice);
    FieldViewUtils.resetField(txtInStock, txtDescription);
    lbProductImage.setGraphic(null);
    selectedImagePath = null;
    cbProductType.getSelectionModel().selectFirst();
  }

  @FXML
  public void chooseImage(ActionEvent event) {
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

  @FXML
  void addProductTypeAction(ActionEvent event) {
    Optional<String> newProductType = DialogUtils.showTextDialog("Nhập loại sản phẩm mối");
    if (newProductType.isPresent()) {
      cbProductType.getItems().add(newProductType.get());
      cbProductType.setValue(newProductType.get());
    }
  }
}
