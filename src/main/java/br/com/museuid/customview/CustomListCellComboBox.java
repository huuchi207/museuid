package br.com.museuid.customview;

import javafx.scene.control.ListCell;

public class CustomListCellComboBox<T> extends ListCell<T> {

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setText(empty ? "" : item.toString());
    }

}
