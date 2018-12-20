package br.com.museuid.screen.statistic;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.fx.ChartViewer;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.category.SlidingCategoryDataset;

import java.io.IOException;
import java.util.ResourceBundle;

import br.com.museuid.config.ConstantConfig;
import br.com.museuid.customview.calendarpicker.FXCalendar;
import br.com.museuid.dto.ChartData;
import br.com.museuid.screen.app.AppController;
import br.com.museuid.service.remote.BaseCallback;
import br.com.museuid.service.remote.ServiceBuilder;
import br.com.museuid.service.remote.requestbody.StatisticRequest;
import br.com.museuid.util.BarChartUtils;
import br.com.museuid.util.BundleUtils;
import br.com.museuid.util.ChartUtils;
import br.com.museuid.util.FakeDataUtils;
import br.com.museuid.util.FieldViewUtils;
import br.com.museuid.util.Messenger;
import br.com.museuid.util.TimeUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class RevenueStatisticController extends AnchorPane {
  @FXML
  private HBox boxPeriod;
  @FXML
  private HBox boxPeriodForManager;
  @FXML
  private ToggleGroup menuPeriodForManager;
  private int period = 0;
  public static final int SESSION = 0;
  public static final int DAY = 1;
  public static final int MONTH = 2;
  public static final int YEAR = 3;

  @FXML
  private AnchorPane boxGraphic;

  @FXML
  private Button btRelatorio;
  @FXML
  private Label lbPrincipal;
  @FXML
  private FXCalendar datePickerStart;

  @FXML
  private FXCalendar datePickerEnd;
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
  @FXML
  private GridPane gridPane;
  private ResourceBundle bundle;
  private JFreeChart freeChart;
  private ChartViewer chartViewer;
  private SlidingCategoryDataset dataset;
  public HBox datePickerStartBox;
  public HBox datePickerEndBox;
  public Button btStatistic;

  public RevenueStatisticController() {
    try {
      FXMLLoader fxml = new FXMLLoader(getClass().getResource("revenue_statistic.fxml"));
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

//    datePickerStart.setValue(LocalDate.now());
//    datePickerEnd.setValue(LocalDate.now());
//    TimeUtils.reformatDatePickerValue(datePickerStart);
//    TimeUtils.reformatDatePickerValue(datePickerEnd);

    chartViewer = new ChartViewer(freeChart);
    scrollBar.setVisible(false);
    scrollBar.valueProperty().addListener(new ChangeListener<Number>() {
      public void changed(ObservableValue<? extends Number> observable,
                          Number oldValue, Number newValue) {
        dataset.setFirstCategoryIndex(newValue.intValue() * 10);
      }
    });

//    if (UserDTO.UserRole.ADMIN.name().equals(StaticVarUtils.getSessionUserInfo().getInfo().getRole())) {
//      boxPeriod.setVisible(true);
//      boxPeriodForManager.setVisible(false);
//      setupPeriod(menuPeriod);
//    } else {
      boxPeriod.setVisible(false);
      boxPeriodForManager.setVisible(true);
      setupPeriod(menuPeriodForManager);
//    }

    FieldViewUtils.setGlobalEventHandler(this, btStatistic);


  }


  private void setupPeriod(ToggleGroup group) {
    group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
      public void changed(ObservableValue<? extends Toggle> obs, Toggle old, Toggle novo) {
        int newPosition = novo != null ? group.getToggles().indexOf(group.getSelectedToggle()) : 0;
        if (novo == null) {
          old.setSelected(true);
        } else {
          setPeriod(newPosition);
          switch (period){
            case SESSION:
            case DAY:
              datePickerStartBox.getChildren().clear();
              datePickerEndBox.getChildren().clear();
              datePickerStart = new FXCalendar(FXCalendar.PickerMode.DATE);
              datePickerEnd = new FXCalendar(FXCalendar.PickerMode.DATE);
              datePickerStartBox.getChildren().add(datePickerStart);
              datePickerEndBox.getChildren().add(datePickerEnd);
              break;
            case MONTH:
              datePickerStartBox.getChildren().clear();
              datePickerEndBox.getChildren().clear();
              datePickerStart = new FXCalendar(FXCalendar.PickerMode.MONTH);
              datePickerEnd = new FXCalendar(FXCalendar.PickerMode.MONTH);
              datePickerStartBox.getChildren().add(datePickerStart);
              datePickerEndBox.getChildren().add(datePickerEnd);
              break;
            case YEAR:
              datePickerStartBox.getChildren().clear();
              datePickerEndBox.getChildren().clear();
              datePickerStart = new FXCalendar(FXCalendar.PickerMode.YEAR);
              datePickerEnd = new FXCalendar(FXCalendar.PickerMode.YEAR);
              datePickerStartBox.getChildren().add(datePickerStart);
              datePickerEndBox.getChildren().add(datePickerEnd);
              break;
          }
        }

      }
    });
  }

  @FXML
  void doStatistic(ActionEvent event) {
    if (!TimeUtils.validDate(datePickerStart.getFormattedTime(), datePickerEnd.getFormattedTime(), period)){
      Messenger.info("Thời gian bắt đầu phải sớm hơn hoặc bằng thời gian kết thúc");
      return;
    }
    if (!TimeUtils.validDistanceDate(datePickerStart.getFormattedTime(), datePickerEnd.getFormattedTime(), period)){
      Messenger.info("Thời gian bắt đầu và kết thúc quá cách xa nhau!");
      return;
    }
    if (ConstantConfig.FAKE) {
      ChartData chartData = FakeDataUtils.getFakeGroupBarChart();
      makeBarChart(chartData);
      BarRenderer renderer = (BarRenderer) freeChart.getCategoryPlot().getRenderer();
      if (period == SESSION){
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER, TextAnchor.CENTER, 45));
      } else {
        renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
      }
    } else {

      AppController.getInstance().showProgressDialog();
      BaseCallback<ChartData> chartDataCallback = new BaseCallback<ChartData>() {
        @Override
        public void onError(String errorCode, String errorMessage) {
          AppController.getInstance().hideProgressDialog();
          Messenger.erro(errorMessage);
        }

        @Override
        public void onSuccess(ChartData data) {
          AppController.getInstance().hideProgressDialog();
          makeBarChart(data);
          BarRenderer renderer = (BarRenderer) freeChart.getCategoryPlot().getRenderer();
          if (period == SESSION){
            renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER, TextAnchor.CENTER, 45));
          } else {
            renderer.setDefaultPositiveItemLabelPosition(new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER));
          }
        }
      };
      StatisticRequest statisticRequest;
      switch (period){
        case SESSION:
          statisticRequest= new StatisticRequest(
            TimeUtils.convertDateTimeToRequestTimeFormat(
              datePickerStart.getSelectedDate(), datePickerStart.getSelectedMonth(), datePickerStart.getSelectedYear(), true),
            TimeUtils.convertDateTimeToRequestTimeFormat(
              datePickerEnd.getSelectedDate(), datePickerEnd.getSelectedMonth(), datePickerEnd.getSelectedYear(), false));
          ServiceBuilder.getApiService().getDayStatisticPeriod(statisticRequest).enqueue(chartDataCallback);
          break;
        case DAY:
          statisticRequest= new StatisticRequest(
            TimeUtils.convertDateTimeToRequestTimeFormat(
              datePickerStart.getSelectedDate(), datePickerStart.getSelectedMonth(), datePickerStart.getSelectedYear(), true),
            TimeUtils.convertDateTimeToRequestTimeFormat(
              datePickerEnd.getSelectedDate(), datePickerEnd.getSelectedMonth(), datePickerEnd.getSelectedYear(), false));
          ServiceBuilder.getApiService().getDayStatistic(statisticRequest).enqueue(chartDataCallback);
          break;
        case MONTH:
          statisticRequest= new StatisticRequest(
            TimeUtils.convertDateTimeToRequestTimeFormat(
              null, datePickerStart.getSelectedMonth(), datePickerStart.getSelectedYear(), true),
            TimeUtils.convertDateTimeToRequestTimeFormat(
              null, datePickerEnd.getSelectedMonth(), datePickerEnd.getSelectedYear(), false));
          ServiceBuilder.getApiService().getMonthStatistic(statisticRequest).enqueue(chartDataCallback);
          break;
        case YEAR:
          statisticRequest= new StatisticRequest(
            TimeUtils.convertDateTimeToRequestTimeFormat(
              null, null, datePickerStart.getSelectedYear(), true),
            TimeUtils.convertDateTimeToRequestTimeFormat(
              null, null, datePickerEnd.getSelectedYear(), false));
          ServiceBuilder.getApiService().getYearStatistic(statisticRequest).enqueue(chartDataCallback);
          break;
      }
    }
  }

  private void setPeriod(int n) {
//    if (UserDTO.UserRole.ADMIN.name().equals(StaticVarUtils.getSessionUserInfo().getInfo().getRole())) {
//      period = n + 1;
//    } else {
      period = n;
//    }
  }

  private int getRealPeriodPosition() {
//    if (UserDTO.UserRole.ADMIN.name().equals(StaticVarUtils.getSessionUserInfo().getInfo().getRole())) {
//      return period - 1;
//    } else {
      return period;
//    }
  }

  private void makeBarChart(ChartData data){
    DefaultCategoryDataset defaultCategoryDataset = ChartUtils.convertChartDataToCategoryDataset(data);
    dataset = new SlidingCategoryDataset(defaultCategoryDataset, 0, 10);
    ChartUtils.configScrollBar(dataset, scrollBar, defaultCategoryDataset.getColumnCount());
    freeChart = BarChartUtils.createJFreeBarChart(data.getxAxisUnit(), data.getyAxisUnit(), data.getChartName(), dataset);
    ChartUtils.makeChart(freeChart, chartViewer, boxGraphic);
  }
}
