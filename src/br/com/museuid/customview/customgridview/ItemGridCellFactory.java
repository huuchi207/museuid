package br.com.museuid.customview.customgridview;

import br.com.museuid.dto.ProductWithImage;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

public class ItemGridCellFactory implements Callback<GridView<ProductWithImage>, GridCell<ProductWithImage>> {
    private OnTouch onTouch;
    @Override
    public GridCell<ProductWithImage> call(GridView<ProductWithImage> param) {
        ItemGridCell itemGridCell = new ItemGridCell();
        itemGridCell.setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
//                if (((MouseEvent) event).getClickCount() >= 2) {
//                    //do something when it's clicked
//
//                }
                if (onTouch!= null){
                    onTouch.onClick(itemGridCell.getItem());
                }
            }
        });

        return itemGridCell;
    }
    public interface OnTouch{
        void onClick(ProductWithImage item);
    }
    public void setOnTouch(OnTouch onTouch){
        this.onTouch = onTouch;
    }
}
