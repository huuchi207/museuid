package br.com.museuid.controller;

import br.com.museuid.banco.controle.ControleDAO;
import br.com.museuid.model.Local;
import br.com.museuid.model.Setor;
import br.com.museuid.util.*;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;

public class LocalController extends AnchorPane {

    private List<Local> listaLocal;
    private int idLocal = 0;

    @FXML
    private GridPane telaCadastro;
    @FXML
    private Label legenda;
    @FXML
    private Button btExcluir;
    @FXML
    private TextField txtNome;
    @FXML
    private Button btSalvar;
    @FXML
    private TextArea txtDescricao;
    @FXML
    private ToggleGroup menu;
    @FXML
    private TableView<Local> tbLocal;
    @FXML
    private TableColumn colSetor;
    @FXML
    private TextField txtPesquisar;
    @FXML
    private Label lbTitulo;
    @FXML
    private TableColumn colDescricao;
    @FXML
    private Button btEditar;
    @FXML
    private TableColumn colId;
    @FXML
    private ComboBox<Setor> cbSetor;
    @FXML
    private TableColumn colLocal;
    @FXML
    private AnchorPane telaEdicao;

    public LocalController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("../view/local.fxml"));

            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();

        } catch (IOException ex) {
            Messenger.erro("Erro ao carregar tela local da local! \n" + ex);
            ex.printStackTrace();
        }
    }

    @FXML
    void telaCadastro(ActionEvent event) {
        configTela("Cadastrar Local", "Các trường bắt buộc", 0);
        Model.visualize(true, telaCadastro, btSalvar);
        limpar();
    }

    @FXML
    void telaEdicao(ActionEvent event) {
        configTela("Editar Local", "Quantidade de locais encontrados", 1);
        Model.visualize(true, telaEdicao, btEditar, txtPesquisar);
        tabela();
    }

    @FXML
    void telaExcluir(ActionEvent event) {
        configTela("Excluir Local", "Quantidade de locais encontrados", 2);
        Model.visualize(true, telaEdicao, btExcluir, txtPesquisar);
        tabela();
    }

    @FXML
    void salvar(ActionEvent event) {

        boolean vazio = FieldViewUtils.noEmpty(txtNome);

        String nome = txtNome.getText();
        String descricao = txtDescricao.getText();

        Setor setor = cbSetor.getValue();

        if (vazio) {
            NoticeUtils.alert("Vui lòng nhập đủ thông tin!");
        } else if (cbSetor.getValue() == null) {
            NoticeUtils.alert("Setor do local não encontrada!");
        } else {
            Local local = new Local(idLocal, nome, descricao, setor);

            if (idLocal == 0) {
                ControleDAO.getBanco().getLocalDAO().inserir(local);
                Messenger.info("Local cadastrada com sucesso!");
            } else {
                ControleDAO.getBanco().getLocalDAO().editar(local);
                Messenger.info("Local atualizada com sucesso!");
            }

            telaCadastro(null);
            sincronizarBase();
        }
    }

    @FXML
    void editar(ActionEvent event) {
        try {
            Local local = tbLocal.getSelectionModel().getSelectedItem();
            local.getClass();

            telaCadastro(null);

            txtNome.setText(local.getNome());
            cbSetor.setValue(local.getSetor());
            txtDescricao.setText(local.getDescricao());

            lbTitulo.setText("Editar Local");
            menu.selectToggle(menu.getToggles().get(1));

            idLocal = local.getId();

        } catch (NullPointerException ex) {
            NoticeUtils.alert("Selecione um local na tabela para edição!");
        }
    }

    @FXML
    void excluir(ActionEvent event) {
        try {
            Local local = tbLocal.getSelectionModel().getSelectedItem();

            DialogUtils.Resposta response = Messenger.confirm("Excluir local " + local.getNome() + " ?");

            if (response == DialogUtils.Resposta.YES) {
                ControleDAO.getBanco().getLocalDAO().excluir(local.getId());
                sincronizarBase();
                tabela();
            }

            tbLocal.getSelectionModel().clearSelection();

        } catch (NullPointerException ex) {
            Messenger.alert("Selecione local na tabela para exclusão!");
        }
    }

    @FXML
    public void initialize() {
        telaCadastro(null);

        Grupo.notEmpty(menu);
        sincronizarBase();
        combos();

        txtPesquisar.textProperty().addListener((obs, old, novo) -> {
            filtro(novo, FXCollections.observableArrayList(listaLocal));
        });
    }

    /**
     * Configurações de tela, titulos e exibição de telas e menus
     */
    private void configTela(String tituloTela, String msg, int grupoMenu) {
        lbTitulo.setText(tituloTela);
        Model.visualize(false, btExcluir, btSalvar, btEditar, telaCadastro, telaEdicao, txtPesquisar);

        legenda.setText(msg);//mensagem legenda
        tbLocal.getSelectionModel().clearSelection();
        menu.selectToggle(menu.getToggles().get(grupoMenu));

        idLocal = 0;
    }

    /**
     * Preencher combos tela
     */
    public void combos() {
        Combo.popular(cbSetor, ControleDAO.getBanco().getSetorDAO().combo());
    }

    /**
     * Sincronizar dados com banco de dados
     */
    private void sincronizarBase() {
        listaLocal = ControleDAO.getBanco().getLocalDAO().listar();
    }

    /**
     * Mapear dados objetos para inserção dos dados na tabela
     */
    private void tabela() {
        ObservableList data = FXCollections.observableArrayList(listaLocal);

        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colSetor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Local, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Local, String> obj) {
                return new SimpleStringProperty(obj.getValue().getSetor().getNome());
            }
        });
        colDescricao.setCellValueFactory(new PropertyValueFactory<>("descricao"));

        tbLocal.setItems(data);
    }

    /**
     * FieldViewUtils de pesquisar para filtrar dados na tabela
     */
    private void filtro(String valor, ObservableList<Local> listaLocal) {

        FilteredList<Local> dadosFiltrados = new FilteredList<>(listaLocal, local -> true);
        dadosFiltrados.setPredicate(local -> {

            if (valor == null || valor.isEmpty()) {
                return true;
            } else if (local.getNome().toLowerCase().startsWith(valor.toLowerCase())) {
                return true;
            } else if (local.getSetor().getNome().toLowerCase().startsWith(valor.toLowerCase())) {
                return true;
            }

            return false;
        });

        SortedList<Local> dadosOrdenados = new SortedList<>(dadosFiltrados);
        dadosOrdenados.comparatorProperty().bind(tbLocal.comparatorProperty());
        Filtro.mensagem(legenda, dadosOrdenados.size(), "Quantidade de locais encontradas");

        tbLocal.setItems(dadosOrdenados);
    }

    /**
     * Limpar campos textfield cadastro de coleções
     */
    private void limpar() {
        FieldViewUtils.resetField(txtNome);
        FieldViewUtils.resetField(txtDescricao);
    }

}
