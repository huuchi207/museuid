package br.com.museuid.view.employee_management;

import java.util.List;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.model.Employee;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Dialogo;
import br.com.museuid.util.FileUtils;
import br.com.museuid.util.Filtro;
import br.com.museuid.util.Grupo;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.Model;
import br.com.museuid.util.Nota;
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

public class EmployeeManagementControler extends AnchorPane{

  public TableColumn colUserName;
  public TextArea txtAddress;
  private List<Employee> employeeList;
  private String selectedEmployeeId = "0";

  @FXML
  private GridPane telaCadastro;
  @FXML
  private Label legenda;
  @FXML
  private Button btExclude;
  @FXML
  private TableView<Employee> tbUser;
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
  private TextField txtPesquisar;
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

  public EmployeeManagementControler() {
    try {
      FXMLLoader fxml = new FXMLLoader(getClass().getResource("employee_management.fxml"));

      fxml.setRoot(this);
      fxml.setController(this);
      fxml.load();

    } catch (Exception ex) {
      Messenger.erro("Erro ao carregar tela estratigrafia! \n" + ex);
    }
  }

  @FXML
  void tbAdd(ActionEvent event) {
    config("Cadastrar Estratigrafia", "Các trường bắt buộc", 0);
    Model.visualize(true, telaCadastro, btSave);
    resetField();
  }

  @FXML
  void tbEdit(ActionEvent event) {
    config("Editar Estratigrafia", "Quantidade de estratigrafias encontradas", 1);
    Model.visualize(true, apEdit, btEdit, txtPesquisar);
    updateTable();
  }

  @FXML
  void tbExclude(ActionEvent event) {
    config("Excluir Estratigrafia", "Quantidade de estratigrafias encontradas", 2);
    Model.visualize(true, apEdit, btExclude, txtPesquisar);
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
      Nota.alert("Vui lòng nhập đủ thông tin!");
    }
//    else if (ControleDAO.getBanco().getEstratigrafiaDAO().isEstratigrafia(formacao, selectedEmployeeId)) {
//      Nota.alert("Formação já cadastrada!");
//    }
    else {
      Employee employee = new Employee(selectedEmployeeId, userName, name, phoneNumber, address);

      if (selectedEmployeeId .equals("0")) {
        //TODO: insert user
//        ControleDAO.getBanco().getEstratigrafiaDAO().inserir(estratigrafia);
        if (ConstantConfig.FAKE){
          employeeList.add(employee);
          updateTable();
          Messenger.info("Thao tác thành công!");
        }
      } else {
        //TODO: edit user
//        ControleDAO.getBanco().getEstratigrafiaDAO().editar(estratigrafia);
        if (ConstantConfig.FAKE){
          employeeList.remove(tbUser.getSelectionModel().getSelectedItem());
          employeeList.add(employee);
          updateTable();
          Messenger.info("Thao tác thành công!");
        }
      }

      tbAdd(null);
      synchronizeBase();
    }
  }

  @FXML
  void edit(ActionEvent event) {
    try {
      Employee selectedEmployee = tbUser.getSelectionModel().getSelectedItem();

      tbAdd(null);

      txtUserName.setText(selectedEmployee.getUserName());
      txtName.setText(selectedEmployee.getName());
      txtPhoneNumber.setText(selectedEmployee.getPhoneNumber());

      lbTitle.setText("Editar Estratigrafia");
      menu.selectToggle(menu.getToggles().get(1));

      selectedEmployeeId = selectedEmployee.getId();

    } catch (NullPointerException ex) {
      Nota.alert("Selecione um estratigrafia na updateTable para edição!");
    }
  }

  @FXML
  void exclude(ActionEvent event) {
    try {
      Employee selectedEmployee = tbUser.getSelectionModel().getSelectedItem();

      Dialogo.Resposta response = Messenger.confirmar("Excluir estratigrafia " + selectedEmployee.getUserName() + " ?");

      if (response == Dialogo.Resposta.YES) {
//        ControleDAO.getBanco().getEstratigrafiaDAO().excluir(selectedEmployee.getId());
        //TODO: call api delete user
        if(ConstantConfig.FAKE){
          Messenger.info("Xóa thành công");
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
      Messenger.alerta("Selecione estratigrafia na updateTable para exclusão!");
    }
  }

  @FXML
  public void initialize() {
    tbAdd(null);

    Grupo.notEmpty(menu);
    synchronizeBase();

//        txtPesquisar.textProperty().addListener((obs, old, novo) -> {
//            filter(novo, FXCollections.observableArrayList(employeeList));
//        });
  }

  /**
   * Configurações de tela, titulos e exibição de telas e menus
   */
  private void config(String tituloTela, String msg, int grupoMenu) {
    lbTitle.setText(tituloTela);
    Model.visualize(false, btExclude, btSave, btEdit, telaCadastro, apEdit, txtPesquisar);

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
    if(ConstantConfig.FAKE && employeeList!= null){

    }else
      employeeList = FileUtils.getFakeEmployeeList();
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
  private void filter(String valor, ObservableList<Employee> employees) {

    FilteredList<Employee> dadosFiltrados = new FilteredList<>(employees, estratigrafia -> true);
    dadosFiltrados.setPredicate(employee -> {

      if (valor == null || valor.isEmpty()) {
        return true;
      } else if (employee.getName().toLowerCase().startsWith(valor.toLowerCase())) {
        return true;
      } else if (employee.getUserName().toLowerCase().startsWith(valor.toLowerCase())) {
        return true;
      }

      return false;
    });

    SortedList<Employee> dadosOrdenados = new SortedList<>(dadosFiltrados);
    dadosOrdenados.comparatorProperty().bind(tbUser.comparatorProperty());
    Filtro.mensagem(legenda, dadosOrdenados.size(), "Quantidade de estratigrafias encontradas");

    tbUser.setItems(dadosOrdenados);
  }

  /**
   * Limpar campos textfield cadastro de estratigrafias
   */
  private void resetField() {
    FieldViewUtils.resetField(txtUserName, txtName);
    FieldViewUtils.resetField(txtPhoneNumber);
  }
}
