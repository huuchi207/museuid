package br.com.museuid.screen.product_statistic;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.ChartData;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.StatisticRequest;
import br.com.museuid.util.BarChartUtils;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.FakeDataUtils;
import br.com.museuid.util.LineChartUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.ResizeUtils;
import br.com.museuid.util.TimeUtils;
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
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

public class ProductStatisticController extends AnchorPane {
    @FXML
    private HBox boxPeriod;
    @FXML
    private HBox boxPeriodForManager;

    private int period = 0;

    private static final int DAY = 0;
    private static final int MONTH = 1;
    private static final int YEAR = 2;

    @FXML
    private AnchorPane boxGraphic;

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
    @FXML
    private ToggleButton tbSession;
    @FXML
    private ToggleButton tbDay;
    private ResourceBundle bundle;
    private JFreeChart freeChart;
    private ChartViewer chartViewer;
    private SlidingCategoryDataset dataset;
    public ProductStatisticController() {
        try {
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("product_statistic.fxml"));
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

        freeChart = LineChartUtils.createLineChart("Thời gian", "VND", null);
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
            ChartData chartData = FakeDataUtils.getFakeGroupBarChart();

//            addChart(boxGraphic, BarChartUtils.create(chartData.getChartName(), "", chartData.getUnit(), mapData));
//            ChartViewer chartViewer = new ChartViewer(BarChartUtils.createJFreeBarChart(chartData))
          updateChartData(BarChartUtils.convertChartDataToCategoryDataset(chartData));
        }

        setupPeriod(menuPeriod);


    }


    private void setupPeriod(ToggleGroup group) {
        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> obs, Toggle old, Toggle novo) {
                int newPosition = novo != null ? group.getToggles().indexOf(group.getSelectedToggle()) : 0;
                if (novo == null){
                    old.setSelected(true);
                } else {
                    setPeriod(newPosition);
                }

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
                    ServiceBuilder.getApiService().getDayStatistic(statisticRequest).enqueue(chartDataCallback);
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
    private void setPeriod(int n){
//        if (UserDTO.UserRole.ADMIN.name().equals(StaticVarUtils.getSessionUserInfo().getInfo().getRole())){
//            period = n+1;
//        } else {
            period = n;
//        }
    }
    private int getRealPeriodPosition(){
//        if (UserDTO.UserRole.ADMIN.name().equals(StaticVarUtils.getSessionUserInfo().getInfo().getRole())){
//            return period-1;
//        } else {
            return period;
//        }
    }
}
