package br.com.museuid.screen.user_management;

import br.com.museuid.Constants;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.customview.CustomListCellComboBox;
import br.com.museuid.customview.PasswordDialog;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.sample.SampleCallback;
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
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserManagementControler extends AnchorPane {
    @FXML
    private TableColumn colUserName;

    private List<UserDTO> listUser = new ArrayList<>();
    private String selectedUserId = "0";

    @FXML
    private GridPane apAdd;
    @FXML
    private Label legenda;
    @FXML
    private Button btExclude;
    @FXML
    private TableView<UserDTO> tbUser;

    @FXML
    private Button btSave;

    @FXML
    private ToggleGroup menu;
    @FXML
    private TableColumn colName;
    @FXML
    private TextField txtSearch;
    @FXML
    private TableColumn colPhoneNumber;
    @FXML
    private Label lbTitle;
    @FXML
    private TableColumn colAddress;
    @FXML
    private Button btEdit;
    @FXML
    private TableColumn colEmail;
    @FXML
    private AnchorPane apEdit;
    @FXML
    private Button btResetPassword;
    private ResourceBundle bundle;

    @FXML
    private TextField txtUserName;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtAddress;

    @FXML
    private TableColumn colUserRole;
    @FXML
    private ComboBox<UserDTO.UserRole> cbUserRole;
    @FXML
    private HBox boxEdit;

    public UserManagementControler() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("user_management.fxml"));
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
        txtUserName.setDisable(false);
        config(bundle.getString("txt_create_user"), bundle.getString("txt_required_fields"), 0);
        NavigationUtils.setVisibility(true, apAdd, btSave);
        NavigationUtils.setVisibility(false, apEdit, boxEdit, txtSearch);
        resetField();
    }

    @FXML
    void tbEdit(ActionEvent event) {
        tbUser.getSelectionModel().clearSelection();

        config(bundle.getString("txt_edit_user_info"), "", 1);
        NavigationUtils.setVisibility(true, apEdit, txtSearch, boxEdit);
        NavigationUtils.setVisibility(false, apAdd, btSave);
        updateTable();
    }


    @FXML
    void save(ActionEvent event) {

        boolean isValid = FieldViewUtils.noEmpty(txtUserName, txtName);

        String userName = txtUserName.getText();
        String name = txtName.getText().trim();
        String phoneNumber = txtPhoneNumber.getText();
        String address = txtAddress.getText();
        String email = txtEmail.getText();
        String role = cbUserRole.getValue().name();
        if (isValid) {
            NoticeUtils.alert(bundle.getString("txt_please_enter_info"));
            return;
        }
        PasswordDialog pd = new PasswordDialog();
        Optional<String> result = pd.showAndWait();
        result.ifPresent(password -> {
            if (selectedUserId.equals("0")) {
                UserDTO userDTO = new UserDTO(userName, name, phoneNumber, address, email, role);
                userDTO.setPassword(password);
                if (ConstantConfig.FAKE) {
                    listUser.add(userDTO);
                    updateTable();
                    Messenger.info(bundle.getString("txt_operation_successful"));
                } else {
                    AppController.getInstance().showProgressDialog();
                    ServiceBuilder.getApiService().addUser(userDTO).enqueue(new BaseCallback<UserDTO>() {
                        @Override
                        public void onError(String errorCode, String errorMessage) {
                            AppController.getInstance().hideProgressDialog();
                            Messenger.erro(errorMessage);
                        }

                        @Override
                        public void onSuccess(UserDTO data) {
                            AppController.getInstance().hideProgressDialog();
                            Messenger.info("Người dùng " + data.getUsername() + " vừa được tạo với mật khẩu là: " + Constants.DEFAULT_PASSWORD);
                            getListUser();
                        }
                    });
                }
            } else {
                if (ConstantConfig.FAKE) {
//                    listUser.remove(tbUser.getSelectionModel().getSelectedItem());
//                    listUser.add(u);
                    updateTable();
                    Messenger.info(bundle.getString("txt_operation_successful"));
                } else {
                    AppController.getInstance().showProgressDialog();
                    UserDTO userDTO = tbUser.getSelectionModel().getSelectedItem().copy();
                    userDTO.setUserid(userDTO.getId());
                    userDTO.setUsername(userName);
                    userDTO.setFullname(name);
                    userDTO.setPhone(phoneNumber);
                    userDTO.setAddress(address);
                    userDTO.setEmail(email);
                    userDTO.setRole(role);
                    userDTO.setPassword(password);
                    ServiceBuilder.getApiService().changeUserInfo(userDTO).enqueue(new BaseCallback<UserDTO>() {
                        @Override
                        public void onError(String errorCode, String errorMessage) {
                            AppController.getInstance().hideProgressDialog();
                            Messenger.erro(errorMessage);
                        }

                        @Override
                        public void onSuccess(UserDTO data) {
                            AppController.getInstance().hideProgressDialog();
                            Messenger.info(bundle.getString("txt_operation_successful"));
                            getListUser();
                        }
                    });
                }

            }
        });
    }

    @FXML
    void edit(ActionEvent event) {
        try {
            UserDTO selectedUser = tbUser.getSelectionModel().getSelectedItem();

            if (selectedUser == null) {
                NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
                return;
            }

            tbAdd(null);
            txtUserName.setDisable(true);

            txtUserName.setText(selectedUser.getUsername());
            txtName.setText(selectedUser.getFullname());
            txtPhoneNumber.setText(selectedUser.getPhone());
            txtAddress.setText(selectedUser.getAddress());
            txtEmail.setText(selectedUser.getEmail());
            cbUserRole.setValue(UserDTO.UserRole.valueOf(selectedUser.getRole()));

            lbTitle.setText(bundle.getString("txt_edit_user_info"));
            menu.selectToggle(menu.getToggles().get(1));

            selectedUserId = selectedUser.getId();

        } catch (NullPointerException ex) {
            NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
        }
    }

    @FXML
    void exclude(ActionEvent event) {
        try {
            UserDTO selectedUser = tbUser.getSelectionModel().getSelectedItem();

            if (selectedUser == null) {
                NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
                return;
            }

            DialogUtils.ResponseMessage responseMessage = Messenger.confirm("Xóa " + selectedUser.getUsername() + " ?");

            if (responseMessage == DialogUtils.ResponseMessage.YES) {
                if (ConstantConfig.FAKE) {
                    Messenger.info(bundle.getString("txt_delete_successfully"));
                    listUser.remove(selectedUser);
                    updateTable();
                    tbUser.getSelectionModel().clearSelection();
                    return;
                } else {
                    AppController.getInstance().showProgressDialog();
                    ServiceBuilder.getApiService().deleteUser(selectedUser.getId()).enqueue(new BaseCallback<Object>() {
                        @Override
                        public void onError(String errorCode, String errorMessage) {
                            AppController.getInstance().hideProgressDialog();
                            Messenger.erro(errorMessage);
                        }

                        @Override
                        public void onSuccess(Object data) {
                            AppController.getInstance().hideProgressDialog();
                            getListUser();
                        }
                    });
                }
            }

            tbUser.getSelectionModel().clearSelection();

        } catch (NullPointerException ex) {
            Messenger.alert(bundle.getString("txt_please_choose_target"));
        }
    }

    @FXML
    public void initialize() {
        tbAdd(null);

        getListUser();
        //init combobox
        ObservableList<UserDTO.UserRole> listRole = FXCollections.observableArrayList(UserDTO.UserRole.getListUserRole());
        Callback<ListView<UserDTO.UserRole>, ListCell<UserDTO.UserRole>> cellFactory = new Callback<ListView<UserDTO.UserRole>, ListCell<UserDTO.UserRole>>() {
            @Override
            public ListCell<UserDTO.UserRole> call(ListView<UserDTO.UserRole> param) {
                return new CustomListCellComboBox<>();
            }
        };
        cbUserRole.setCellFactory(cellFactory);
        cbUserRole.setButtonCell(cellFactory.call(null));
        cbUserRole.valueProperty().addListener(new ChangeListener<UserDTO.UserRole>() {
            @Override
            public void changed(ObservableValue<? extends UserDTO.UserRole> observable, UserDTO.UserRole oldValue, UserDTO.UserRole newValue) {
                cbUserRole.setValue(newValue);
            }
        });
        ComboUtils.popular(cbUserRole, listRole);

        txtSearch.textProperty().addListener((obs, old, novo) -> {
            filter(novo, FXCollections.observableArrayList(listUser));
        });
    }

    /**
     * Configurações de tela, titulos e exibição de telas e menus
     */
    private void config(String tituloTela, String msg, int grupoMenu) {
        lbTitle.setText(tituloTela);
//        NavigationUtils.setVisibility(false, btExclude, btSave, btEdit, apAdd, apEdit, txtSearch);

        legenda.setText(msg);

        menu.selectToggle(menu.getToggles().get(grupoMenu));

        selectedUserId = "0";
    }

    /**
     * Sincronizar dados com banco de dados
     */
    private void getListUser() {
        //TODO: call list user api
        if (ConstantConfig.FAKE) {
            if (listUser == null) {
//                listUser = FakeDataUtils.getFakeEmployeeList();
            }
            AppController.getInstance().showProgressDialog();
            ServiceBuilder.getApiService().getSample("12,32,15,37,10", "b1412ab2fc899acb1e7612034bfdf412").enqueue(new SampleCallback<Item>() {
                @Override
                public void onError(String errorCode, String errorMessage) {
                    AppController.getInstance().hideProgressDialog();
                    Messenger.erro(errorMessage);
                }

                @Override
                public void onSuccess(List<Item> data) {
                    AppController.getInstance().hideProgressDialog();
                }
            });
        } else {
            AppController.getInstance().showProgressDialog();
            ServiceBuilder.getApiService().getListUser().enqueue(new BaseCallback<List<UserDTO>>() {
                @Override
                public void onError(String errorCode, String errorMessage) {
                    AppController.getInstance().hideProgressDialog();
                    Messenger.erro(errorMessage);
                }

                @Override
                public void onSuccess(List<UserDTO> data) {
                    AppController.getInstance().hideProgressDialog();
                    if (data != null && !data.isEmpty()) {
                        for (UserDTO userDTO : data) {
                            userDTO.setRoleText(UserDTO.UserRole.valueOf(userDTO.getRole()).toString());
                        }
                    }
                    listUser = data;
                    updateTable();
                }
            });
        }
    }

    /**
     * Mapear dados objetos para inserção dos dados na updateTable
     */
    private void updateTable() {
        ObservableList data = FXCollections.observableArrayList(listUser);

        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("username"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colUserRole.setCellValueFactory(new PropertyValueFactory<>("roleText"));

        tbUser.setItems(data);
    }

    /**
     * FieldViewUtils de pesquisar para filtrar dados na updateTable
     */
    private void filter(String valor, ObservableList<UserDTO> userDTOS) {

        FilteredList<UserDTO> filtedList = new FilteredList<>(userDTOS, userDTO -> true);
        filtedList.setPredicate(user -> {

            if (valor == null || valor.isEmpty()) {
                return true;
            } else if (user.getFullname().toLowerCase().contains(valor.toLowerCase())) {
                return true;
            }

            return false;
        });

        SortedList<UserDTO> dadosOrdenados = new SortedList<>(filtedList);
        dadosOrdenados.comparatorProperty().bind(tbUser.comparatorProperty());
//    FilterUtils.mensage(legenda, dadosOrdenados.size(), "Quantidade de Estratigrafias encontradas");

        tbUser.setItems(dadosOrdenados);
    }

    /**
     * Limpar campos textfield cadastro de Estratigrafias
     */
    private void resetField() {
        FieldViewUtils.resetField(txtUserName, txtName);
        FieldViewUtils.resetField(txtPhoneNumber, txtAddress, txtEmail);
        cbUserRole.getSelectionModel().select(UserDTO.UserRole.EMPLOYEE);
    }

    public void resetPassword() {
        UserDTO selected = tbUser.getSelectionModel().getSelectedItem();
        if (selected == null) {
            Messenger.erro(BundleUtils.getResourceBundle().getString("txt_please_choose_target"));
            return;
        }
        DialogUtils.ResponseMessage responseMessage = DialogUtils.mensageConfirmer(bundle.getString("txt_notice"),
            bundle.getString("txt_are_you_sure_want_to_reset_password_of_this_user"));
        if (responseMessage == DialogUtils.ResponseMessage.YES) {
            //TODO: call api reset password
            if (ConstantConfig.FAKE) {
                Messenger.info(bundle.getString("txt_reset_password_successfully"));
                return;
            } else {
                UserDTO userDTO = new UserDTO();
                userDTO.setUserid(selected.getId());
                AppController.getInstance().showProgressDialog();
                ServiceBuilder.getApiService().resetPasswordForCustomer(userDTO).enqueue(new BaseCallback<UserDTO>() {
                    @Override
                    public void onError(String errorCode, String errorMessage) {
                        AppController.getInstance().hideProgressDialog();
                        Messenger.erro(errorMessage);
                    }

                    @Override
                    public void onSuccess(UserDTO data) {
                        AppController.getInstance().hideProgressDialog();
                        Messenger.info("Mật khẩu được reset về " + Constants.DEFAULT_PASSWORD);
                    }
                });
            }

        }
    }
}
