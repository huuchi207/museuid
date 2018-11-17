package br.com.museuid.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.ChartData;
import br.com.museuid.dto.GroupColumn;
import br.com.museuid.model.data.BaseChartItem;
import br.com.museuid.util.BarChartUtils;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.ComboUtils;
import br.com.museuid.util.FakeDataUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.NoticeUtils;
import br.com.museuid.util.ReportUtils;
import br.com.museuid.util.ResizeUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class StatisticController extends AnchorPane {

    public HBox boxPeriod;
    private int periodo = 0;
    private int relatorio;

    @FXML
    private AnchorPane boxGraphic;
    @FXML
    private HBox boxPeriodo;
    @FXML
    private Button btRelatorio;
    @FXML
    private Label lbPrincipal;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ToggleGroup menuPeriod;
    @FXML
    private Label lbTitulo;
    @FXML
    private ToggleGroup menu;
    @FXML
    private HBox boxLegenda;
    @FXML
    private ComboBox<String> cbReportType;
    private ResourceBundle bundle;
    public StatisticController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("/statistic.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            bundle = BundleUtils.getResourceBundle();
            fxml.setResources(bundle);
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
    void relatorio(ActionEvent event) {
        if (datePicker == null) {
            NoticeUtils.alert("Data not informed!");
        } else if (cbReportType.getItems().isEmpty()) {
            NoticeUtils.alert("Type report not informed!");
        } else if (periodo == 0) {
            NoticeUtils.alert("Select the period for report generation!");
        } else {
            ReportUtils.create(boxGraphic, cbReportType.getValue(), periodo, datePicker.getValue());
        }
    }

    @FXML
    void initialize() {
//        Grupo.notEmpty(menu);

        datePicker.setValue(LocalDate.now());
        periodo();
        desabilitarPeriodo();
        if (ConstantConfig.FAKE){
            ChartData chartData = FakeDataUtils.getFakeGroupBarChart();
            Map<String, List<? extends BaseChartItem>> mapData = new HashMap<>();
            for (GroupColumn groupColumn: chartData.getGroupColumns()){
                mapData.put(groupColumn.getTitle(), groupColumn.getColumns());
            }
            addChart(boxGraphic, BarChartUtils.create(chartData.getChartName(), "", chartData.getUnit(), mapData));
        }
    }

    /**
     * Block period selection for reports that are not periodic
     */
    private void desabilitarPeriodo() {
        cbReportType.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue obs, String old, String novo) {
//                Platform.runLater(() -> {
//                    datePicker.setDisable(!"Date Cataloging".equals(novo) && relatorio == 1);
//                    boxPeriodo.setDisable(!"Date Cataloging".equals(novo) && relatorio == 1);
//                    periodo = !"Date Cataloging".equals(novo) && relatorio == 1 ? 1 : menuPeriod.getToggles().indexOf(menuPeriod.getSelectedToggle()) + 1;
//                });
            }
        });

    }

    /**
     * Screen settings, titles, and display of screens and menus
     */
    private void config(String tituloTela, int grupoMenu) {
        lbTitulo.setText(tituloTela);
        relatorio = grupoMenu;
        menu.selectToggle(menu.getToggles().get(grupoMenu - 1));
    }

    /**
     * Fill main combobox with items according to the type of report
     */
    private void combo(String... itens) {
        ComboUtils.popular(cbReportType, itens);
    }

    /**
     * Add escutador to the period group group to know which period is active
     */
    private void periodo() {
        menuPeriod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> obs, Toggle old, Toggle novo) {
                periodo = novo != null ? menuPeriod.getToggles().indexOf(menuPeriod.getSelectedToggle()) + 1 : 0;
            }
        });
    }

    private static void addChart(AnchorPane box, Node chart) {
        box.getChildren().clear();
        ResizeUtils.margin(chart, 0);
        box.getChildren().add(chart);
    }
}
