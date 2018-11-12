package br.com.museuid.customview;

import javafx.scene.control.TableCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class MutipleLineTableCell extends TableCell<Object, String> {
    private final VBox lines;
    public MutipleLineTableCell()
    {
        lines = new VBox();

//        lines.getStyleClass().add("address");
        setGraphic(lines);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        lines.getChildren().clear();

        if (!empty && item != null) {
//            int lineNo = 1;
            for (String line : item.split("\n")) {
                Text text = new Text(line);
                text.setFill(Color.web("#808080"));
//                text.getStyleClass().add("line-" + (lineNo++));
                lines.getChildren().add(text);
            }
        }
    }

}
