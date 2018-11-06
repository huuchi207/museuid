package br.com.museuid.util;

import br.com.museuid.model.Relatorio;
import br.com.museuid.model.data.BaseChartItem;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;
import java.util.Map;

/**
 * Criar gr�ficos de area
 */
public class AreaChartUtils {

    private static CategoryAxis axisX;
    private static NumberAxis axisY;
    private static AreaChart<String, Number> chart;

    /**
     * Create area chart and enter data from series, dates and values from the map informed
     */
    public static AreaChart create(Map<String, List<BaseChartItem>> map, String titulo) {

        config(titulo);

        axisX = new CategoryAxis();
        axisY = new NumberAxis();
        chart = new AreaChart<>(axisX, axisY);

        for (String key : map.keySet()) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(key);

            List<BaseChartItem> dataset = map.get(key);
            for (BaseChartItem data : dataset) {
                serie.getData().add(new XYChart.Data<>(data.getTitle(), data.getValue()));
            }

            chart.getData().add(serie);
        }

        return chart;
    }

    /**
     * Configure graph title and its X and Y axis
     */
    public static void config(String titulo) {
        chart.getData().clear();
        chart.setTitle(titulo);
        chart.setVerticalGridLinesVisible(false);

        axisX.setLabel("Data");
    }
}
