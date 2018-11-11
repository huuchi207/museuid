package customview.sectiongridview;

import org.controlsfx.control.GridView;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SectionView {
    @FXML
    private GridView gridView;
    @FXML
    private Label label;
    @FXML
    private VBox vBox;
    public SectionView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("item_grid_view.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public GridView getGridView() {
        return gridView;
    }

    public Label getLabel() {
        return label;
    }

    public VBox getBox(){
        return vBox;
    }
}
