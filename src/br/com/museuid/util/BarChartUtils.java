package br.com.museuid.util;

import br.com.museuid.model.Relatorio;
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

import java.util.List;
import java.util.Map;

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
    public static BarChart create(String title, String axis, Map<String, List<Relatorio>> mapa) {

        axisX = new CategoryAxis();
        axisY = new NumberAxis();
        graphic = new BarChart<>(axisX, axisY);
        config(title, axis);

        for (String key : mapa.keySet()) {
            XYChart.Series<String, Number> serie = new XYChart.Series<>();
            serie.setName(key);

            List<Relatorio> relatorios = mapa.get(key);
            for (Relatorio relatorio : relatorios) {
                XYChart.Data<String, Number> dado = new XYChart.Data<>(relatorio.getFormatar(), relatorio.getTotal());

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
    public static void config(String titulo, String eixo) {
        graphic.getData().clear();
        axisX.setLabel(eixo);
        graphic.setLegendVisible(false);
    }
}