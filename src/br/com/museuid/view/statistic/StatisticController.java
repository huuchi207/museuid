package br.com.museuid.view.statistic;

import java.io.IOException;
import java.time.LocalDate;

import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.Combo;
import br.com.museuid.util.Grupo;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NoticeUtils;
import br.com.museuid.util.ReportUtils;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class StatisticController extends AnchorPane {

    private int periodo = 0;
    private int relatorio;

    @FXML
    private AnchorPane boxGrafico;
    @FXML
    private HBox boxPeriodo;
    @FXML
    private Button btRelatorio;
    @FXML
    private Label lbPrincipal;
    @FXML
    private DatePicker data;
    @FXML
    private ToggleGroup menuPeriodo;
    @FXML
    private Label lbTitulo;
    @FXML
    private ToggleGroup menu;
    @FXML
    private HBox boxLegenda;
    @FXML
    private ComboBox<String> cbTipoRelatorio;

    public StatisticController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("../view/statistic.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            fxml.load();

        } catch (IOException ex) {
            Messenger.erro(BundleUtils.getResourceBundle().getString("txt_loading_screen_error")+" \n" + ex);
            ex.printStackTrace();
        }
    }

    @FXML
    void catalogacao(ActionEvent event) {
        config("Catalog Report", 1);
        combo("Date Cataloging", "Search", "Dimensions", "N ° Parties", "Location", "Designation", "Estratigrafia", "Collection");
    }

    @FXML
    void visitas(ActionEvent event) {
        config("Visit Report", 2);
        combo("Visitas");
    }

    @FXML
    void emprestimo(ActionEvent event) {
        config("Loan Report", 3);
        combo("Empréstimo");
    }

    @FXML
    void validacao(ActionEvent event) {
        config("Relatório Validação", 4);
        combo("Validação");
    }

    @FXML
    void movimentacao(ActionEvent event) {
        config("Relatório Movimentação", 5);
        combo("Movimentação");
    }

    @FXML
    void relatorio(ActionEvent event) {
        if (data == null) {
            NoticeUtils.alert("Data not informed!");
        } else if (cbTipoRelatorio.getItems().isEmpty()) {
            NoticeUtils.alert("Type report not informed!");
        } else if (periodo == 0) {
            NoticeUtils.alert("Select the period for report generation!");
        } else {
            ReportUtils.create(boxGrafico, cbTipoRelatorio.getValue(), periodo, data.getValue());
        }
    }

    @FXML
    void initialize() {
        Grupo.notEmpty(menu);

        data.setValue(LocalDate.now());
        periodo();
        desabilitarPeriodo();

        movimentacao(null);
    }

    /**
     * Bloquear seleção de periodo para relatorios que não são periodicos
     */
    private void desabilitarPeriodo() {
        cbTipoRelatorio.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue obs, String old, String novo) {
                Platform.runLater(() -> {
                    data.setDisable(!"Date Cataloging".equals(novo) && relatorio == 1);
                    boxPeriodo.setDisable(!"Date Cataloging".equals(novo) && relatorio == 1);
                    periodo = !"Date Cataloging".equals(novo) && relatorio == 1 ? 1 : menuPeriodo.getToggles().indexOf(menuPeriodo.getSelectedToggle()) + 1;
                });
            }
        });

    }

    /**
     * Configurações de tela, titulos e exibição de telas e menus
     */
    private void config(String tituloTela, int grupoMenu) {
        lbTitulo.setText(tituloTela);
        relatorio = grupoMenu;
        menu.selectToggle(menu.getToggles().get(grupoMenu - 1));
    }

    /**
     * Preencher combobox principal com itens de acordo com o tipo de relatorio
     */
    private void combo(String... itens) {
        Combo.popular(cbTipoRelatorio, itens);
    }

    /**
     * Adionar escutador ao grupo de menus periodo para saber qual periodo esta ativo
     */
    private void periodo() {
        menuPeriodo.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> obs, Toggle old, Toggle novo) {
                periodo = novo != null ? menuPeriodo.getToggles().indexOf(menuPeriodo.getSelectedToggle()) + 1 : 0;
            }
        });
    }
}
