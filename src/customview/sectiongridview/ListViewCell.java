package customview.sectiongridview;

import org.controlsfx.control.GridView;

import br.com.museuid.util.Messenger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ListCell;

public class ListViewCell  extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
//        if (empty || item == null) {
//            setText(null);
//            setGraphic(null);
//        } else {
//            setText(item.toString());
//        }

        ItemGridCellFactory itemGridCellFactory = new ItemGridCellFactory();
        itemGridCellFactory.setOnTouch(new ItemGridCellFactory.OnTouch() {
            @Override
            public void onClick(ItemGridView item) {
                Messenger.info(item.getDeviceName());
            }
        });
        SectionView sectionView = new SectionView();
        sectionView.getLabel().setText("Section AFG");
        GridView gridView = sectionView.getGridView();
        final ObservableList<ItemGridView> list = FXCollections.observableArrayList();
        gridView.setCellFactory(itemGridCellFactory);
        gridView.cellWidthProperty().set(100);
        gridView.cellHeightProperty().set(100);
        for (int i = 0; i < 50; i++) {
            ItemGridView itemGridView = new ItemGridView();
            itemGridView.setDeviceName("Máy " + (i + 1));

//            itemGridView.setNewOrder();
            list.add(itemGridView);
        }
        gridView.setItems(list);
        setGraphic(sectionView.getBox());
        gridView.setPrefHeight(400);
        gridView.setNodeOrientation(NodeOrientation.INHERIT);
    }
}
