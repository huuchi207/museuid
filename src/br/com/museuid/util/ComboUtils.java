package br.com.museuid.util;

import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * Auxiliary class in the combobox population
 *
 * @author Angelica Leite
 */
public class ComboUtils {

    private ComboUtils() {
    }

    /**
     * Popular generic combos through a Collection of type Item
     */
    public static void popular(ComboBox combo, List list) {
        data(combo, FXCollections.observableArrayList(list));
    }

    /**
     * Popular generic combos through an array of strings passed
     */
    public static void popular(ComboBox combo, String... item) {
        data(combo, FXCollections.observableArrayList(item));
    }

    /**
     * Popular combos with data reported to the combo
     */
    private static void data(ComboBox combo, ObservableList data) {
        if (data.isEmpty() || data == null) {
            clean(combo);
        } else {
            combo.setItems(data);
            combo.getSelectionModel().selectFirst();
        }
    }

    /**
     * Clear informed combo
     */
    private static void clean(ComboBox<Object> combo) {
        combo.getItems().clear();
        combo.setPromptText(BundleUtils.getResourceBundle().getString("txt_no_data"));
    }
}