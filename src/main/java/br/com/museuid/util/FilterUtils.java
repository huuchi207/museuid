package br.com.museuid.util;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Label;

/**
 * Filter table data through a search field and showDialog result of the amount
 * of items returned from the search in the caption
 */
public class FilterUtils {

    private FilterUtils() {
    }

    /**
     * Display message in the caption of the amount of items filtered by searches done in the edit and delete screens
     */
    public static void mensage(Label legend, int qtItens, String mensage) {
        // create a property for list size of filtered and sorted items
        IntegerProperty size = new SimpleIntegerProperty(qtItens);

        legend.textProperty().bind(new StringBinding() {
            {
                // monitors change
                bind(size);
            }

            protected String computeValue() {
                // message to be displayed in search filtering with filtering result
                return mensage + ": " + size.get();
            }
        });

        legend.textProperty().unbind();
    }
}
