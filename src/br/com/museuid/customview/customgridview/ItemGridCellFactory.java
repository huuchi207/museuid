package br.com.museuid.customview.customgridview;

import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;

import br.com.museuid.dto.ProductWithImage;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.util.Callback;

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
        itemGridCell.getItem().setSelected(!itemGridCell.getItem().isSelected());
        if (itemGridCell.getItem().isSelected()){
          itemGridCell.setStyle("-fx-background-color: #cacaca; -fx-background-radius: 0; -fx-border-radius: 0;" );
        } else {
          itemGridCell.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;" );
        }

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
