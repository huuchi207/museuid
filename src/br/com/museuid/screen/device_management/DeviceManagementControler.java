package br.com.museuid.screen.device_management;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.DeviceInfo;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.AddDeviceRequest;
import br.com.museuid.util.AppNoticeUtils;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NavigationUtils;
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

public class DeviceManagementControler extends AnchorPane {

    public TableColumn colDeviceName;
    public TextField txtDescription;
    private List<DeviceInfo> deviceInfoList = new ArrayList<>();
    private String selectedDeviceId = "0";

    @FXML
    private GridPane apAdd;
    @FXML
    private Label legenda;
    @FXML
    private Button btExclude;
    @FXML
    private TableView<DeviceInfo> tbDevice;
    @FXML
    private TextField txtDeviceName;
    @FXML
    private Button btSave;
    @FXML
    private TextField txtMacAddress;
    @FXML
    private ToggleGroup menu;
    @FXML
    private TableColumn colLocation;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableColumn colMacAddress;
    @FXML
    private Label lbTitle;
    @FXML
    private TableColumn colDescription;
    @FXML
    private Button btEdit;
//    @FXML
//    private TableColumn colId;
    @FXML
    private TextField txtLocation;
    @FXML
    private AnchorPane apEdit;
    @FXML
    private HBox boxEdit;
    private ResourceBundle bundle;

    public DeviceManagementControler() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("device_management.fxml"));
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
        txtMacAddress.setDisable(false);
        config(bundle.getString("txt_create_device"), bundle.getString("txt_required_fields"), 0);
        NavigationUtils.setVisibility(true, apAdd, btSave);
        NavigationUtils.setVisibility(false, boxEdit, apEdit, txtSearch);
        resetField();

        FieldViewUtils.setGlobalEventHandler(this, btSave);

    }

    @FXML
    void tbEdit(ActionEvent event) {
        config(bundle.getString("txt_edit_device_info"), "", 1);
        NavigationUtils.setVisibility(true, apEdit, boxEdit, txtSearch);
        NavigationUtils.setVisibility(false, btSave, apAdd);
        updateTable();
        FieldViewUtils.setGlobalEventHandler(this, null);


    }



    @FXML
    void save(ActionEvent event) {
        boolean isValid = FieldViewUtils.noEmpty(txtDeviceName, txtLocation, txtMacAddress);

        String deviceNameText = txtDeviceName.getText();
        String location = txtLocation.getText().trim();
        String macAddress = txtMacAddress.getText();

        if (isValid) {
            AppNoticeUtils.alert(bundle.getString("txt_please_enter_info"));
            return;
        }

        AppController.getInstance().showProgressDialog();
        if (selectedDeviceId.equals("0")) {
            AddDeviceRequest deviceInfo = new AddDeviceRequest(macAddress, deviceNameText, location);
            //TODO: insert device
            if (ConstantConfig.FAKE) {
                deviceInfoList.add(new DeviceInfo(deviceNameText, location, macAddress));
                updateTable();
                AppController.getInstance().hideProgressDialog();
                Messenger.info(bundle.getString("txt_operation_successful"));
            } else {
                ServiceBuilder.getApiService().addDevice(deviceInfo).enqueue(new BaseCallback<DeviceInfo>() {
                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        AppController.getInstance().hideProgressDialog();
                        Messenger.erro(errorMessage);
                    }

                    @Override
                    public void onSuccess(DeviceInfo data) {
                        getDeviceList();
                        AppController.getInstance().hideProgressDialog();
                        Messenger.info(BundleUtils.getResourceBundle().getString("txt_operation_successful"));
                        tbAdd(null);
                    }
                });
            }
        } else {
            //TODO: edit device
            DeviceInfo deviceInfo = new DeviceInfo(selectedDeviceId, deviceNameText, location, macAddress);
            if (ConstantConfig.FAKE) {
                deviceInfoList.remove(tbDevice.getSelectionModel().getSelectedItem());
                deviceInfoList.add(deviceInfo);
                updateTable();
                Messenger.info(bundle.getString("txt_operation_successful"));
            } else {
                ServiceBuilder.getApiService().updateDeviceInfo(deviceInfo).enqueue(new BaseCallback<Object>() {
                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        AppController.getInstance().hideProgressDialog();
                        Messenger.erro(errorMessage);
                    }

                    @Override
                    public void onSuccess(Object data) {
                        getDeviceList();
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
            DeviceInfo selectedItem = tbDevice.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {
                AppNoticeUtils.alert(bundle.getString("txt_please_choose_target"));
                return;
            }

            tbAdd(null);
            txtMacAddress.setDisable(true);
            txtDeviceName.setText(selectedItem.getName());
            txtLocation.setText(selectedItem.getLocation());
            txtMacAddress.setText(selectedItem.getMacaddress());

            lbTitle.setText(bundle.getString("txt_edit_device_info"));
            menu.selectToggle(menu.getToggles().get(1));

            selectedDeviceId = selectedItem.getId();

        } catch (NullPointerException ex) {
            AppNoticeUtils.alert(bundle.getString("txt_please_choose_target"));
        }
    }

    @FXML
    void exclude(ActionEvent event) {
        try {
            DeviceInfo selectedItem = tbDevice.getSelectionModel().getSelectedItem();

            if (selectedItem == null) {
                AppNoticeUtils.alert(bundle.getString("txt_please_choose_target"));
                return;
            }

            DialogUtils.ResponseMessage responseMessage = Messenger.confirm("Loại trừ " + selectedItem.getName() + " ?");

            if (responseMessage == DialogUtils.ResponseMessage.YES) {
                //TODO: call api delete user
                if (ConstantConfig.FAKE) {
                    Messenger.info(bundle.getString("txt_delete_successfully"));
                    deviceInfoList.remove(selectedItem);
                    updateTable();
                    tbDevice.getSelectionModel().clearSelection();
                    return;
                } else {
                    AppController.getInstance().showProgressDialog();
                    ServiceBuilder.getApiService().deleteDevice(selectedItem.getId()).enqueue(new BaseCallback<Object>() {
                        @Override
                        public void onError(String errorCode, String errorMessage) {
                            AppController.getInstance().hideProgressDialog();
                            Messenger.erro(errorMessage);
                        }

                        @Override
                        public void onSuccess(Object data) {
                            AppController.getInstance().hideProgressDialog();
                            Messenger.info(bundle.getString("txt_operation_successful"));
                            getDeviceList();
                            tbDevice.getSelectionModel().clearSelection();
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
        tbAdd(null);

//        Grupo.notEmpty(menu);
        getDeviceList();

        txtSearch.textProperty().addListener((obs, old, novo) -> {
            filter(novo, FXCollections.observableArrayList(deviceInfoList));
        });
    }

    /**
     * Configurações de tela, titulos e exibição de telas e menus
     */
    private void config(String tituloTela, String msg, int grupoMenu) {
        lbTitle.setText(tituloTela);
//        NavigationUtils.setVisibility(false, btExclude, btSave, btEdit, apAdd, apEdit, txtSearch);

        legenda.setText(msg);
        tbDevice.getSelectionModel().clearSelection();
        menu.selectToggle(menu.getToggles().get(grupoMenu));

        selectedDeviceId = "0";
    }

    /**
     * Sincronizar dados com banco de dados
     */
    private void getDeviceList() {
//    deviceInfoList = ControleDAO.getBanco().getEstratigrafiaDAO().listar();
        //TODO: call list user api and set interval
        if (ConstantConfig.FAKE) {
            if (deviceInfoList == null) {
//                deviceInfoList = FakeDataUtils.getFakeProductList();
            }
            updateTable();
        } else {
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
                    deviceInfoList = data;
                    updateTable();
                }
            });
        }
    }

    /**
     * Mapear dados objetos para inserção dos dados na updateTable
     */
    private void updateTable() {
        ObservableList data = FXCollections.observableArrayList(deviceInfoList);

//        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLocation.setCellValueFactory(new PropertyValueFactory<>("location"));
        colDeviceName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colMacAddress.setCellValueFactory(new PropertyValueFactory<>("macaddress"));

        tbDevice.setItems(data);
    }

    /**
     * FieldViewUtils de pesquisar para filtrar dados na updateTable
     */
    private void filter(String valor, ObservableList<DeviceInfo> deviceInfos) {

        FilteredList<DeviceInfo> filtedList = new FilteredList<>(deviceInfos, deviceInfo -> true);
        filtedList.setPredicate(deviceInfo -> {

            if (valor == null || valor.isEmpty()) {
                return true;
            } else if (deviceInfo.getName().toLowerCase().contains(valor.toLowerCase())) {
                return true;
            } else if (deviceInfo.getMacaddress().toLowerCase().contains(valor.toLowerCase())) {
                return true;
            }
            return false;
        });

        SortedList<DeviceInfo> dadosOrdenados = new SortedList<>(filtedList);
        dadosOrdenados.comparatorProperty().bind(tbDevice.comparatorProperty());
//    FilterUtils.mensage(legenda, dadosOrdenados.size(), "Quantidade de Estratigrafias encontradas");

        tbDevice.setItems(dadosOrdenados);
    }

    /**
     * Limpar campos textfield cadastro de Estratigrafias
     */
    private void resetField() {
        FieldViewUtils.resetField(txtDeviceName, txtLocation);
        FieldViewUtils.resetField(txtMacAddress);
    }
}
