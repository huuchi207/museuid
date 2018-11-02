package br.com.museuid.view.app;

import br.com.museuid.app.App;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.util.*;
import br.com.museuid.app.Login;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AppController {

    private static AppController instance;
    @FXML
    public ToggleButton btEmployeeManagement;
    public VBox boxEmployee;
    public ToggleButton btAppManagementSection;
    public ToggleGroup gAppManagementSection;
    public VBox boxOrderSection;
    public ToggleButton btCreateOrder;
    public ToggleButton btMyHandlingOrder;
    public VBox boxStatistic;

    @FXML
    private AnchorPane boxContainer;
    @FXML
    private VBox boxEmprestimo;
    @FXML
    private VBox boxVisitas;
    @FXML
    private VBox boxLocalizacao;
    @FXML
    private VBox boxUtilitarios;
    @FXML
    private VBox boxNotas;
    @FXML
    private VBox boxCatalogacao;
    @FXML
    private Label lbUser;
    @FXML
    private Label lbMensagem;
    @FXML
    private ToggleButton btSetor;
    @FXML
    private ToggleButton btVisitas;
    @FXML
    private ToggleButton btLocal;
    @FXML
    private ToggleButton btInstituicao;
    @FXML
    private ToggleButton btExcursao;
    @FXML
    private ToggleButton btCatalogar;
    @FXML
    private ToggleButton btLocalizacao;
    @FXML
    private ToggleButton btLocalizar;
    @FXML
    private ToggleButton btColecao;
    @FXML
    private ToggleButton btEmprestimos;
    @FXML
    private ToggleButton btEmprestimo;
    @FXML
    private ToggleButton btHistorico;
    @FXML
    private ToggleButton btItens;
    @FXML
    private ToggleButton btUtilitarios;
    @FXML
    private ToggleButton btOrganizacao;
    @FXML
    private ToggleButton btDesginacao;
    @FXML
    private ToggleButton btUsuarios;
    @FXML
    private ToggleButton btDevolucao;
    @FXML
    private ToggleButton btCatalogacao;
    @FXML
    private ToggleButton btEstratigrafia;
    @FXML
    private ToggleButton btVisitantes;
    @FXML
    private ToggleButton btMovimentacao;
    @FXML
    private ToggleButton btIdentificacao;
    @FXML
    private ToggleButton btRelatorios;
    @FXML
    private ToggleButton btPesquisa;
    @FXML
    private ToggleButton btSeguranca;
    @FXML
    private ToggleGroup grupoLocaliacao;
    @FXML
    private ToggleGroup grupoUtilidades;
    @FXML
    private ToggleGroup grupoMenus;
    @FXML
    private ToggleGroup grupoEmprestimo;
    @FXML
    private ToggleGroup grupoCatalogacao;
    @FXML
    private ToggleGroup grupoVisitantes;


    /**
     * Obter instancia do controler
     */
    public static AppController getInstance() {
        return instance;
    }


    @FXML
    void menuDashboard(MouseEvent event) {
        Model.getDashboard(boxContainer);
    }

    @FXML
    void menuVisitas(ActionEvent event) {
        submenus(btVisitas, boxVisitas, btVisitantes, btInstituicao, btExcursao);
    }

    @FXML
    void menuAppManagement(ActionEvent event) {
        submenus(btAppManagementSection, boxEmployee, btEmployeeManagement);
    }

    @FXML
    void menuLocalizacao(ActionEvent event) {
        submenus(btLocalizacao, boxLocalizacao, btLocalizar, btOrganizacao, btSetor, btLocal);
    }

    @FXML
    void menuUtilitario(ActionEvent event) {
        submenus(btUtilitarios, boxUtilitarios, btUsuarios);
    }

    @FXML
    void menuEmprestimo(ActionEvent event) {
        submenus(btEmprestimos, boxEmprestimo, btEmprestimo, btItens, btDevolucao, btHistorico);
    }

    @FXML
    void menuIdentificacao(ActionEvent event) {
        Model.getIdentificacao(boxContainer);
    }

    @FXML
    void menuMovimentacao(ActionEvent event) {
        Model.getMovimentacao(boxContainer);
    }

    @FXML
    void menuSeguranca(ActionEvent event) {
        Model.getValidacao(boxContainer);
    }

    @FXML
    void menuPesquisa(ActionEvent event) {
        Model.getPesquisar(boxContainer);
    }

    @FXML
    void menuRelatorios(ActionEvent event) {
        Model.getRelatorio(boxContainer);
    }

    @FXML
    void subVisitantes(ActionEvent event) {
        Model.getVisitante(boxContainer);
    }

    @FXML
    void subInstituicao(ActionEvent event) {
        Model.getInstituicao(boxContainer);
    }

    @FXML
    void subExcursao(ActionEvent event) {
        Model.getExcursao(boxContainer);
    }

    @FXML
    void subCatalogar(ActionEvent event) {
        Model.getCatalogar(boxContainer);
    }

    @FXML
    void subDesignacao(ActionEvent event) {
        Model.getDesignacao(boxContainer);
    }

    @FXML
    void subEstratigrafia(ActionEvent event) {
        Model.getEstratigrafia(boxContainer);
    }

    @FXML
    void subColecao(ActionEvent event) {
        Model.getColecao(boxContainer);
    }

    @FXML
    void subEmprestimo(ActionEvent event) {
        Model.getEmprestimo(boxContainer);
    }

    @FXML
    void subItensEmprestimo(ActionEvent event) {
        Model.getItensEmprestimo(boxContainer);
    }

    @FXML
    void subDevolucaoEmprestimo(ActionEvent event) {
        Model.getDevolucao(boxContainer);
    }

    @FXML
    void subHistoricoEmprestimo(ActionEvent event) {
        Model.getHistorico(boxContainer);
    }

    @FXML
    void subOrganizacao(ActionEvent event) {
        Model.getOrganizacao(boxContainer);
    }

    @FXML
    void subSetor(ActionEvent event) {
        Model.getSetor(boxContainer);
    }

    @FXML
    void subLocal(ActionEvent event) {
        Model.getLocal(boxContainer);
    }

    @FXML
    void subLocalizar(ActionEvent event) {
        Model.getLocalizacao(boxContainer);
    }

    @FXML
    void subUsuarios(ActionEvent event) {
        Model.getUsuario(boxContainer);
    }

    @FXML
    void siteMuseu(ActionEvent event) {
        Link.endereco("http://museuid.com.br");
    }

    @FXML
    void menuSair(ActionEvent event) {
        App.palco.close();
        new Login().start(new Stage());
    }

    @FXML
    void initialize() {
        instance = this;
        if (ConstantConfig.FAKE)
            return;
        Grupo.notEmpty(grupoMenus, grupoCatalogacao, grupoEmprestimo, grupoLocaliacao, grupoUtilidades, grupoVisitantes);//não permite grupos de menus com menus deselecionados
        menuDashboard(null);
        //lbUser.setText("Olá, " + LoginController.usuarioLogado.getNome());
    }

    /**
     * Obter componente para exbição das notas
     */
    public VBox boxNotas() {
        return boxNotas;
    }

    /**
     * Obter componente para exibição dos modulos da aplicação
     */
    public AnchorPane getBoxConteudo() {
        return boxContainer;
    }

    /**
     * Exibir e ocultar submenus
     */
    public void submenus(ToggleButton menu, VBox box, ToggleButton... submenus) {
//        if (box.getChildren().isEmpty()) {
//            box.getChildren().addAll(submenus);
//            Animacao.fade(box);
//            estilo(menu, "menu-grupo");
//        } else {
//            desativarSubmenus(box);
//            estilo(menu, "menu-grupo-inativo");
//        }
    }

    /**
     * Desativar e esconder todos submenus
     */
    public void desativarSubmenus(VBox... boxes) {
        for (VBox box : boxes) {
            box.getChildren().clear();
        }
    }

    /**
     * Aplicar estilo para mostrar/ocultar submenus
     */
    public void estilo(Node no, String estilo) {
        no.getStyleClass().remove(3);
        no.getStyleClass().add(estilo);
    }

    public void openEmployeeManagement(ActionEvent actionEvent) {
        Model.getEmployeeManagementScreen(boxContainer);

    }

    public void openCreateOrderScreen(ActionEvent actionEvent) {
        Model.getCreateOrderScreen(boxContainer);
    }

    public void openMyHandlingOrderScreen(ActionEvent actionEvent) {

    }

    public void openOrderCreatedScreen(ActionEvent actionEvent) {

    }

    public void openStatistic(ActionEvent actionEvent) {

    }
}
