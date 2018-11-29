package br.com.museuid.util;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.ui.TextAnchor;
import org.jfree.data.category.CategoryDataset;

import java.awt.Color;
import java.awt.GradientPaint;
import java.util.List;
import java.util.Map;

import br.com.museuid.model.data.BaseChartItem;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Text;

/**
 * Barchart
 */
public class BarChartUtils {

    private static CategoryAxis axisX;
    private static NumberAxis axisY;
    private static BarChart<String, Number> graphic;

    private BarChartUtils() {
    }

    /**
     * Create bar graph and insert data of the series, dates and values from the map informed
     */
    public static BarChart create(String title, String axisXLabel, String axisYLabel, Map<String, List<? extends BaseChartItem>> mapa) {

        axisX = new CategoryAxis();
        axisY = new NumberAxis();
        graphic = new BarChart<>(axisX, axisY);
        config(title, axisXLabel, axisYLabel);

        for (String key : mapa.keySet()) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(key);

            List<? extends BaseChartItem> dataset = mapa.get(key);
            for (BaseChartItem data : dataset) {
                XYChart.Data<String, Number> dado = new XYChart.Data<>(data.getDate(), data.getValue());

                dado.nodeProperty().addListener((ObservableValue<? extends Node> obs, Node old, Node novo) -> {
                    if (novo != null) {
                        info(dado);
                    }
                });

                serie.getData().add(dado);
            }
            graphic.getData().add(serie);
        }

        axisY.setUpperBound(axisY.getUpperBound() + 10);

        return graphic;
    }

    /**
     * Display above bar value reached
     */
    private static void info(XYChart.Data<String, Number> data) {
        Text texto = new Text(data.getYValue().toString());
        texto.setStyle("-fx-fill: #555; -fx-font-size: 11px;");

        data.getNode().parentProperty().addListener((ObservableValue<? extends Parent> obs, Parent old, Parent novo) -> {
            Platform.runLater(() -> {
                if (novo != null) {
                    Group grupo = (Group) novo;
                    grupo.getChildren().add(texto);
                }
            });
        });

        data.getNode().boundsInParentProperty().addListener((ObservableValue<? extends Bounds> obs, Bounds old, Bounds novo) -> {
            texto.setLayoutX(Math.round(novo.getMinX() + novo.getWidth() / 2 - texto.prefWidth(-1) / 2));
            texto.setLayoutY(Math.round(novo.getMinY() - texto.prefHeight(-1) * 0.5));
        });

    }

    /**
     * Configure Graphical Elements Axes, Titles, Captions
     */
    public static void config(String titulo, String axisXlabel, String axisYlabel) {
        graphic.getData().clear();
        axisX.setLabel(axisXlabel);
        axisY.setLabel(axisYlabel);
        graphic.setLegendVisible(false);
    }

    public static JFreeChart createJFreeBarChart(String xAxisLabel, String yAxisLabel, String title,
                                                 CategoryDataset categoryDataset) {
        JFreeChart chart = ChartFactory.createBarChart(
          title, xAxisLabel  /* x-axis label*/,
          yAxisLabel/* y-axis label */, categoryDataset,
            PlotOrientation.VERTICAL, true, true, false);
//        chart.addSubtitle(new TextTitle("Time to generate 1000 charts in SVG "
//            + "format (lower bars = better performance)"));
        chart.setBackgroundPaint(Color.white);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        org.jfree.chart.axis.NumberAxis rangeAxis = (org.jfree.chart.axis.NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(org.jfree.chart.axis.NumberAxis.createIntegerTickUnits());
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);
        renderer.setMaximumBarWidth(0.35);

        renderer.setDefaultItemLabelGenerator(
            new StandardCategoryItemLabelGenerator() {
                @Override
                public String generateLabel(CategoryDataset categoryDataset, int i, int i1) {
                    return NumberUtils.formatValue(categoryDataset.getValue(i, i1).floatValue());
                }
            });
        renderer.setDefaultItemLabelsVisible(true);

        chart.getCategoryPlot().getRangeAxis().setUpperMargin(0.15);

        ItemLabelPosition position = new ItemLabelPosition(ItemLabelAnchor.OUTSIDE12, TextAnchor.BOTTOM_CENTER);
        renderer.setDefaultPositiveItemLabelPosition(position);
        // set up gradient paints for series...
        GradientPaint gp0 = new GradientPaint(0.0f, 0.0f, Color.red,
          0.0f, 0.0f, new Color(76, 0, 0));
        renderer.setSeriesPaint(0, gp0);
        renderer.setSeriesPaint(1, new GradientPaint(0.0f, 0.0f, Color.yellow,
          0.0f, 0.0f, new Color(124, 127, 13)));
        renderer.setSeriesPaint(2, new GradientPaint(0.0f, 0.0f, Color.blue,
          0.0f, 0.0f, new Color(12, 33, 127)));
        chart.getLegend().setFrame(BlockBorder.NONE);
        return chart;
    }


}
