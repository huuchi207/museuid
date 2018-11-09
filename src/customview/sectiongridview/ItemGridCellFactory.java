package customview.sectiongridview;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Callback;

public class ItemGridCellFactory implements Callback<GridView<ItemGridView>, GridCell<ItemGridView>> {
    private OnTouch onTouch;
    @Override
    public GridCell<ItemGridView> call(GridView<ItemGridView> param) {
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
        void onClick(ItemGridView item);
    }
    public void setOnTouch(OnTouch onTouch){
        this.onTouch = onTouch;
    }
}
