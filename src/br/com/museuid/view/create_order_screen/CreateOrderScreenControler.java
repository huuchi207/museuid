package br.com.museuid.view.create_order_screen;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.Product;
import br.com.museuid.model.data.ProductInOrder;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.FakeDataUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.Model;
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
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;

import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

public class CreateOrderScreenControler extends AnchorPane {
    //header
    public Label lbTitle;
    public TextField txtSearch;
    //product list
    public AnchorPane apProductList;
    public TableView<Product> tbProduct;
    public TableColumn colId;
    public TableColumn colProductName;
    public TableColumn colDescription;
    public TableColumn colPrice;
    //order
    public AnchorPane apEditOrderList;
    public TableView<ProductInOrder> tbProductInOrder;
    public TableColumn colIdInOrder;
    public TableColumn colProductNameInOrder;
    public TableColumn colDescriptionInOrder;
    public TableColumn colPriceInOrder;
    public TableColumn<ProductInOrder, String> colCountInOrder;
    public TableColumn colStatus;

    //bottom button
    public Button btEditOrder;
    public Button btCreateOrder;
    public Button btBackToList;
    public Label lbLegend;

    private ResourceBundle bundle;

    private List<Product> productList;
    private List<Product> selectedProductList;
    private List<ProductInOrder> productInOrders;

    private ObservableList<Product> productObservableList;
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
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));


        colIdInOrder.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProductNameInOrder.setCellValueFactory(new PropertyValueFactory<>("productName"));
        colDescriptionInOrder.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPriceInOrder.setCellValueFactory(new PropertyValueFactory<>("price"));
        colCountInOrder.setCellValueFactory(new PropertyValueFactory<>("countString"));

        tbProductInOrder.setEditable(true);
//        colCountInOrder.setMinWidth(200);
        colCountInOrder.setCellFactory(TextFieldTableCell.<ProductInOrder> forTableColumn());
        // Khi edit xong 1 ô ở cột FullName
        colCountInOrder.setOnEditCommit((TableColumn.CellEditEvent<ProductInOrder, String> event) -> {
            TablePosition<ProductInOrder, String> pos = event.getTablePosition();

            String newCountString = event.getNewValue();
            int newCount;
            try{
                newCount = Integer.valueOf(newCountString);
            }catch (NumberFormatException e){
                newCount = -1;
            }
            int row = pos.getRow();
            ProductInOrder productInOrder = event.getTableView().getItems().get(row);
            if (newCount <0){
                Messenger.erro(bundle.getString("txt_number_must_large_or_equal_0"));
                tbProductInOrder.getItems().set(row, productInOrder);
                return;
            }

            productInOrder.setCount(newCount);

            updateProductInOrderScreenData();
        });
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
    private void filter(String valor, ObservableList<Product> products) {

        FilteredList<Product> filteredList = new FilteredList<>(products, Product -> true);
        filteredList.setPredicate(product -> {

            if (valor == null || valor.isEmpty()) {
                return true;
            } else if (product.getProductName().toLowerCase().contains(valor.toLowerCase())) {
                return true;
            }

            return false;
        });

        SortedList<Product> dadosOrdenados = new SortedList<>(filteredList);
        dadosOrdenados.comparatorProperty().bind(tbProduct.comparatorProperty());
//    Filtro.mensagem(legenda, dadosOrdenados.size(), "Quantidade de estratigrafias encontradas");

        tbProduct.setItems(dadosOrdenados);
    }
    //TODO: call get product list api
    private void getProductList(){
        if (ConstantConfig.FAKE){

            new Thread(new Runnable() {
                public void run()
                {
                    try
                    {
                        Thread.sleep(10000);
                       DialogUtils.closeDialog();
                    }
                    catch ( Throwable th )
                    {

                    }
                }
            }).start();
            productList = FakeDataUtils.getFakeProductList();
            updateProductTable();
        }
    }

    @FXML
    void editOrder(ActionEvent event){
        productInOrders = createProductInOrderListFromSelectedProductList();
        if (productInOrders.isEmpty()){
            Messenger.info(bundle.getString("txt_please_choose_product"));
            return;
        }
        updateProductInOrderTable();
        lbTitle.setText(bundle.getString("txt_edit_order"));
        lbLegend.setText(bundle.getString("txt_total_price")+ ": 0 "+ bundle.getString("txt_vnd"));
        Model.setVisibility(false, btEditOrder, apProductList, txtSearch);
        Model.setVisibility(true, btBackToList, btCreateOrder, apEditOrderList);
    }

    @FXML
    void createOrder(ActionEvent event){
        //TODO: call create order api
        if (totalPrice<=0){
            Messenger.alert(bundle.getString("txt_please_choose_number_of_product_correctly"));
            return;
        }
        if (ConstantConfig.FAKE){
            Messenger.info(bundle.getString("msg_create_order_successfully") +"\""+ bundle.getString("txt_order_created") +"\"");
        }

        goToProductList(event);
    }

    @FXML
    void goToProductList(ActionEvent event){
        getProductList();
        lbTitle.setText(bundle.getString("txt_product_list"));
        lbLegend.setText(bundle.getString("txt_hold_ctrl_to_choose_items"));
        tbProduct.getSelectionModel().clearSelection();

        Model.setVisibility(true, btEditOrder, apProductList,txtSearch);
        Model.setVisibility(false, btBackToList, btCreateOrder, apEditOrderList);
    }
    List<ProductInOrder> createProductInOrderListFromSelectedProductList(){
        if (productList == null || productList.isEmpty()){
            return new ArrayList<>();
        }

        ObservableList<Product> selectedItems = tbProduct.getSelectionModel().getSelectedItems();

        ListIterator<Product> iterator = selectedItems.listIterator();
        List<ProductInOrder> selectedProducts = new ArrayList<>();
        while (iterator.hasNext()) {
            Product item = iterator.next();
            for (Product product: productList){
                if (product.getId().equals(item.getId())){
                    selectedProducts.add(product.convertToProductInOrder());
                }
            }
        }
        return selectedProducts;
    }
    private void updateProductInOrderScreenData(){
        //reset total for counting
        totalPrice = 0;
        ObservableList<ProductInOrder> orderObservableList = tbProductInOrder.getItems();

        ListIterator<ProductInOrder> iterator = orderObservableList.listIterator();
        while (iterator.hasNext()) {
            ProductInOrder item = iterator.next();
            totalPrice+= item.getPrice() * item.getCount();
        }
        //update list
        iterator.forEachRemaining(productInOrders::add);
        //update text
        lbLegend.setText(bundle.getString("txt_total_price")+ ": " + totalPrice +" "+ bundle.getString("txt_vnd"));
    }
}
