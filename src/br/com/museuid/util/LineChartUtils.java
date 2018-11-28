package br.com.museuid.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;

import java.util.List;
import java.util.Map;

import br.com.museuid.model.data.BaseChartItem;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Create line graph
 */
public class LineChartUtils {

    private static CategoryAxis axisX;
    private static NumberAxis axisY;
    private static LineChart<String, Number> chart;

    /**
     * Create line graph and insert data of the series, dates and values from the map informed
     */
    public static LineChart create(String titulo, String eixo, Map<String, List<BaseChartItem>> map) {

        config(titulo);

        axisX = new CategoryAxis();
        axisY = new NumberAxis();
        chart = new LineChart<>(axisX, axisY);

        for (String key : map.keySet()) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(key);

            List<BaseChartItem> dataset = map.get(key);
            for (BaseChartItem item : dataset) {
                XYChart.Data data = new XYChart.Data(item.getDate(), item.getValue());
                data.setNode(info((double) item.getValue()));
                serie.getData().add(data);
            }

            chart.getData().add(serie);
        }

        return chart;
    }

    /**
     * Set title of the graphs and their X and Y axes
     */
    public static void config(String titulo) {
        chart.getData().clear();
        chart.setVerticalGridLinesVisible(false);

        axisX.setLabel("Dia");
    }

    /**
     * By passing the mouse through showDialog series value reached
     */
    private static StackPane info(double value) {

        StackPane stack = new StackPane();
        Label label = new Label(value + "");
        label.getStyleClass().add("chart-line-conteudo");

        stack.setOnMouseEntered((MouseEvent mouseEvent) -> {
            stack.getChildren().setAll(label);
            stack.toFront();
        });

        stack.setOnMouseExited((MouseEvent mouseEvent) -> {
            stack.getChildren().clear();
        });

        return stack;
    }
    public static JFreeChart createLineChart(String xAxisLabel, String yAxisLabel, String title){
      return ChartFactory.createLineChart(
        title,
        xAxisLabel, yAxisLabel,
        null,
        PlotOrientation.VERTICAL,
        true,     // include legend
        true,
        true
      );
    }
}
