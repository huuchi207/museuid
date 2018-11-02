package br.com.museuid.view.employee_management;

import java.util.List;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.sample.Item;
import br.com.museuid.dto.EmployeeDTO;
import br.com.museuid.service.remote.sample.SampleCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.DialogUtils;
import br.com.museuid.util.FileUtils;
import br.com.museuid.util.Grupo;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.Model;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class EmployeeManagementControler extends AnchorPane {

    public TableColumn colUserName;
    public TextArea txtAddress;
    private List<EmployeeDTO> employeeList;
    private String selectedEmployeeId = "0";

    @FXML
    private GridPane apAdd;
    @FXML
    private Label legenda;
    @FXML
    private Button btExclude;
    @FXML
    private TableView<EmployeeDTO> tbUser;
    @FXML
    private TextField txtUserName;
    @FXML
    private Button btSave;
    @FXML
    private TextArea txtPhoneNumber;
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
    private TableColumn colId;
    @FXML
    private TextField txtName;
    @FXML
    private AnchorPane apEdit;
    private ResourceBundle bundle;

    public EmployeeManagementControler() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("employee_management.fxml"));

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
        config(bundle.getString("txt_create_employee"), bundle.getString("txt_required_fields"), 0);
        Model.setVisibility(true, apAdd, btSave);
        resetField();
    }

    @FXML
    void tbEdit(ActionEvent event) {
        config(bundle.getString("txt_edit_employee_info"), "", 1);
        Model.setVisibility(true, apEdit, btEdit, txtSearch);
        updateTable();
    }

    @FXML
    void tbExclude(ActionEvent event) {
        config(bundle.getString("txt_employee_deleted"), "", 2);
        Model.setVisibility(true, apEdit, btExclude, txtSearch);
        updateTable();
    }

    @FXML
    void save(ActionEvent event) {

        boolean isValid = FieldViewUtils.noEmpty(txtUserName, txtName);

        String userName = txtUserName.getText();
        String name = txtName.getText().replaceAll(" ", "").trim();
        String phoneNumber = txtPhoneNumber.getText();
        String address = txtAddress.getText();
        if (isValid) {
            NoticeUtils.alert(bundle.getString("txt_please_enter_info"));
        }
//    else if (ControleDAO.getBanco().getEstratigrafiaDAO().isEstratigrafia(formacao, selectedEmployeeId)) {
//      NoticeUtils.alert("Formação já cadastrada!");
//    }
        else {
            EmployeeDTO employee = new EmployeeDTO(selectedEmployeeId, userName, name, phoneNumber, address);

            if (selectedEmployeeId.equals("0")) {
                //TODO: insert user
//        ControleDAO.getBanco().getEstratigrafiaDAO().inserir(estratigrafia);
                if (ConstantConfig.FAKE) {
                    employeeList.add(employee);
                    updateTable();
                    Messenger.info(bundle.getString("txt_operation_successful"));
                }
            } else {
                //TODO: edit user
//        ControleDAO.getBanco().getEstratigrafiaDAO().editar(estratigrafia);
                if (ConstantConfig.FAKE) {
                    employeeList.remove(tbUser.getSelectionModel().getSelectedItem());
                    employeeList.add(employee);
                    updateTable();
                    Messenger.info(bundle.getString("txt_operation_successful"));
                }
            }

            tbAdd(null);
            synchronizeBase();
        }
    }

    @FXML
    void edit(ActionEvent event) {
        try {
            EmployeeDTO selectedEmployee = tbUser.getSelectionModel().getSelectedItem();

            if (selectedEmployee == null) {
                NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
                return;
            }

            tbAdd(null);

            txtUserName.setText(selectedEmployee.getUserName());
            txtName.setText(selectedEmployee.getName());
            txtPhoneNumber.setText(selectedEmployee.getPhoneNumber());
            txtAddress.setText(selectedEmployee.getAddress());

            lbTitle.setText(bundle.getString("txt_edit_employee_info"));
            menu.selectToggle(menu.getToggles().get(1));

            selectedEmployeeId = selectedEmployee.getId();

        } catch (NullPointerException ex) {
            NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
        }
    }

    @FXML
    void exclude(ActionEvent event) {
        try {
            EmployeeDTO selectedEmployee = tbUser.getSelectionModel().getSelectedItem();

            if (selectedEmployee == null) {
                NoticeUtils.alert(bundle.getString("txt_please_choose_target"));
                return;
            }

            DialogUtils.ResponseMessage responseMessage = Messenger.confirm("Loại trừ " + selectedEmployee.getUserName() + " ?");

            if (responseMessage == DialogUtils.ResponseMessage.YES) {
//        ControleDAO.getBanco().getEstratigrafiaDAO().excluir(selectedEmployee.getId());
                //TODO: call api delete user
                if (ConstantConfig.FAKE) {
                    Messenger.info(bundle.getString("txt_delete_successfully"));
                    employeeList.remove(selectedEmployee);
                    updateTable();
                    tbUser.getSelectionModel().clearSelection();
                    return;
                }
                synchronizeBase();
                updateTable();
            }

            tbUser.getSelectionModel().clearSelection();

        } catch (NullPointerException ex) {
            Messenger.alert(bundle.getString("txt_please_choose_target"));
        }
    }

    @FXML
    public void initialize() {
        tbAdd(null);

        Grupo.notEmpty(menu);
        synchronizeBase();

        txtSearch.textProperty().addListener((obs, old, novo) -> {
            filter(novo, FXCollections.observableArrayList(employeeList));
        });
    }

    /**
     * Configurações de tela, titulos e exibição de telas e menus
     */
    private void config(String tituloTela, String msg, int grupoMenu) {
        lbTitle.setText(tituloTela);
        Model.setVisibility(false, btExclude, btSave, btEdit, apAdd, apEdit, txtSearch);

        legenda.setText(msg);
        tbUser.getSelectionModel().clearSelection();
        menu.selectToggle(menu.getToggles().get(grupoMenu));

        selectedEmployeeId = "0";
    }

    /**
     * Sincronizar dados com banco de dados
     */
    private void synchronizeBase() {
//    employeeList = ControleDAO.getBanco().getEstratigrafiaDAO().listar();
        //TODO: call list user api and set interval
        if (ConstantConfig.FAKE) {
            if (employeeList == null) {
                employeeList = FileUtils.getFakeEmployeeList();
            }
            ServiceBuilder.getApiService().getSample("12,32,15,37,10", "b1412ab2fc899acb1e7612034bfdf412").enqueue(new SampleCallback<Item>() {
                @Override
                public void onError(String errorCode, String errorMessage) {

                }

                @Override
                public void onSuccess(List<Item> data) {

                }
            });
        }
    }

    /**
     * Mapear dados objetos para inserção dos dados na updateTable
     */
    private void updateTable() {
        ObservableList data = FXCollections.observableArrayList(employeeList);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));

        tbUser.setItems(data);
    }

    /**
     * FieldViewUtils de pesquisar para filtrar dados na updateTable
     */
    private void filter(String valor, ObservableList<EmployeeDTO> employees) {

        FilteredList<EmployeeDTO> filtedList = new FilteredList<>(employees, employee -> true);
        filtedList.setPredicate(employee -> {

            if (valor == null || valor.isEmpty()) {
                return true;
            } else if (employee.getName().toLowerCase().contains(valor.toLowerCase())) {
                return true;
            } else if (employee.getUserName().toLowerCase().contains(valor.toLowerCase())) {
                return true;
            }

            return false;
        });

        SortedList<EmployeeDTO> dadosOrdenados = new SortedList<>(filtedList);
        dadosOrdenados.comparatorProperty().bind(tbUser.comparatorProperty());
//    Filtro.mensagem(legenda, dadosOrdenados.size(), "Quantidade de estratigrafias encontradas");

        tbUser.setItems(dadosOrdenados);
    }

    /**
     * Limpar campos textfield cadastro de estratigrafias
     */
    private void resetField() {
        FieldViewUtils.resetField(txtUserName, txtName);
        FieldViewUtils.resetField(txtPhoneNumber, txtAddress);
    }
}
