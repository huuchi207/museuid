package br.com.museuid.customview.customgridview;

import br.com.museuid.app.App;
import br.com.museuid.config.ConstantConfig;
import br.com.museuid.dto.ProductWithImage;
import br.com.museuid.util.NumberUtils;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import org.controlsfx.control.GridCell;

public class ItemGridCell extends GridCell<ProductWithImage> {
    private final VBox lines;
    ItemGridCell(){
        lines = new VBox();
        lines.setAlignment(Pos.CENTER);
        setGraphic(lines);
    }
    @Override
    protected void updateItem(ProductWithImage item, boolean empty) {
        super.updateItem(item, empty);
        lines.getChildren().clear();
        if (empty || item == null) {


        } else {

            ImageView imageView;
            if ( ConstantConfig.FAKE){
                String imagePath = "/br/com/museuid/img/icon/ic-computer-noti-new-order.png";
                final Image image = new Image(App.class.getClass().getResourceAsStream(imagePath),
                        200, 0, true, true);
                imageView = new ImageView();
            } else {
                imageView = item.getProductImage();
            }
            imageView.setFitHeight(100);
            imageView.setFitWidth(100);

            Label name = new Label(item.getProductName());
            name.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
            Label price = new Label(NumberUtils.convertVNDMoneyToDecimaFormatString(item.getPrice()) + " VND");

            Label status = new Label(item.getStatus());
            if (item.isAvailable()){
                status.setTextFill(Color.GREEN);
            } else {
                status.setTextFill(Color.RED);
            }

            VBox.setMargin(name, new Insets(10, 0, 10, 0));
            VBox.setMargin(price, new Insets(0, 0, 10, 0));
            VBox.setMargin(status, new Insets(0, 0, 10, 0));

            lines.getChildren().addAll(imageView, name, price, status);

//            if (item.isSelected()){
//                setStyle("-fx-background-color: #808080; -fx-background-radius: 0; -fx-border-radius: 0;" +
//                        " -fx-border-width: 0; -fx-padding: 10; -fx-pref-width: 200; -fx-max-width: 145; " +
//                        "-fx-pref-height: 200; -fx-max-height: 130; ");
//            } else {
//                setStyle("-fx-background-color: #ffffff; -fx-background-radius: 0; -fx-border-radius: 0;" +
//                        " -fx-border-width: 0; -fx-padding: 10; -fx-pref-width: 200; -fx-max-width: 145; " +
//                        "-fx-pref-height: 200; -fx-max-height: 130; ");
//            }

//-fx-effect: dropshadow(three-pass-box, #93948d, 10, 0, 0, 0);
        }
    }
}
