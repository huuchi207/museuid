package customview.sectiongridview;

import org.controlsfx.control.GridCell;

import br.com.museuid.app.App;
import br.com.museuid.util.Messenger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ItemGridCell extends GridCell<ItemGridView> {
    ItemGridCell(){
        setOnMouseClicked(new EventHandler<Event>() {

            @Override
            public void handle(Event event) {
                // TODO Auto-generated method stub
//                if (((MouseEvent) event).getClickCount() >= 2) {
//                    //do something when it's clicked
//
//                }
                Messenger.info(getItem().getDeviceName());
            }
        });
    }
    @Override
    protected void updateItem(ItemGridView item, boolean empty) {
        // TODO Auto-generated method stub
        super.updateItem(item, empty);
        if (empty || item == null) {


        } else {
            String imagePath = "/br/com/museuid/img/icon/ic-computer.png";
//            if (item.getNewOrder()!= null&& !item.getNewOrder().isEmpty()){
                imagePath = "/br/com/museuid/img/icon/ic-computer-noti-new-order.png";
//            }
            final Image image = new Image(App.class.getClass().getResourceAsStream(imagePath),
                200, 0, true, true);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);

            setGraphic(imageView);
            setText(item.getDeviceName());
            setAlignment(Pos.CENTER);
            setContentDisplay(ContentDisplay.TOP);

            setStyle("-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;" +
                " -fx-border-width: 0; -fx-padding: 10; -fx-pref-width: 200; -fx-max-width: 145; " +
                "-fx-pref-height: 200; -fx-max-height: 130; ");
//-fx-effect: dropshadow(three-pass-box, #93948d, 10, 0, 0, 0);
        }
    }
}
