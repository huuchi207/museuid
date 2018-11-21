package br.com.museuid.screen.statistic;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.ChartData;
import br.com.museuid.dto.GroupColumn;
import br.com.museuid.dto.PeriodChartData;
import br.com.museuid.dto.UserDTO;
import br.com.museuid.model.data.BaseChartItem;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.StatisticRequest;
import br.com.museuid.util.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class StatisticController extends AnchorPane {

    public HBox boxPeriod;
    private int period = 0;
    private static final int DAY = 0;
    private static final int MONTH = 1;
    private static final int YEAR = 2;

    @FXML
    private AnchorPane boxGraphic;
    @FXML
    private HBox boxPeriodo;
    @FXML
    private Button btRelatorio;
    @FXML
    private Label lbPrincipal;
    @FXML
    private DatePicker datePickerStart;

    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private ToggleGroup menuPeriod;
    @FXML
    private Label lbTitulo;
    @FXML
    private ToggleGroup menu;
    @FXML
    private AnchorPane boxLegenda;
    @FXML
    private ComboBox<String> cbReportType;
    @FXML
    private ScrollBar scrollBar;
    private ResourceBundle bundle;
    private JFreeChart freeChart;
    private ChartViewer chartViewer;
    private SlidingCategoryDataset dataset;
    public StatisticController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("statistic.fxml"));
            fxml.setRoot(this);
            fxml.setController(this);
            bundle = BundleUtils.getResourceBundle();
            fxml.setResources(bundle);
            fxml.load();

        } catch (IOException ex) {
            Messenger.erro(BundleUtils.getResourceBundle().getString("txt_loading_screen_error") + " \n" + ex);
            ex.printStackTrace();
        }
    }

    @FXML
    void initialize() {

        datePickerStart.setValue(LocalDate.now());
        datePickerEnd.setValue(LocalDate.now());
        TimeUtils.reformatDatePickerValue(datePickerStart);
        TimeUtils.reformatDatePickerValue(datePickerEnd);
        setupPeriod();

        freeChart = BarChartUtils.createJFreeChart(null);
        chartViewer = new ChartViewer(freeChart);
        addChart(boxGraphic, chartViewer);
        scrollBar.setVisible(false);
        scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable,
                                Number oldValue, Number newValue) {
                    dataset.setFirstCategoryIndex(newValue.intValue()*10);
            }
        });
        if (ConstantConfig.FAKE) {
            PeriodChartData chartData = FakeDataUtils.getFakeGroupBarChart();
            Map<String, List<? extends BaseChartItem>> mapData = new HashMap<>();
            for (GroupColumn groupColumn : chartData.getGroupColumns()) {
                mapData.put(groupColumn.getTitle(), groupColumn.getColumns());
            }
//            addChart(boxGraphic, BarChartUtils.create(chartData.getChartName(), "", chartData.getUnit(), mapData));
//            ChartViewer chartViewer = new ChartViewer(BarChartUtils.createJFreeChart(chartData))

        }
    }


    /**
     * Screen settings, titles, and display of screens and menus
     */
    private void config(String tituloTela, int grupoMenu) {
        lbTitulo.setText(tituloTela);
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
    private void setupPeriod() {
        menuPeriod.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> obs, Toggle old, Toggle novo) {
                period = novo != null ? menuPeriod.getToggles().indexOf(menuPeriod.getSelectedToggle()) : 0;
            }
        });
    }

    private static void addChart(AnchorPane box, Node chart) {
        box.getChildren().clear();
        ResizeUtils.margin(chart, 0);
        box.getChildren().add(chart);
    }

    @FXML
    void doStatistic(ActionEvent event) {
        if (ConstantConfig.FAKE) {

        } else {
            AppController.getInstance().showProgressDialog();
            StatisticRequest statisticRequest = new StatisticRequest(
                TimeUtils.convertDateTimeToStartTimeFormat(datePickerStart.getValue()),
                TimeUtils.convertDateTimeToEndTimeFormat(datePickerEnd.getValue()));
            BaseCallback<PeriodChartData> periodChartDataCallback = new BaseCallback<PeriodChartData>() {
                @Override
                public void onError(String errorCode, String errorMessage) {
                    AppController.getInstance().hideProgressDialog();
                    Messenger.erro(errorMessage);
                }

                @Override
                public void onSuccess(PeriodChartData data) {
                    AppController.getInstance().hideProgressDialog();
                    updateChartData(BarChartUtils.convertPeriodChartDataToCategoryDataset(data));
                }
            };

            BaseCallback<ChartData> chartDataCallback = new BaseCallback<ChartData>() {
                @Override
                public void onError(String errorCode, String errorMessage) {
                    AppController.getInstance().hideProgressDialog();
                    Messenger.erro(errorMessage);
                }

                @Override
                public void onSuccess(ChartData data) {
                    AppController.getInstance().hideProgressDialog();
                    updateChartData(BarChartUtils.convertChartDataToCategoryDataset(data));
                }
            };

            switch (period) {
                case DAY:
                    if (UserDTO.UserRole.ADMIN.name().equals(StaticVarUtils.getSessionUserInfo().getInfo().getRole())){
                        ServiceBuilder.getApiService().getDayStatistic(statisticRequest).enqueue(chartDataCallback);
                    } else {
                        ServiceBuilder.getApiService().getDayStatisticPeriod(statisticRequest).enqueue(periodChartDataCallback);
                    }

                    break;
                case MONTH:
                    ServiceBuilder.getApiService().getMonthStatistic(statisticRequest).enqueue(chartDataCallback);
                    break;
                case YEAR:
                    ServiceBuilder.getApiService().getYearStatistic(statisticRequest).enqueue(chartDataCallback);
                    break;
            }
        }
    }

    private void updateChartData(DefaultCategoryDataset defaultCategoryDataset){
        dataset = new SlidingCategoryDataset(defaultCategoryDataset, 0, 10);
        if (dataset.getColumnCount()<10) {
            scrollBar.setVisible(false);
        } else {
            scrollBar.setVisible(true);
            scrollBar.setMin(0);
            scrollBar.setValue(0);
            scrollBar.setMax((double)defaultCategoryDataset.getColumnCount()/10);
            scrollBar.setVisibleAmount(1);
        }
        freeChart.getCategoryPlot().setDataset(dataset);
//        freeChart.setTitle(chartData.getChartName());
//        freeChart.getCategoryPlot().
        freeChart.fireChartChanged();
    }
}
