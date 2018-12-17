package br.com.museuid.customview.customgridview;

import br.com.museuid.app.App;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.ProductWithImage;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import org.controlsfx.control.GridCell;

public class ItemGridCell extends GridCell<ProductWithImage> {
    private final VBox lines;
    ItemGridCell(){
        lines = new VBox();
        setGraphic(lines);
    }
    @Override
    protected void updateItem(ProductWithImage item, boolean empty) {
        super.updateItem(item, empty);
        lines.getChildren().clear();
        if (empty || item == null) {


        } else {
            String imagePath = "/br/com/museuid/img/icon/ic-computer-noti-new-order.png";
            final Image image = new Image(App.class.getClass().getResourceAsStream(imagePath),
                200, 0, true, true);
            ImageView imageView;
            if ( ConstantConfig.FAKE){
                imageView = new ImageView(image);
            } else {
                imageView = item.getProductImage();
            }
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);

            Label name = new Label(item.getProductName());
            Label price = new Label(item.getPrice()+ " VND");
            Label status = new Label(item.getStatus());

            lines.getChildren().addAll(imageView, name, price, status);


            setStyle("-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;" +
                " -fx-border-width: 0; -fx-padding: 10; -fx-pref-width: 200; -fx-max-width: 145; " +
                "-fx-pref-height: 200; -fx-max-height: 130; ");
//-fx-effect: dropshadow(three-pass-box, #93948d, 10, 0, 0, 0);
        }
    }
}
